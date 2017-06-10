package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Context;

public class LoopWrapper implements InstructionExecutor {
	public String initInstr;
	public String loopInstr;
	public String termInstr;
	public int startLine;
	public Context functionContext;
	
	@Override
	public void run(String[] args) {
		//This intentionally left blank
		Interpreter.throwError("The loop wrapper was mistakenly run");
	}
}
