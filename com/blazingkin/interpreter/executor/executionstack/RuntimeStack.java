package com.blazingkin.interpreter.executor.executionstack;

import java.util.Stack;

import com.blazingkin.interpreter.executor.Method;
import com.blazingkin.interpreter.executor.executionorder.LoopWrapper;
import com.blazingkin.interpreter.variables.Value;

public class RuntimeStack {
	public static Stack<RuntimeStackElement> runtimeStack = new Stack<RuntimeStackElement>();
	public static Stack<Process> processStack = new Stack<Process>();
	public static Stack<Method> methodStack = new Stack<Method>();
	public static Stack<LoopWrapper> loopStack = new Stack<LoopWrapper>();
	
	
	public static void push(RuntimeStackElement se){

	}
	
	public static Value pop(){
		return null;
	}
}
