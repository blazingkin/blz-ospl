package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class LogicalAnd implements InstructionExecutor, LambdaFunction {
	/*	And
	 * 	Puts in a variable the value of a1 && a2
	 */
	
	public void run(String[] args){
		Value v1 = Variable.getValue(args[0]);
		Value v2 = Variable.getValue(args[1]);
		
		Variable.setValue(args[2], new Value(VariableTypes.Boolean,(boolean)v1.value && (boolean)v2.value));
	}

	@Override
	public Value evaluate(String[] args) {
		Value v1 = Variable.getValue(args[0]);
		Value v2 = Variable.getValue(args[1]);
		return new Value(VariableTypes.Boolean, (boolean)v1.value && (boolean)v2.value);
	}

}
