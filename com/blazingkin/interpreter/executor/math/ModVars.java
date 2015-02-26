package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ModVars implements InstructionExecutor {
	/*	Mod
	 * 	Gets the remainder of 2 values
	 */
	public void run(String args[]){
		Value v = new Value(VariableTypes.Integer, Integer.parseInt(Variable.parseString(args[0]))%Integer.parseInt(Variable.parseString(args[1])));
		Variable.setValue(args[2], v);
	}
	
}
