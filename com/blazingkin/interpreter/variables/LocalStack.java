package com.blazingkin.interpreter.variables;

import java.util.Stack;

public class LocalStack {
	
	private static Stack<Integer> theStack = new Stack<Integer>();
	
	public static void push(int o){
		theStack.push(o);
	}
	
	public static int pop(){
		return theStack.pop();
	}
	
	public static int peek(){
		return theStack.peek();
	}
	
	

}
