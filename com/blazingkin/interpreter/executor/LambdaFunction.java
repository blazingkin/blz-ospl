package com.blazingkin.interpreter.executor;

import com.blazingkin.interpreter.variables.Value;

public interface LambdaFunction {

	public abstract Value evaluate(String[] args); //Evaluates the lambda expression
	
}
