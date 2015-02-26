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
		Value v = new Value(VariableTypes.Boolean, (Integer.parseInt(Variable.parseString(args[0]))==1)&&(Integer.parseInt(Variable.parseString(args[1]))==1));
		Variable.setValue(args[2], v);
	}

}
