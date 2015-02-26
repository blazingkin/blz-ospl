package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class SubVars implements InstructionExecutor {
	/*	Subtract
	 * 	Subtracts two numbers and stores them in a third variable
	 */
	public void run(String[] args) {
		Value v = new Value(VariableTypes.Integer, Long.parseLong(Variable.parseString(args[0]))-Long.parseLong(Variable.parseString(args[1])));
		Variable.setValue(args[2], v);
	}

}
