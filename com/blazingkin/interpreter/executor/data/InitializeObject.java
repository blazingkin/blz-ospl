package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class InitializeObject implements InstructionExecutor {

	@Override
	public Value run(String line) {
		Variable.setValue(line, new Value(VariableTypes.Object, new BLZObject(Variable.getGlobalContext())));
		return Variable.getVariableValue(line);
	}

}
