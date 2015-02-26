package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.Instruction;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Set implements InstructionExecutor {
	/*	Set
	 * 	Sets the value of a variable
	 */
	@Override
	public void run(String[] args) {
		if (Variable.isInteger(Variable.parseString(args[1]))){
			Variable.setValue(args[0], new Value(VariableTypes.Integer, Integer.parseInt(Variable.parseString(args[1]))));
			return;
		}
		else if (Variable.isDouble(Variable.parseString(args[1]))){
			Variable.setValue(args[0], new Value(VariableTypes.Double, Double.parseDouble(Variable.parseString(args[1]))));
			return;
		}else{
			Instruction.STRINGSET.executor.run(args);
			return;
		}
	}
	


}
