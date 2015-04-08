package com.blazingkin.interpreter.executor.listener;

import com.blazingkin.interpreter.executor.Method;

public class Event {
	public Method method;
	public String[] arguments;
	public Event(Method m, String[] args){
		method = m;
		arguments = args;
	}
	public Event(Method m){
		method = m;
		arguments = new String[0];
	}

}
