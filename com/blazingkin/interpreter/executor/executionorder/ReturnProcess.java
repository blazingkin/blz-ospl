package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;

public class ReturnProcess implements InstructionExecutorStringArray {
	
	public void run(String args[]){
		Executor.getCurrentProcess().lineReturns.clear();
		Executor.popStack();
	}


}
