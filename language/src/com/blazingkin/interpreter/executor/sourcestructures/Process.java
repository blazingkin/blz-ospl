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
import com.blazingkin.interpreter.executor.executionstack.RuntimeStackElement;
import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.parser.BlockParser;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.MethodBlockParser;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SplitStream;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

import in.blazingk.blz.packagemanager.FileImportManager;
import in.blazingk.blz.packagemanager.ImportPackageInstruction;
import in.blazingk.blz.packagemanager.Package;

public class Process implements RuntimeStackElement {
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
			return "<Process "+readingFrom.getName()+">";
		}
		return "<Process "+hashCode()+">";
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
			ArrayList<Either<String, ParseBlock>> parsed = BlockParser.parseBody(new SplitStream<String>(lines));
			registerMethodsAndConstructors(parsed);
			findImports(parsed);
			staticCode = new BlockNode(parsed, false);
			if (runImports) {
				handleImports();
			}
			processes.add(this);	
		}catch(SyntaxException exception){
			Interpreter.throwError(exception.getMessage());
		}catch (IOException exception){
			Interpreter.throwError(exception.getMessage());
		}
	}

	private void registerMethodsAndConstructors(ArrayList<Either<String, ParseBlock>> code) throws SyntaxException {
		MethodBlockParser parser = new MethodBlockParser(this);
		ArrayList<Either<String, ParseBlock>> methods = new ArrayList<Either<String, ParseBlock>>();
		ArrayList<Either<String, ParseBlock>> constructors = new ArrayList<Either<String, ParseBlock>>();
		for (Either<String, ParseBlock> line : code){
			if (line.isRight()){
				ParseBlock block = line.getRight().get();
				if (parser.shouldParse(block.getHeader())){
					methods.add(line);
				}else if (block.getHeader().startsWith("constructor")){
					constructors.add(line);
				}
			}
		}
		for (Either<String, ParseBlock> line : methods) {
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
		for (Either<String, ParseBlock> line : constructors){
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
			Process p = FileImportManager.importFile(f);
			importedMethods.addAll(p.methods);
			importedConstructors.addAll(p.constructors);
		}
	}

	public void findImports(ArrayList<Either<String, ParseBlock>> lines){
		//Always import core
		ImportPackageInstruction importer = (ImportPackageInstruction) Instruction.IMPORTPACKAGE.executor;
		try{
			ArrayList<Either<String, ParseBlock>> importInstructions = new ArrayList<Either<String, ParseBlock>>();
			ArrayList<Either<String, ParseBlock>> requireInstructions = new ArrayList<Either<String, ParseBlock>>();;
			for (Either<String, ParseBlock> line : lines){
				if (line.isLeft()){
					if (line.getLeft().get().startsWith("import")){
						importInstructions.add(line);
					}else if (line.getLeft().get().startsWith("require")){
						requireInstructions.add(line);
					}
				}
			}
			for (Either<String, ParseBlock> line : importInstructions){
				lines.remove(line);
				String packageName = line.getLeft().get().replaceFirst("import", "").trim();
				packagesToImport.add(importer.findPackage(packageName));
			}
			for (Either<String, ParseBlock> line : requireInstructions){
				lines.remove(line);
				String fileName = line.getLeft().get().replaceFirst("require", "").trim();
				processesToImport.add(calculateFileLocation(fileName));
			}
		}catch(Exception e){
			e.printStackTrace();
			Interpreter.throwError(e.getMessage());
		}
		
	}
	
	
	public String getLine(int lineNumber){
		if (lineNumber >= lines.length){
			valid = false;
			Interpreter.throwError("Attempted to get a line out of code range");
		}
		return lines[lineNumber];
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
	
	
	@Override
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


	@Override
	public void onBlockEnd() {

	}

	@Override
	public int getLineNum() {
		return -1;
	}
	
}
