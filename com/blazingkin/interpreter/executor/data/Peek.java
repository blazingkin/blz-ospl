package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.LocalStack;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class Peek implements InstructionExecutor, LambdaFunction {
	/*	Peek
	 * 	Peeks off of the stack
	 */
	
	public void run(String args[]){
			Variable.setValue(args[0], LocalStack.peek());
	}
	
	public Value evaluate(String args[]){
		return LocalStack.peek();
	}

}
