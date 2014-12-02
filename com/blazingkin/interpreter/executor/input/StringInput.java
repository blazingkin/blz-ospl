package com.blazingkin.interpreter.executor.input;

import java.util.Scanner;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class StringInput implements InstructionExecutor {
	/*	StringInput
	 * 	Gets a string as input
	 * 
	 */
	public Scanner s = new Scanner(System.in);
	public void run(String[] vars){
		Variable.setValue(vars[0], new Value(VariableTypes.String, s.nextLine()));
	}
	
}
