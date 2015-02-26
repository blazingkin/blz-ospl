package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class DivideVars implements InstructionExecutor {
	/*	Divide
	 * 	Divides two numbers and puts the output as a variable
	 */
	public void run(String[] args) {
		Value v = new Value(VariableTypes.Double, (long)Long.parseLong(Variable.parseString(args[0]))/(long)Long.parseLong(Variable.parseString(args[1])));
		Variable.setValue(args[2], v);
	}
	
	
}
