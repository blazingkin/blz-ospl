package com.blazingkin.interpreter.executor;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.output.graphics.GraphicsExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Executor {
	public static Stack<Process> runningProcesses = new Stack<Process>();	// A list of all of the independently running files
	public static Stack<Method> runningMethods = new Stack<Method>();
	public static Process getCurrentProcess(){
		try{
		return runningProcesses.peek();
		}catch(Exception e){
			return null;
		}
	}
	public static Method getCurrentMethod(){
		try{
		return runningMethods.peek();
		}catch(Exception e){
			return null;
		}
	}
	public static Method getMethodInCurrentProcess(String methodName){
		for (int i = 0; i < methods.size(); i++){
			if (methods.get(i).isItThis(methodName, getCurrentProcess().UUID)){
				return methods.get(i);
			}
		}
		return null;
	}
	
	public static Method getMethod(String methodName, int processID){
		for (int i = 0; i < methods.size(); i++){
			if (methods.get(i).isItThis(methodName, processID)){
				return methods.get(i);
			}
		}
		return null;	
	}
	public static void executeMethod(Method m){
		getCurrentProcess().lineReturns.add((Integer)(Variable.getValue("pc"+getCurrentProcess().UUID).value)+1);
		runningMethods.push(m);
		if (getCurrentProcess().UUID == m.parent.UUID){
			Variable.setValue("pc"+getCurrentProcess().UUID, new Value(VariableTypes.Integer, m.lineNumber));
		}else{
			addProcess(m.parent);
			Variable.setValue("pc"+getCurrentProcess().UUID, new Value(VariableTypes.Integer, m.lineNumber));
		}
	}
	public static void executeMethod(Method m, String[] args){

		Value[] values = new Value[args.length];
		for (int i = 0; i < values.length; i++){
			values[i] = new Value(VariableTypes.Integer, Variable.parseString(args[i]));
		}
		executeMethod(m);
		if (m.takesVariables){
			for (int i = 0; i < (m.variables.length > values.length?values.length:m.variables.length); i++){
				Variable.setValue(m.variables[i], values[i]);
			}
		}
	}
	public static void popStack(){									// This is used to return to the previous process or function
		if (!runningMethods.isEmpty()){
			Variable.clearLocalVariables(runningMethods.pop());
		}
		if (!Executor.lineReturns.isEmpty()){		// If there is a function in the current process, go to it
			Executor.setLine(Executor.lineReturns.pop());
		}else{											// If there is not a function in the current process, go to the previous process
			if (Executor.runningProcesses.size() > 1){
				runningProcesses.pop();
				Executor.setLine(Executor.getCurrentProcess().getLine()+1);
			}else{										// If there is not a previous process, request a close
				Executor.closeRequested = true;
			}
		}
	}
	public static void addProcess(Process p){
		runningProcesses.push(p);
	}
	public static ArrayList<Method> methods = new ArrayList<Method>();	// List of all functions within their respective processes
	public static boolean closeRequested = false;
	public static Stack<Integer> lineReturns = new Stack<Integer>();
	public static String startingMethod;
	public static void setLine(int num){				// Sets line within the current process
		Variable.setValue("pc"+getCurrentProcess().UUID,new Value(VariableTypes.Integer, num-2));
	}
	
	public static void setLine(int num, int UUID){
		if (UUID == getCurrentProcess().UUID){
			Variable.setValue("pc"+getCurrentProcess().UUID,new Value(VariableTypes.Integer, num-2));
		}
	}
	
	
	
	public static void run(File runFile, List<String> args) throws IOException{			// runs the executor
		for (int i = 0; i < args.size(); i+=2){
			String s = args.get(i);
			if (s.substring(0,2).equals("-m")){			// denotation for indicating a starting method
				startingMethod = args.get(i+1);
			}
		}
		runningProcesses.push(new Process(runFile));		// puts the file passed to us as the current process
		if (startingMethod != null){
			if (!(Method.contains(methods, startingMethod) == null)){
				runningMethods.push(Method.contains(methods, startingMethod));
				Executor.setLine(getCurrentMethod().lineNumber+1);		//if there is a starting method and we can find it, set the line number to it
			}
		}
		while (!runningProcesses.isEmpty()){			// while we have a thing to do, we will continue to execute
			for (;(Integer)Variable.getValue("pc"+getCurrentProcess().UUID).value < getCurrentProcess().getSize();){
				String split[] = getCurrentProcess().getLine(getCurrentProcess().getLine()).split(" ");
				String newSplit[] = new String[split.length-1];
				for (int i = 1; i < split.length; i++){
					newSplit[i-1] = split[i];
				}
				if (split[0].length() > 0 && split[0].substring(0,1).equals(":")){
					Method nM = new Method(getCurrentProcess(),(Integer)Variable.getValue("pc"+getCurrentProcess().UUID).value, split[0].substring(1));
					methods.add(nM);
					runningMethods.push(nM);
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
					System.exit(0);
				}
			}
		}
		
			
	}
	
	public static void executeLineInCurrentProcess(int lineNumber){
		String split[] = getCurrentProcess().getLine(lineNumber).split(" ");
		String newSplit[] = new String[split.length-1];
		for (int i = 1; i < split.length; i++){
			newSplit[i-1] = split[i];
		}
		if (split[0].length() > 0 && split[0].substring(0,1).equals(":")){
			Method nM = new Method(getCurrentProcess(),(Integer)Variable.getValue("pc"+getCurrentProcess().UUID).value, split[0].substring(1));
			methods.add(nM);
			runningMethods.push(nM);

			return;
		}
		if (split.length == 1 && split[0].equals("")){
			return;
		}
		Instruction it = InstructionType.getInstructionType(split[0]);
		if (it.name.equals(Instruction.INVALID.name)){
			
			Interpreter.throwError("Invalid instruction "+split[0]);
		}
		it.executor.run(newSplit);
	}
	
	public static int getUUID(){
		int id;
		do{
			id = (int) (Math.random() * Integer.MAX_VALUE);
		}while(UUIDsUsed.contains(id));
		return id;
	}
	
	public static ArrayList<Integer> UUIDsUsed = new ArrayList<Integer>();
	public static long timeStarted = 0;
	public static int frames = 0;
	public static ArrayList<Integer> eventsToBeHandled = new ArrayList<Integer>();
	
	
	
}
