package com.blazingkin.interpreter.executor.string;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Substring implements InstructionExecutor, LambdaFunction {

	@Override
	public void run(String[] args) {
		String newString = "";
		if (args.length > 2){
			if (Variable.getValue(args[1]).type == VariableTypes.Integer 
					&& Variable.getValue(args[2]).type == VariableTypes.Integer){
				int i1 = Variable.getIntValue(Variable.getValue(args[1]));
				int i2 = Variable.getIntValue(Variable.getValue(args[2]));
				String currentString = Variable.getValue(args[0]).value+"";
				newString = currentString.substring(i1, i2);
			}
		}else{
			if (Variable.getValue(args[1]).type == VariableTypes.Integer){
				int i1 = Variable.getIntValue(Variable.getValue(args[1]));
				String currentString = Variable.getValue(args[0]).value+"";
				newString = currentString.substring(i1);
			}
		}
		Variable.setValue(args[0], new Value(VariableTypes.String, newString));
	}

	@Override
	public Value evaluate(String[] args) {
		String newString = "";
		if (args.length > 2){
			if (Variable.getValue(args[1]).type == VariableTypes.Integer 
					&& Variable.getValue(args[2]).type == VariableTypes.Integer){
				int i1 = Variable.getIntValue(Variable.getValue(args[1]));
				int i2 = Variable.getIntValue(Variable.getValue(args[2]));
				String currentString = Variable.getValue(args[0]).value+"";
				newString = currentString.substring(i1, i2);
			}
		}else{
			if (Variable.getValue(args[0]).type == VariableTypes.Integer){
				int i1 = Variable.getIntValue(Variable.getValue(args[1]));
				String currentString = Variable.getValue(args[0]).value+"";
				newString = currentString.substring(i1);
			}
		}
		return new Value(VariableTypes.String, newString);
	}

}
