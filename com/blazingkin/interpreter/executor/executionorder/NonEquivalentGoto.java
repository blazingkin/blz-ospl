package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;

@Deprecated
public class NonEquivalentGoto implements InstructionExecutor {
	/*	NonEquivalentGoto
	 * 	If the passed variables are not equivalent then the executor will be set to the line number
	 */
	@Override
	public void run(String[] args) {
		if (Integer.parseInt(Variable.parseString((args[1]))) != Integer.parseInt(Variable.parseString((args[2])))){
			Executor.setLine(Integer.parseInt(args[0]));
		}
	}

}
