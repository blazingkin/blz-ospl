package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ReturnValue implements InstructionExecutorValue {

	public Value run(Value v){
		Executor.setReturnBuffer(v);
		Value elem = RuntimeStack.pop();
		Executor.setBreakMode(true);
		while (elem == null || elem.type != VariableTypes.Method){
			elem = RuntimeStack.pop();
		}
		return v;
	}

}
