package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.LocalStack;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Peek implements InstructionExecutor {
	/*	Peek
	 * 	Peeks off of the stack
	 */
	
	public void run(String args[]){
			Variable.setValue(args[0], new Value(VariableTypes.Integer,LocalStack.peek()));
	}

}
