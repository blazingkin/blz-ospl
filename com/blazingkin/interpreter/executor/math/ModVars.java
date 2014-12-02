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
		int i1 = Integer.parseInt(Variable.parseString(args[0]));
		int i2 = Integer.parseInt(Variable.parseString(args[1]));
		Value v = new Value(VariableTypes.Integer, i1%i2);
		Variable.setValue(args[2], v);
	}
	
}
