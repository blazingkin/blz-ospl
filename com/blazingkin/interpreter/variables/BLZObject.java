package com.blazingkin.interpreter.variables;

public class BLZObject {

	public Context objectContext;
	public BLZObject(Context parent){
		objectContext = new Context(parent);
	}
	
	public BLZObject(){
		objectContext = new Context(Variable.getGlobalContext());
	}
	
}
