package com.blazingkin.interpreter.executor.output;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;

public class SameLineEcho implements InstructionExecutorValue {
	/*	Print
	 * 	Outputs the given text
	 */
	public Value run(Value v) {
		Executor.getEventHandler().print(v.value.toString());
		return Value.string(v.value.toString());
	}
	

}
