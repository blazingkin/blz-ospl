package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class MultiplyVars implements InstructionExecutor, LambdaFunction {
	/*	Multiply
	 * 	Multiplies 2 numbers and stores them as a third variable
	 */
	public void run(String[] args) {
		Value v1 = Variable.getValue(args[0]);
		Value v2 = Variable.getValue(args[1]);
		if ((v1.type == VariableTypes.Integer || v1.type == VariableTypes.Double)
				&& (v2.type == VariableTypes.Integer || v2.type == VariableTypes.Double)){
			double d1;
			double d2;
			if (v1.value instanceof Integer){
				d1 = (double)((int)v1.value);
			}else{
				d1 = (double)v1.value;
			}
			if (v2.value instanceof Integer){
				d2 = (double)((int)v2.value);
			}else{
				d2 = (double) v2.value;
			}
			Value v = new Value((v1.type == v2.type && v1.type == VariableTypes.Integer)?v1.type:VariableTypes.Double, d1*d2);
			Variable.setValue(args[2], v);
			return;
		}else{
			Interpreter.throwError("Invalid types for multiplication");
		}
	}

	@Override
	public Value evaluate(String[] args) {
		Value v1 = Variable.getValue(args[0]);
		Value v2 = Variable.getValue(args[1]);
		if ((Variable.isValDouble(v1) || Variable.isValInt(v1))
				&& (Variable.isValDouble(v2) || Variable.isValInt(v2))){
			double d1;
			double d2;
			if (v1.value instanceof Integer){
				d1 = (double)((int)v1.value);
			}else{
				d1 = (double)v1.value;
			}
			if (v2.value instanceof Integer){
				d2 = (double)((int)v2.value);
			}else{
				d2 = (double) v2.value;
			}
			return new Value((v1.type == v2.type && v1.type == VariableTypes.Integer)?v1.type:VariableTypes.Double, d1*d2);
		}else{
			Interpreter.throwError("Invalid types for multiplication");
			return new Value(VariableTypes.Nil, null);
		}
	}

}
