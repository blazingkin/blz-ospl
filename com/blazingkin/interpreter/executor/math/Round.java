package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Round implements InstructionExecutor, LambdaFunction {

	@Override
	public void run(String[] args) {
		if (Variable.getValue(args[0]).type == VariableTypes.Integer){
			Variable.setValue(args[0], new Value(VariableTypes.Integer, (int)Math.round((Integer)Variable.getValue(args[0]).value)));
		}else if (Variable.getValue(args[0]).type == VariableTypes.Double){
			Variable.setValue(args[0], new Value(VariableTypes.Integer, (int)Math.round((Double)Variable.getValue(args[0]).value)));
		}
	}

	@Override
	public Value evaluate(String[] args) {
		if (Variable.getValue(args[0]).type == VariableTypes.Integer){
			return new Value(VariableTypes.Integer, (int)Math.round((Integer)Variable.getValue(args[0]).value));
		}else if (Variable.getValue(args[0]).type == VariableTypes.Double){
			return new Value(VariableTypes.Integer, (int)Math.round((Double)Variable.getValue(args[0]).value));
		}
		Interpreter.throwError("Incorrect data type given to round as an argument");
		return new Value(VariableTypes.Nil, null);
	}

}
