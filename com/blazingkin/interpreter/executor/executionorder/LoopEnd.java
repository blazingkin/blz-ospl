package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class LoopEnd implements InstructionExecutor {

	@Override
	public void run(String[] args) {
		if (Executor.loopStack.size() == 0){
			Executor.loopIgnoreMode = false;
			return;
		}
		if (Executor.loopIgnoreMode){
			Executor.loopIgnoreMode = false;
			Executor.loopStack.pop();
		}else{
			int newLine = Executor.loopStack.peek().startLine;
			Executor.setLine(newLine + 1);
		}
	}

}
