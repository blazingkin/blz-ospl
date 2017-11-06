package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.BlockInstruction;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;

public class Constructor implements InstructionExecutor, BlockInstruction {
	
	boolean isDefinition = false;
	
	public Constructor() {
		
	}
	
	public Constructor(String[] definition) {
		isDefinition = true;
	}

	@Override
	public void onBlockStart() {

	}

	@Override
	public void onBlockEnd() {
		
	}

	@Override
	public Value run(String line) {
		// We want to 
		Executor.setLine(Executor.getCurrentBlockEnd());
		return Value.nil();
	}

}
