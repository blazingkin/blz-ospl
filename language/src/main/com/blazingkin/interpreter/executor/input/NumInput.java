package com.blazingkin.interpreter.executor.input;

import java.math.BigDecimal;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.Context;

public class NumInput implements InstructionExecutor {
	/*	NumInput
	 * 	Gets a number as input (Only a number)
	 * 
	 */
	public Value run(String l, Context c){
		try{
			String str = Executor.getEventHandler().getInput();
			if (Variable.isInteger(str)){
				return Value.integer(Integer.parseInt(str));
			}else{
				return Value.doub(new BigDecimal(str));
			}
		}catch(Exception e){
			System.err.println("Invalid Input, Please Input A Number");
			return run(l, c);
		}
	}

	
}
