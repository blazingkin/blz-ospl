package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Context;

public interface InstructionExecutor {

	public abstract Value run(String line, Context con);
	
}
