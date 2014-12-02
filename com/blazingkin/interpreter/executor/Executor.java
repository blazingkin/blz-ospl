package com.blazingkin.interpreter.executor;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.output.graphics.GraphicsExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Executor {
	public static Stack<Process> runningProcesses = new Stack<Process>();
	public static Process getCurrentProcess(){
		return runningProcesses.peek();
	}
	public static void popStack(){
		if (!Executor.getCurrentProcess().lineReturns.isEmpty()){
			Executor.setLine(Executor.lineReturns.pop());
		}else{
			if (Executor.runningProcesses.size() > 1){
				runningProcesses.pop();
				Executor.setLine(Executor.getCurrentProcess().getLine());
			}else{
				Executor.closeRequested = true;
			}
		}
	}
	public static int numLines = 0;
	public static HashMap<String, Integer> functionLines = new HashMap<String, Integer>();
	public static boolean closeRequested = false;
	public static Stack<Integer> lineReturns = new Stack<Integer>();
	public static void setLine(int num){
		Variable.setValue("pc"+getCurrentProcess().UUID,new Value(VariableTypes.Integer, num-2));
	}
	
	public static void run(File runFile, List<String> args) throws FileNotFoundException{
		Variable.setValue("pc", new Value(VariableTypes.Integer, 0));
		Scanner s = new Scanner(runFile);
		ArrayList<String> file = new ArrayList<String>();
		while (s.hasNextLine()){
			file.add(s.nextLine());
			
		}
		s.close();
		runningProcesses.push(new Process(runFile));
		for (int i = 0 ; i < file.size(); i++){
			if (file.get(i).length() > 0 && file.get(i).substring(0, 1).equals(":")){
				functionLines.put(file.get(i).substring(1), i);
			}
		}
		for (;(Integer)Variable.getValue("pc").value < file.size();){
			String split[] = file.get((Integer)Variable.getValue("pc").value).split(" ");
			String newSplit[] = new String[split.length-1];
			for (int i = 1; i < split.length; i++){
				newSplit[i-1] = split[i];
			}
			if (split[0].length() > 0 && split[0].substring(0,1).equals(":")){
				functionLines.put(split[0].substring(1), (Integer)Variable.getValue("pc").value);
				Variable.setValue("pc", new Value(VariableTypes.Integer,(Integer)(Variable.getValue("pc").value)+1));
				continue;
			}
			if (split.length == 1 && split[0].equals("")){
				Variable.setValue("pc", new Value(VariableTypes.Integer, (Integer)(Variable.getValue("pc").value)+1));
				continue;
			}
			Variable.setValue("pc", new Value(VariableTypes.Integer, (Integer)(Variable.getValue("pc").value)+1));
			Instruction it = InstructionType.getInstructionType(split[0]);
			if (it.name.equals("INVALID")){
				
				Interpreter.throwError("Invalid instruction "+split[0]);
			}
			it.executor.run(newSplit);
			if (GraphicsExecutor.jf != null){
				for (Component c:GraphicsExecutor.jf.getComponents()){
					c.repaint();
				}
			}
			if (eventsToBeHandled.size() > 0){
				lineReturns.add((Integer)Variable.getValue("pc").value + 2);
				Variable.setValue("pc", new Value(VariableTypes.Integer,eventsToBeHandled.get(0)));
				eventsToBeHandled.remove(0);
			}
			if (closeRequested){
				System.exit(1);
			}
		}
		
			
	}
	
	public static long timeStarted = 0;
	public static int frames = 0;
	public static ArrayList<Integer> eventsToBeHandled = new ArrayList<Integer>();
	
	
	
}
