package com.blazingkin.interpreter.executor.timing;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class Wait implements InstructionExecutorStringArray {
	/*
	 * Wait
	 * Makes the current thread sleep for the given amount of time
	 */
	public Value run(String args[]){
		Value val = Variable.getValue(args[0]);
		if (Variable.isDecimalValue(val)){
			double time = Variable.getDoubleVal(val).doubleValue();
			try {
				Thread.sleep((long)time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return val;
		}
		Interpreter.throwError("Invalid Type For Wait "+val.value);
		return Value.integer(-1);
	}
	
	
}
