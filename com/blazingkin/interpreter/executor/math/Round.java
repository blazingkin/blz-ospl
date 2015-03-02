package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Round implements InstructionExecutor {

	@Override
	public void run(String[] args) {
		if (Variable.getValue(args[0]).type == VariableTypes.Integer){
			Variable.setValue(args[0], new Value(VariableTypes.Integer, (int)Math.round((Integer)Variable.getValue(args[0]).value)));
		}else if (Variable.getValue(args[0]).type == VariableTypes.Double){
			Variable.setValue(args[0], new Value(VariableTypes.Integer, (int)Math.round((Double)Variable.getValue(args[0]).value)));
		}
	}

}
