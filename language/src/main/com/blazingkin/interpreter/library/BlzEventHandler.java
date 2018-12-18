package com.blazingkin.interpreter.library;

public interface BlzEventHandler {
	//If you wish to interface with the BLZ language, you can implement this class to handle
	//the state of the program (currently mostly I/O)
	
	public void print(String contents);	//This handles all text output
	public void err(String contents); // This handles output to stderr
	public void exitProgram(String exitMessage);	//This handles a close request from inside the code
	public String getInput();	//This handles input requests
}
