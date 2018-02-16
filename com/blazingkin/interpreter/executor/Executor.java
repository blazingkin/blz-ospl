package com.blazingkin.interpreter.executor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import com.blazingkin.interpreter.executor.executionorder.LoopWrapper;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.sourcestructures.Closure;
import com.blazingkin.interpreter.executor.sourcestructures.Constructor;
import com.blazingkin.interpreter.executor.sourcestructures.Method;
import com.blazingkin.interpreter.executor.sourcestructures.Process;
import com.blazingkin.interpreter.executor.sourcestructures.Process.BlockArc;
import com.blazingkin.interpreter.executor.sourcestructures.RegisteredLine;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.library.BlzEventHandler;
import com.blazingkin.interpreter.library.StandAloneEventHandler;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

import in.blazingk.blz.packagemanager.FileImportManager;

public class Executor {

	// Instance objects
	private static BlzEventHandler eventHandler = new StandAloneEventHandler();
	private static ArrayList<Method> methods = new ArrayList<Method>();	// List of all functions within their respective processes
	private static HashMap<String, Constructor> constructor = new HashMap<String, Constructor>();
	private static String startingMethod = "main";
	private static ArrayList<Integer> UUIDsUsed = new ArrayList<Integer>();

	//State Variables
	private static long timeStarted = 0;
	public static boolean immediateMode = false;
	private static boolean closeRequested = false;
	private static boolean breakMode = false;
	private static Value returnBuffer = new Value(VariableTypes.Nil, null);
	
	private static Stack<Integer> processLineStack = new Stack<Integer>();
	
	// This is the main loop
	public static void codeLoop() throws Exception{
		while (!RuntimeStack.isEmpty()){			// while we have a thing to do, we will continue to execute
			for (setLine(getStartOfMain());getLine() < getCurrentProcess().getSize();){
				executeCurrentLine();
			}
			RuntimeStack.pop();
		}
		cleanup();
	}
	
	public static void executeCurrentLine(){
		Process currentProcess = getCurrentProcess();
		if (currentProcess.isRegistered(getLine())){	// If we've already registered this line, we can just run it
			RegisteredLine line = currentProcess.getRegisteredLine(getLine());
			setLine(getLine()+1);
			line.run(Executor.getCurrentContext());
		}
		else{
			String line = currentProcess.getLine(getLine());
			if (line.trim().length() == 0){
				setLine(getLine()+1);
				return;
			}
			String split[] = line.split(" ");
			if (split[0].length() > 0 && split[0].substring(0,1).equals(":")){
				if (RuntimeStack.runtimeStack.peek() instanceof Constructor){
					setLine(getLine()+1);
					Closure closure = new Closure(currentProcess, getLine(), line.trim(), Executor.getCurrentContext());
					Variable.setValue(closure.functionName, Value.closure(closure), closure.context);
					setLine(getCurrentBlockEnd());
				}else{
					Method nM = new Method(currentProcess,getLine(), split[0].substring(1));
					getMethods().add(nM);
					RuntimeStack.push(nM);
					setLine(getLine()+1);
				}				
				return;
			}
			if (split.length == 1 && split[0].equals("")){
				setLine(getLine()+1);
				return;
			}
			setLine(getLine()+1);
			ExpressionExecutor.parseExpression(line, Executor.getCurrentContext());	// If it hasn't been anything so far it must be a simple expression
		}
		if (isCloseRequested()){
			getEventHandler().exitProgram("Close was requested");
			cleanup();
			return;
		}
	}
	
	public static Value functionCall(Method m, Value[] values, boolean passByReference){
		int runtimeStackDepth = RuntimeStack.runtimeStack.size();
		if (m.parent != getCurrentProcess()){
			RuntimeStack.push(m.parent);
		}else{
			getCurrentProcess().lineReturns.add(getLine());
		}
		if (m instanceof Closure){
			Closure clos = (Closure) m;
			RuntimeStack.pushContext(clos.context);
		}
		RuntimeStack.push(m);
		if (m.takesVariables){
			int variableCount = (m.variables.length > values.length?values.length:m.variables.length);
			if (passByReference){
				for (int i = 0; i < variableCount; i++){
					Variable.setValue(m.variables[i], values[i]);
				}
			}else{
				for (int i = 0; i < variableCount; i++){
					Variable.setValue(m.variables[i], (values[i]).clone());
				}
			}
			/* Bind variables that weren't passed to nil */
			for (int i = variableCount; i < m.variables.length; i++) {
				Variable.setValue(m.variables[i], Value.nil());
			}
		}
		setLine(m.lineNumber);
		while (RuntimeStack.runtimeStack.size() > runtimeStackDepth){
			executeCurrentLine();
		}
		return returnBuffer;
	}
	
	private static int getStartOfMain(){
		Method start = getMethodInCurrentProcess(startingMethod);
		if (start != null){
			return start.lineNumber;
		}else{
			return 0;
		}
	}
	
	public static Stack<Integer> getProcessLineStack(){
		return processLineStack;
	}
	
	public static Context getCurrentContext(){
		if (RuntimeStack.contextStack.isEmpty()){
			return Variable.getGlobalContext();
		}
		return RuntimeStack.contextStack.peek();
	}
	
	
	public static void executeMethod(Method m){
		RuntimeStack.push(m);
		setLine(m.getLineNum());
	}
	
	public static void executeMethod(Method m, Value[] values){
		executeMethod(m);
		if (m.takesVariables){
			for (int i = 0; i < (m.variables.length > values.length?values.length:m.variables.length); i++){
				Variable.setValue(m.variables[i], values[i]);
			}
		}
	}
	
	//Run Executor when running from file
	public static void run(File runFile, List<String> args) throws Exception{			// runs the executor
		for (int i = 0; i < args.size(); i+=2){
			String s = args.get(i);
			if (s.substring(0,2).equals("-m")){			// denotation for indicating a starting method
				startingMethod = args.get(i+1);
			}
		}
		// puts the file passed to us as the current process
		RuntimeStack.push(FileImportManager.importFile(runFile.toPath()));	
		RuntimeStack.processLineStack.push(-1);
		setEventHandler(new StandAloneEventHandler());
		Method startMethod = getMethodInCurrentProcess(startingMethod);
		if (startMethod != null){
			executeMethod(startMethod);
		}
		importCore();
		codeLoop();
			
	}
	
	public static void run(String[] code, List<String> args, BlzEventHandler handler) throws Exception{
		for (int i = 0; i < args.size(); i+=2){
			String s = args.get(i);
			if (s.substring(0,2).equals("-m")){			// denotation for indicating a starting method
				startingMethod = args.get(i+1);
			}
		}
		RuntimeStack.push(new Process(code));
		RuntimeStack.processLineStack.push(-1);
		setEventHandler(handler);
		Method startMethod = getMethodInCurrentProcess(startingMethod);
		if (startMethod != null){
			executeMethod(startMethod);
		}
		importCore();
		codeLoop();
	}
	
	//This cleans the execution environment so that another BLZ program can be run without restarting the Java program
	public static void cleanup(){
		RuntimeStack.cleanup();
		in.blazingk.blz.packagemanager.FileImportManager.importedFiles.clear();
		VariableTypes.clear();
		setLoopStack(new Stack<LoopWrapper>());
		setEventHandler(null);
		UUIDsUsed = new ArrayList<Integer>();
		setTimeStarted(0);
		Variable.clearVariables();
		setMethods(new ArrayList<Method>());
		setCloseRequested(false);
		startingMethod = "";
	}
	

	public static void importCore(){
		try {
			in.blazingk.blz.packagemanager.Package.importCore();
		}catch(Exception e){
			e.printStackTrace();
			eventHandler.err(e.getMessage());
			eventHandler.exitProgram("Failed to import core directory");
		}
	}
	
	//Lots of getters / setters below this point
	
	public static int getUUID(){
		int id;
		do{
			id = (int) (Math.random() * Integer.MAX_VALUE);
		}while(UUIDsUsed.contains(id));
		UUIDsUsed.add(id);
		return id;
	}
	
	public static Stack<LoopWrapper> getLoopStack() {
		return RuntimeStack.loopStack;
	}
	
	public static void setLoopStack(Stack<LoopWrapper> loopStack) {
		RuntimeStack.loopStack = loopStack;
	}
	
	public static boolean isImmediateMode(){
		return immediateMode;
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
	
	public static long getTimeStarted() {
		return timeStarted;
	}
	
	public static void setTimeStarted(long timeStarted) {
		Executor.timeStarted = timeStarted;
	}
	
	
	static int lineNum = -1;
	// Sets line within the current process
	public static void setLine(int num){
		lineNum = num;
	}
	
	public static int getLine(){
		return lineNum;
	}
	
	public static Stack<Process> getRunningProcesses(){
		return RuntimeStack.processStack;
	}
	
	public static Process getCurrentProcess(){
		try{
		return RuntimeStack.processStack.peek();
		}catch(Exception e){
			return null;
		}
	}
	
	public static Method getCurrentMethod(){
		try{
			return RuntimeStack.methodStack.peek();
		}catch(Exception e){
			return null;
		}
	}
	
	public static Method getMethodInCurrentProcess(String methodName){
		for (Method m : getCurrentProcess().methods){
			if (m.functionName.equals(methodName)){
				return m;
			}
		}
		return null;
	}
	
	public static void addProcess(Process p){
		processLineStack.push(getLine());
		RuntimeStack.push(p);
	}
	
	public static void addConstructor(String name, Constructor con){
		constructor.put(name, con);
	}
	
	public static Constructor getConstructor(String name){
		return constructor.get(name);
	}
	
	public static Stack<Method> getMethodStack(){
		return RuntimeStack.methodStack;
	}
	
	public static int getCurrentBlockEnd(){
		return getCurrentProcess().blockArcs.get(getLine()).end;
	}
	
	public static int getCurrentBlockStart(){
		return getCurrentProcess().blockArcs.get(getLine()).start;
	}
	
	public static BlockArc getCurrentBlock(){
		return getCurrentProcess().blockArcs.get(getLine());
	}

	public static boolean isBreakMode() {
		return breakMode;
	}

	public static void setBreakMode(boolean breakMode) {
		Executor.breakMode = breakMode;
	}
	
	public static void setReturnBuffer(Value v){
		returnBuffer = v;
	}
	
}
