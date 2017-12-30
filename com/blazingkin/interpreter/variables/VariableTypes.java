package com.blazingkin.interpreter.variables;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import com.blazingkin.interpreter.executor.lambda.LambdaExpression;
import com.blazingkin.interpreter.executor.sourcestructures.Closure;
import com.blazingkin.interpreter.executor.sourcestructures.Method;

@SuppressWarnings("rawtypes")
public enum VariableTypes {
	
	Array(Value[].class),
	Boolean(java.lang.Boolean.class),
	Double(BigDecimal.class),
	Hash(HashMap.class),
	Integer(BigInteger.class),
	LambdaExpression(LambdaExpression.class),
	Method(Method.class),
	Closure(Closure.class),
	Nil(null),
	Object(BLZObject.class),
	Rational(BLZRational.class),
	String(java.lang.String.class),
	Tensor(BLZTensor.class);
	
	VariableTypes(Class n){
		dataType = n;
	}

	public final Class dataType;
	
	
}
