package com.blazingkin.interpreter.variables;

import com.blazingkin.interpreter.executor.Method;

public class Value {
	public VariableTypes type;
	public Object value = new BLZInt(0);
	public boolean isGlobal = false;
	public Method parent = null;
	public Value(VariableTypes t, Object val){	//This stores the value and the type of value that it is
		if (val == null){
			type = VariableTypes.Integer;
			value = new Integer(0);
			return;
		}
		type = t;
		value = val;
	}
	public Value(VariableTypes t, Object val, Method par, boolean global){
		if (val == null){
			type = VariableTypes.Integer;
			value = new BLZInt(0);
			return;
		}
		type = t;
		value = val;
		isGlobal = global;
		parent = par;
	}
	public Value(VariableTypes t, Object val, Method par){
		if (val == null){
			type = VariableTypes.Integer;
			value = new BLZInt(0);
			return;
		}
		type = t;
		value = val;
		parent = par;
	}
	public void printValue(){
		System.out.println(value);
	}
}
