package com.blazingkin.interpreter.executor.executionstack;

import java.util.ArrayDeque;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.sourcestructures.Process;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class RuntimeStack {
	
	public static ArrayDeque<RuntimeStackElement> runtimeStack = new ArrayDeque<RuntimeStackElement>();
	public static ArrayDeque<Process> processStack = new ArrayDeque<Process>();
	public static ArrayDeque<Context> contextStack = new ArrayDeque<Context>();
	public static ArrayDeque<Context> processContextStack = new ArrayDeque<Context>();
	
	public static void push(RuntimeStackElement se) throws BLZRuntimeException{
		runtimeStack.push(se);
	    if (se instanceof Process){
			processStack.push((Process) se);
			contextStack.push(new Context(Variable.getGlobalContext()));
			processContextStack.push(contextStack.peek());
		}
		se.onBlockStart();
	}
	
	public static Value pop(){
		RuntimeStackElement se = runtimeStack.pop();
		if (se instanceof Process){
			processStack.pop();
			processContextStack.pop();
			Variable.killContext(contextStack.pop());
		}
		se.onBlockEnd();
		return null;
	}
	
	public static void pushContext(Context con){
		contextStack.push(con);
	}
	
	public static Context popContext(){
		return contextStack.pop();
	}

	public static boolean isEmpty() {
		return runtimeStack.isEmpty();
	}
	
	public static void cleanup(){
		runtimeStack.clear();
		processStack.clear();
		contextStack.clear();
	}
}
