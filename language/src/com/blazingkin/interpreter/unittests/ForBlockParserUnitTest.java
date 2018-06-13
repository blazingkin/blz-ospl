package com.blazingkin.interpreter.unittests;

import java.util.ArrayList;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.parser.BlockParser;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.ForBlockParser;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SourceLine;
import com.blazingkin.interpreter.parser.SplitStream;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ForBlockParserUnitTest {

	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@AfterClass
	public static void clear(){
		UnitTestUtil.clearEnv();
    }

    @Test
    public void shouldKnowWhenToParse(){
        ForBlockParser parser = new ForBlockParser();
        UnitTestUtil.assertEqual(parser.shouldParse("for a = 0; a < 20; a++"), true);
        UnitTestUtil.assertEqual(parser.shouldParse("for blasdhfldsd"), true);
        UnitTestUtil.assertEqual(parser.shouldParse("fasdf"), false);
    }

    @Test
    public void shouldParseProperly(){
        try{
            ForBlockParser parser = new ForBlockParser();
            String input[] = {"for i = 0; i < 10; i++", "20", "i", "end"};
            ArrayList<Either<SourceLine, ParseBlock>> block = BlockParser.parseBody(new SplitStream<String>(input), 1);
            ParseBlock forBlock = block.get(0).getRight().get();
            ASTNode node = parser.parseBlock(forBlock);
            Value result = node.execute(new Context());
            UnitTestUtil.assertEqual(result, Value.integer(9));
        }catch(SyntaxException err){
            /* Should not have a syntax error */
            UnitTestUtil.fail();
        }catch (BLZRuntimeException e){
            UnitTestUtil.fail();
        }
    }

    @Test
    public void shouldReturnProperly(){
        try{
            ForBlockParser parser = new ForBlockParser();
            String input[] = {"for i = 0; i < 10; i++", "20", "i ** 2", "end"};
            ArrayList<Either<SourceLine, ParseBlock>> block = BlockParser.parseBody(new SplitStream<String>(input), 1);
            UnitTestUtil.assertEqual(block.get(0).isRight(), true);
            ParseBlock forBlock = block.get(0).getRight().get();
            ASTNode node = parser.parseBlock(forBlock);
            Value result = node.execute(new Context());
            UnitTestUtil.assertEqual(result, Value.integer(81));
        }catch(SyntaxException err){
            /* Should not have a syntax error */
            UnitTestUtil.fail();
        }catch(BLZRuntimeException e){
            UnitTestUtil.fail();
        }
    }

}