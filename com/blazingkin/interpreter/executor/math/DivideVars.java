package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

@Deprecated
public class DivideVars implements InstructionExecutor, LambdaFunction {
	/*	Divide
	 * 	Divides two numbers and puts the output as a variable
	 */
	public void run(String[] args) {
		try{
			Value v1 = Variable.getValue(args[0]);
			Value v2 = Variable.getValue(args[1]);
			if ((v1.type == VariableTypes.Integer || v1.type == VariableTypes.Double)
					&& (v2.type == VariableTypes.Integer || v2.type == VariableTypes.Double)){
				if (Variable.isValInt(v1) && Variable.isValInt(v2)){
					int i1 = Variable.getIntValue(v1);
					int i2 = Variable.getIntValue(v2);
					Value v = new Value(VariableTypes.Integer, i1/i2);
					Variable.setValue(args[2], v);
					return;	
				}else{
					double d1 = Variable.getDoubleVal(v1);
					double d2 = Variable.getDoubleVal(v2);
					Value v = new Value(VariableTypes.Double, d1/d2);
					Variable.setValue(args[2], v);
					return;	
				}
			}else{
				Interpreter.throwError("Invalid Data Types");
			}
		}catch(Exception e){
			Interpreter.throwError("Cannot divide by 0");
		}
	}

	@Override
	public Value evaluate(String[] args) {
		try{
			Value v1 = Variable.getValue(args[0]);
			Value v2 = Variable.getValue(args[1]);
			if ((v1.type == VariableTypes.Integer || v1.type == VariableTypes.Double)
					&& (v2.type == VariableTypes.Integer || v2.type == VariableTypes.Double)){
				if (Variable.isValInt(v1) && Variable.isValInt(v2)){
					int i1 = Variable.getIntValue(v1);
					int i2 = Variable.getIntValue(v2);
					return new Value(VariableTypes.Integer, i1/i2);
				}else{
					double d1 = Variable.getDoubleVal(v1);
					double d2 = Variable.getDoubleVal(v2);
					return new Value(VariableTypes.Double, d1/d2);	
				}
			}else{
				Interpreter.throwError("Invalid Data Types");
			}
		}catch(Exception e){
			Interpreter.throwError("Cannot divide by 0");
		}
		return new Value(VariableTypes.Nil, null);
	}
	
	
}
