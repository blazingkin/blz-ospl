package com.blazingkin.interpreter.executor.data;

import java.util.HashMap;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorCommaDelimited;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class HashHasKey implements InstructionExecutorCommaDelimited {

	@Override
	public Value run(Value[] args) {
		if (args.length != 3){
			Interpreter.throwError("Hash has key check did not have 2 arguments");
		}
		if (args[0].type != VariableTypes.Hash){
			Interpreter.throwError("Hash has key check was not passed a hash as the first argument");
		}
		@SuppressWarnings("unchecked")
		HashMap<Value, Value> hsh = (HashMap<Value, Value>) args[0].value;
		Value result = Value.bool(hsh.containsKey(args[1]));
		String name = (String) args[2].value;
		Variable.setValue(name, result);
		return result;
	}

}
