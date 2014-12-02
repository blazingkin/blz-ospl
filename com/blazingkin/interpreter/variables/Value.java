package com.blazingkin.interpreter.variables;

public class Value {
	public VariableTypes type;
	@SuppressWarnings("rawtypes")
	public Comparable value;
	public Value(VariableTypes t, @SuppressWarnings("rawtypes") Comparable val){
		type = t;
		value = val;
		
		
	}
}
