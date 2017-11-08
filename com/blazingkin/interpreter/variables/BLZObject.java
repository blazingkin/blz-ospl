package com.blazingkin.interpreter.variables;

public class BLZObject implements Cloneable {

	public Context objectContext;
	public BLZObject(Context parent){
		objectContext = new Context(null);
	}
	
	public BLZObject(){
		objectContext = new Context(Variable.getGlobalContext());
	}
	
	@Override
	public BLZObject clone(){
		BLZObject newObj = new BLZObject();
		for (String s : objectContext.variables.keySet()){
			newObj.objectContext.setValue(s, objectContext.variables.get(s));
		}
		return newObj;
	}
	
}
