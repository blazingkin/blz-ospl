package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Value;

public class Continue implements InstructionExecutorStringArray {

	@Override
	public Value run(String[] args) {
		while (!(RuntimeStack.runtimeStack.peek() instanceof LoopWrapper)){
			RuntimeStack.pop();
		}
		LoopWrapper lw = (LoopWrapper) RuntimeStack.runtimeStack.peek();
		Executor.setLine(lw.startLine);
		Executor.setLine(Executor.getCurrentBlockEnd());
		return RuntimeStack.pop();
	}

}
