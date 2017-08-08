package com.blazingkin.interpreter.unittests;

import com.blazingkin.interpreter.library.BlzEventHandler;

public class TestEventHandler implements BlzEventHandler {
	
	@Override
	public void print(String contents) {
		UnitTestUtil.log(contents);
	}

	@Override
	public void exitProgram(String exitMessage) {
		UnitTestUtil.exitLog(exitMessage);
	}

	@Override
	public String getInput() {
		return UnitTestUtil.getInputFromBuffer();
	}

}
