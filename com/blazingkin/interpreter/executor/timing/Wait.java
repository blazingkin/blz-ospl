package com.blazingkin.interpreter.executor.timing;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;

public class Wait implements InstructionExecutor {

	public void run(String args[]){
		if (Variable.isInteger(Variable.parseString(args[0]))){
			int time = Integer.parseInt(Variable.parseString(args[0]));
			long startTime = System.currentTimeMillis();
			while ((System.currentTimeMillis() - startTime) < time){
				
			}
		}
	}
	
	
}
