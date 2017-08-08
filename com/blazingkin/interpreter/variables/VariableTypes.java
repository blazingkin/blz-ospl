package com.blazingkin.interpreter.variables;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.blazingkin.interpreter.executor.lambda.LambdaExpression;

@SuppressWarnings("rawtypes")
public enum VariableTypes {

	Integer(BigInteger.class),
	String(java.lang.String.class),
	Double(BigDecimal.class),
	Boolean(java.lang.Boolean.class),
	Rational(BLZRational.class),
	Tensor(BLZTensor.class),
	Array(Value[].class),
	LambdaExpression(LambdaExpression.class),
	Nil(null);
	
	VariableTypes(Class n){
		dataType = n;
	}

	public final Class dataType;
	
	
}
