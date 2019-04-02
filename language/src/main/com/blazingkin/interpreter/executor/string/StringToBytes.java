package com.blazingkin.interpreter.executor.string;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class StringToBytes implements InstructionExecutorValue {


    @Override
	public Value run(Value arg) throws BLZRuntimeException {
		if (arg.type != VariableTypes.String) {
            throw new BLZRuntimeException("Can only get bytes of a string");
        }
        String s = (String) arg.value;
        Value[] result = new Value[s.length()];
        for (int i = 0; i < s.length(); i++) {
            result[i] = Value.integer(s.charAt(i));
        }
        return Value.arr(result);
	}

}