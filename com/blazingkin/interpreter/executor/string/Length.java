package com.blazingkin.interpreter.executor.string;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class Length implements InstructionExecutorStringArray {
	public boolean string = true;
	public Length(boolean string){
		this.string = string;
	}
	@Override
	public void run(String[] args) {
		if (string){
			String str = (String) Variable.getValue(args[0]).value;
			Variable.setValue(args[1], Value.integer(str.length()));
		}else{
			Variable.setValue(args[1], Value.integer(Variable.getArray(args[0]).size()));
		}
	}

}
