package com.blazingkin.interpreter.executor;

import java.util.regex.Pattern;

public enum SimpleExpression {

	preorderIncrement(Pattern.compile("^\\s*\\+\\+"), 1),	// Check for things of the form ++var
	preorderDecrement(Pattern.compile("^\\s*\\-\\-"), 1),	// Check for things of the form --var
	modulus(Pattern.compile("%"), 2),
	multiplication(Pattern.compile("\\*"), 3),
	division(Pattern.compile("\\/"), 3),
	addition(Pattern.compile("(?<!\\+)\\+(?!\\+)"), 4),	// Make sure it's not an increment
	subtraction(Pattern.compile("(?<!\\-)\\-(?!\\-)"), 3),	// Make sure it's not a decrement
	exponentiation(Pattern.compile("\\*\\*"), 5),	// Two literal astersisk
	logarithm(Pattern.compile("__"), 5),			// Two literal underscores
	assignment(Pattern.compile("(?<!\\!)(?<!\\>)(?<!\\<)(?<!~)(?<!=)=(?!=)(?!~)(?!\\!)(?!\\<)(?!\\>)"), 9),	// The regex here makes sure it is not a == or a ~=
	comparison(Pattern.compile("=="),7),
	lessThan(Pattern.compile("\\<"), 7),
	greaterThan(Pattern.compile("\\>"), 7),
	lessThanEqual(Pattern.compile("(\\<=|=\\<)"), 8),	// These are higher priority than > / < because otherwise the regex is more complicated and it shouldn't be functionally different
	greaterThanEqual(Pattern.compile("(\\>=|=\\>)"), 8),
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
