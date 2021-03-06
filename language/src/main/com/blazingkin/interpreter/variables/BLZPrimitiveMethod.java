package com.blazingkin.interpreter.variables;

import com.blazingkin.interpreter.executor.astnodes.MethodNode;

public class BLZPrimitiveMethod {
	public MethodNode m;
	public Value v;
	public boolean passByReference;

	/* This is used when using a dot operator with a primitive (This is necessary in order to pass the primitive) */
	public BLZPrimitiveMethod(MethodNode m, Value v, boolean passByReference) {
		this.m = m;
		this.v = v;
		this.passByReference = passByReference;
	}
	public String toString() {
		return m.toString();
	}
}
