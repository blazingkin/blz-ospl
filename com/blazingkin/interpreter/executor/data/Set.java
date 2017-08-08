package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Variable;

@Deprecated
public class Set implements InstructionExecutorStringArray {
	/*	Set
	 * 	Sets the value of a variable
	 */
	@Override
	public void run(String[] args) {
		String buildingString = "";
		for (int i = 1; i < args.length; i++){
			buildingString+=args[i]+" ";
		}
		buildingString.trim();
		Variable.setValue(args[0], Variable.getValue(buildingString));
	}
	


}
