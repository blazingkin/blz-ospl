package com.blazingkin.interpreter.executor.input;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Context;

public class StringInput implements InstructionExecutor {
	/*	StringInput
	 * 	Gets a string as input
	 * 
	 */
	public Value run(String l, Context c){
		String result = Executor.getEventHandler().getInput();
		return Value.string(result);
	}

	
}
