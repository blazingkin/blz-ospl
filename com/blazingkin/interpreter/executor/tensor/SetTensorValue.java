package com.blazingkin.interpreter.executor.tensor;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.BLZTensor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class SetTensorValue implements InstructionExecutor {

	//Params are Tensor Name, New Value, List, Of, Indices
	@Override
	public void run(String[] args) {
		Value tensor = Variable.getValue(args[0]);
		if (tensor.type != VariableTypes.Tensor){
			Interpreter.throwError("Variable was not of type tensor");
		}
		@SuppressWarnings("unchecked")
		BLZTensor<Integer> blztensor = (BLZTensor<Integer>) tensor.value;
		int[] indexList = new int[args.length - 2];
		int value = 0;
		try{
			value = Integer.parseInt(Variable.parseString(args[1]));
			for (int i = 2; i < args.length; i++){
				indexList[i-2] = Integer.parseInt(Variable.parseString(args[i]));
			}
		}catch(Exception e){
			Interpreter.throwError("Failure parsing an int when setting a tensor value");
		}
		blztensor.setTensorValue(indexList, value);
	}

}
