package com.blazingkin.interpreter.executor.math;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

@Deprecated
public class AddVars implements InstructionExecutor, LambdaFunction {

	public void run(String[] args) {
		/*	Add Vars
		 * 	Will try to add them as integers first
		 * 	If it cannot it will pass the variables to concatenate as strings
		 * 
		 * 
		 * 
		 */
		Value v1 = Variable.getValue(args[0]);
		Value v2 = Variable.getValue(args[1]);
		if (Variable.isValInt(v1) && Variable.isValInt(v2)){
			BigInteger i1 = Variable.getIntValue(v1);
			BigInteger i2 = Variable.getIntValue(v2);
			Variable.setValue(args[2], new Value(VariableTypes.Integer, i1.add(i2)));
			return;
		}else if ((Variable.isValDouble(v1) || Variable.isValInt(v1))
				&& (Variable.isValDouble(v2) || Variable.isValInt(v2))){
			BigDecimal d1 = Variable.getDoubleVal(v1);
			BigDecimal d2 = Variable.getDoubleVal(v2);
			Variable.setValue(args[2], new Value(VariableTypes.Double, d1.add(d2)));
			return;
		}
		String arr[] = new String[args.length];
		for (int i = 0; i < args.length; i++)
		{
			arr[i] = args[i];
		}
		{
			String temp = arr[arr.length-1];
			for (int i = arr.length-1; i > 0; i--){
				arr[i] = arr[i-1];
			}
			arr[0] = temp;
		}
		Instruction.CONCATENATE.executor.run(arr);
	}

	@Override
	public Value evaluate(String[] args) {
		Value v1 = Variable.getValue(args[0]);
		Value v2 = Variable.getValue(args[1]);
		if (Variable.isValInt(v1) && Variable.isValInt(v2)){
			BigInteger i1 = Variable.getIntValue(v1);
			BigInteger i2 = Variable.getIntValue(v2);
			return new Value(VariableTypes.Integer, i1.add(i2));
		}else if ((Variable.isValDouble(v1) || Variable.isValInt(v1))
				&& (Variable.isValDouble(v2) || Variable.isValInt(v2))){
			BigDecimal d1 = Variable.getDoubleVal(v1);
			BigDecimal d2 = Variable.getDoubleVal(v2);
			return new Value(VariableTypes.Double, d1.add(d2));
		}
		Interpreter.throwError("Invalid arguments "+v1.value+" and "+v2.value+" passed to adding vars");
		return new Value(VariableTypes.Nil, null);
	}
	
	

}
