package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class SetString implements InstructionExecutor {
	/*	SetString
	 * 	Sets the value of a string
	 */
	public void run(String args[]){
		String finalString = "";
		for (int i =  1 ; i< args.length; i++){
			finalString = finalString 
					+ args[i] + 
					(i == args.length-1?"":" ");	// Adds trailing spaces
		}
		Variable.setValue(args[0], new Value(VariableTypes.String,Variable.parseString(finalString)));
	}

}
