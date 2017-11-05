package com.blazingkin.interpreter.executor.listener;

import com.blazingkin.interpreter.executor.sourcestructures.Method;

public class Event {
	/*	
	 * 	Event - More accurate name is interupt, likely will be renamed soon
	 * 	
	 */
	
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
