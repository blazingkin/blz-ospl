package com.blazingkin.interpreter.unittests.astnodes;

import com.blazingkin.interpreter.executor.astnodes.MultiplicationNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class MultiplicationNodeUnitTest {
    @BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
    }
    
    @Test
    public void shouldRequireTwoArgs(){
        ASTNode[] args = {};
        new MultiplicationNode(args);
        UnitTestUtil.assertLastError("Multiplication did not have 2 arguments");
    }

    @Test
    public void shouldMultiplyIntegers(){
        ASTNode[] args = {new ValueASTNode("3"), new ValueASTNode("2")};
        Value result = new MultiplicationNode(args).execute(new Context());
        UnitTestUtil.assertEqual(result, Value.integer(6));
    }

    @Test
    public void shouldMultiplyRationals(){
        ASTNode[] args = {new ValueASTNode("3"), new ValueASTNode(Value.rational(2, 4))};
        Value result = new MultiplicationNode(args).execute(new Context());
        UnitTestUtil.assertEqual(result, Value.rational(3, 2));
    }

    @Test
    public void shouldFailToMultiplyStrings(){
        ASTNode[] args = {new ValueASTNode("\"asdf\""), new ValueASTNode("3")};
        new MultiplicationNode(args).execute(new Context());
        UnitTestUtil.assertLastError("Failed Multiplying Values asdf and 3");
    }


}