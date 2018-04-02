package com.blazingkin.interpreter.unittests.astnodes;

import com.blazingkin.interpreter.executor.astnodes.LogarithmNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class LogarithmNodeUnitTest {

    @BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
    }

    @Test
    public void shouldRequireTwoArguments(){
        ValueASTNode[] args = {};
        new LogarithmNode(args);
        UnitTestUtil.assertLastError("Logarithm didn't have 2 arguments");
    }

    @Test
    public void shouldAccuratelyComputeLogarithm(){
        ValueASTNode[] args = {new ValueASTNode("10"), new ValueASTNode("10")};
        Value result = new LogarithmNode(args).execute(new Context());
        UnitTestUtil.assertAlmostEqual(result, Value.doub(1d));
    }

    @Test
    public void anotherLogarithm(){
        ValueASTNode[] args = {new ValueASTNode("1000"), new ValueASTNode("10")};
        Value result = new LogarithmNode(args).execute(new Context());
        UnitTestUtil.assertAlmostEqual(result, Value.doub(3d));
    }

    @Test
    public void shouldFailToTakeALogOfAString(){
        ValueASTNode[] args = {new ValueASTNode("\"asdf\""), new ValueASTNode("12")};
        new LogarithmNode(args).execute(new Context());
        UnitTestUtil.assertLastError("Failed Taking a Logarithm with asdf and 12");
    }

}