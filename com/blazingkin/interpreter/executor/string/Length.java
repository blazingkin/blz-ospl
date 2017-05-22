package com.blazingkin.interpreter.executor.string;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Length implements InstructionExecutor, LambdaFunction {
	public boolean string = true;
	public Length(boolean string){
		this.string = string;
	}
	@Override
	public void run(String[] args) {
		if (string){
			String str = (String) Variable.getValue(args[0]).value;
			Variable.setValue(args[1], new Value(VariableTypes.Integer, str.length()));
		}else{
			Variable.setValue(args[1], new Value(VariableTypes.Integer, Variable.getArray(args[0]).size()));
		}
	}
	@Override
	public Value evaluate(String[] args) {
		if (string){
			String str = (String) Variable.getValue(args[0]).value;
			return new Value(VariableTypes.Integer, str.length());
		}else{
			return new Value(VariableTypes.Integer, Variable.getArray(args[0]).size());
		}
	}

}
