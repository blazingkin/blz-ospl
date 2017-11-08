package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.variables.Value;

public abstract interface InstructionExecutorValue extends InstructionExecutor {
	public default Value run(String line){
		return run(ExpressionExecutor.parseExpression(line, Executor.getCurrentContext()));
	}
	
	public abstract Value run(Value val);		//All of the Executors Implement this interface so that they can be referenced from an enum

}
