package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.InstructionExecutor;
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
			int i1 = Variable.getIntValue(Variable.getValue(args[0]));
			int i2 = Variable.getIntValue(Variable.getValue(args[1]));
			Value v = new Value(VariableTypes.Integer, i1%i2);
			Variable.setValue(args[2], v);
			return;
		}
		else if (Variable.getValue(args[0]).type == VariableTypes.Double
				&& Variable.getValue(args[1]).type == VariableTypes.Double){
			double d1 = Variable.getDoubleVal(Variable.getValue(args[0]));
			double d2 = Variable.getDoubleVal(Variable.getValue(args[1]));
			Value v = new Value(VariableTypes.Double, d1%d2);
			Variable.setValue(args[2], v);
			return;
		}
	}

	@Override
	public Value evaluate(String[] args) {
		if (Variable.getValue(args[0]).type == VariableTypes.Integer
				&& Variable.getValue(args[1]).type == VariableTypes.Integer){
				int i1 = Variable.getIntValue(Variable.getValue(args[0]));
				int i2 = Variable.getIntValue(Variable.getValue(args[1]));
				return new Value(VariableTypes.Integer, i1%i2);
			}
			else if (Variable.getValue(args[0]).type == VariableTypes.Double
					&& Variable.getValue(args[1]).type == VariableTypes.Double){
				double d1 = Variable.getDoubleVal(Variable.getValue(args[0]));
				double d2 = Variable.getDoubleVal(Variable.getValue(args[1]));
				return new Value(VariableTypes.Double, d1%d2);
			}
		Interpreter.throwError("Incorrect datatypes for modvars");
		return new Value(VariableTypes.Nil, null);
	}
	
}
