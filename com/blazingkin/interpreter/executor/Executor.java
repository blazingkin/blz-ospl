package com.blazingkin.interpreter.executor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.executionorder.LoopWrapper;
import com.blazingkin.interpreter.executor.listener.Event;
import com.blazingkin.interpreter.library.BlzEventHandler;
import com.blazingkin.interpreter.library.StandAloneEventHandler;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Executor {
	private static Stack<Process> runningProcesses = new Stack<Process>();	// A list of all of the independently running files
	private static Stack<Method> runningMethods = new Stack<Method>();
	private static Stack<Integer> functionUUID = new Stack<Integer>();
	private static Stack<LoopWrapper> loopStack = new Stack<LoopWrapper>();
	private static boolean loopIgnoreMode = false;
	private static int loopsIgnored = 0;
	private static BlzEventHandler eventHandler;
	private static ArrayList<Method> methods = new ArrayList<Method>();	// List of all functions within their respective processes
	private static boolean closeRequested = false;
	private static String startingMethod;
	private static ArrayList<Integer> UUIDsUsed = new ArrayList<Integer>();
	private static long timeStarted = 0;
	private static int frames = 0;
	private static ArrayList<Event> eventsToBeHandled = new ArrayList<Event>();
	
	public static void executeMethod(Method m){
		runningMethods.push(m);
		functionUUID.push(Executor.getUUID());
		if (getCurrentProcess().UUID == m.parent.UUID){
			Variable.setValue("pc"+getCurrentProcess().UUID, new Value(VariableTypes.Integer, m.lineNumber));
		}else{
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
	
	public static void codeLoop() throws Exception{
		while (!runningProcesses.isEmpty()){			// while we have a thing to do, we will continue to execute
			for (;(Integer)Variable.getValue("pc"+getCurrentProcess().UUID).value < getCurrentProcess().getSize();){
				String split[] = getCurrentProcess().getLine(getCurrentProcess().getLine()).split(" ");
				String newSplit[] = new String[split.length-1];
				for (int i = 1; i < split.length; i++){
					newSplit[i-1] = split[i];
				}
				if (isLoopIgnoreMode()){
					if (split[0].equals(Instruction.ENDLOOP.instruction)){
						if (loopsIgnored > 0){
							loopsIgnored--;
						}else{
							setLoopIgnoreMode(false);	
							getLoopStack().pop();
						}
					}else if (split[0].equals(Instruction.FORLOOP) || split[0].equals(Instruction.WHILE)){
						loopsIgnored++;
					}
					Variable.setValue("pc"+getCurrentProcess().UUID, new Value(VariableTypes.Integer,(Integer)(Variable.getValue("pc"+getCurrentProcess().UUID).value)+1));
					//System.out.println(loopStack.size() +": ls size");
					continue;
				}
				if (split[0].length() > 0 && split[0].substring(0,1).equals(":")){
					Method nM = new Method(getCurrentProcess(),(Integer)Variable.getValue("pc"+getCurrentProcess().UUID).value, split[0].substring(1));
					getMethods().add(nM);
					runningMethods.push(nM);
					functionUUID.push(Executor.getUUID());
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
				if (getEventsToBeHandled().size() > 0 && getCurrentMethod().interuptable){
					Executor.getCurrentProcess().lineReturns.add((Integer)Variable.getValue("pc"+Executor.getCurrentProcess().UUID).value+2);
					Executor.executeMethod(getEventsToBeHandled().get(0).method, getEventsToBeHandled().get(0).arguments);
					getEventsToBeHandled().remove(0);
				}
				if (isCloseRequested()){
					getEventHandler().exitProgram("Close was requested");
					cleanup();
					return;
				}
			}
		}
		cleanup();
	}
	
	
	//Run Executor when running from file
	public static void run(File runFile, List<String> args) throws Exception{			// runs the executor
		for (int i = 0; i < args.size(); i+=2){
			String s = args.get(i);
			if (s.substring(0,2).equals("-m")){			// denotation for indicating a starting method
				startingMethod = args.get(i+1);
			}
		}
		runningProcesses.push(new Process(runFile));		// puts the file passed to us as the current process
		if (startingMethod != null){
			if (!(Method.contains(getMethods(), startingMethod) == null)){
				runningMethods.push(Method.contains(getMethods(), startingMethod));
				functionUUID.push(Executor.getUUID());
				Executor.setLine(getCurrentMethod().lineNumber+1);		//if there is a starting method and we can find it, set the line number to it
			}
		}
		setEventHandler(new StandAloneEventHandler());
		codeLoop();
			
	}
	
	public static void run(ArrayList<String> code, List<String> args, BlzEventHandler handler) throws Exception{
		for (int i = 0; i < args.size(); i+=2){
			String s = args.get(i);
			if (s.substring(0,2).equals("-m")){			// denotation for indicating a starting method
				startingMethod = args.get(i+1);
			}
		}
		runningProcesses.push(new Process(code));
		if (startingMethod != null){
			if (!(Method.contains(getMethods(), startingMethod) == null)){
				runningMethods.push(Method.contains(getMethods(), startingMethod));
				functionUUID.push(Executor.getUUID());
				Executor.setLine(getCurrentMethod().lineNumber+1);		//if there is a starting method and we can find it, set the line number to it
			}
		}
		setEventHandler(handler);
		codeLoop();
	}
	
	//This cleans the execution environment so that another BLZ program can be run without restarting the Java program
	public static void cleanup(){
		runningProcesses = new Stack<Process>();
		runningMethods = new Stack<Method>();
		functionUUID = new Stack<Integer>();
		setLoopStack(new Stack<LoopWrapper>());
		setLoopIgnoreMode(false);
		loopsIgnored = 0;
		setEventHandler(null);
		UUIDsUsed = new ArrayList<Integer>();
		setTimeStarted(0);
		setFrames(0);
		setEventsToBeHandled(new ArrayList<Event>());
		Variable.lists = new HashMap<String, HashMap<Integer, Value>>();
		Variable.variables = new HashMap<String,Value>();
		setMethods(new ArrayList<Method>());
		setCloseRequested(false);
		startingMethod = "";
	}
	
	
	
	public static void executeLineInCurrentProcess(int lineNumber){
		String split[] = getCurrentProcess().getLine(lineNumber).split(" ");
		String newSplit[] = new String[split.length-1];
		for (int i = 1; i < split.length; i++){
			newSplit[i-1] = split[i];
		}
		if (split[0].length() > 0 && split[0].substring(0,1).equals(":")){
			Method nM = new Method(getCurrentProcess(),(Integer)Variable.getValue("pc"+getCurrentProcess().UUID).value, split[0].substring(1));
			getMethods().add(nM);
			runningMethods.push(nM);
			functionUUID.push(Executor.getUUID());
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
	public static void executeLineInCurrentProcess(int i, int returnLine) {
		executeLineInCurrentProcess(i);
		
	}
	
	
	//Lots of getters / setters below this point
	
	public static int getUUID(){
		int id;
		do{
			id = (int) (Math.random() * Integer.MAX_VALUE);
		}while(UUIDsUsed.contains(id));
		return id;
	}
	public static Stack<LoopWrapper> getLoopStack() {
		return loopStack;
	}
	public static void setLoopStack(Stack<LoopWrapper> loopStack) {
		Executor.loopStack = loopStack;
	}
	public static boolean isLoopIgnoreMode() {
		return loopIgnoreMode;
	}
	public static void setLoopIgnoreMode(boolean loopIgnoreMode) {
		Executor.loopIgnoreMode = loopIgnoreMode;
	}
	public static BlzEventHandler getEventHandler() {
		return eventHandler;
	}
	public static void setEventHandler(BlzEventHandler eventHandler) {
		Executor.eventHandler = eventHandler;
	}
	public static ArrayList<Method> getMethods() {
		return methods;
	}
	public static void setMethods(ArrayList<Method> methods) {
		Executor.methods = methods;
	}
	public static boolean isCloseRequested() {
		return closeRequested;
	}
	public static void setCloseRequested(boolean closeRequested) {
		Executor.closeRequested = closeRequested;
	}
	public static int getFrames() {
		return frames;
	}
	public static void setFrames(int frames) {
		Executor.frames = frames;
	}
	public static long getTimeStarted() {
		return timeStarted;
	}
	public static void setTimeStarted(long timeStarted) {
		Executor.timeStarted = timeStarted;
	}
	public static ArrayList<Event> getEventsToBeHandled() {
		return eventsToBeHandled;
	}
	public static void setEventsToBeHandled(ArrayList<Event> eventsToBeHandled) {
		Executor.eventsToBeHandled = eventsToBeHandled;
	}
	public static void setLine(int num){				// Sets line within the current process
		Variable.setValue("pc"+getCurrentProcess().UUID,new Value(VariableTypes.Integer, num-2));
	}
	public static void setLine(int num, int UUID){
		if (UUID == getCurrentProcess().UUID){
			Variable.setValue("pc"+getCurrentProcess().UUID,new Value(VariableTypes.Integer, num-2));
		}
	}
	public static Stack<Process> getRunningProcesses(){
		return runningProcesses;
	}
	public static Process getCurrentProcess(){
		try{
		return runningProcesses.peek();
		}catch(Exception e){
			return null;
		}
	}
	public static int getCurrentMethodUUID(){
		try{
			return functionUUID.peek();
		}catch(Exception e){
			return 0;
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
		for (int i = 0; i < getMethods().size(); i++){
			if (getMethods().get(i).isItThis(methodName, getCurrentProcess().UUID)){
				return getMethods().get(i);
			}
		}
		return null;
	}
	
	public static Method getMethod(String methodName, int processID){
		for (int i = 0; i < getMethods().size(); i++){
			if (getMethods().get(i).isItThis(methodName, processID)){
				return getMethods().get(i);
			}
		}
		return null;	
	}
	public static void addProcess(Process p){
		runningProcesses.push(p);
	}
	public static void popStack(){									// This is used to return to the previous process or function

		if (!runningMethods.isEmpty()){
			runningMethods.pop();
			Variable.clearLocalVariables(functionUUID.pop());
		}
		if (!getCurrentProcess().lineReturns.isEmpty()){		// If there is a function in the current process, go to it
			Executor.setLine(Executor.getCurrentProcess().lineReturns.pop());
		}else{											// If there is not a function in the current process, go to the previous process
			if (Executor.runningProcesses.size() > 1){
				runningProcesses.pop();
				Executor.setLine(Executor.getCurrentProcess().getLine()+2);
			}else{										// If there is not a previous process, request a close
				Executor.setCloseRequested(true);
				getEventHandler().exitProgram("Reached end of program");
			}
		}
	}
	
	
}
