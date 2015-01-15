package com.blazingkin.interpreter.executor;

@Deprecated
//@TODO delete when done implementing process
public class FunctionLine {
	public int UUID;
	public int lineNumber;
	public FunctionLine(int UUID, int lineNumber){
		this.UUID = UUID;
		this.lineNumber = lineNumber;
	}
}
