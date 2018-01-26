package com.blazingkin.interpreter.variables;

import com.blazingkin.interpreter.executor.sourcestructures.Method;

public class BLZPrimitiveMethod {
	public Method m;
	public Value v;
	public boolean passByReference;
	public BLZPrimitiveMethod(Method m, Value v, boolean passByReference) {
		this.m = m;
		this.v = v;
		this.passByReference = passByReference;
	}
	public String toString() {
		return m.toString();
	}
}
