package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ExponentVars implements InstructionExecutor {

	public void run(String args[]){
		long i1 = Long.parseLong(Variable.parseString(args[0]));
		long i2 = Long.parseLong(Variable.parseString(args[1]));
		Value v = new Value(VariableTypes.Integer, (long)Math.pow(i1, i2));
		Variable.setValue(args[2], v);
	}
	
}
