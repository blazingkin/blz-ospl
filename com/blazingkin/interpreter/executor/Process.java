package com.blazingkin.interpreter.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import com.blazingkin.interpreter.Interpreter;

public class Process {
	public boolean runningFromFile = false;
	public File readingFrom;
	public int UUID;
	public Stack<Integer> lineReturns = new Stack<Integer>();
	private String[] lines;
	private RegisteredLine[] registeredLines;
	
	
	public Process(File runFile) throws FileNotFoundException{
		runningFromFile = true;
		if (!runFile.exists()){
			Interpreter.throwError("Could Not Find File: "+runFile.getName()+" at path: "+runFile.getPath());
		}
		UUID = Executor.getUUID();
		readingFrom = runFile;			//the file passed to us exists and we can use it
		Scanner scan = new Scanner(readingFrom);
		ArrayList<String> lns = new ArrayList<String>();
		while (scan.hasNextLine()){
			lns.add(scan.nextLine());
		}
		scan.close();
		lines = new String[lns.size()];
		for (int i = 0; i < lns.size(); i++){
			lines[i] = lns.get(i);
		}
		if (lines.length == 0){
			Interpreter.throwError("File: "+runFile.getName()+" did not contain any lines");
		}
		registerMethods();
		preprocessLines();
		processes.add(this);
	}
	
	
	public Process(ArrayList<String> code){
		this((String[]) code.toArray());
	}
	
	public Process(String[] code){
		lines = new String[code.length];
		for (int i = 0; i < code.length; i++){
			lines[i] = code[i];
		}

		if (lines.length == 0){
			Interpreter.throwError("The code recieved as a library argument did not contain any lines");	
		}
		registerMethods();
		preprocessLines();
		processes.add(this);
	}
	
	public void registerMethods(){
		for (int i = 0 ; i < lines.length; i++){						//registers all of the functions found in the file
			if (lines[i].length() > 0){
				if (lines[i].substring(0,1).equals(":")){
				Method nM = new Method(this, i+1,lines[i].substring(1));
				Executor.getMethods().add(nM);
				}
			}
		}
	}

	
	public void preprocessLines(){
		registeredLines = new RegisteredLine[lines.length];
		for (int i = 0; i < lines.length; i++){
			String splits[] = lines[i].split(" ");
			if (splits.length == 0){
				registeredLines[i] = null;
				continue;
			}
			Instruction instr = InstructionType.getInstructionType(splits[0]);
			if (instr == null || instr == Instruction.INVALID){
				registeredLines[i] = null;
				continue;
			}
			String newStr = lines[i].replaceFirst(splits[0], "").trim();
			registeredLines[i] = new RegisteredLine(instr, Executor.parseExpressions(newStr));
		}
	}
	
	public boolean isRegistered(int lineNumber){
		if (lineNumber >= lines.length){
			Interpreter.throwError("Attempted to get a line out of code range");
		}
		return registeredLines[lineNumber] != null;
	}
	
	public RegisteredLine getRegisteredLine(int lineNumber){
		if (lineNumber >= lines.length){
			Interpreter.throwError("Attempted to get a line out of code range");
		}
		return registeredLines[lineNumber];
	}
	
	public String getLine(int lineNumber){
		if (lineNumber >= lines.length){
			Interpreter.throwError("Attempted to get a line out of code range");
		}
		return lines[lineNumber];
	}
	
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
	
	public static ArrayList<Process> processes = new ArrayList<Process>();
	
	
	public class RegisteredLine{
		public final Instruction instr;
		public final String[] args;
		public RegisteredLine(Instruction instr, String[] args){
			this.instr = instr;
			this.args = args;
		}
		public Instruction getInstr(){
			return instr;
		}
		public String[] getArgs(){
			return args;
		}
	}
	
}
