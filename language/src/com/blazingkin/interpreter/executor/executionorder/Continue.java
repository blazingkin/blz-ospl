package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Value;

public class Continue implements InstructionExecutorStringArray {

	@Override
	public Value run(String[] args) {
		Executor.setBreakMode(true);
		return Value.nil();
	}

}
