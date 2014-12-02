package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;

public class EqualsReturnJump implements InstructionExecutor {
	public final boolean appositive;
	
	
	public EqualsReturnJump(boolean app){
		appositive = app;
	}
	
	public void run(String[] args) {
		/*	Equals Return Jump
		 * 	If the variables are equal then it will jump to the function
		 * 	Then it adds a lineReturn called by the end executor
		 * 
		 */
		if ((Variable.parseString((args[1]))).equals((Variable.parseString((args[2])))) == appositive){
			Executor.lineReturns.add((Integer)Variable.getValue("pc").value+2);
			String fName = (String) args[0];
			if (Executor.functionLines.get(fName) != null){
				Executor.setLine(Executor.functionLines.get(fName)+2);
			}
		}
	}

}
