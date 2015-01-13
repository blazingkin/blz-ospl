package com.blazingkin.interpreter.variables;

public class Value {
	public VariableTypes type;
	@SuppressWarnings("rawtypes")
	public Comparable value;
	public Value(VariableTypes t, @SuppressWarnings("rawtypes") Comparable val){	//This stores the value and the type of value that it is
		type = t;
		value = val;
		
		
	}
}
