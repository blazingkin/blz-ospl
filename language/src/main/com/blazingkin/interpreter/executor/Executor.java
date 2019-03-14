package com.blazingkin.interpreter.executor;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.astnodes.MethodNode;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.sourcestructures.Process;
import com.blazingkin.interpreter.library.BlzEventHandler;
import com.blazingkin.interpreter.library.StandAloneEventHandler;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

import in.blazingk.blz.packagemanager.FileImportManager;

public class Executor {

	// Instance objects
	private static BlzEventHandler eventHandler;
	public static String startingMethod = "main";
	private static ArrayList<Integer> UUIDsUsed = new ArrayList<Integer>();

	//State Variables
	private static long timeStarted = 0;
	public static ThreadLocal<Boolean> immediateMode = new ThreadLocal<Boolean>() {
		@Override protected Boolean initialValue() {
			return false;
		}
	};
	private static ThreadLocal<Boolean> closeRequested = new ThreadLocal<Boolean>() {
		@Override protected Boolean initialValue() {
			return false;
		}
	};
	private static ThreadLocal<Boolean> returnMode = new ThreadLocal<Boolean>() {
		@Override protected Boolean initialValue() {
			return false;
		}
	};
	private static ThreadLocal<Boolean> continueMode = new ThreadLocal<Boolean>() {
		@Override protected Boolean initialValue() {
			return false;
		}
	};
	private static ThreadLocal<Boolean> breakMode = new ThreadLocal<Boolean>() {
		@Override protected Boolean initialValue() {
			return false;
		}
	};
	private static ThreadLocal<Value> returnBuffer = new ThreadLocal<>();

	public static void initializeThreadLocal(){
		immediateMode.set(false);
		closeRequested.set(false);
		returnMode.set(false);
		continueMode.set(false);
		breakMode.set(false);
		returnBuffer.set(Value.nil());
	}

	private static String[] programArguments = {};



	public static Value getProgramArguments(){
		Value[] args = new Value[programArguments.length];
		for (int i = 0; i < programArguments.length; i++){
			args[i] = Value.string(programArguments[i]);
		}
		return Value.arr(args);
	}

	
	
	//Run Executor when running from file
	public static void run(File runFile, List<String> args) throws BLZRuntimeException {			// runs the executor
		initializeThreadLocal();
		setEventHandler(new StandAloneEventHandler());
		handleArgs(args);
		// puts the file passed to us as the current process
		importCore();
		RuntimeStack.push(FileImportManager.importFile(runFile.toPath()));	
		setEventHandler(new StandAloneEventHandler());
		MethodNode startMethod = getMethodInCurrentProcess(startingMethod);
		if (startMethod != null){
			Value blank[] = {};
			startMethod.execute(new Context(), blank, false);
		}
		eventHandler.exitProgram("");
	}
	
	public static void run(String[] code, List<String> args, BlzEventHandler handler) throws BLZRuntimeException {
		initializeThreadLocal();
		handleArgs(args);
		RuntimeStack.push(new Process(code));
		setEventHandler(handler);
		MethodNode startMethod = getMethodInCurrentProcess(startingMethod);
		importCore();
		if (startMethod != null){
			Value blank[] = {};
			startMethod.execute(Variable.getGlobalContext(), blank, false);
		}
		eventHandler.exitProgram("");
	}

	public static void handleArgs(List<String> args){
		ArrayList<String> programArgs = new ArrayList<String>();
		for (int i = 0; i < args.size(); i++){
			String s = args.get(i);
			if (s.length() >= 2 && s.substring(0,2).equals("-m") && i != args.size() - 1){			// denotation for indicating a starting method
				i++;
				startingMethod = args.get(i);
				continue;
			}
			programArgs.add(s);
		}
		programArguments = new String[programArgs.size()];
		programArgs.toArray(programArguments);
	}

	
	//This cleans the execution environment so that another BLZ program can be run without restarting the Java program
	public static void cleanup(){
		RuntimeStack.cleanup();
		in.blazingk.blz.packagemanager.FileImportManager.importedFiles.clear();
		VariableTypes.clear();
		UUIDsUsed = new ArrayList<Integer>();
		setTimeStarted(0);
		Variable.clearVariables();
		setCloseRequested(false);
		setBreakMode(false);
		setContinueMode(false);
		setReturnBuffer(Value.nil());
		setReturnMode(false);
		startingMethod = "main";
		programArguments = new String[0];
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
	
	
	public static boolean isImmediateMode(){
		return immediateMode.get();
	}
	
	public static BlzEventHandler getEventHandler() {
		return eventHandler;
	}
	
	public static void setEventHandler(BlzEventHandler eventHandler) {
		Executor.eventHandler = eventHandler;
	}
	
	
	public static boolean isCloseRequested() {
		return closeRequested.get();
	}
	
	public static void setCloseRequested(boolean closeRequested) {
		Executor.closeRequested.set(closeRequested);
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
	
	public static ArrayDeque<Process> getRunningProcesses(){
		return RuntimeStack.getProcessStack();
	}
	
	public static Process getCurrentProcess(){
		try{
		return RuntimeStack.getProcessStack().peek();
		}catch(Exception e){
			return null;
		}
	}
	
	
	public static MethodNode getMethodInCurrentProcess(String methodName){
		Process p = getCurrentProcess();
		if (p == null){
			return null;
		}
		for (MethodNode m : p.methods){
			if (m.getStoreName().equals(methodName)){
				return m;
			}
		}
		return null;
	}
	
	public static void addProcess(Process p) throws BLZRuntimeException {
		RuntimeStack.push(p);
	}
	
	public static boolean isReturnMode() {
		return returnMode.get();
	}

	public static boolean isBreakMode() {
		return breakMode.get();
	}

	public static void setReturnMode(boolean returnMode) {
		Executor.returnMode.set(returnMode);
	}
	
	public static void setReturnBuffer(Value v){
		returnBuffer.set(v);
	}

	public static void setContinueMode(boolean continueMode){
		Executor.continueMode.set(continueMode);
	}


	public static boolean shouldBlockBreak(){
		return returnMode.get() || continueMode.get() || breakMode.get();
	}

	public static Value getReturnBuffer(){
		return returnBuffer.get();
	}

	public static void setBreakMode(boolean bm){
		breakMode.set(bm);
	}

	class ExecutorState {

	}
	
}
