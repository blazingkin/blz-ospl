package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.LocalStack;
import com.blazingkin.interpreter.variables.Variable;

public class Push implements InstructionExecutor {
	/*	Push
	 * 	Pushes a value onto the stack
	 */
	public void run(String args[]){
		LocalStack.push(Variable.getValue(args[0]));
	}
	
}
