package com.blazingkin.interpreter.executor.executionstack;

import java.util.Stack;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.executionorder.LoopWrapper;
import com.blazingkin.interpreter.executor.sourcestructures.Method;
import com.blazingkin.interpreter.executor.sourcestructures.Process;
import com.blazingkin.interpreter.executor.sourcestructures.RegisteredLine;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class RuntimeStack {
	
	public static Stack<RuntimeStackElement> runtimeStack = new Stack<RuntimeStackElement>();
	public static Stack<Process> processStack = new Stack<Process>();
	public static Stack<Method> methodStack = new Stack<Method>();
	public static Stack<Context> contextStack = new Stack<Context>();
	public static Stack<LoopWrapper> loopStack = new Stack<LoopWrapper>();
	public static Stack<Integer> processLineStack = new Stack<Integer>();
	public static Stack<Context> processContextStack = new Stack<Context>();
	
	public static void push(RuntimeStackElement se){
		runtimeStack.push(se);
		if (se instanceof RegisteredLine){
			
		}else if (se instanceof LoopWrapper){
			loopStack.push((LoopWrapper)se);
		}else if (se instanceof Method){
			methodStack.push((Method) se);
			contextStack.push(new Context(processContextStack.peek()));
		}else if (se instanceof Process){
			processStack.push((Process) se);
			contextStack.push(new Context(Variable.getGlobalContext()));
			processContextStack.push(contextStack.peek());
			processLineStack.push(Executor.getLine());
		}
		se.onBlockStart();
	}
	
	public static Value pop(){
		RuntimeStackElement se = runtimeStack.pop();
		if (se instanceof RegisteredLine){
			return ((RegisteredLine) se).run();
		}else if (se instanceof LoopWrapper){
			loopStack.pop();
		}else if (se instanceof Method){
			Method m = methodStack.pop();
			Variable.killContext(contextStack.pop());
			se.onBlockEnd();
			return new Value(VariableTypes.Method, m);
		}else if (se instanceof Process){
			processStack.pop();
			processContextStack.pop();
			Variable.killContext(contextStack.pop());
			Executor.setLine(processLineStack.pop());
		}
		se.onBlockEnd();
		if (runtimeStack.empty() && !Executor.immediateMode){
			Executor.setCloseRequested(true);
			Executor.getEventHandler().exitProgram("Reached end of program");
			return null;
		}
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
		methodStack.clear();
		contextStack.clear();
		loopStack.clear();
	}
}
