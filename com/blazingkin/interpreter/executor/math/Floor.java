package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Floor implements InstructionExecutor, LambdaFunction {

	public void run(String[] args) {
		if (Variable.getValue(args[0]).type == VariableTypes.Integer){
			Variable.setValue(args[0], new Value(VariableTypes.Integer, (int)Math.floor((Integer)Variable.getValue(args[0]).value)));
		}else if (Variable.getValue(args[0]).type == VariableTypes.Double){
			Variable.setValue(args[0], new Value(VariableTypes.Integer, (int)Math.floor((Double)Variable.getValue(args[0]).value)));
		}
	}

	@Override
	public Value evaluate(String[] args) {
		if (Variable.getValue(args[0]).type == VariableTypes.Integer){
			return new Value(VariableTypes.Integer, (int)Math.floor((Integer)Variable.getValue(args[0]).value));
		}else if (Variable.getValue(args[0]).type == VariableTypes.Double){
			return new Value(VariableTypes.Integer, (int)Math.floor((Double)Variable.getValue(args[0]).value));
		}
		Interpreter.throwError("Wrong Datatypes for Floor");
		return new Value(VariableTypes.Nil, null);
	}
	
}
