package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;

@Deprecated
public class Goto implements InstructionExecutor {

	public void run(String[] args) {
		/*	Goto
		 * 	Sets to the line number given
		 * 
		 */
		Executor.setLine(Integer.parseInt(args[0]));
	}

}
