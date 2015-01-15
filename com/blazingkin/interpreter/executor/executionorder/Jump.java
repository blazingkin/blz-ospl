package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.Method;
@Deprecated
public class Jump implements InstructionExecutor {
	/*	Jump
	 * 	Moves the execution line to a given line number
	 */

	public void run(String[] args) {
		String fName = args[0];
		if (Method.contains(Executor.methods, fName) != null){
			Executor.executeMethod(Executor.getMethodInCurrentProcess(fName));
		}
	}

}
