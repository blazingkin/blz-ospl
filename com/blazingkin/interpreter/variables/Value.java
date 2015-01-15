package com.blazingkin.interpreter.variables;

import com.blazingkin.interpreter.executor.Method;

public class Value {
	public VariableTypes type;
	@SuppressWarnings("rawtypes")
	public Comparable value;
	public boolean isGlobal = false;
	public Method parent = null;
	public Value(VariableTypes t, @SuppressWarnings("rawtypes") Comparable val){	//This stores the value and the type of value that it is
		type = t;
		value = val;
	}
	public Value(VariableTypes t, @SuppressWarnings("rawtypes") Comparable val, Method par, boolean global){
		type = t;
		value = val;
		isGlobal = global;
		parent = par;
	}
	public Value(VariableTypes t, @SuppressWarnings("rawtypes") Comparable val, Method par){
		type = t;
		value = val;
		parent = par;
	}
}
