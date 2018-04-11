package com.blazingkin.interpreter.unittests.astnodes;

import java.util.ArrayList;

import com.blazingkin.interpreter.executor.astnodes.BlockNode;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class BlockNodeUnitTest {

    @BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
    }
    
    @Test
    public void shouldProperlyConstruct(){
        try {
            ArrayList<Either<String, ParseBlock>> input = new ArrayList<Either<String, ParseBlock>>();
            input.add(Either.left("3"));
            input.add(Either.left("20"));
            new BlockNode(input, true).execute(new Context());
        } catch (Exception e){
            e.printStackTrace();
            UnitTestUtil.assertNoErrors();
            /* Assert False */
            UnitTestUtil.assertEqual(false, true);
        }
    }

    @Test
    public void shouldReturnLastValue(){
        try {
            ArrayList<Either<String, ParseBlock>> input = new ArrayList<Either<String, ParseBlock>>();
            input.add(Either.left("3"));
            input.add(Either.left("20"));
            Value result = new BlockNode(input, true).execute(new Context());
            UnitTestUtil.assertEqual(result, Value.integer(20));
        } catch (Exception e){
            e.printStackTrace();
            UnitTestUtil.assertNoErrors();
            /* Assert False */
            UnitTestUtil.assertEqual(false, true);
        }
    }

    @Test
    public void shouldHandleComplexExpressions(){
        try {
            ArrayList<Either<String, ParseBlock>> input = new ArrayList<Either<String, ParseBlock>>();
            input.add(Either.left("a = 3"));
            input.add(Either.left("a - 2"));
            Value result = new BlockNode(input, true).execute(new Context());
            UnitTestUtil.assertEqual(result, Value.integer(1));
        } catch (Exception e){
            e.printStackTrace();
            UnitTestUtil.assertNoErrors();
            /* Assert False */
            UnitTestUtil.assertEqual(false, true);
        }
    }

    @Test
    public void shouldHandleReturnStatements(){
        try {
            ArrayList<Either<String, ParseBlock>> input = new ArrayList<Either<String, ParseBlock>>();
            input.add(Either.left("a = 3"));
            input.add(Either.left("return 20"));
            input.add(Either.left("a - 2"));
            Value result = new BlockNode(input, true).execute(new Context());
            UnitTestUtil.assertEqual(result, Value.integer(20));
        } catch (Exception e){
            e.printStackTrace();
            UnitTestUtil.assertNoErrors();
            /* Assert False */
            UnitTestUtil.assertEqual(false, true);
        }
    }


}