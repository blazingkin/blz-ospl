package com.blazingkin.interpreter.executor.string;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;

public class Length implements InstructionExecutorValue {
	public boolean string = true;
	public Length(boolean string){
		this.string = string;
	}
	@Override
	public Value run(Value arg) {
		if (string){
			String str = (String) arg.value;
			return Value.integer(str.length());
		}else{
			Value arr[] = (Value []) arg.value;
			return Value.integer(arr.length);
		}
	}

}
