package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.Instruction;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class AddVars implements InstructionExecutor {

	public void run(String[] args) {
		/*	Add Vars
		 * 	Will try to add them as integers first
		 * 	If it cannot it will pass the variables to concatenate as strings
		 * 
		 * 
		 * 
		 */
		
		
		if (Variable.isInteger(Variable.parseString(args[0])) && Variable.isInteger(Variable.parseString(args[1]))){
			long i1 = Long.parseLong(Variable.parseString(args[0]));
			long i2 = Long.parseLong(Variable.parseString(args[1]));
			Value v = new Value(VariableTypes.Integer, i1+i2);
			Variable.setValue(args[2], v);
			return;
		}
		String arr[] = new String[args.length];
		for (int i = 0; i < args.length; i++)
		{
			arr[i] = args[i];
		}
		{
			String temp = arr[arr.length-1];
			for (int i = arr.length-1; i > 0; i--){
				arr[i] = arr[i-1];
			}
			arr[0] = temp;
		}
		Instruction.CONCATENATE.executor.run(arr);
	}

}
