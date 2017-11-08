package com.blazingkin.interpreter.variables;

public class BLZObject {

	public Context objectContext;
	public BLZObject(Context parent){
		objectContext = new Context(null);
	}
	
	public BLZObject(){
		objectContext = new Context(Variable.getGlobalContext());
	}
	
}
