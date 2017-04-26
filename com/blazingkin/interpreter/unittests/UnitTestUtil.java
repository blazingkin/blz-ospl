package com.blazingkin.interpreter.unittests;

import java.util.Random;

import com.blazingkin.interpreter.variables.Value;
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
		if (a != b){
			System.err.println("An assertion was false!");
			System.err.println(a + "!=" + b);
		}
		assert a == b;
	}
	
	public static void assertEqual(Value a, Value b){
		assert a.equals(b);
	}
	
	public static void assertEqualArrays(Object[] a, Object[] b){
		if (a.length != b.length){
			System.err.println("Two 'equal' arrays were not the same length");
			System.err.println("len of: "+a+" != len of: "+b);
		}
		for (int i = 0; i < a.length; i++){
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
