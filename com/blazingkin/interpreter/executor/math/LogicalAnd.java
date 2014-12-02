package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class LogicalAnd implements InstructionExecutor {
	/*	And
	 * 	Puts in a variable the value of a1 && a2
	 */
	
	public void run(String[] args){
		int i1 = Integer.parseInt(Variable.parseString(args[0]));
		int i2 = Integer.parseInt(Variable.parseString(args[1]));
		Value v = new Value(VariableTypes.Boolean, (i1==1)&&(i2==1));
		Variable.setValue(args[2], v);
	}

}
