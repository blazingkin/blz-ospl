package com.blazingkin.interpreter.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Process {
	public boolean runningFromFile = false;
	public File readingFrom;
	public int UUID;
	public Stack<Integer> lineReturns = new Stack<Integer>();
	public String[] lines;
	
	
	@SuppressWarnings("resource")
	public Process(File runFile) throws FileNotFoundException{
		runningFromFile = true;
		UUID = Executor.getUUID();
		if (!runFile.exists()){
			Interpreter.throwError("Could Not Find File: "+runFile.getName()+" at path: "+runFile.getPath());
		}
		readingFrom = runFile;			//the file passed to us exists and we can use it
		Variable.setValue("pc"+UUID, new Value(VariableTypes.Integer, 0));
		Scanner s = new Scanner(readingFrom);
		ArrayList<String> li = new ArrayList<String>();
		while (s.hasNextLine()){
			//System.out.println("got line");
			li.add(s.nextLine());
		}
		lines = new String[li.size()];
		for (int i = 0; i < li.size(); i++){
			lines[i] = li.get(i);
		}
		if (lines.length == 0){
			Interpreter.throwError("File: "+runFile.getName()+" did not contain any lines");
		}
		registerMethods();
		processes.add(this);
	}
	
	
	public Process(ArrayList<String> code){
		UUID = Executor.getUUID();
		Variable.setValue("pc"+UUID, new Value(VariableTypes.Integer, 0));
		lines = new String[code.size()];
		for (int i = 0; i < code.size(); i++){
			lines[i] = code.get(i);
		}
		if (lines.length == 0){
			Interpreter.throwError("The code recieved as a library argument did not contain any lines");
			
		}
		registerMethods();
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
	
	
	public String getLine(int lineNumber){
		if (lineNumber >= lines.length){
			Executor.setCloseRequested(true);
			Executor.getEventHandler().exitProgram("Attempted to get a line out of code range");
			
		}
		return lines[lineNumber];
	}
	
	public int getLine(){
		return (Integer) Variable.getValue("pc"+UUID).value;
	}
	@SuppressWarnings("resource")
	public int getSize() throws IOException{
		if (!runningFromFile){
			return lines.length;
		}
		return new Scanner(readingFrom).useDelimiter("\\Z").next().split("\\n|\\r").length;
	}
	public void setLine(int lineNumber){
		Variable.setValue("pc"+UUID, new Value(VariableTypes.Integer, lineNumber));
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
	static Random r = new Random(System.currentTimeMillis());
	
}
