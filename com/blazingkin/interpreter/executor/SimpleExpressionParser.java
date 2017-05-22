package com.blazingkin.interpreter.executor;

import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class SimpleExpressionParser {
	public final static SimpleExpression[] functions;
	public static Value parseExpression(String expr){
		/*for (SimpleExpression se : SimpleExpressionParser.functions){
			
		}*/
		return new Value(VariableTypes.Nil, null);
	}
	
	
	static {
		SimpleExpression[] fncts = SimpleExpression.values();
		for (int i = 1; i < fncts.length; i++){	// Insertion Sort Functions By Priority
			for (int y = i; y > 0; y--){
				if (fncts[y].precedence > fncts[y-1].precedence){
					SimpleExpression temp = fncts[y];
					fncts[y] = fncts[y-1];
					fncts[y-1] = temp;
				}
			}
		}
		functions = fncts;
	}
	
}
