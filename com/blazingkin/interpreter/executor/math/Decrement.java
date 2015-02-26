package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Decrement implements InstructionExecutor {
	/*	Decrement
	 * 	Decrements the value of a variable
	 */

	public void run(String[] args){
		if (Variable.contains(args[0])){
			Variable.setValue(args[0], new Value(VariableTypes.Integer, (Integer)(Variable.getValue(args[0]).value)-1));
		}
	}
	
}
