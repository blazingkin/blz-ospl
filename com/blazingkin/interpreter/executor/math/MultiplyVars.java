package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class MultiplyVars implements InstructionExecutor {
	/*	Multiply
	 * 	Multiplies 2 numbers and stores them as a third variable
	 */
	public void run(String[] args) {
		long i1 = Long.parseLong(Variable.parseString(args[0]));
		long i2 = Long.parseLong(Variable.parseString(args[1]));
		Value v = new Value(VariableTypes.Integer, i1*i2);
		Variable.setValue(args[2], v);
	}

}
