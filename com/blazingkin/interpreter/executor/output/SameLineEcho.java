package com.blazingkin.interpreter.executor.output;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class SameLineEcho implements InstructionExecutor, LambdaFunction {
	/*	Print
	 * 	Outputs the given text
	 */
	public void run(String[] args) {
		String out = "";
		for (int i = 0; i < args.length; i++){
			out += Variable.getValue(args[i]).value;
		}
		Executor.getEventHandler().print(out);
	}
	@Override
	public Value evaluate(String[] args) {
		String out = "";
		for (int i = 0; i < args.length; i++){
			out += Variable.getValue(args[i]).value;
		}
		Executor.getEventHandler().print(out);
		return new Value(VariableTypes.String, out);
	}
	

}
