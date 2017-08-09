package com.blazingkin.interpreter.variables;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.blazingkin.interpreter.executor.Method;
import com.blazingkin.interpreter.executor.lambda.LambdaExpression;

@SuppressWarnings("rawtypes")
public enum VariableTypes {
	
	Array(Value[].class),
	Boolean(java.lang.Boolean.class),
	Double(BigDecimal.class),
	Integer(BigInteger.class),
	LambdaExpression(LambdaExpression.class),
	Method(Method.class),
	Rational(BLZRational.class),
	String(java.lang.String.class),
	Tensor(BLZTensor.class),
	Nil(null);
	
	VariableTypes(Class n){
		dataType = n;
	}

	public final Class dataType;
	
	
}
