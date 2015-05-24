package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Parse implements InstructionExecutor {
	public void run(String args[]){
		Variable.setValue(args[0], new Value(VariableTypes.String, Variable.parseString(args[0])));
	}
}
