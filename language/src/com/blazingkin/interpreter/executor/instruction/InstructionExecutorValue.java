package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public abstract interface InstructionExecutorValue extends InstructionExecutor {
	public default Value run(String line, Context con) throws BLZRuntimeException{
		return run(ExpressionExecutor.parseExpression(line, con));
	}
	
	public abstract Value run(Value val);		//All of the Executors Implement this interface so that they can be referenced from an enum

}
