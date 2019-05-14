package com.blazingkin.interpreter.executor.sourcestructures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.astnodes.BlockNode;
import com.blazingkin.interpreter.executor.astnodes.MethodNode;
import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.parser.BlockParser;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.MethodBlockParser;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SourceLine;
import com.blazingkin.interpreter.parser.SplitStream;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

import in.blazingk.blz.packagemanager.FileImportManager;
import in.blazingk.blz.packagemanager.ImportPackageInstruction;
import in.blazingk.blz.packagemanager.Package;

public class Process {
	public boolean runningFromFile = false;
	public boolean runImports = true;
	public File readingFrom;
	public int UUID;
	public Stack<Integer> lineReturns = new Stack<Integer>();
	private String[] lines;
	public ArrayList<MethodNode> methods = new ArrayList<MethodNode>();
	public ArrayList<Constructor> constructors = new ArrayList<Constructor>();
	public Collection<MethodNode> importedMethods = new HashSet<MethodNode>();
	public Collection<Constructor> importedConstructors = new HashSet<Constructor>();
	public static ArrayList<Process> processes = new ArrayList<Process>();
	Set<Path> packagesToImport = new HashSet<Path>();
	Set<Path> processesToImport = new HashSet<Path>();
	private ASTNode staticCode;
	public Context processContext = new Context();
	private boolean staticRan = false;


	public Process(File runFile) throws FileNotFoundException{
		runningFromFile = true;
		setupFileProcess(runFile);
	}
	
	public Process(Path runPath) throws FileNotFoundException {
		runningFromFile = true;
		setupFileProcess(runPath.toFile());
	}
	
	public Process(Path runPath, boolean runImports) throws FileNotFoundException {
		runningFromFile = true;
		this.runImports = runImports;
		setupFileProcess(runPath.toFile());
	}
	
	public String toString() {
		if (readingFrom != null) {
			return "<Source File "+readingFrom.getName()+">";
		}
		return "<Source File "+hashCode()+">";
	}

	public String getLocation() {
		if (readingFrom != null) {
			return readingFrom.getAbsolutePath();
		}
		return "In memory file";
	}
	
	private void setupFileProcess(File runFile) throws FileNotFoundException{
		runningFromFile = true;
		if (!runFile.exists()){
			Interpreter.throwError("Could Not Find File: "+runFile.getName()+" at path: "+runFile.getPath());
		}
		UUID = Executor.getUUID();
		readingFrom = runFile;			//the file passed to us exists and we can use it
		Scanner scan = new Scanner(readingFrom);
		ArrayList<String> lns = new ArrayList<String>();
		while (scan.hasNextLine()){
			try{
				lns.add(scan.nextLine().split("(?<!\\\\)#")[0].trim());	// Ignore extra whitespace and comments
			}catch(ArrayIndexOutOfBoundsException e){
				/* Basically the line starts with a comment */
				lns.add("");
			}
		}
		scan.close();
		lines = new String[lns.size()];
		for (int i = 0; i < lns.size(); i++){
			lines[i] = lns.get(i);
		}
		if (lines.length == 0){
			Interpreter.throwError("File: "+runFile.getName()+" did not contain any lines");
		}
		setup();
	}
	

	
	public Process(String[] code){
		lines = new String[code.length];
		for (int i = 0; i < code.length; i++){
			lines[i] = code[i].split("(?<!\\\\)#")[0].trim();	// Ignore extra whitespace and comments;
		}
		if (lines.length == 0){
			Interpreter.throwError("The code recieved as a library argument did not contain any lines");	
		}
		setup();
	}
	
	private void setup(){
		try {
			// For BlockParser.parseBody, the initial offset is 1 because file lines are 1-indexed
			ArrayList<Either<SourceLine, ParseBlock>> parsed = BlockParser.parseBody(new SplitStream<String>(lines), 1);
			registerMethodsAndConstructors(parsed);
			findImports(parsed);
			staticCode = new BlockNode(parsed, false);
			if (runImports) {
				handleImports();
			}
			processes.add(this);	
		}catch(SyntaxException exception){
			Interpreter.throwError(exception.getMessage() + " In "+this.toString());
		}catch (IOException exception){
			Interpreter.throwError(exception.getMessage());
		}
	}

	private void registerMethodsAndConstructors(ArrayList<Either<SourceLine, ParseBlock>> code) throws SyntaxException {
		MethodBlockParser parser = new MethodBlockParser(this);
		ArrayList<Either<SourceLine, ParseBlock>> methods = new ArrayList<Either<SourceLine, ParseBlock>>();
		ArrayList<Either<SourceLine, ParseBlock>> constructors = new ArrayList<Either<SourceLine, ParseBlock>>();
		for (Either<SourceLine, ParseBlock> line : code){
			if (line.isRight()){
				ParseBlock block = line.getRight().get();
				if (parser.shouldParse(block.getHeader())){
					methods.add(line);
				}else if (block.getHeader().startsWith("constructor")){
					constructors.add(line);
				}
			}
		}
		for (Either<SourceLine, ParseBlock> line : methods) {
			code.remove(line);
			ParseBlock method = line.getRight().get();
			try{
				this.methods.add(new MethodNode(method.getHeader(), method.getLines(), this));
			}catch(SyntaxException e){
				String message = e.getMessage();
				message = "In "+this.toString()+":\n" + message;
				throw new SyntaxException(message);
			}
		}
		for (Either<SourceLine, ParseBlock> line : constructors){
			code.remove(line);
			ParseBlock constructor = line.getRight().get();
			try{
				this.constructors.add(new Constructor(this, constructor));
			}catch(SyntaxException e){
				String message = e.getMessage();
				message = "In "+this.toString()+":\n" + message;
				throw new SyntaxException(message);
			}
		}
	}
	
	private Path calculateFileLocation(String name) {
		if (!name.endsWith(".blz")) {
			name = name + ".blz";
		}
		/* Try a relative path first. Otherwise look for the absolute path */
		if (runningFromFile) {
			Path base = readingFrom.toPath();
			Path result = Paths.get(base.toString(), name);
			if (result.toFile().exists()) {
				return result;
			}
		}
		return Paths.get(name);
	}
	
	public void handleImports() throws IOException{
		for (Path f : packagesToImport){
			Package p = new in.blazingk.blz.packagemanager.Package(f);
			importedMethods.addAll(p.getAllMethodsInPackage());
			importedConstructors.addAll(p.getAllConstructorsInPackage());
		}
		for (Path f: processesToImport) {
			if (!f.isAbsolute() && runningFromFile){
				/* Resolve local requires from the location of the file, not blz's cwd */
				f = Paths.get(readingFrom.getAbsoluteFile().getParentFile().getPath(), f.toString());
			}
			Process p = FileImportManager.importFile(f);
			importedMethods.addAll(p.methods);
			importedConstructors.addAll(p.constructors);
		}
	}

	public void findImports(ArrayList<Either<SourceLine, ParseBlock>> lines){
		//Always import core
		ImportPackageInstruction importer = (ImportPackageInstruction) Instruction.IMPORTPACKAGE.executor;
		try{
			ArrayList<SourceLine> importInstructions = new ArrayList<>();
			ArrayList<SourceLine> requireInstructions = new ArrayList<>();
			ArrayList<Either<SourceLine, ParseBlock>> toRemove = new ArrayList<>();
			for (Either<SourceLine, ParseBlock> line : lines){
				if (line.isLeft()){
					if (line.getLeft().get().line.startsWith("import")){
						importInstructions.add(line.getLeft().get());
						toRemove.add(line);
					}else if (line.getLeft().get().line.startsWith("require")){
						requireInstructions.add(line.getLeft().get());
						toRemove.add(line);
					}
				}
			}

			// Remove all of the import instructions from the source list
			toRemove.forEach(x -> lines.remove(x));

			for (SourceLine line : importInstructions){
				try {
					String importLine = line.line.replaceFirst("import", "").trim();
					ImportStatement importStatement = new ImportStatement(importLine);
					packagesToImport.add(importer.findPackage(importStatement.packageName));
				}catch(IOException e){
					throw new BLZRuntimeException("On line "+line.lineNumber+" in "+this.toString()+"\n"+e.getMessage());
				}catch(SyntaxException e){
					throw new BLZRuntimeException("On line "+line.lineNumber+" in "+this.toString()+"\n"+e.getMessage());
				}
			}
			for (SourceLine line : requireInstructions){
				String fileName = line.line.replaceFirst("require", "").trim();
				processesToImport.add(calculateFileLocation(fileName));
			}
		}catch(Exception e){
			Interpreter.throwError(e.getMessage());
		}
		
	}
	
	public boolean valid = true;
	
	public int getLine(){
		return Executor.getLine();
	}
	
	public int getSize(){
		return lines.length;
	}
	
	public void setLine(int lineNumber){
		Executor.setLine(lineNumber);
	}
	
	public static Process getProcessByUUID(int UUID){
		for (int i = 0; i < processes.size(); i++){
			if (processes.get(i).UUID == UUID){
				return processes.get(i);
			}
		}
		return null;
	}
	
	
	public void onBlockStart() throws BLZRuntimeException {
		if (!staticRan){
			staticRan = true;
			for (MethodNode m : importedMethods){
				Variable.setValue(m.getStoreName(), Value.method(m), processContext);
			}
			for (Constructor c : importedConstructors) {
				Variable.setValue(c.getName(), Value.constructor(c), processContext);
			}
			for (MethodNode m : methods){
				Variable.setValue(m.getStoreName(), new Value(VariableTypes.Method, m), processContext);
			}
			for (Constructor c : constructors){
				Variable.setValue(c.getName(), Value.constructor(c), processContext);
			}
			staticCode.execute(processContext);
		}
	}
	
}
