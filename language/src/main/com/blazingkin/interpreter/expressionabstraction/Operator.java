package com.blazingkin.interpreter.expressionabstraction;


public enum Operator{
	
	// The enum values that start with lowercase do not follow
	// normal operator notation

	String("", 1, OperatorType.Unary),
	Ident("", 1, OperatorType.Unary),
	Number("", 1, OperatorType.Unary),	
	Increment("++", 1, OperatorType.Unary),	
	Decrement("--", 1, OperatorType.Unary),
	DotOperator(".", 2, OperatorType.Binary),
	Modulus("%", 2, OperatorType.Binary),
	Multiplication("*", 3, OperatorType.Binary),
	Division("/", 3, OperatorType.Binary),
	ExclusiveOr("^", 3, OperatorType.Binary),
	BitwiseAnd("&", 3, OperatorType.Binary),
	LeftShift("<<", 3, OperatorType.Binary),
	RightShift(">>", 3, OperatorType.Binary),
	Addition("+", 4, OperatorType.Binary),
	Subtraction("-", 3, OperatorType.UnaryOrBinary),
	Exponentiation("**", 5, OperatorType.Binary),
	Logarithm("__", 5, OperatorType.Binary),
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
	Lambda("->", 10, OperatorType.Binary),
	Assignment("=", 11, OperatorType.Binary),
	ExpressionDelimit(";", 20, OperatorType.Binary),
	functionCall("", 10000, OperatorType.Binary),
	parensOpen("", 10000, OperatorType.None),
	parensClose("", 10000, OperatorType.None),
	arrayLookup("", 10000, OperatorType.Binary),
	sqBracketOpen("", 10000, OperatorType.Binary),
	sqBracketClose("", 10000, OperatorType.Binary),
	arrayLiteral("", 10000, OperatorType.Unary),
	environmentVariableLookup("",10000, OperatorType.Unary);

	public final String syntax;
	public final int precedence;
	public final OperatorType type;
	Operator(String syntax, int precedence, OperatorType type){
		this.syntax = syntax;
		this.precedence = precedence;
		this.type = type;
	}
	
	
}
