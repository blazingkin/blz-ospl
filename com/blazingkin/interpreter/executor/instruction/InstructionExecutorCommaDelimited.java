package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.executor.SimpleExpressionParser;
import com.blazingkin.interpreter.variables.Value;

public interface InstructionExecutorCommaDelimited extends InstructionExecutor {

	public default Value run(String line){
		String[] args = line.split(",");
		Value[] vals = new Value[args.length];
		for (int i = 0; i < args.length; i++){
			vals[i] = SimpleExpressionParser.parseExpression(args[i]);
		}
		return run(vals);
	}
	
	public abstract Value run(Value[] args);
	
}
