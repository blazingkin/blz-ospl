package com.blazingkin.interpreter.executor;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

@Deprecated
public class SimpleExpressionParser {
	public static Value parseExpression(String expr){
		expr = expr.trim();
		if (Variable.isInteger(expr)){
			return new Value(VariableTypes.Integer, new BigInteger(expr));
		}
		if (Variable.isDouble(expr)){
			return new Value(VariableTypes.Double, new BigDecimal(expr));
		}
		if (Variable.isBool(expr)){
			return new Value(VariableTypes.Boolean, Variable.convertToBool(expr));
		}
		for (SimpleExpression se : SimpleExpressionParser.functions){
			if (se.syntax.matcher(expr).find()){
				return SimpleExpressionExecutor.evaluate(se, expr);
			}
		}
		return Variable.getValue(expr.trim());
	}
	
	public final static SimpleExpression[] functions;
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
