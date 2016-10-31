package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.InstructionExecutor;

public class LoopWrapper implements InstructionExecutor {
	public String initInstr;
	public String loopInstr;
	public String termInstr;
	public int startLine;
	public int functionUUID;
	
	@Override
	public void run(String[] args) {
		// TODO Auto-generated method stub
		
	}

	
}
