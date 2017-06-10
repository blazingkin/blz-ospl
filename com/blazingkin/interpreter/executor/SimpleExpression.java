package com.blazingkin.interpreter.executor;

import java.util.regex.Pattern;

public enum SimpleExpression {

	multiplication(Pattern.compile("\\*"), 3),
	division(Pattern.compile("\\/"), 3),
	addition(Pattern.compile("\\+"), 4),
	subtraction(Pattern.compile("\\-"), 3),
	exponentiation(Pattern.compile("\\*\\*"), 5),	// Two literal astersisk
	logarithm(Pattern.compile("__"), 5),			// Two literal underscores
	assignment(Pattern.compile("(?<!\\!)(?<!\\>)(?<!\\<)(?<!~)(?<!=)=(?!=)(?!~)(?!\\!)(?!\\<)(?!\\>)"), 8),	// The regex here makes sure it is not a == or a ~=
	comparison(Pattern.compile("=="),7),
	lessThan(Pattern.compile("\\<"), 7),
	greaterThan(Pattern.compile("\\>"), 7),
	lessThanEqual(Pattern.compile("(\\<=|=\\<)"), 7),
	greaterThanEqual(Pattern.compile("(\\>=|=\\>)"), 7),
	notEqual(Pattern.compile("(!=|=!)"), 7),
	approximateComparison(Pattern.compile("((~=)|(=~))"), 6),// Approximately equal to
	parenthesis(Pattern.compile("\\(.*?\\)"), 1000);
	
	
	public final Pattern syntax;		// Regex to check for
	public final int precedence;	// Precedence - Higher numbers first
	SimpleExpression(Pattern syntax, int precedence){
		this.syntax = syntax;
		this.precedence = precedence;
	}
	
}
