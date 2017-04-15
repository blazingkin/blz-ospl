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
			Value v = Variable.getValue(args[1]);
			value = Variable.getIntValue(v);
			
			for (int i = 2; i < args.length; i++){
				Value va = Variable.getValue(args[i]);
				int val = Variable.getIntValue(va);
				indexList[i-2] = val;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		blztensor.setTensorValue(indexList, value);
	}

}
