package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;

public class LessThanReturnJump implements InstructionExecutor {
	
	/*	LessThanReturnJump
	 * 	If condition 1 is less than condition 2 then jump to specified function, return at the correlating end
	 * 
	 */

	public void run(String[] args){
		if (Integer.parseInt(Variable.parseString((args[1]))) < Integer.parseInt(Variable.parseString((args[2])))){
			Executor.lineReturns.add((Integer)Variable.getValue("pc").value+2);
			String fName = (String) args[0];
			if (Executor.functionLines.get(fName) != null){
				Executor.setLine(Executor.functionLines.get(fName)+2);
			}
		}
	}
	
}
