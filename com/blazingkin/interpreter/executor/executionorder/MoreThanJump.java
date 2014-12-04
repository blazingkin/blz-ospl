package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;
@Deprecated
public class MoreThanJump implements InstructionExecutor {
	/*	MoreThanJump
	 * 	If Condition 1 is more than condition 2 then jump to line number
	 */
	public void run(String[] args) {
		if (Integer.parseInt(Variable.parseString((args[1]))) > Integer.parseInt(Variable.parseString((args[2])))){
			String fName = args[0];
			if (Executor.functionLines.get(fName) != null){
				Executor.setLine(Executor.functionLines.get(fName).lineNumber+2, Executor.functionLines.get(fName).UUID);
			}
		}
	}

}
