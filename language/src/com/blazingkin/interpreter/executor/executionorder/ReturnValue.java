package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;

public class ReturnValue implements InstructionExecutorValue {

	public Value run(Value v){
		Executor.setReturnBuffer(v);
		Executor.setReturnMode(true);
		/*while (elem == null || elem.type != VariableTypes.Method){
			elem = RuntimeStack.pop();
		}
		Executor.setBreakMode(false);*/
		return v;
	}

}
