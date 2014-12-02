package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;

public class End implements InstructionExecutor {

	public void run(String[] args){
		/*	End
		 * 	Will set the line to the top number of the lineReturns
		 * 	If none are left it will set the close requested flag to true
		 */
		Executor.popStack();
	}
	
}
