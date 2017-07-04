package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;

public class ReturnProcess implements InstructionExecutor {
	
	public void run(String args[]){
		Executor.getCurrentProcess().lineReturns.clear();
		Executor.popStack();
	}


}
