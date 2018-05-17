package com.blazingkin.interpreter.unittests.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.astnodes.AdditionNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class AdditionNodeUnitTest {
	
	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
	}

	@Test
	public void shouldRequireArguments() {
		ASTNode args[] = {};
		new AdditionNode(args);
		UnitTestUtil.assertLastError("Addition did not have 2 arguments");
	}
	
	@Test
	public void shouldRequireTwoArguments(){
		ASTNode args[] = {new ValueASTNode("3")};
		new AdditionNode(args);
		UnitTestUtil.assertLastError("Addition did not have 2 arguments");
	}
	
	@Test
	public void shouldAddTwoAndTwo() throws BLZRuntimeException {
		ASTNode args[] = {new ValueASTNode("2"), new ValueASTNode("2")};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.integer(4), add.execute(new Context()));
	}
	
	@Test
	public void shouldAddDoubles() throws BLZRuntimeException {
		ASTNode args[] = {new ValueASTNode("0.2"), new ValueASTNode("0.2")};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.doub(0.4), add.execute(new Context()));
	}
	
	@Test
	public void shouldAddIntAndDouble() throws BLZRuntimeException {
		ASTNode args[] = {new ValueASTNode("1"), new ValueASTNode("0.2")};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.doub(1.2), add.execute(new Context()));
	}
	
	@Test
	public void shouldAddRationals() throws BLZRuntimeException {
		Value twoThirds = Value.rational(2, 3);
		ASTNode args[] = {new ValueASTNode(twoThirds), new ValueASTNode(twoThirds)};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.rational(4, 3), add.execute(new Context()));
	}
	
	@Test
	public void shouldAddRationalAndInteger() throws BLZRuntimeException {
		Value sevenSixths = Value.rational(7, 6);
		ASTNode args[] = {new ValueASTNode(sevenSixths), new ValueASTNode("1")};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.rational(13, 6), add.execute(new Context()));
	}

	@Test
	public void shouldAddStrings() throws BLZRuntimeException {
		Value strVal = Value.string("hi");
		ASTNode args[] = {new ValueASTNode(strVal), new ValueASTNode(strVal)};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.string("hihi"), add.execute(new Context()));
	}
	
	@Test
	public void shouldAddStringAndNumber() throws BLZRuntimeException {
		ASTNode args[] = {new ValueASTNode(Value.string("12")), new ValueASTNode("3")};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.string("123"), add.execute(new Context()));
		UnitTestUtil.assertNoErrors();
	}
	
	@Test
	public void shouldAddNumberAndString() throws BLZRuntimeException {
		ASTNode args[] = {new ValueASTNode("3"), new ValueASTNode(Value.string("12"))};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.string("312"), add.execute(new Context()));
		UnitTestUtil.assertNoErrors();
	}
	
	@Test
	public void shouldAddSeveralThings() throws BLZRuntimeException {
		ExpressionExecutor.parseExpression("x = 1");
		ExpressionExecutor.parseExpression("y = {e}");
		ExpressionExecutor.parseExpression("z = {pi}");
		UnitTestUtil.assertEqual(Value.string("x = 1, y = 2.718281828459045, z = 3.141592653589793"), 
				ExpressionExecutor.parseExpression("\"x = \" + x + \", y = \" + y + \", z = \" + z"));
	}
	
}
