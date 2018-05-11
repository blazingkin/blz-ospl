package com.blazingkin.interpreter.executor.input;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Value;

public class StringInput implements InstructionExecutorStringArray {
	/*	StringInput
	 * 	Gets a string as input
	 * 
	 */
	public Value run(String[] vars){
		return Value.string(Executor.getEventHandler().getInput());
	}

	
}
