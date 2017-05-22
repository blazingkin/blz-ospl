package com.blazingkin.interpreter.executor;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class SimpleExpressionExecutor {

	SimpleExpression type;
	
	public SimpleExpressionExecutor(SimpleExpression type){
		this.type = type;
	}
	
	public Value evaluate(String expr){
		switch(type){
		case addition:
			String[] splits = expr.split(type.syntax);
			if (splits.length < 2){
				Interpreter.throwError("Not enough arguments for addition");
			}
			Value accumulator = SimpleExpressionParser.parseExpression(splits[0]);
			for (int i = 1; i < splits.length; i++){
				accumulator = Variable.addValues(accumulator, SimpleExpressionParser.parseExpression(splits[i]));
			}
			return accumulator;
		case assignment:
			break;
		case comparison:
			break;
		case division:
			break;
		case multiplication:
			break;
		case parenthesis:
			break;
		case subtraction:
			break;
		default:
			Interpreter.throwError("Simple Expression Executor Inititialized with invalid type");
			break;
			
		
		}
		Interpreter.throwError("Simple Expression Executor Inititialized with invalid type");
		return new Value(VariableTypes.Nil, null);
	}
	
	
}
