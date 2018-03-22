package com.blazingkin.interpreter;

import java.util.LinkedList;

import com.blazingkin.interpreter.library.BlzEventHandler;
import com.blazingkin.interpreter.library.StandAloneEventHandler;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.source.Source;

public class BLZCallTarget implements CallTarget {

	Source source;
	public BLZCallTarget(Source source) {
		this.source = source;
	}

	@Override
	public Object call(Object... arguments) {
		LinkedList<String> noArgs = new LinkedList<String>();
		BlzEventHandler standard = new StandAloneEventHandler();
		String[] s = source.getCode().split("\n"); 
		try {
		Interpreter.executeCodeAsLibrary(s, noArgs, standard);
		}catch(Exception e) {}
		return null;  
	} 

}
