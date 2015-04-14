package com.blazingkin.interpreter.executor.string;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class StringLength implements InstructionExecutor {

	@Override
	public void run(String[] args) {
		String str = (String) Variable.getValue(Variable.parseString(args[0])).value;
		Variable.setValue(Variable.parseString(args[1]), new Value(VariableTypes.Integer, str.length()));
	}

}
