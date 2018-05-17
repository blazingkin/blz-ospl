package com.blazingkin.interpreter.unittests.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.astnodes.ArrayLookupNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class ArrayLookupNodeUnitTest {
	static Value[] testArray = { Value.integer(3), Value.integer(4), Value.string("hi"), Value.bool(false)};
	
	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
	}

	@Test
	public void shouldRequireAnArgument() {
		ASTNode[] args = {};
		new ArrayLookupNode(args);
		UnitTestUtil.assertLastError("Array Lookup did not have 2 arguments.");
	}
	
	@Test
	public void shouldRequireTwoArguments() {
		ASTNode[] args = { new ValueASTNode(Value.integer(3))};
		new ArrayLookupNode(args);
		UnitTestUtil.assertLastError("Array Lookup did not have 2 arguments.");
	}
	
	@Test
	public void shouldRequireAnArrayAsTheFirstType() {
		ASTNode[] args = { new ValueASTNode(Value.integer(3)), new ValueASTNode(Value.integer(3)) };
		try {
		new ArrayLookupNode(args).execute(new Context());
		} catch (BLZRuntimeException e) {
			UnitTestUtil.assertEqual(e.getMessage(), "Did not know how to access 3 as an array.");
			return;
		} // We aren't catching the exception later because we normally exit
		UnitTestUtil.fail();
	}
	
	@Test
	public void shouldCorrectlyLookupAValue() throws BLZRuntimeException {
		ASTNode[] args = {new ValueASTNode(Value.arr(testArray)), new ValueASTNode(Value.integer(1)) };
		ArrayLookupNode n = new ArrayLookupNode(args);
		UnitTestUtil.assertEqual(Value.integer(4), n.execute(new Context()));
	}
	
	@Test
	public void shouldCorrectlyLookupAnotherValue() throws BLZRuntimeException {
		ASTNode[] args = {new ValueASTNode(Value.arr(testArray)), new ValueASTNode(Value.integer(3)) };
		ArrayLookupNode n = new ArrayLookupNode(args);
		UnitTestUtil.assertEqual(Value.bool(false), n.execute(new Context()));
	}
	
	@Test
	public void shouldLookupCharacterInString() throws BLZRuntimeException {
		UnitTestUtil.assertEqual(Value.string("b"), ExpressionExecutor.parseExpression("\"abc\"[1]"));
	}
	
	@Test
	public void shouldLookupCharacterInStringSetInVariable() throws BLZRuntimeException{
		ExpressionExecutor.parseExpression("a = \"lmno\"");
		UnitTestUtil.assertEqual(Value.string("l"), ExpressionExecutor.parseExpression("a[0]"));
		UnitTestUtil.assertEqual(Value.string("m"), ExpressionExecutor.parseExpression("a[1]"));
		UnitTestUtil.assertEqual(Value.string("n"), ExpressionExecutor.parseExpression("a[2]"));
		UnitTestUtil.assertEqual(Value.string("o"), ExpressionExecutor.parseExpression("a[3]"));
	}

}
