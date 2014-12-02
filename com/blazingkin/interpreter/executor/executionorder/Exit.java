package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;

public class Exit implements InstructionExecutor {

	@Override
	public void run(String[] args) {
		/*	Exit
		 * 	sets the closeRequested flag to true
		 * 
		 */
		Executor.closeRequested = true;
	}

}
