package com.blazingkin.interpreter.unittests.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.Value;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

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
	public void emptyBracketsShouldBeEmptyArray() throws BLZRuntimeException {
		Value[] arr = {};
		UnitTestUtil.assertEqual(Value.arr(arr), ExpressionExecutor.parseExpression("a = []"));
	}

}
