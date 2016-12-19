package com.blazingkin.interpreter.library;

public interface BlzEventHandler {
	
	public void print(String contents);
	public void exitProgram(String exitMessage);
	public String getInput();
}
