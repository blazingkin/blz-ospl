package com.blazingkin.interpreter.variables;
@SuppressWarnings("rawtypes")
public enum VariableTypes {

	Integer(BLZInt.class),
	String(java.lang.String.class),
	Double(java.lang.Double.class),
	Boolean(java.lang.Boolean.class),
	Tensor(BLZTensor.class);
	
	VariableTypes(Class n){
		dataType = n;
	}

	public final Class dataType;
	
	
}
