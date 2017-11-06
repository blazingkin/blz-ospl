package com.blazingkin.interpreter.executor.executionstack;

public interface RuntimeStackElement {

	public void onBlockStart();
	public void onBlockEnd();
	public int getLineNum();
	
}
