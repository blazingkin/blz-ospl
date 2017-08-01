package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.SimpleExpressionParser;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;

public class ReturnValue implements InstructionExecutor {

	public Value run(String line){
		Value v = SimpleExpressionParser.parseExpression(line);
		Executor.setReturnBuffer(v);
		Executor.popStack();
		return v;
	}

}
