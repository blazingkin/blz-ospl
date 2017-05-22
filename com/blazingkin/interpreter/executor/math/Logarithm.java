package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Logarithm implements InstructionExecutor, LambdaFunction {

	@Override
	public void run(String[] args) {
		double num = Variable.getDoubleVal(Variable.getValue(args[0]));
		if (args.length == 2){
			String address = args[1];
			double result = Math.log10(num);
			Variable.setValue(address, new Value(VariableTypes.Double, result));
		}else{
			if (args[1].toLowerCase().equals("e")){
				String address = args[2];
				double result = Math.log(num);
				Variable.setValue(address, new Value(VariableTypes.Double, result));
			}else{
				String address = args[2];
				double base = (double) Variable.getValue(args[1]).value;
				double result = Math.log(num) / Math.log(base);
				Variable.setValue(address, new Value(VariableTypes.Double, result));
			}
		}
	}

	@Override
	public Value evaluate(String[] args) {
		double num = Variable.getDoubleVal(Variable.getValue(args[0]));
		if (args.length == 1){
			double result = Math.log10(num);
			return new Value(VariableTypes.Double, result);
		}else{
			if (args[1].toLowerCase().equals("e")){
				double result = Math.log(num);
				return new Value(VariableTypes.Double, result);
			}else{
				double base = (double) Variable.getValue(args[1]).value;
				double result = Math.log(num) / Math.log(base);
				return new Value(VariableTypes.Double, result);
			}
		}
	}

}
