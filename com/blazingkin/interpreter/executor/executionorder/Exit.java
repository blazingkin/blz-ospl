package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;

public class Exit implements InstructionExecutorStringArray {

	@Override
	public void run(String[] args) {
		/*	Exit
		 * 	sets the closeRequested flag to true
		 */
		Executor.setCloseRequested(true);
	}

}
