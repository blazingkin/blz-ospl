package com.blazingkin.interpreter.variables;

import java.util.Stack;

public class LocalStack {
	/*
	 * @LocalStack
	 * This is the stack that the user puts variables on to
	 */
	
	private static Stack<Value> theStack = new Stack<Value>();
	
	public static void push(Value o){
		theStack.push(o);
	}
	
	public static Value pop(){
		return theStack.pop();
	}
	
	public static Value peek(){
		return theStack.peek();
	}
}
