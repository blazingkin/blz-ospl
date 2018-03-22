package com.blazingkin.interpreter.unittests.astnodes;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blazingkin.interpreter.expressionabstraction.ExpressionParser;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class LessThanNodeUnitTest {

	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
	}
	
	@Test 
	public void StringsShouldBeSortable() {
		UnitTestUtil.assertEqual(Value.bool(true), ExpressionParser.parseExpression("\"a\" < \"b\"").execute(new Context()));
	}
	
	@Test
	public void TwoShouldBeLessThanThree() {
		UnitTestUtil.assertEqual(Value.bool(true), ExpressionParser.parseExpression("2 < 3").execute(new Context()));
	}
	
	@Test
	public void TwoShouldNotBeLessThanTwo() {
		UnitTestUtil.assertEqual(Value.bool(false), ExpressionParser.parseExpression("2 < 2").execute(new Context()));
	}

}