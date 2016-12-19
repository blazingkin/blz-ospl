package com.blazingkin.interpreter.executor.input;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class NumInput implements InstructionExecutor {
	/*	NumInput
	 * 	Gets a number as input (Only a number)
	 * 
	 */
	public void run(String[] vars){
		try{
		String str = Executor.eventHandler.getInput();
		if (Variable.isInteger(str)){
			Variable.setValue(vars[0], new Value(VariableTypes.Integer,Integer.parseInt(str)));
		}else{
			Variable.setValue(vars[0], new Value(VariableTypes.Double,Double.parseDouble(str)));
		}
		}catch(Exception e){
			System.err.println("Invalid Input, Please Input A Number");
			run(vars);
		}
	}
	
}
