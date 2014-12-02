package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;
@Deprecated
public class NonEquivalentJump implements InstructionExecutor {
	/*	NonEquivalentJump
	 * 	If the variables are not equivalent then it will jump to the specified function
	 */
	
	public void run(String[] args){
		if (Integer.parseInt(Variable.parseString((args[1]))) != Integer.parseInt(Variable.parseString((args[2])))){
			String fName = args[0];
			if (Executor.functionLines.get(fName) != null){
				Executor.setLine(Executor.functionLines.get(fName)+2);
			}
		}
	}

}
