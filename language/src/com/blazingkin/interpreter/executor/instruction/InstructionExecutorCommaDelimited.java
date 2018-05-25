package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Context;

public interface InstructionExecutorCommaDelimited extends InstructionExecutor {

	public default Value run(String line, Context con) throws BLZRuntimeException{
		return run(ExpressionExecutor.extractCommaDelimits(ExpressionParser.parseExpression(line), con));
	}
	
	public abstract Value run(Value[] args) throws BLZRuntimeException;
	
}
