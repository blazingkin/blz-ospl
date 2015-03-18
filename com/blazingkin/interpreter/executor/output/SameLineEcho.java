package com.blazingkin.interpreter.executor.output;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;

public class SameLineEcho implements InstructionExecutor {
	/*	Print
	 * 	Outputs the given text
	 */
	public void run(String[] args) {
		for (int i = 0; i < args.length; i++){
			System.out.print(Variable.parseString(args[i]));
			if (i != args.length - 1){
				System.out.print(" ");
			}
		}
	}

}
