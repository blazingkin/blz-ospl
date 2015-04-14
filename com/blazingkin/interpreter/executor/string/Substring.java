package com.blazingkin.interpreter.executor.string;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Substring implements InstructionExecutor {

	@Override
	public void run(String[] args) {
		String newString = "";
		if (args.length > 2){
			if (Variable.isInteger(Variable.parseString(args[1])) && Variable.isInteger(Variable.parseString(args[2]))){
				int i1 = Integer.parseInt(Variable.parseString(args[1]));
				int i2 = Integer.parseInt(Variable.parseString(args[2]));
				String currentString = Variable.getValue(Variable.parseString(args[0])).value+"";
				newString = currentString.substring(i1, i2);
			}
		}else{
			if (Variable.isInteger(Variable.parseString(args[1]))){
				int i1 = Integer.parseInt(Variable.parseString(args[1]));
				String currentString = Variable.getValue(Variable.parseString(args[0])).value+"";
				newString = currentString.substring(i1);
			}
		}
		Variable.setValue(Variable.parseString(args[0]), new Value(VariableTypes.String, newString));
	}

}
