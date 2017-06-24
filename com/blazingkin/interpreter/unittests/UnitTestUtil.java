package com.blazingkin.interpreter.unittests;

import java.util.Random;


import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class UnitTestUtil {
	
	public static Random rand = new Random();
	
	public static boolean checkExpect(Object o1, Object o2){
		return o1.equals(o2);
	}
	
	public static void printArr(String[] a){
		for (String s: a){
			System.out.println(s);
		}
	}

	public static void assertEqual(Object a, Object b){
		org.junit.Assert.assertEquals(a, b);
		if (a != b){
			System.err.println("An assertion was false!");
			System.err.println(a + "!=" + b);
		}
		assert a == b;
	}
	
	public static void assertEqual(Value a, Value b){
		org.junit.Assert.assertEquals(a, b);
		if (!a.equals(b)){
			System.err.println(a.value + " != "+ b.value);
			new Exception().printStackTrace();
			assert false;
		}
		assert a.equals(b);
	}

	public static final double EPSILON = 1E-10;
	public static void assertAlmostEqual(Value a, Value b){

		if (a.equals(b)){
			assert a.equals(b);
			return;
		}
		if (Variable.isDecimalValue(a) && Variable.isDecimalValue(b)){
			double v1 = Variable.getDoubleVal(a);
			double v2 = Variable.getDoubleVal(b);
			double diff = Math.abs(v2-v1);
			if (diff > EPSILON){
				System.out.println(a.value + " !~= " + b.value);
				new Exception().printStackTrace();
			}
			assert diff < EPSILON;
			return;
		}
		System.err.println("Assert Almost Equal can't handle the two values "+ a.value+" and "+b.value);
		new Exception().printStackTrace();
	}
	
	public static void assertValEqual(String name, Value b){
		org.junit.Assert.assertEquals(Variable.getValue(name), b);
		if (!Variable.getValue(name).equals(b)){
			System.err.println(name + " != "+ b.value);
			new Exception().printStackTrace();
		}
		assert Variable.getValue(name).equals(b);
	}
	
	public static void assertEqualArrays(Object[] a, Object[] b){
		if (a.length != b.length){
			System.err.println("Two 'equal' arrays were not the same length");
			System.err.println("len of: "+a+" != len of: "+b);
		}
		for (int i = 0; i < a.length; i++){
			org.junit.Assert.assertEquals(a[i], b[i]);
			assert a[i] == b[i];
		}
	}
	
	public static Value getIntValue(int v){
		return new Value(VariableTypes.Integer, v);
	}
	
	public static Value getDoubleValue(double v){
		return new Value(VariableTypes.Double, v);
	}
	
}
