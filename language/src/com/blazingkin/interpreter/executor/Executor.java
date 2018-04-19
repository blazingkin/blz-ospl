package com.blazingkin.interpreter.executor;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
	private static BlzEventHandler eventHandler = new StandAloneEventHandler();
	private static String startingMethod = "main";
	private static ArrayList<Integer> UUIDsUsed = new ArrayList<Integer>();

	//State Variables
	private static long timeStarted = 0;
	public static boolean immediateMode = false;
	private static boolean closeRequested = false;
	private static boolean returnMode = false;
	private static boolean continueMode = false;
	private static boolean breakMode = false;
	private static Value returnBuffer = new Value(VariableTypes.Nil, null);
	
	private static Stack<Integer> processLineStack = new Stack<Integer>();

	public static Stack<Integer> getProcessLineStack(){
		return processLineStack;
	}
	
	public static Context getCurrentContext(){
		if (RuntimeStack.contextStack.isEmpty()){
			return Variable.getGlobalContext();
		}
		return RuntimeStack.contextStack.peek();
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
		importCore();
		RuntimeStack.push(FileImportManager.importFile(runFile.toPath()));	
		RuntimeStack.processLineStack.push(-1);
		setEventHandler(new StandAloneEventHandler());
		MethodNode startMethod = getMethodInCurrentProcess(startingMethod);
		if (startMethod != null){
			Value blank[] = {};
			startMethod.execute(new Context(), blank, false);
		}
		eventHandler.exitProgram("");
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
		MethodNode startMethod = getMethodInCurrentProcess(startingMethod);
		importCore();
		if (startMethod != null){
			Value blank[] = {};
			startMethod.execute(getCurrentContext(), blank, false);
		}
		eventHandler.exitProgram("");
	}
	
	//This cleans the execution environment so that another BLZ program can be run without restarting the Java program
	public static void cleanup(){
		RuntimeStack.cleanup();
		in.blazingk.blz.packagemanager.FileImportManager.importedFiles.clear();
		VariableTypes.clear();
		setEventHandler(null);
		UUIDsUsed = new ArrayList<Integer>();
		setTimeStarted(0);
		Variable.clearVariables();
		setCloseRequested(false);
		setBreakMode(false);
		setContinueMode(false);
		setReturnBuffer(Value.nil());
		setReturnMode(false);
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
	
	
	public static boolean isImmediateMode(){
		return immediateMode;
	}
	
	public static BlzEventHandler getEventHandler() {
		return eventHandler;
	}
	
	public static void setEventHandler(BlzEventHandler eventHandler) {
		Executor.eventHandler = eventHandler;
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
	
	public static ArrayDeque<Process> getRunningProcesses(){
		return RuntimeStack.processStack;
	}
	
	public static Process getCurrentProcess(){
		try{
		return RuntimeStack.processStack.peek();
		}catch(Exception e){
			return null;
		}
	}
	
	
	public static MethodNode getMethodInCurrentProcess(String methodName){
		for (MethodNode m : getCurrentProcess().methods){
			if (m.getStoreName().equals(methodName)){
				return m;
			}
		}
		return null;
	}
	
	public static void addProcess(Process p){
		processLineStack.push(getLine());
		RuntimeStack.push(p);
	}
	
	public static boolean isReturnMode() {
		return returnMode;
	}

	public static boolean isBreakMode() {
		return breakMode;
	}

	public static void setReturnMode(boolean returnMode) {
		Executor.returnMode = returnMode;
	}
	
	public static void setReturnBuffer(Value v){
		returnBuffer = v;
	}

	public static void setContinueMode(boolean continueMode){
		Executor.continueMode = continueMode;
	}


	public static boolean shouldBlockBreak(){
		return returnMode || continueMode || breakMode;
	}

	public static Value getReturnBuffer(){
		return returnBuffer;
	}

	public static void setBreakMode(boolean bm){
		breakMode = bm;
	}
	
}
