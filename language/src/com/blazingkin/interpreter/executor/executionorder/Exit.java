package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class Exit implements InstructionExecutorValue {

	@Override
	public Value run(Value val) {
		/*	Exit
		 * 	sets the closeRequested flag to true
		 */
		Executor.setCloseRequested(true);
		if (Variable.isValInt(val)){
			int exitCode = Variable.getIntValue(val).intValue();
			Interpreter.setExitCode(exitCode);
		}
		return Value.nil();
	}

}
