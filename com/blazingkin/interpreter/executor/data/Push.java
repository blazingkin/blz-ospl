package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.LocalStack;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class Push implements InstructionExecutorStringArray, LambdaFunction {
	/*	Push
	 * 	Pushes a value onto the stack
	 */
	public void run(String args[]){
		LocalStack.push(Variable.getValue(args[0]));
	}
	
	
	public Value evaluate(String args[]){
		LocalStack.push(Variable.getValue(args[0]));
		return Variable.getValue(args[0]);
	}
	
}
