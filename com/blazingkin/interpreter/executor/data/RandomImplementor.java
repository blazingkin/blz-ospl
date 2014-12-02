package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class RandomImplementor implements InstructionExecutor {
	/*	Random
	 * Returns a random integer from 0-99
	 * 
	 */
	@Override
	public void run(String[] args) {
		Variable.setValue(args[0], new Value(VariableTypes.Integer,(int)(Math.random()*100)));
	}

}
