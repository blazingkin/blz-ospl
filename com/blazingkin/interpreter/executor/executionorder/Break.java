
package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;

public class Break implements InstructionExecutorStringArray {

	@Override
	public void run(String[] args) {
		Executor.setBreakMode(true);
		while (!(RuntimeStack.runtimeStack.peek() instanceof LoopWrapper)){
			RuntimeStack.pop();
		}
		LoopWrapper lw = (LoopWrapper) RuntimeStack.runtimeStack.peek();
		RuntimeStack.pop();
		Executor.setLine(lw.startLine);
		Executor.setLine(Executor.getCurrentBlockEnd());
	}

}
