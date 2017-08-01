package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public abstract interface InstructionExecutorStringArray extends InstructionExecutor {
	
	public default Value run(String line){
		run(Executor.parseExpressions(line));
		return new Value(VariableTypes.Nil, null);
	}
	
	public abstract void run(String args[]);		//All of the Executors Implement this interface so that they can be referenced from an enum
}
