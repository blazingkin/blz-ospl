package com.blazingkin.interpreter.variables;

import com.blazingkin.interpreter.executor.Method;

public class Value {
	public VariableTypes type;
	public Object value = null;
	public boolean isGlobal = false;
	public Method parent = null;
	public Value(VariableTypes t, Object val){	//This stores the value and the type of value that it is
		if (val == null){
			type = VariableTypes.Nil;
			value = null;
			return;
		}
		type = t;
		value = val;
	}
	public Value(VariableTypes t, Object val, Method par, boolean global){
		if (val == null){
			type = VariableTypes.Nil;
			value = null;
			isGlobal = global;
			parent = par;
			return;
		}
		type = t;
		value = val;
		isGlobal = global;
		parent = par;
	}
	public Value(VariableTypes t, Object val, Method par){
		if (val == null){
			type = VariableTypes.Nil;
			value = null;
			parent = par;
			return;
		}
		type = t;
		value = val;
		parent = par;
	}
	public void printValue(){
		System.out.println(value);
	}
	
	public boolean equals(Object other){
		if (other instanceof Value){
			Value v2 = (Value) other;
			return this.value == v2.value && this.type == v2.type;
		}
		return false;
	}
	
}
