package com.blazingkin.interpreter.executor.input;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class NumInput implements InstructionExecutorStringArray {
	/*	NumInput
	 * 	Gets a number as input (Only a number)
	 * 
	 */
	public Value run(String[] vars){
		try{
			String str = Executor.getEventHandler().getInput();
			if (Variable.isInteger(str)){
				Variable.setValue(vars[0], new Value(VariableTypes.Integer,new BigInteger(str)));
				return Variable.getValue(vars[0]);
			}else{
				Variable.setValue(vars[0], new Value(VariableTypes.Double,new BigDecimal(str)));
				return Variable.getValue(vars[0]);
			}
		}catch(Exception e){
			System.err.println("Invalid Input, Please Input A Number");
			return run(vars);
		}
	}

	
}