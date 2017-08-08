package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;

@Deprecated
public class Goto implements InstructionExecutorStringArray {

	public void run(String[] args) {
		/*	Goto
		 * 	Sets to the line number given
		 * 
		 */
		Executor.setLine(Integer.parseInt(args[0]));
	}

}
