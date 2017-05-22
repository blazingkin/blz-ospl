package com.blazingkin.interpreter.executor.lambda;


import java.util.HashMap;

public class LambdaRegistrar {

	public static LambdaExpression getLambdaExpression(String name, String[] args){
		if (name.contains("(") && name.contains(")")){
			LambdaExpression le = LambdaParser.parseLambdaExpression(name);
			return ((LambdaExpression) le.getValue().value).cloneWithArgs(args);
		}
		return runtimeDefinedLambdaExpressions.get(name).cloneWithArgs(args);
	}
	
	public static boolean isRegisteredLambdaExpression(String name){
		return runtimeDefinedLambdaExpressions.containsKey(name);
	}
	
	public static void registerLambdaExpression(LambdaExpression le, String expName){
		runtimeDefinedLambdaExpressions.put(expName, le);
	}
	
	private static HashMap<String, LambdaExpression> runtimeDefinedLambdaExpressions =
			new HashMap<String, LambdaExpression>();
}
