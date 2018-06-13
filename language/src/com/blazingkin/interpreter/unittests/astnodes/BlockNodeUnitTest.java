package com.blazingkin.interpreter.unittests.astnodes;

import java.util.ArrayList;

import com.blazingkin.interpreter.executor.astnodes.BlockNode;
import com.blazingkin.interpreter.parser.BlockParser;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SourceLine;
import com.blazingkin.interpreter.parser.SplitStream;
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
            ArrayList<Either<SourceLine, ParseBlock>> input = new ArrayList<Either<SourceLine, ParseBlock>>();
            input.add(Either.left(new SourceLine("3", 1)));
            input.add(Either.left(new SourceLine("20", 2)));
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
            ArrayList<Either<SourceLine, ParseBlock>> input = new ArrayList<Either<SourceLine, ParseBlock>>();
            input.add(Either.left(new SourceLine("3", 1)));
            input.add(Either.left(new SourceLine("20", 2)));
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
            ArrayList<Either<SourceLine, ParseBlock>> input = new ArrayList<Either<SourceLine, ParseBlock>>();
            input.add(Either.left(new SourceLine("a = 3", 1)));
            input.add(Either.left(new SourceLine("a - 2", 2)));
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
            ArrayList<Either<SourceLine, ParseBlock>> input = new ArrayList<Either<SourceLine, ParseBlock>>();
            input.add(Either.left(new SourceLine("a = 3", 1)));
            input.add(Either.left(new SourceLine("return 20", 2)));
            input.add(Either.left(new SourceLine("a - 2", 3)));
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
    public void shouldHandleBlocks(){
        try {
            String code[] = {"a = 20", "for i = 0; i < 5; i++", "if i % 2 == 0", "a++", "end", "end", "a"};
            ArrayList<Either<SourceLine, ParseBlock>> input = BlockParser.parseBody(new SplitStream<String>(code), 1);
            Value result = new BlockNode(input, true).execute(new Context());
            UnitTestUtil.assertEqual(result, Value.integer(23));
        } catch (Exception e){
            e.printStackTrace();
            UnitTestUtil.assertNoErrors();
            /* Assert False */
            UnitTestUtil.assertEqual(false, true);
        }
    }

}