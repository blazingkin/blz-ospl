package com.blazingkin.interpreter.executor.sourcestructures;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.executionorder.End;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStackElement;
import com.blazingkin.interpreter.executor.instruction.BlockInstruction;
import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.instruction.InstructionType;
import com.blazingkin.interpreter.executor.instruction.LabeledInstruction;
import com.blazingkin.interpreter.expressionabstraction.ExpressionParser;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

import in.blazingk.blz.packagemanager.ImportPackageInstruction;
import in.blazingk.blz.packagemanager.Package;

public class Process implements RuntimeStackElement {
	public boolean runningFromFile = false;
	public File readingFrom;
	public int UUID;
	public Stack<Integer> lineReturns = new Stack<Integer>();
	private String[] lines;
	private RegisteredLine[] registeredLines;
	public ArrayList<Method> methods = new ArrayList<Method>();
	public ArrayList<Constructor> constructors = new ArrayList<Constructor>();
	public Collection<Method> importedMethods = new HashSet<Method>();
	public HashMap<Integer, BlockArc> blockArcs = new HashMap<Integer, BlockArc>();	// Both the start and end of the block point to the arc
	public static ArrayList<Process> processes = new ArrayList<Process>();
	private boolean shouldImportCore = true;
	
	public Process(File runFile) throws FileNotFoundException{
		runningFromFile = true;
		setupFileProcess(runFile);
	}
	
	public Process(File runFile, boolean shouldImportCore) throws FileNotFoundException{
		this.shouldImportCore = shouldImportCore;
		setupFileProcess(runFile);
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
		registerMethods();
		registerBlocks();
		registerConstructors();
		preprocessLines();
		handleImports();
		processes.add(this);		
	}
	
	private void registerMethods(){
		for (int i = 0 ; i < lines.length; i++){						//registers all of the functions found in the file
			if (lines[i].length() > 0){
				if (lines[i].substring(0,1).equals(":")){
					Method nM = new Method(this, i+1,lines[i].substring(1));
					Executor.getMethods().add(nM);
					methods.add(nM);
				}
			}
		}
	}
	
	private void registerConstructors(){
		for (int i = 0; i < lines.length; i++){
			if (lines[i].length() > 0){
				if (lines[i].startsWith("constructor")){
					String constructorName = lines[i].replaceFirst("constructor", "").trim();
					Constructor con = new Constructor(this, i+1, constructorName);
					constructors.add(con);
					Executor.addConstructor(constructorName, con);
				}
			}
		}
	}

	
	private void preprocessLines(){
		String errors = "";
		registeredLines = new RegisteredLine[lines.length];
		for (int i = 0; i < lines.length; i++){
			String splits[] = lines[i].split(" ");
			if (splits.length == 0){
				registeredLines[i] = null;
				continue;
			}
			try{
				Instruction instr = InstructionType.getInstructionType(splits[0]);
				if (instr == null || instr == Instruction.INVALID){
					if (lines[i].trim().isEmpty() || lines[i].trim().charAt(0) == ':' || lines[i].trim().charAt(0) == '('){
						continue;
					}
					registeredLines[i] = new RegisteredLine(ExpressionParser.parseExpression(lines[i]));
					continue;
				}
				String newStr = lines[i].replaceFirst(splits[0], "").trim();
				registeredLines[i] = new RegisteredLine(instr, newStr);
			}catch(Exception e){
				e.printStackTrace();
				valid = false;
				errors += "Syntax error on line: "+(i+1)+"\n"+lines[i]+"\n";
			}
		}
		if (!errors.isEmpty()){
			Interpreter.throwError(errors);
		}
	}
	
	private boolean isBlock(String line){
		if (line.startsWith(":")){
			return true;
		}
		if (line.startsWith("constructor")){
			return true;
		}
		String[] splits = line.split(" ");
		Instruction inst = InstructionType.getInstructionType(splits[0]);
		return inst != null && inst.executor instanceof BlockInstruction;
	}
	
	private boolean isEnd(String line){
		String[] splits = line.split(" ");
		Instruction inst = InstructionType.getInstructionType(splits[0]);
		return inst != null && inst.executor instanceof End;		
	}
	
	private boolean isLabel(String line){
		String[] splits = line.split(" ");
		Instruction inst = InstructionType.getInstructionType(splits[0]);
		return inst != null && inst.executor instanceof LabeledInstruction;
	}
	
	private String getLabel(String line){
		String[] splits = line.split(" ");
		Instruction inst = InstructionType.getInstructionType(splits[0]);
		String args = "";
		for (int i = 1; i < splits.length; i++){
			args += splits[i] + " ";
		}
		args.trim();
		return ((LabeledInstruction) inst.executor).getLabel(args);
	}
	
	private void registerBlocks(){
		Stack<Integer> blckStack = new Stack<Integer>();
		HashMap<Integer, HashMap<String, Integer>> labelMap = new HashMap<Integer, HashMap<String, Integer>>();
		for (int i = 0; i < lines.length; i++){
			if (isBlock(lines[i])){
				blckStack.push(i+1);	// Array 0 indexed - File 1 indexed
			}
			else if (isEnd(lines[i])){
				if (blckStack.empty()){
					valid = false;
					Interpreter.throwError("Unexpected "+getRegisteredLine(i).instr.name+" on line "+(i+1));
				}
				BlockArc ba = new BlockArc(blckStack.pop(), i+1);// Array 0 indexed - File 1 indexed
				
				if (labelMap.containsKey(ba.start)){
					for (String key : labelMap.get(ba.start).keySet()){
						int line = labelMap.get(ba.start).get(key);
						ba.addLabel(key, line);
						blockArcs.put(line, ba);
					}
				}
				
				blockArcs.put(ba.start, ba);
				blockArcs.put(ba.end, ba);
			}else if (isLabel(lines[i])){
				String label = getLabel(lines[i]);
				if (blckStack.empty()){
					Interpreter.throwError("Unexpected label "+label+" on line "+(i+1));
				}
				int startLine = blckStack.peek();
				if (!labelMap.containsKey(startLine)){
					labelMap.put(startLine, new HashMap<String, Integer>());
				}
				labelMap.get(startLine).put(label, i+1);
			}
		}
		if (!blckStack.empty()){
			while (!blckStack.empty()){
				valid = false;
				if (!Executor.isImmediateMode()){
				Executor.getEventHandler().print("Block starting on line "+blckStack.pop()+" not closed");
				}
			}
			valid = false;
			Interpreter.throwError("Some blocks not closed!");
		}
	}
	
	private void handleImports(){
		//Always import core
		Set<File> packagesToImport = new HashSet<File>();
		ImportPackageInstruction importer = (ImportPackageInstruction) Instruction.IMPORTPACKAGE.executor;
		try{
			if (shouldImportCore){
				packagesToImport.add(importer.findPackage("Core"));		
			}
			for (RegisteredLine line : registeredLines){
				if (line != null && line.instr == Instruction.IMPORTPACKAGE){
					packagesToImport.add(importer.findPackage(line.args));
				}
			}
			for (File f : packagesToImport){
				Package p = new in.blazingk.blz.packagemanager.Package(f);
				importedMethods.addAll(p.getAllMethodsInPackage());
			}
		}catch(Exception e){
			e.printStackTrace();
			Interpreter.throwError(e.getMessage());
		}
		
	}
	
	public boolean isRegistered(int lineNumber){
		if (lineNumber >= lines.length){
			valid = false;
			Interpreter.throwError("Attempted to get a line out of code range");
		}
		return registeredLines[lineNumber] != null;
	}
	
	public RegisteredLine getRegisteredLine(int lineNumber){
		if (lineNumber >= lines.length){
			valid = false;
			Interpreter.throwError("Attempted to get a line out of code range");
		}
		return registeredLines[lineNumber];
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
	
	public class BlockArc {
		public final int start, end;
		public BlockArc(int s, int e){
			start = s;
			end = e;
		}
		
		public HashMap<String, Integer> labelMap;
		
		public void addLabel(String name, int line){
			if (labelMap == null){
				labelMap = new HashMap<String, Integer>();
			}
			labelMap.put(name, line);
		}
		
		public boolean hasLabel(String name){
			return name.equals("start") || name.equals("end") || (labelMap != null && labelMap.containsKey(name));
		}
		
		public int getBlockLine(String name) throws Exception{
			if (labelMap != null && labelMap.containsKey(name)){
				return labelMap.get(name);
			}else if (name.equals("start")){
				return start;
			}else if(name.equals("end")){
				return end;
			}
			throw new Exception("Could not find label "+name+" for block starting on line "+start);
		}
	}
	

	@Override
	public void onBlockStart() {
		for (Method m : methods){
			Variable.setValue(m.functionName, new Value(VariableTypes.Method, m));
		}
		for (Method m : importedMethods){
			Variable.setValue(m.functionName, new Value(VariableTypes.Method, m));
		}
		for (Constructor c : constructors){
			Variable.setValue(c.getName(), Value.constructor(c));
		}
	}


	@Override
	public void onBlockEnd() {
		if (!Executor.getProcessLineStack().empty()){
			Executor.setLine(Executor.getProcessLineStack().pop());
		}
	}

	@Override
	public int getLineNum() {
		return -1;
	}
	
}
