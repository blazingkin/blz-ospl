package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.SimpleExpressionParser;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;

public class ReturnValue implements InstructionExecutor {

	public void run(String[] args) {
		String buildingString = "";
		for (String s: args){
			buildingString += s + " ";
		}
		Executor.setReturnBuffer(SimpleExpressionParser.parseExpression(buildingString));
		Executor.popStack();
	}

}
