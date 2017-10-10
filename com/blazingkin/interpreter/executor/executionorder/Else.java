package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.executor.instruction.LabeledInstruction;
import com.blazingkin.interpreter.variables.Value;

public class Else implements InstructionExecutor, LabeledInstruction {

	
	@Override
	public String getLabel(String line) {
		return "else";
	}

	@Override
	public Value run(String line) {
		// We want to jump to the end, not to after the block
		Executor.setLine(Executor.getCurrentBlockEnd() - 1);
		return Value.nil();
	}


}
