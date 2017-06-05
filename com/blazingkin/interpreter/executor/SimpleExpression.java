package com.blazingkin.interpreter.executor;

import java.util.regex.Pattern;

public enum SimpleExpression {

	multiplication(Pattern.compile("\\*"), 3),
	division(Pattern.compile("\\/"), 3),
	addition(Pattern.compile("\\+"), 4),
	subtraction(Pattern.compile("\\-"), 3),
	assignment(Pattern.compile("(?<!=)=(?!=)"), 6),	// The regex here makes sure it is not a ==
	comparison(Pattern.compile("=="),5),
	parenthesis(Pattern.compile("\\(.*?\\)"), 1000);
	
	
	public final Pattern syntax;		// Regex to check for
	public final int precedence;	// Precedence - Higher numbers first
	SimpleExpression(Pattern syntax, int precedence){
		this.syntax = syntax;
		this.precedence = precedence;
	}
	
}
