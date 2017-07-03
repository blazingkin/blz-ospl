package com.blazingkin.interpreter.executor.instruction;

public abstract interface InstructionExecutor {
	
	public abstract void run(String args[]);		//All of the Executors Implement this interface so that they can be referenced from an enum
}
