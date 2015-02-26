package com.blazingkin.interpreter.variables;
@SuppressWarnings("rawtypes")
public enum VariableTypes {

	Integer(new java.lang.Integer(0).getClass()),
	String(new java.lang.String("").getClass()),
	Double(new java.lang.Double(0.0d).getClass()),
	Boolean(new java.lang.Boolean(false).getClass());
	
	VariableTypes(Class n){
		dataType = n;
	}

	public final Class dataType;
	
	
}
