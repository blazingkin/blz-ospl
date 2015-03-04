package com.blazingkin.interpreter.variables;

public enum SystemEnv {
	//screen
	windowSizeX("screen.window.size.x"),
	windowSizeY("screen.window.size.y"),
	windowPosX("screen.window.position.x"),
	windowPosY("screen.window.position.y"),
	screenResX("screen.resolution.size.x"),
	screenResY("screen.resolution.size.y"),
	cursorPosX("screen.cursor.position.x"),
	cursorPosY("screen.cursor.position.y"),
	FPS("screen.window.fps"),
	
	//system
	time("system.time.currenttimemillis"),
	osName("system.os.name"),
	osVersion("system.os.version"),
	
	
	//Running Location
	runningFileLocation("file.location"),		//returns the path to the folder it is in
	runningFileName("file.name"),
	runningFilePath("file.path"),
	
	//env variables
	processUUID("process.current.uuid"),
	processesRunning("process.all"),
	lineReturns("process.lines.returns"),
	
	
	//blz variables
	version("blz.version");
	
	SystemEnv(String st){
		name = st;
	}
	public final String name;
}
