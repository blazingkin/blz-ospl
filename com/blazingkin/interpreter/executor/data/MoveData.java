package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Variable;

@Deprecated
public class MoveData implements InstructionExecutorStringArray {
	/*	MoveData
	 * 	Copies the value stored in one variable to another place
	 */
	public void run(String[] args) {
		if (Variable.contains(args[0])){
			Variable.setValue(args[1], Variable.getValue(args[0]));
		}
	}
	
}
