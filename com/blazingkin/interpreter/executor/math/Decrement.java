package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Decrement implements InstructionExecutor {
	/*	Decrement
	 * 	Decrements the value of a variable
	 */

	public void run(String[] args){
		if (Variable.contains(args[0])){
			Value v = null;
			switch(Variable.getValue(args[0]).type){
			case Integer:
				v = new Value(VariableTypes.Integer, (Integer)(Variable.getValue(args[0]).value) - 1);
				break;
			case Boolean:
				v = new Value(VariableTypes.Boolean, !(Boolean)(Variable.getValue(args[0]).value));
				break;
			case Double:
				v = new Value(VariableTypes.Double, (Double)(Variable.getValue(args[0]).value) - 1.0d);
				break;
			case String:
				break;
			default:
				break;
			}
			Variable.setValue(args[0], v);
		}
	}
	
}
