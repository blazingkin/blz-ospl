package com.blazingkin.interpreter.variables;

public class Context {
	private int contextID;
	public Context(){
		contextID = getUID();
	}
	public int getID(){
		return contextID;
	}
	
	
	private static int getUID(){
		return contextCounter++;
	}
	private static int contextCounter = 0;
}
