package com.blazingkin.interpreter.executor.output;


import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;

public class Echo implements InstructionExecutorValue {
	/*	Print
	 * 	Outputs the given text
	 */

	public Value run(Value arg){
		Executor.getEventHandler().print(arg.toString()+"\n");
		return Value.string(arg.toString());
	}
	

}
