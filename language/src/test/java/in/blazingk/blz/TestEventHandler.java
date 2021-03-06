package in.blazingk.blz;

import com.blazingkin.interpreter.library.BlzEventHandler;

public class TestEventHandler implements BlzEventHandler {
	
	@Override
	public void print(String contents) {
		UnitTestUtil.log(contents);
	}
	
	@Override
	public void err(String contents){
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
