package com.blazingkin.interpreter.unittests.astnodes;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.astnodes.LogicalOrNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class LogicalOrNodeUnitTest {

	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
	}
	
	@Test
	public void TestShouldComplainAboutWrongNumberOfArgs() {
		ASTNode args[] = {new ValueASTNode(Value.bool(false))};
		new LogicalOrNode(args);
		UnitTestUtil.assertLastError("Logical Or did not have 2 arguments");
	}
	
	@Test
	public void TestShouldReturnCorrectValueOne() throws BLZRuntimeException {
		ASTNode args[] = {new ValueASTNode(Value.bool(false)), new ValueASTNode(Value.bool(false))};
		LogicalOrNode node = new LogicalOrNode(args);
		UnitTestUtil.assertEqual(Value.bool(false), node.execute(new Context(null)));
		UnitTestUtil.assertNoErrors();
	}

	@Test
	public void TestShouldReturnCorrectValueTwo() throws BLZRuntimeException {
		ASTNode args[] = {new ValueASTNode(Value.bool(false)), new ValueASTNode(Value.bool(true))};
		LogicalOrNode node = new LogicalOrNode(args);
		UnitTestUtil.assertEqual(Value.bool(true), node.execute(new Context(null)));
		UnitTestUtil.assertNoErrors();
	}
	@Test
	public void TestShouldReturnCorrectValueThree() throws BLZRuntimeException {
		ASTNode args[] = {new ValueASTNode(Value.bool(true)), new ValueASTNode(Value.bool(false))};
		LogicalOrNode node = new LogicalOrNode(args);
		UnitTestUtil.assertEqual(Value.bool(true), node.execute(new Context(null)));
		UnitTestUtil.assertNoErrors();
	}
	@Test
	public void TestShouldReturnCorrectValueFour() throws BLZRuntimeException {
		ASTNode args[] = {new ValueASTNode(Value.bool(true)), new ValueASTNode(Value.bool(true))};
		LogicalOrNode node = new LogicalOrNode(args);
		UnitTestUtil.assertEqual(Value.bool(true), node.execute(new Context(null)));
		UnitTestUtil.assertNoErrors();
	}
	
	@Test
	public void TestShortCircuiting() throws BLZRuntimeException {
		/* Variable lookup should fail if it gets there */
		ASTNode args[] = {new ValueASTNode(Value.bool(true)), new ValueASTNode("asdf")};
		LogicalOrNode node = new LogicalOrNode(args);
		UnitTestUtil.assertEqual(Value.bool(true), node.execute(new Context(null)));
		UnitTestUtil.assertNoErrors();
	}

}
