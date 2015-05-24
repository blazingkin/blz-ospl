package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Logarithm implements InstructionExecutor {

	@Override
	public void run(String[] args) {
		double num = Double.parseDouble(Variable.parseString(args[0]));
		if (args.length == 2){
			String address = Variable.parseString(args[1]);
			double result = Math.log10(num);
			Variable.setValue(address, new Value(VariableTypes.Double, result));
		}else{
			if (args[1].toLowerCase().equals("e")){
				String address = Variable.parseString(args[2]);
				double result = Math.log(num);
				Variable.setValue(address, new Value(VariableTypes.Double, result));
			}else{
				String address = Variable.parseString(args[2]);
				double base = Double.parseDouble(Variable.parseString(args[1]));
				double result = Math.log(num) / Math.log(base);
				Variable.setValue(address, new Value(VariableTypes.Double, result));
			}
		}
	}

}
