package com.blazingkin.interpreter.unittests.astnodes;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blazingkin.interpreter.executor.astnodes.ApproximateComparisonNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class ApproximateComparisonNodeUnitTest {

	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
	}
	
	@Test
	public void shouldNotAllowNoArguments() {
		ASTNode[] args = {};
		new ApproximateComparisonNode(args);
		UnitTestUtil.assertLastError("Approximate Comparison did not have 2 arguments.");
	}
	
	@Test
	public void shouldNotAllowOneArgument() {
		ASTNode[] args = {new ValueASTNode("3")};
		new ApproximateComparisonNode(args);
		UnitTestUtil.assertLastError("Approximate Comparison did not have 2 arguments.");		
	}
	
	@Test
	public void shouldNotAllowStringArgs() {
		ASTNode[] args = {new ValueASTNode("3"), new ValueASTNode("\"asdf\"")};
		new ApproximateComparisonNode(args).execute(new Context());
		UnitTestUtil.assertLastError("Attempted to cast asdf to a double and failed!");
	}
	
	@Test
	public void sameValuesShouldBeApproximatelyEqual() {
		for (int i = 0; i < 100; i++) {
			ASTNode[] args = {new ValueASTNode(Value.integer(i)), new ValueASTNode(Value.integer(i))};
			ApproximateComparisonNode n = new ApproximateComparisonNode(args);
			UnitTestUtil.assertEqual(Value.bool(true), n.execute(new Context()));
		}
	}
	
	@Test
	public void shouldDeclareSimilarValuesApproximatelyEqual() {
		ASTNode[] args = {new ValueASTNode("3.14159265358"), new ValueASTNode("3.14159265357")};
		ApproximateComparisonNode n = new ApproximateComparisonNode(args);
		UnitTestUtil.assertEqual(Value.bool(true), n.execute(new Context()));
	}
	
	@Test
	public void shouldDeclareDissimilarValuesNotApproximatelyEqual() {
		ASTNode[] args = {new ValueASTNode("3.1"), new ValueASTNode("3.2")};
		ApproximateComparisonNode n = new ApproximateComparisonNode(args);
		UnitTestUtil.assertEqual(Value.bool(false), n.execute(new Context()));
	}

}
