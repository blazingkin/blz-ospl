package com.blazingkin.interpreter.executor.executionstack;

import com.blazingkin.interpreter.BLZRuntimeException;

public interface RuntimeStackElement {

	public void onBlockStart() throws BLZRuntimeException;
	public void onBlockEnd();
	public int getLineNum();
	
}
