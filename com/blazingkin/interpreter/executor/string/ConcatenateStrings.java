package com.blazingkin.interpreter.executor.string;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ConcatenateStrings implements InstructionExecutor, LambdaFunction {
	
	public String parseString(String[] args){
		String s = "";
		for (int i = 1; i < args.length; i++){
			s = s+args[i];
			if (i != args.length-1){
				s = s + " ";
			}
		}
		String[] splits = s.split("\"");
		String g = "";
			for (int i = 0; i < splits.length; i++){
				if (i%2 == 0){
					g = g+Variable.getValue(splits[i].replace(" ", ""));	
				}else{
					g = g+(splits[i]);
				}
			}
		return g;
	}
	
	public void run(String[] args){
		Variable.setValue(args[0], new Value(VariableTypes.String, parseString(args)));
	}

	@Override
	public Value evaluate(String[] args) {

		return new Value(VariableTypes.String, parseString(args));
	}

}
