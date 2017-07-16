package com.blazingkin.interpreter.executor.lambda;


import java.util.HashMap;

public class LambdaRegistrar {

	public static LambdaExpression getLambdaExpression(String name, String[] args){
		if (name.contains("(") && name.contains(")")){
			LambdaExpression le = LambdaParser.parseLambdaExpression(name.toUpperCase());
			return ((LambdaExpression) le.getValue().value).cloneWithArgs(args);
		}
		return runtimeDefinedLambdaExpressions.get(name.toUpperCase()).cloneWithArgs(args);
	}
	
	public static boolean isRegisteredLambdaExpression(String name){
		return runtimeDefinedLambdaExpressions.containsKey(name.toUpperCase());
	}
	
	public static void registerLambdaExpression(LambdaExpression le, String expName){
		runtimeDefinedLambdaExpressions.put(expName.toUpperCase(), le);
	}
	
	private static HashMap<String, LambdaExpression> runtimeDefinedLambdaExpressions =
			new HashMap<String, LambdaExpression>();
}
