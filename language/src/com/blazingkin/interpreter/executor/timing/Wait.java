package com.blazingkin.interpreter.executor.timing;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class Wait implements InstructionExecutorValue {
	/*
	 * Wait
	 * Makes the current thread sleep for the given amount of time
	 */
	public Value run(Value val) throws BLZRuntimeException{
		if (Variable.isDecimalValue(val)){
			double time = Variable.getDoubleVal(val).doubleValue();
			try {
				Thread.sleep((long)time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return val;
		}
		throw new BLZRuntimeException("Invalid type for wait "+val);
	}
	
	
}
