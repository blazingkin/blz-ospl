package com.blazingkin.interpreter.executor.output.graphics;

public enum GraphicsProperty {
	
	borderless("borderless"),
	fullscreen("fullscreen"),
	bordered("bordered"),
	windowed("windowed"),
	visible("visible"),
	invisible("invisible");
	
	GraphicsProperty(String nme){
		name = nme;
	}
	public final String name;

}
