package com.blazingkin.interpreter.executor;

public enum SimpleExpression {

	multiplication("*", 4),
	division("/", 4),
	addition("+", 3),
	subtraction("-", 3),
	assignment("=", 1),
	comparison("==",2),
	parenthesis("\\(.*\\)", 1000);
	
	
	public final String syntax;		// Regex to check for
	public final int precedence;	// Precedence - Higher numbers first
	SimpleExpression(String syntax, int precedence){
		this.syntax = syntax;
		this.precedence = precedence;
	}
	
}
