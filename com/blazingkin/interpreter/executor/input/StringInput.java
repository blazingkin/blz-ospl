package com.blazingkin.interpreter.executor.input;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class StringInput implements InstructionExecutor {
	/*	StringInput
	 * 	Gets a string as input
	 * 
	 */
	public void run(String[] vars){
		Variable.setValue(vars[0], new Value(VariableTypes.String, Executor.getEventHandler().getInput()));
	}
	
}
