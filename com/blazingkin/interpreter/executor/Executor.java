package com.blazingkin.interpreter.executor;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
				Executor.setLine(Executor.getCurrentProcess().getLine()+1);
			}else{
				Executor.closeRequested = true;
			}
		}
	}
	public static void addProcess(Process p){
		runningProcesses.push(p);
	}
	public static int numLines = 0;
	public static HashMap<String, FunctionLine> functionLines = new HashMap<String, FunctionLine>();
	public static boolean closeRequested = false;
	public static Stack<Integer> lineReturns = new Stack<Integer>();
	public static void setLine(int num){
		Variable.setValue("pc"+getCurrentProcess().UUID,new Value(VariableTypes.Integer, num-1));
	}
	
	public static void setLine(int num, int UUID){
		if (UUID == getCurrentProcess().UUID){
			Variable.setValue("pc"+getCurrentProcess().UUID,new Value(VariableTypes.Integer, num-1));
		}
	}
	public static void run(File runFile, List<String> args) throws IOException{
		runningProcesses.push(new Process(runFile));
		while (!runningProcesses.isEmpty()){
			for (;(Integer)Variable.getValue("pc"+getCurrentProcess().UUID).value < getCurrentProcess().getSize();){
				String split[] = getCurrentProcess().getLine(getCurrentProcess().getLine()).split(" ");
				String newSplit[] = new String[split.length-1];
				for (int i = 1; i < split.length; i++){
					newSplit[i-1] = split[i];
				}
				if (split[0].length() > 0 && split[0].substring(0,1).equals(":")){
					functionLines.put(split[0].substring(1),new FunctionLine(getCurrentProcess().UUID, (Integer)Variable.getValue("pc"+getCurrentProcess().UUID).value));
					Variable.setValue("pc"+getCurrentProcess().UUID, new Value(VariableTypes.Integer,(Integer)(Variable.getValue("pc"+getCurrentProcess().UUID).value)+1));
					continue;
				}
				if (split.length == 1 && split[0].equals("")){
					Variable.setValue("pc"+getCurrentProcess().UUID, new Value(VariableTypes.Integer, (Integer)(Variable.getValue("pc"+getCurrentProcess().UUID).value)+1));
					continue;
				}
				Variable.setValue("pc"+getCurrentProcess().UUID, new Value(VariableTypes.Integer, (Integer)(Variable.getValue("pc"+getCurrentProcess().UUID).value)+1));
				Instruction it = InstructionType.getInstructionType(split[0]);
				if (it.name.equals(Instruction.INVALID.name)){
					
					Interpreter.throwError("Invalid instruction "+split[0]);
				}
				it.executor.run(newSplit);
				if (GraphicsExecutor.jf != null){
					for (Component c:GraphicsExecutor.jf.getComponents()){
						c.repaint();
					}
				}
				if (eventsToBeHandled.size() > 0){
					lineReturns.add((Integer)Variable.getValue("pc"+getCurrentProcess().UUID).value + 2);
					Variable.setValue("pc"+getCurrentProcess().UUID, new Value(VariableTypes.Integer,eventsToBeHandled.get(0)));
					eventsToBeHandled.remove(0);
				}
				if (closeRequested){
					System.exit(1);
				}
			}
		}
		
			
	}
	
	public static long timeStarted = 0;
	public static int frames = 0;
	public static ArrayList<Integer> eventsToBeHandled = new ArrayList<Integer>();
	
	
	
}
