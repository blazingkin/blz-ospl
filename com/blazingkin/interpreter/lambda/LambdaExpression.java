package com.blazingkin.interpreter.lambda;


import com.blazingkin.interpreter.variables.Value;

public class LambdaExpression {
	private LambdaFunction func;
	private String[] arguments;
	public LambdaExpression(LambdaFunction f, String[] args){
		func = f;
		arguments = args;
	}
	
	public Value getValue(){
		return func.evaluate(arguments);
	}
	
	public LambdaExpression cloneWithArgs(String[] args){
		String[] newArgs = new String[arguments.length + args.length];
		int counter = 0;
		for (String a : arguments){
			newArgs[counter++] = a;
		}
		for (String a : args){
			newArgs[counter++] = a;
		}
		return new LambdaExpression(func, newArgs);
	}

}
