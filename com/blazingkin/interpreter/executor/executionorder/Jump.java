package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
@Deprecated
public class Jump implements InstructionExecutor {
	/*	Jump
	 * 	Moves the execution line to a given line number
	 */

	public void run(String[] args) {
		String fName = args[0];
		if (Executor.functionLines.get(fName) != null){
			Executor.setLine(Executor.functionLines.get(fName)+2);
		}
	}

}
