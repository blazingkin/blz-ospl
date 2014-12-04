package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;

public class JumpReturn implements InstructionExecutor {
	/*	JumpReturn
	 * 	Jumps to a function and then returns on the following line at the next end
	 */
	public void run(String[] args) {
		String fName = args[0];
		Executor.lineReturns.add((Integer)Variable.getValue("pc"+Executor.getCurrentProcess().UUID).value+2);
		if (Executor.functionLines.get(fName) != null){
			Executor.setLine(Executor.functionLines.get(fName).lineNumber+2, Executor.functionLines.get(fName).UUID);
		}
	}

}
