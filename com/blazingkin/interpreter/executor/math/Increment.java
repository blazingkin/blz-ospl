package com.blazingkin.interpreter.executor.math;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Increment implements InstructionExecutorStringArray {
	/*	Increment
	 * 	Increments a variable
	 */
	public void run(String[] args){

			Value v = null;
			switch(Variable.getValue(args[0]).type){
			case Integer:
				v = new Value(VariableTypes.Integer, ((BigInteger)(Variable.getValue(args[0]).value)).add(BigInteger.ONE));
				break;
			case Double:
				v = new Value(VariableTypes.Double, ((BigDecimal)(Variable.getValue(args[0]).value)).add(BigDecimal.ONE));
				break;
			default:
				break;
			}
			Variable.setValue(args[0], v);
	}
	
}
