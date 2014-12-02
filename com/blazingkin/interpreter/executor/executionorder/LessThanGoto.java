package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;
@Deprecated
public class LessThanGoto implements InstructionExecutor {
	/*	Less than Goto
	 * 	If The 1st argument is less than the 2nd argument then goto line number
	 */
	
	@Override
	public void run(String[] args) {
		if (Integer.parseInt(Variable.parseString((args[1]))) < Integer.parseInt(Variable.parseString((args[2])))){
			Executor.setLine(Integer.parseInt(args[0]));
		}
	}
	
}
