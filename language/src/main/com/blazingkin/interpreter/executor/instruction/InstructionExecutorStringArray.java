package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Context;

public abstract interface InstructionExecutorStringArray extends InstructionExecutor {
	

	public default Value run(String line, Context con){
		return run(line.split(" "));
	}
	

	
	public abstract Value run(String args[]);		//All of the Executors Implement this interface so that they can be referenced from an enum
}
