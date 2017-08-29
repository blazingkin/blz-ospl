package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.executor.instruction.LabeledInstruction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Else implements InstructionExecutor, LabeledInstruction {

	@Override
	public String getLabel(String line) {
		return "else";
	}

	@Override
	public Value run(String line) {
		return new Value(VariableTypes.Nil, null);
	}

}
