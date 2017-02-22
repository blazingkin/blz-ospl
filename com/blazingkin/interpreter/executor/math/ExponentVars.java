package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ExponentVars implements InstructionExecutor, LambdaFunction {

	public void run(String args[]){
		Value v1 = Variable.getValue(args[0]);
		Value v2 = Variable.getValue(args[1]);
		if ((v1.type == VariableTypes.Integer || v1.type == VariableTypes.Double)
				&& (v2.type == VariableTypes.Integer || v2.type == VariableTypes.Double)){
			double d1 = (double) v1.value;
			double d2 = (double) v2.value;
			Value v = new Value((v1.type == v2.type && v1.type == VariableTypes.Integer)?v1.type:VariableTypes.Double, Math.pow(d1, d2));
			Variable.setValue(args[2], v);
			return;
		}else{
			Interpreter.throwError("Invalid types for exponentiation");
		}

	}

	@Override
	public Value evaluate(String[] args) {
		Value v1 = Variable.getValue(args[0]);
		Value v2 = Variable.getValue(args[1]);
		if ((v1.type == VariableTypes.Integer || v1.type == VariableTypes.Double)
				&& (v2.type == VariableTypes.Integer || v2.type == VariableTypes.Double)){
			double d1 = (double) v1.value;
			double d2 = (double) v2.value;
			return new Value((v1.type == v2.type && v1.type == VariableTypes.Integer)?v1.type:VariableTypes.Double, Math.pow(d1, d2));
		}else{
			Interpreter.throwError("Invalid types for exponentiation");
		}
		return new Value(VariableTypes.Nil, null);
	}
	
}
