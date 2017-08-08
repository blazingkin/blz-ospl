package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.variables.Value;

public interface InstructionExecutor {

	public abstract Value run(String line);
	
}
