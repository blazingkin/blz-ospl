package com.blazingkin.interpreter.unittests.astnodes;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blazingkin.interpreter.executor.astnodes.ArrayLiteralNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionParser;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.Value;

public class ArrayLiteralNodeUnitTest {
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
		new ArrayLiteralNode(args);
		UnitTestUtil.assertLastError("Array Literal did not have 1 argument");
	}
	
	@Test
	public void shouldParseToCorrectList() {
		ASTNode[] args = {ExpressionParser.parseExpression("2, 3, 4")};
		ArrayLiteralNode n = new ArrayLiteralNode(args);
		Value[] vals = {Value.integer(2), Value.integer(3), Value.integer(4)};
		UnitTestUtil.assertEqual(Value.arr(vals), n.execute());
	}
	
	@Test
	public void shouldParseListWithMultipleTypes() {
		ASTNode[] args = {ExpressionParser.parseExpression("\"asdf\", 234, 123.02")};
		ArrayLiteralNode n = new ArrayLiteralNode(args);
		Value[] vals = {Value.string("asdf"), Value.integer(234), Value.doub(123.02)};
		UnitTestUtil.assertEqual(Value.arr(vals), n.execute());
	}
	

}
