package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class SubVars implements InstructionExecutor {
	/*	Subtract
	 * 	Subtracts two numbers and stores them in a third variable
	 */
	public void run(String[] args) {
		if (Variable.isInteger(Variable.parseString(args[0])) && Variable.isInteger(Variable.parseString(args[1]))){
			Value v = new Value(VariableTypes.Integer, Integer.parseInt(Variable.parseString(args[0]))-Integer.parseInt(Variable.parseString(args[1])));
			Variable.setValue(args[2], v);
			return;
		}
		else if (Variable.isDouble(Variable.parseString(args[0])) && Variable.isDouble(Variable.parseString(args[1]))){
			Value v = new Value(VariableTypes.Double, Double.parseDouble(Variable.parseString(args[0]))-Double.parseDouble(Variable.parseString(args[1])));
			Variable.setValue(args[2], v);
			return;
		}
	}

}
