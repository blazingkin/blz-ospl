package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;

public class LoopEnd implements InstructionExecutor {

	@Override
	public void run(String[] args) {
		if (Executor.getLoopStack().size() == 0){
			Executor.setLoopIgnoreMode(false);
			return;
		}
		if (Executor.isLoopIgnoreMode()){
			Executor.setLoopIgnoreMode(false);
			Executor.getLoopStack().pop();
		}else{
			int newLine = Executor.getLoopStack().peek().startLine;
			Executor.setLine(newLine + 1);
		}
	}

}
