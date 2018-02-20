package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Value;

public class Exit implements InstructionExecutorStringArray {

	@Override
	public Value run(String[] args) {
		/*	Exit
		 * 	sets the closeRequested flag to true
		 */
		Executor.setCloseRequested(true);
		return Value.nil();
	}

}
