package com.blazingkin.interpreter.executor.output;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Variable;

public class SameLineEcho implements InstructionExecutorStringArray {
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
	

}
