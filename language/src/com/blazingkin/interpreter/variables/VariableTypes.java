package com.blazingkin.interpreter.variables;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import com.blazingkin.interpreter.executor.astnodes.MethodNode;
import com.blazingkin.interpreter.executor.sourcestructures.Closure;
import com.blazingkin.interpreter.executor.sourcestructures.Constructor;
@SuppressWarnings("rawtypes")
public enum VariableTypes {
	
	Array(Value[].class),
	Boolean(java.lang.Boolean.class),
	Closure(Closure.class),
	Constructor(Constructor.class),
	Double(BigDecimal.class),
	Hash(HashMap.class),
	Integer(BigInteger.class),
	Method(MethodNode.class),
	Nil(null),
	Object(BLZObject.class),
	PrimitiveMethod(BLZPrimitiveMethod.class),
	Rational(BLZRational.class),
	String(java.lang.String.class),
	Tensor(BLZTensor.class);
	
	VariableTypes(Class n){
		dataType = n;
	}

	public final Class dataType;
	public static HashMap<VariableTypes, Context> primitiveContexts = new HashMap<VariableTypes, Context>();
	public static void initialize() {
		for (VariableTypes vt : VariableTypes.values()) {
			primitiveContexts.put(vt, new Context(null));
		}
	}
	public static void clear() {
		primitiveContexts = new HashMap<VariableTypes, Context>();
		initialize();
	}
	static {
		initialize();
	}
}
