package com.blazingkin.interpreter.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.blazingkin.interpreter.variables.Variable;

public class VariableUnitTest {

	@Test
	public void testIsInteger() {
		assertTrue(Variable.isInteger("124214"));
		assertTrue(Variable.isInteger("35125214"));
		assertTrue(Variable.isInteger("90891535214"));
		assertTrue(Variable.isInteger("-52315904"));
		assertTrue(Variable.isInteger("-9801235120"));
		assertFalse(Variable.isInteger("12.4214"));
		assertFalse(Variable.isInteger("12a4214"));
		assertFalse(Variable.isInteger(""));
		assertFalse(Variable.isInteger("124214."));
	}
	
	@Test
	public void testIsDouble(){
		assertTrue(Variable.isDouble("21215125"));
		assertTrue(Variable.isDouble("80126"));
		assertTrue(Variable.isDouble("-8903515.9805231"));
		assertTrue(Variable.isDouble(".9315209"));
		assertTrue(Variable.isDouble("-.912509"));
		assertFalse(Variable.isDouble("90421a809412"));
		assertFalse(Variable.isDouble("90421.809.412"));
		assertFalse(Variable.isDouble(""));
		assertFalse(Variable.isDouble("..940214"));
	}

}
