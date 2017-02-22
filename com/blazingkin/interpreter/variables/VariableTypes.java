package com.blazingkin.interpreter.variables;
@SuppressWarnings("rawtypes")
public enum VariableTypes {

	Integer(java.lang.Integer.class),
	String(java.lang.String.class),
	Double(java.lang.Double.class),
	Boolean(java.lang.Boolean.class),
	Tensor(BLZTensor.class),
	Array(java.util.ArrayList.class),
	Nil(null);
	
	VariableTypes(Class n){
		dataType = n;
	}

	public final Class dataType;
	
	
}
