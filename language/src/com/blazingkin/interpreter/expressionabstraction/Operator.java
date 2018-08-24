package com.blazingkin.interpreter.expressionabstraction;

import java.util.HashMap;
import java.util.HashSet;

public enum Operator{
	
	// The enum values that start with lowercase do not follow
	// normal operator notation

	String("", 1, OperatorType.Unary),
	Ident("", 1, OperatorType.Unary),
	Number("", 1, OperatorType.Unary),	
	Increment("++", 1, OperatorType.Unary),	
	Decrement("--", 1, OperatorType.Unary),
	DotOperator(".", 2, OperatorType.Binary),
	Modulus("%", 2, OperatorType.UnaryOrBinary),
	Multiplication("*", 3, OperatorType.Binary),
	Division("/", 3, OperatorType.Binary),
	Addition("+", 4, OperatorType.Binary),
	Subtraction("-", 3, OperatorType.UnaryOrBinary),
	Exponentiation("**", 5, OperatorType.Binary),
	Logarithm("__", 5, OperatorType.UnaryOrBinary),
	CommaDelimit(",", 6, OperatorType.Binary),
	Comparison("==",7, OperatorType.Binary),
	LessThan("<", 7, OperatorType.Binary),
	GreaterThan(">", 7, OperatorType.Binary),
	NotEqual("!=", 7, OperatorType.Binary),
	ApproximateComparison("~=", 7, OperatorType.Binary),
	LessThanEqual("<=", 8, OperatorType.Binary),
	GreaterThanEqual(">=", 8, OperatorType.Binary),
	Exclam("", 8, OperatorType.Unary),
	LogicalAnd("&&", 9, OperatorType.Binary),
	LogicalOr("||", 9, OperatorType.Binary),
	Assignment("=", 10, OperatorType.Binary),
	ExpressionDelimit(";", 20, OperatorType.Binary),
	Lambda("->", 25, OperatorType.Binary),
	functionCall("", 10000, OperatorType.Binary),
	parensOpen("", 10000, OperatorType.None),
	parensClose("", 10000, OperatorType.None),
	arrayLookup("", 10000, OperatorType.Binary),
	sqBracketOpen("", 10000, OperatorType.Binary),
	sqBracketClose("", 10000, OperatorType.Binary),
	arrayLiteral("", 10000, OperatorType.Unary),
	environmentVariableLookup("",10000, OperatorType.Unary),
	environmentVariableOpen("",10000, OperatorType.Unary),
	environmentVariableClose("",10000, OperatorType.Unary);
	
	public final String syntax;
	public final int precedence;
	public final OperatorType type;
	Operator(String syntax, int precedence, OperatorType type){
		this.syntax = syntax;
		this.precedence = precedence;
		this.type = type;
	}
	
	
	public static HashSet<String> symbols = new HashSet<String>();
	public static HashMap<String, Operator> symbolLookup = new HashMap<String, Operator>();
	static{
		for (Operator op : Operator.values()){
			if (op.syntax.equals("")){
				continue;
			}
			String building = "";
			for (char ch : op.syntax.toCharArray()){
				building += ch;
				symbols.add(building);
			}
			symbolLookup.put(building, op);
		}
	}

}
