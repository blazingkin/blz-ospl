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
	public File readingFrom;
	public int UUID;
	public Stack<Integer> lineReturns = new Stack<Integer>();
	public String[] lines;
	@SuppressWarnings("resource")
	public Process(File runFile) throws FileNotFoundException{
		do{
			UUID = r.nextInt(Integer.MAX_VALUE);
		}while(!Process.UUIDsUsed.contains(UUID));
		if (!runFile.exists()){
			Interpreter.throwError("Could Not Find File: "+runFile.getName()+" at path: "+runFile.getPath());
		}
		readingFrom = runFile;
		Variable.setValue("pc"+UUID, new Value(VariableTypes.Integer, 0));
		for (String s: new Scanner(readingFrom).useDelimiter("\\Z").next().split("\\n?\\r")){
			System.out.println(s);
		}
		lines = new Scanner(readingFrom).useDelimiter("\\Z").next().split("\\n?\\r");
	}
	public String getLine(int lineNumber){
		return lines[lineNumber-1];
	}
	
	public int getLine(){
		return (Integer) Variable.getValue("pc"+UUID).value;
	}
	@SuppressWarnings("resource")
	public int getSize() throws IOException{
		return new Scanner(readingFrom).useDelimiter("\\Z").next().split("\\n?\\r").length;
	}
	
	
	public static Process getProcessByUUID(int UUID){
		for (int i = 0; i < processes.size(); i++){
			if (processes.get(i).UUID == UUID){
				return processes.get(i);
			}
		}
		return null;
	}
	
	static ArrayList<Process> processes = new ArrayList<Process>();
	static ArrayList<Integer> UUIDsUsed = new ArrayList<Integer>();
	static Random r = new Random(System.currentTimeMillis());
	
}
