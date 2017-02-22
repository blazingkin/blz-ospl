package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.LambdaFunction;
import com.blazingkin.interpreter.variables.LocalStack;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class Pop implements InstructionExecutor, LambdaFunction {
	/*	Pop
	 * 	Pops off of the stack
	 */
	
	public void run(String args[]){
			Variable.setValue(args[0], LocalStack.pop());
	}
	
	public Value evaluate(String[] args){
		return LocalStack.pop();
	}

}
