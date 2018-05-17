package com.blazingkin.interpreter.unittests.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.astnodes.ComparisonNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Context;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class ComparisonNodeUnitTest {

    @BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
    }
    
    @Test
    public void shouldHaveTwoArgs(){
        ASTNode[] args = {};
        new ComparisonNode(args);
        UnitTestUtil.assertLastError("Comparison did not have 2 arguments");
    }

    @Test
    public void shouldSayTrueIsTrue() throws BLZRuntimeException{
        ASTNode[] args = {new ValueASTNode("true"), new ValueASTNode("true")};
        Value result = new ComparisonNode(args).execute(new Context());
        UnitTestUtil.assertEqual(result, Value.bool(true));
    }

    @Test
    public void shouldSayFalseIsFalse() throws BLZRuntimeException{
        ASTNode[] args = {new ValueASTNode("false"), new ValueASTNode("false")};
        Value result = new ComparisonNode(args).execute(new Context());
        UnitTestUtil.assertEqual(result, Value.bool(true));
    }

    @Test
    public void shouldSayFalseIsNotTrue() throws BLZRuntimeException{
        ASTNode[] args = {new ValueASTNode("false"), new ValueASTNode("true")};
        Value result = new ComparisonNode(args).execute(new Context());
        UnitTestUtil.assertEqual(result, Value.bool(false));
    }

    @Test
    public void shouldSayStringsAreTheSame() throws BLZRuntimeException {
        ASTNode[] args = {new ValueASTNode("\"asdf\""), new ValueASTNode("\"asdf\"")};
        Value result = new ComparisonNode(args).execute(new Context());
        UnitTestUtil.assertEqual(result, Value.bool(true));
    }

    @Test
    public void shouldSayDifferentStringsAreDifferent() throws BLZRuntimeException {
        ASTNode[] args = {new ValueASTNode("\"asdf\""), new ValueASTNode("\"asd\"")};
        Value result = new ComparisonNode(args).execute(new Context());
        UnitTestUtil.assertEqual(result, Value.bool(false));
    }

}