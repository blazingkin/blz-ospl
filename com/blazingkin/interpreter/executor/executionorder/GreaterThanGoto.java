package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;

@Deprecated
public class GreaterThanGoto implements InstructionExecutor {
	
	@Override
	public void run(String[] args) {
		/*	GreaterThanGoto
		 * 	Sets the line number if variable 1 is greater than variable 2
		 * 
		 */
		if (Integer.parseInt(Variable.parseString((args[1]))) > Integer.parseInt(Variable.parseString((args[2])))){
			Executor.setLine(Integer.parseInt(args[0]));
		}
	}

}
