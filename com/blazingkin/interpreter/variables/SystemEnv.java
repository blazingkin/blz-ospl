package com.blazingkin.interpreter.variables;

public enum SystemEnv {
	//screen
	cursorPosX("screen.cursor.position.x", true),
	cursorPosY("screen.cursor.position.y", true),
	
	//system
	time("system.time.currenttimemillis", true),
	osName("system.os.name", false),
	osVersion("system.os.version", false),
	
	
	//Running Location
	runningFileLocation("file.location", false),		//returns the path to the folder it is in
	runningFileName("file.name", false),
	runningFilePath("file.path", false),
	
	//env variables
	processUUID("process.current.uuid", true),
	processesRunning("process.all", true),
	lineReturns("process.lines.returns", true),
	
	
	//constants
	pi("pi", false),
	euler("e", false),
	
	//blz variables
	version("blz.version", false),
	nil("blz.nil", false),
	context("blz.context.id", true),
	methodStack("blz.method.stack", true),
	
	
	//text special characters
	newline("text.newline", false),
	shift("text.shift", false),
	control("text.control", false),
	alt("text.alt", false),
	systemKey("text.system.key", false),
	tab("text.tab", false),
	back("text.back", false),
	caps("text.caps", false),
	space("text.space", false);
	
	SystemEnv(String st, boolean dynamic){
		name = st;
		this.dynamic = dynamic;
	}
	public final boolean dynamic;
	public final String name;
}
