package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Unsign implements InstructionExecutor {
	/*	Absolute Value
	 * 	Unsigns a variable
	 */
	public void run(String[] args) {
		Variable.setValue(args[0], new Value(VariableTypes.Integer, Math.abs((Integer) Variable.getValue(args[0]).value)));
	}

}
