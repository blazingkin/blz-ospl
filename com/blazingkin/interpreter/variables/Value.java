package com.blazingkin.interpreter.variables;

import com.blazingkin.interpreter.executor.Method;

public class Value {
	public VariableTypes type;
	public Object value = null;
	public boolean isGlobal = false;
	public Method parent = null;
	public Value(VariableTypes t, Object val){	//This stores the value and the type of value that it is
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
			return (this.value == v2.value || this.value.equals(v2.value)) && this.type.equals(v2.type);
		}
		return false;
	}
	
	public static Value rational(long num, long den){
		BLZRational rat = new BLZRational(num, den);
		if (rat.den == 1){
			return new Value(VariableTypes.Integer, (int)rat.num);
		}
		return new Value(VariableTypes.Rational, rat);
	}
	public static Value rational(int num, int den){
		return rational((long)num, (long)den);
	}
	
	public static Value integer(int val){
		return new Value(VariableTypes.Integer, val);
	}
	
	public static Value bool(boolean val){
		return new Value(VariableTypes.Boolean, val);
	}
	
}
