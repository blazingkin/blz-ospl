package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.executor.lambda.LambdaExpression;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.executor.lambda.LambdaRegistrar;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Define implements InstructionExecutorStringArray, LambdaFunction {

	private boolean registerFunctions;
	public Define(boolean shouldRegister){
		registerFunctions = shouldRegister;
	}
	
	@Override
	//arg1 is function name
	//arg2 is the list of variables
	//arg3 is the lambda expression
	public void run(String[] args) {
		String[] newArgs = new String[args.length-1];
		for (int i = 1; i < args.length; i++){
			newArgs[i-1] = args[i];
		}
		LambdaExpression le = new LambdaExpression(RuntimeLambda.staticRuntimeLambda, newArgs);
		if (registerFunctions){
			LambdaRegistrar.registerLambdaExpression(le, args[0]);
		}
	}

	@Override
	public Value evaluate(String[] args) {
		String[] newArgs = new String[args.length-1];
		for (int i = 1; i < args.length; i++){
			newArgs[i-1] = args[i];
		}
		LambdaExpression le = new LambdaExpression(RuntimeLambda.staticRuntimeLambda, newArgs);
		if (registerFunctions){
			LambdaRegistrar.registerLambdaExpression(le, args[0]);
		}
		return new Value(VariableTypes.LambdaExpression, le);
	}

}
