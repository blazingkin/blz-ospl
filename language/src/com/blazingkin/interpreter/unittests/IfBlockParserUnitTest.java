package com.blazingkin.interpreter.unittests;

import java.util.ArrayList;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.parser.BlockParser;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.IfBlockParser;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SourceLine;
import com.blazingkin.interpreter.parser.SplitStream;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class IfBlockParserUnitTest {

	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@AfterClass
	public static void clear(){
		UnitTestUtil.clearEnv();
    }
    
    @Test
    public void shouldKnowWhatToParse(){
        IfBlockParser parser = new IfBlockParser();
        UnitTestUtil.assertEqual(parser.shouldParse("if blah"), true);
        UnitTestUtil.assertEqual(parser.shouldParse("IF blah"), true);
        UnitTestUtil.assertEqual(parser.shouldParse("blah"), false);
        UnitTestUtil.assertEqual(parser.shouldParse("if\tblah"), true);
    }

    @Test
    public void shouldGiveIfNodesThatWork(){
        try {
            IfBlockParser parser = new IfBlockParser();
            String input[] = {"if true", "3", "1", "end"};
            ArrayList<Either<SourceLine, ParseBlock>> block = BlockParser.parseBody(new SplitStream<String>(input), 1);
            ParseBlock ifBlock = block.get(0).getRight().get();
            ASTNode node = parser.parseBlock(ifBlock);
            Value result = node.execute(new Context());
            UnitTestUtil.assertEqual(result, Value.integer(1));
        }catch (SyntaxException e){
            /* There are no syntax exceptions */
            UnitTestUtil.fail();
        }catch (BLZRuntimeException e){
            UnitTestUtil.fail();
        }
    }

    @Test
    public void shouldReturnNilOnFalseIfNodeWithNoElse(){
        try {
            IfBlockParser parser = new IfBlockParser();
            String input[] = {"if 2 == 3", "3", "1", "end"};
            ArrayList<Either<SourceLine, ParseBlock>> block = BlockParser.parseBody(new SplitStream<String>(input), 1);
            ParseBlock ifBlock = block.get(0).getRight().get();
            ASTNode node = parser.parseBlock(ifBlock);
            Value result = node.execute(new Context());
            UnitTestUtil.assertEqual(result, Value.nil());
        }catch (SyntaxException e){
            /* There are no syntax exceptions */
            UnitTestUtil.fail();
        }catch (BLZRuntimeException e){
            UnitTestUtil.fail();
        }
    }

    @Test
    public void shouldExecuteElseStatement(){
        try {
            IfBlockParser parser = new IfBlockParser();
            String input[] = {"if 2 == 3", "3", "1", "else", "2", "end"};
            ArrayList<Either<SourceLine, ParseBlock>> block = BlockParser.parseBody(new SplitStream<String>(input), 1);
            ParseBlock ifBlock = block.get(0).getRight().get();
            ASTNode node = parser.parseBlock(ifBlock);
            Value result = node.execute(new Context());
            UnitTestUtil.assertEqual(result, Value.integer(2));
        }catch (SyntaxException e){
            /* There are no syntax exceptions */
            UnitTestUtil.fail();
        }catch (BLZRuntimeException e){
            UnitTestUtil.fail();
        }
    }

    @Test
    public void shouldComplainAboutElseWithNothing(){

        try {
            IfBlockParser parser = new IfBlockParser();
            String input[] = {"if 2 == 3", "3", "1", "else", "end"};
            ArrayList<Either<SourceLine, ParseBlock>> block = BlockParser.parseBody(new SplitStream<String>(input), 1);
            ParseBlock ifBlock = block.get(0).getRight().get();
            parser.parseBlock(ifBlock);
            /* An error should have been thrown */
            UnitTestUtil.fail();
        }catch (SyntaxException e){
            UnitTestUtil.assertEqual(e.getMessage(), "Else was the last line of an if statement!");
        }
    }

}