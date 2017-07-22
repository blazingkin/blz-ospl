package com.blazingkin.interpreter.executor.math;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ModVars implements InstructionExecutor, LambdaFunction {
	/*	Mod
	 * 	Gets the remainder of 2 values
	 */
	public void run(String args[]){
		if (Variable.getValue(args[0]).type == VariableTypes.Integer
			&& Variable.getValue(args[1]).type == VariableTypes.Integer){
			BigInteger i1 = Variable.getIntValue(Variable.getValue(args[0]));
			BigInteger i2 = Variable.getIntValue(Variable.getValue(args[1]));
			Value v = new Value(VariableTypes.Integer, i1.mod(i2));
			Variable.setValue(args[2], v);
			return;
		}
		else if (Variable.getValue(args[0]).type == VariableTypes.Double
				&& Variable.getValue(args[1]).type == VariableTypes.Double){
			BigDecimal d1 = Variable.getDoubleVal(Variable.getValue(args[0]));
			BigDecimal d2 = Variable.getDoubleVal(Variable.getValue(args[1]));
			Value v = new Value(VariableTypes.Double, d1.toBigInteger().mod(d2.toBigInteger()));
			Variable.setValue(args[2], v);
			return;
		}
	}

	@Override
	public Value evaluate(String[] args) {
		if (Variable.getValue(args[0]).type == VariableTypes.Integer
				&& Variable.getValue(args[1]).type == VariableTypes.Integer){
				BigInteger i1 = Variable.getIntValue(Variable.getValue(args[0]));
				BigInteger i2 = Variable.getIntValue(Variable.getValue(args[1]));
				return new Value(VariableTypes.Integer, i1.mod(i2));
			}
			else if (Variable.getValue(args[0]).type == VariableTypes.Double
					&& Variable.getValue(args[1]).type == VariableTypes.Double){
				BigDecimal d1 = Variable.getDoubleVal(Variable.getValue(args[0]));
				BigDecimal d2 = Variable.getDoubleVal(Variable.getValue(args[1]));
				return new Value(VariableTypes.Double, d1.toBigInteger().mod(d2.toBigInteger()));
			}
		Interpreter.throwError("Incorrect datatypes for modvars");
		return new Value(VariableTypes.Nil, null);
	}
	
}
