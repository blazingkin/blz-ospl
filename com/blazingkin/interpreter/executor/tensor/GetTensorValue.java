package com.blazingkin.interpreter.executor.tensor;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.variables.BLZTensor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class GetTensorValue implements InstructionExecutor {

	//Params go: Tensor name, list, of, indexes, ..., value to set
	@Override
	public void run(String[] args) {
		Value tensorHuh = Variable.getValue(args[0]);
		if (tensorHuh.type != VariableTypes.Tensor){
			Interpreter.throwError("Attempted to get a tensor value from a non-tensor");
		}
		@SuppressWarnings("unchecked")
		BLZTensor<Integer> tensor = (BLZTensor<Integer>) tensorHuh.value;
		int[] indices = new int[args.length-2];
		for (int i = 1; i < args.length-1; i++){
			indices[i-1] = Variable.getIntValue(Variable.getValue(args[i])).intValue();
		}
		Variable.setValue(args[args.length-1], new Value(VariableTypes.Integer,tensor.getTensorEntry(indices)));
	}

}
