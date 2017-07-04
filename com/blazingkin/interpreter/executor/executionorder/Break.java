
package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;

public class Break implements InstructionExecutor {

	@Override
	public void run(String[] args) {
		Executor.setBreakMode(true);
		while (!(Executor.getRuntimeStack().peek() instanceof LoopWrapper)){
			Executor.popStack();
		}
		LoopWrapper lw = (LoopWrapper) Executor.getRuntimeStack().peek();
		Executor.popStack();
		Executor.setLine(lw.startLine);
		Executor.setLine(Executor.getCurrentBlockEnd());
	}

}
