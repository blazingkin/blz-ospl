package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Value;

public class End implements InstructionExecutorStringArray {

	public Value run(String[] args){
		/*	End
		 * 	Will set the line to the top number of the lineReturns
		 * 	If none are left it will set the close requested flag to true
		 */
		return RuntimeStack.pop();
	}
}
