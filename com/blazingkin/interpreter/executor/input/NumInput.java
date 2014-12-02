package com.blazingkin.interpreter.executor.input;

import java.util.Scanner;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class NumInput implements InstructionExecutor {
	/*	NumInput
	 * 	Gets a number as input (Only a number)
	 * 
	 */
	public Scanner s = new Scanner(System.in);
	public void run(String[] vars){
		try{
		Variable.setValue(vars[0], new Value(VariableTypes.Integer,Integer.parseInt(s.nextLine())));
		}catch(Exception e){
			System.err.println("Invalid Input, Please Input A Number");
			run(vars);
		}
	}
	
}
