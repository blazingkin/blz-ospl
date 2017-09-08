package com.blazingkin.interpreter.unittests.astnodes;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blazingkin.interpreter.executor.astnodes.AdditionNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.Value;

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
	public void shouldAddTwoAndTwo(){
		ASTNode args[] = {new ValueASTNode("2"), new ValueASTNode("2")};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.integer(4), add.execute());
	}
	
	@Test
	public void shouldAddDoubles(){
		ASTNode args[] = {new ValueASTNode("0.2"), new ValueASTNode("0.2")};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.doub(0.4), add.execute());
	}
	
	@Test
	public void shouldAddIntAndDouble(){
		ASTNode args[] = {new ValueASTNode("1"), new ValueASTNode("0.2")};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.doub(1.2), add.execute());
	}
	
	@Test
	public void shouldAddRationals(){
		Value twoThirds = Value.rational(2, 3);
		ASTNode args[] = {new ValueASTNode(twoThirds), new ValueASTNode(twoThirds)};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.rational(4, 3), add.execute());
	}
	
	@Test
	public void shouldAddRationalAndInteger(){
		Value sevenSixths = Value.rational(7, 6);
		ASTNode args[] = {new ValueASTNode(sevenSixths), new ValueASTNode("1")};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.rational(13, 6), add.execute());
	}

	@Test
	public void shouldAddStrings(){
		Value strVal = Value.string("hi");
		ASTNode args[] = {new ValueASTNode(strVal), new ValueASTNode(strVal)};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.string("hihi"), add.execute());
	}
	
	@Test
	public void shouldAddStringAndNumber(){
		ASTNode args[] = {new ValueASTNode(Value.string("12")), new ValueASTNode("3")};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.string("123"), add.execute());
		UnitTestUtil.assertNoErrors();
	}
	
	@Test
	public void shouldAddNumberAndString(){
		ASTNode args[] = {new ValueASTNode("3"), new ValueASTNode(Value.string("12"))};
		AdditionNode add = new AdditionNode(args);
		UnitTestUtil.assertEqual(Value.string("312"), add.execute());
		UnitTestUtil.assertNoErrors();
	}
	
}
