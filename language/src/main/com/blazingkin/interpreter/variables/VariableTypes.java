package com.blazingkin.interpreter.variables;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.HashMap;

import com.blazingkin.interpreter.executor.astnodes.Closure;
import com.blazingkin.interpreter.executor.astnodes.MethodNode;
import com.blazingkin.interpreter.executor.sourcestructures.Constructor;
@SuppressWarnings("rawtypes")
public enum VariableTypes {
	
	Array(Value[].class, "Array"),
	Boolean(java.lang.Boolean.class, "Boolean"),
	Closure(Closure.class, "Method"),
	Constructor(Constructor.class, "Constructor"),
	Double(BigDecimal.class, "Double"),
	Hash(HashMap.class, "HashMap"),
	Integer(BigInteger.class, "Integer"),
	Method(MethodNode.class, "Method"),
	Nil(null, "Nil"),
	Object(BLZObject.class, "Object"),
	PrimitiveMethod(BLZPrimitiveMethod.class, "Method"),
	Rational(BLZRational.class, "Rational"),
	Resource(URL.class, "Resource"),
	String(java.lang.String.class, "String");
	
	public final Class dataType;
	public final String typeName;
	VariableTypes(Class n, String typeName){
		dataType = n;
		this.typeName = typeName;
	}

	public static HashMap<VariableTypes, Context> primitiveContexts = new HashMap<VariableTypes, Context>();
	public static void initialize() {
		for (VariableTypes vt : VariableTypes.values()) {
			Context primitiveContext = new Context(null);
			primitiveContext.setValue("type", Value.string(vt.typeName));
			primitiveContexts.put(vt, primitiveContext);
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
