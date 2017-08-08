package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;

public class ReturnValue implements InstructionExecutorValue {

	public Value run(Value v){
		Executor.setReturnBuffer(v);
		Executor.popStack();
		return v;
	}

}
