package com.blazingkin.interpreter.variables;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.blazingkin.interpreter.executor.sourcestructures.Closure;
import com.blazingkin.interpreter.executor.sourcestructures.Constructor;
import com.blazingkin.interpreter.executor.sourcestructures.Method;

public class Value implements Cloneable {
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
	
	@Override
	public Value clone() {
		if (type == VariableTypes.Array) {
			return new Value(type, ((Value[]) value).clone(), parent, isGlobal);
		}else if (type == VariableTypes.Object){
			return new Value(type, ((BLZObject)value).clone(), parent, isGlobal);
		}
		return this;
	}
	
	public String typedToString(){
		return "<"+type+" "+value+">";
	}
	
	public void printValue(){
		System.out.println(value);
	}
	
	public String toString(){
		if (value instanceof Value[]){
			String buildingString = "[";
			Value[] arr = (Value[]) value;
			for (int i = 0; i < arr.length-1; i++){
				buildingString += arr[i].toString() + ", ";
			}
			if (arr.length != 0){
				buildingString += arr[arr.length-1].toString();
			}
			return buildingString + "]";
		}else if (value != null){
			return value.toString();
		}
		return "";
	}
	
	public boolean equals(Object other){
		if (other instanceof Value){
			Value v2 = (Value) other;
			if (type == VariableTypes.Array && v2.type == VariableTypes.Array){
				Value[] ar1 = (Value[]) value;
				Value[] ar2 = (Value[]) v2.value;
				if (ar1.length != ar2.length){
					return false;
				}
				for (int i = 0; i < ar1.length; i++){
					if (!ar1[i].equals(ar2[i])){
						return false;
					}
				}
				return true;
			}
			return (this.value == v2.value || this.value.equals(v2.value)) && this.type.equals(v2.type);
		}
		return false;
	}
	
	public static Value rational(long num, long den){
		BLZRational rat = new BLZRational(BigInteger.valueOf(num), BigInteger.valueOf(den));
		if (rat.den.equals(BigInteger.ONE)){
			return new Value(VariableTypes.Integer, rat.num);
		}
		return new Value(VariableTypes.Rational, rat);
	}
	public static Value rational(int num, int den){
		return rational((long)num, (long)den);
	}
	
	public static Value rational(BigInteger num, BigInteger den){
		BLZRational rat = new BLZRational(num, den);
		if (rat.den.equals(BigInteger.ONE)){
			return new Value(VariableTypes.Integer, rat.num);
		}
		return new Value(VariableTypes.Rational, rat);
	}
	
	public static Value doub(double num){
		return new Value(VariableTypes.Double, BigDecimal.valueOf(num));
	}
	
	public static Value integer(int val){
		return new Value(VariableTypes.Integer, BigInteger.valueOf(val));
	}
	
	public static Value bool(boolean val){
		return new Value(VariableTypes.Boolean, val);
	}
	
	public static Value arr(Value[] val){
		return new Value(VariableTypes.Array, val);
	}
	
	public static Value obj(BLZObject ob){
		return new Value(VariableTypes.Object, ob);
	}
	
	public static Value string(String str){
		return new Value(VariableTypes.String, str);
	}
	
	public static Value closure(Closure closure){
		return new Value(VariableTypes.Closure, closure);
	}
	
	public static Value constructor(Constructor constructor){
		return new Value(VariableTypes.Constructor, constructor);
	}
	
	public static Value method(Method m){
		return new Value(VariableTypes.Method, m);
	}
	
	public static Value nil() {
		return new Value(VariableTypes.Nil, null);
	}
	
	@Override
	public int hashCode(){
		return value.hashCode();
	}
	
}
