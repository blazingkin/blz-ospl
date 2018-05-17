package com.blazingkin.interpreter.unittests.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.astnodes.ApproximateComparisonNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

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
		try {
		ASTNode[] args = {new ValueASTNode("3"), new ValueASTNode("\"asdf\"")};
		new ApproximateComparisonNode(args).execute(new Context());
		}catch (BLZRuntimeException e){
			UnitTestUtil.assertEqual(e.getMessage(), "When comparing approximately, one of 3 or asdf is not a decimal value.");
			return;
		}
		UnitTestUtil.fail();
	}
	
	@Test
	public void sameValuesShouldBeApproximatelyEqual() throws BLZRuntimeException {
		for (int i = 0; i < 100; i++) {
			ASTNode[] args = {new ValueASTNode(Value.integer(i)), new ValueASTNode(Value.integer(i))};
			ApproximateComparisonNode n = new ApproximateComparisonNode(args);
			UnitTestUtil.assertEqual(Value.bool(true), n.execute(new Context()));
		}
	}
	
	@Test
	public void shouldDeclareSimilarValuesApproximatelyEqual() throws BLZRuntimeException {
		ASTNode[] args = {new ValueASTNode("3.14159265358"), new ValueASTNode("3.14159265357")};
		ApproximateComparisonNode n = new ApproximateComparisonNode(args);
		UnitTestUtil.assertEqual(Value.bool(true), n.execute(new Context()));
	}
	
	@Test
	public void shouldDeclareDissimilarValuesNotApproximatelyEqual() throws BLZRuntimeException {
		ASTNode[] args = {new ValueASTNode("3.1"), new ValueASTNode("3.2")};
		ApproximateComparisonNode n = new ApproximateComparisonNode(args);
		UnitTestUtil.assertEqual(Value.bool(false), n.execute(new Context()));
	}

}
