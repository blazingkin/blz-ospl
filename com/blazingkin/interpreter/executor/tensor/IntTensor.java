package com.blazingkin.interpreter.executor.tensor;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.variables.BLZTensor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class IntTensor implements InstructionExecutor {

	//Params are Tensor Name, Tensor Rank
	@Override
	public void run(String[] args) {
		if (args.length != 2){
			Interpreter.throwError("Wrong number of arguments to create tensor");
		}
		try{
			int rank = (int) Variable.getValue(args[1]).value;
			if (rank < 1){
				throw new Exception();
			}
			Value tensor = new Value(VariableTypes.Tensor, new BLZTensor<Integer>(rank));
			Variable.setValue(args[0], tensor);
		}catch(Exception e){
			Interpreter.throwError("Second argument to create tensor was not a positive integer");
		}
	}

}
