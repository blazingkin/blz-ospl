package com.blazingkin.interpreter.executor.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Ceiling implements InstructionExecutor, LambdaFunction {

	public void run(String[] args) {
		if (Variable.getValue(args[0]).type == VariableTypes.Integer){
			Variable.setValue(args[0], new Value(VariableTypes.Integer, Variable.getValue(args[0]).value));
		}else if (Variable.getValue(args[0]).type == VariableTypes.Double){
			Variable.setValue(args[0], new Value(VariableTypes.Integer, ceil(Variable.getDoubleVal(Variable.getValue(args[0])))));
		}
	}
	
	public Value evaluate(String[] args){
		if (Variable.getValue(args[0]).type == VariableTypes.Integer){
			return new Value(VariableTypes.Integer, Variable.getValue(args[0]).value);
		}else if (Variable.getValue(args[0]).type == VariableTypes.Double){
			return new Value(VariableTypes.Integer, ceil(Variable.getDoubleVal(Variable.getValue(args[0]))));
		}
		Interpreter.throwError("Incorrect type of argument for Ceiling, "+args[0]);
		return new Value(VariableTypes.Nil, null);
	}
	
	public BigInteger ceil(BigDecimal bd){
		return bd.setScale(0, RoundingMode.CEILING).toBigInteger();
	}
	
}
