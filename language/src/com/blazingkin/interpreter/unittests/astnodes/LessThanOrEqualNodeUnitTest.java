package com.blazingkin.interpreter.unittests.astnodes;

import com.blazingkin.interpreter.executor.astnodes.LessThanEqualsNode;
import com.blazingkin.interpreter.executor.astnodes.LessThanNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class LessThanOrEqualNodeUnitTest {
    @BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
    }
    
    @Test
    public void equalValuesShouldBeLessThanOrEqual(){
        ASTNode[] args = {new ValueASTNode("2"), new ValueASTNode("2")};
        LessThanEqualsNode node = new LessThanEqualsNode(args);
        UnitTestUtil.assertEqual(Value.bool(true), node.execute(new Context()));
    }

    @Test
    public void lessThanValuesShouldBeLessThanOrEqual(){
        ASTNode[] args = {new ValueASTNode("2"), new ValueASTNode("3")};
        LessThanEqualsNode node = new LessThanEqualsNode(args);
        UnitTestUtil.assertEqual(Value.bool(true), node.execute(new Context()));
    }

    @Test
    public void greaterThanValuesShouldNotBeLessThanOrEqual(){
        ASTNode[] args = {new ValueASTNode("3"), new ValueASTNode("2")};
        LessThanEqualsNode node = new LessThanEqualsNode(args);
        UnitTestUtil.assertEqual(Value.bool(false), node.execute(new Context()));
    }

    @Test
    public void shouldCompareStrings(){
        ASTNode[] args = {new ValueASTNode("\"a\""), new ValueASTNode("\"b\"")};
        LessThanNode node = new LessThanNode(args);
        UnitTestUtil.assertEqual(Value.bool(true), node.execute(new Context()));
    }

    @Test
    public void shouldCompareStringsTwo(){
        ASTNode[] args = {new ValueASTNode("\"b\""), new ValueASTNode("\"a\"")};
        LessThanEqualsNode node = new LessThanEqualsNode(args);
        UnitTestUtil.assertEqual(Value.bool(false), node.execute(new Context()));
    }
}