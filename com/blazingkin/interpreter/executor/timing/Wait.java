package com.blazingkin.interpreter.executor.timing;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Wait implements InstructionExecutor {
	/*
	 * Wait
	 * Will run a for loop until the time is ready
	 * This only for single threaded processes
	 */
	public void run(String args[]){
		if (Variable.getValue(args[0]).type == VariableTypes.Integer){
			int time = Variable.getIntValue(Variable.getValue(args[0]));
			long startTime = System.currentTimeMillis();
			while ((System.currentTimeMillis() - startTime) < time){
				
			}
		}
	}
	
	
}
