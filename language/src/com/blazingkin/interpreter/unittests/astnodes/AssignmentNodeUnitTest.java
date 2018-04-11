package com.blazingkin.interpreter.unittests.astnodes;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.Value;

public class AssignmentNodeUnitTest {

	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
	}
	
	@Test
	public void emptyBracketsShouldBeEmptyArray() {
		Value[] arr = {};
		UnitTestUtil.assertEqual(Value.arr(arr), ExpressionExecutor.parseExpression("a = []"));
	}

}
