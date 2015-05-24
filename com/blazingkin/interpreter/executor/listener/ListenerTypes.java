package com.blazingkin.interpreter.executor.listener;

public enum ListenerTypes {
	KeyboardKeyDown("keyboard.keydown"),
	KeyboardKeyUp("keyboard.keyup"),
	MouseDown("mouse.mouseonedown"),
	MouseUp("mouse.mouseoneup"),
	MouseTwoDown("mouse.mousetwodown"),
	MouseTwoUp("mouse.mousetwoup"),
	MouseEntered("mouse.enter"),
	MouseExited("mouse.exit"),
	MouseOneClick("mouse.click"),
	MouseTwoClick("mouse.clicktwo"),
	MouseMotion("mouse.move");
	
	ListenerTypes(String name){
		this.name = name;
	}
	public final String name;
}
