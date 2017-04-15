package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.LambdaFunction;
import com.blazingkin.interpreter.executor.LambdaParser;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class RuntimeLambda implements LambdaFunction {

	//First arg (a b c) list of args used in the definition
	//Second arg (lambdaexpr) some lambda expression
	//Rest of args, things to replace the args
	@Override
	public Value evaluate(String[] args) {
		try{
			args[0] = args[0].replace("(", "");
			args[0] = args[0].replace(")", "");
			String[] varNames = args[0].split(" |,");
			for (int i = 0; i < varNames.length; i++){
				String s = varNames[i];
				String d = args[i+2];
				Variable.setValue(s, Variable.getValue(d));
			}
			return LambdaParser.parseLambdaExpression(args[1].trim()).getValue();
		}catch(Exception e){
			e.printStackTrace();
			Interpreter.throwError("Not enough arguments to evaluate lambda expr on line "+Executor.getLine());
			return new Value(VariableTypes.Nil, null);
		}
	}

	public static RuntimeLambda staticRuntimeLambda = new RuntimeLambda();
	
	
}
