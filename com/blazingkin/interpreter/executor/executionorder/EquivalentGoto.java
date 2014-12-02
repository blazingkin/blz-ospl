package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;

@Deprecated
public class EquivalentGoto implements InstructionExecutor {

	@Override
	public void run(String[] args) {
		/*	EqualsGoto
		 * 	Sets to line number if variables are the same
		 * 	SUPER DEPRICATED
		 * 
		 */
		if (Integer.parseInt(Variable.parseString((args[1]))) == Integer.parseInt(Variable.parseString((args[2])))){
			Executor.setLine(Integer.parseInt(args[0]));
		}
	}

}
