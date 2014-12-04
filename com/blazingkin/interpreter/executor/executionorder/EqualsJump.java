package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;

@Deprecated
public class EqualsJump implements InstructionExecutor {

	public void run(String[] args) {
		/*	Equals Jump
		 * 	If the variables passed are equals, then it will jump to the function
		 * 	DEPRICATED
		 */
		if ((Variable.parseString((args[1]))).equals((Variable.parseString((args[2]))))){
			String fName = args[0];
			if (Executor.functionLines.get(fName) != null){
				Executor.setLine(Executor.functionLines.get(fName).lineNumber+2, Executor.functionLines.get(fName).UUID);
			}
		}
	}

}
