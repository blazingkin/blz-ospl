package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Unsign implements InstructionExecutor, LambdaFunction {
	/*	Absolute Value
	 * 	Unsigns a variable
	 */
	public void run(String[] args) {
		if (Variable.getValue(args[0]).type == VariableTypes.Integer){
			Variable.setValue(args[0], new Value(VariableTypes.Integer, Math.abs((Integer)Variable.getValue(args[0]).value)));
		}else if (Variable.getValue(args[0]).type == VariableTypes.Double){
			Variable.setValue(args[0], new Value(VariableTypes.Double, Math.abs((Double)Variable.getValue(args[0]).value)));
		}
		Interpreter.throwError("Absolute Value Expects Ints or Doubles");
	}

	@Override
	public Value evaluate(String[] args) {
		if (Variable.getValue(args[0]).type == VariableTypes.Integer){
			return new Value(VariableTypes.Integer, Math.abs((Integer)Variable.getValue(args[0]).value));
		}else if (Variable.getValue(args[0]).type == VariableTypes.Double){
			return new Value(VariableTypes.Double, Math.abs((Double)Variable.getValue(args[0]).value));
		}
		Interpreter.throwError("Absolute Value Expects Ints or Doubles");
		return new Value(VariableTypes.Nil, null);
	}

}
