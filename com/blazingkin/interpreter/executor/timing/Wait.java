package com.blazingkin.interpreter.executor.timing;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class Wait implements InstructionExecutor {
	/*
	 * Wait
	 * Makes the current thread sleep for the given amount of time
	 */
	public void run(String args[]){
		Value val = Variable.getValue(args[0]);
		if (Variable.isDecimalValue(val)){
			double time = Variable.getDoubleVal(val);
			try {
				Thread.sleep((long)time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		Interpreter.throwError("Invalid Type For Wait "+val.value);
	}
	
	
}
