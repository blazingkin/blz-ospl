package com.blazingkin.interpreter.unittests;

import java.util.ArrayList;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.parser.BlockParser;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.IfBlockParser;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SplitStream;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.*;

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
        IfBlockParser parser = new IfBlockParser();
        String input[] = {"if true", "3", "1"};
        ArrayList<Either<String, ParseBlock>> block = BlockParser.parseBody(new SplitStream<String>(input));
        ParseBlock ifBlock = block.get(0).getRight().get();
        try {
            ASTNode node = parser.parseBlock(ifBlock);
            Value result = node.execute(new Context());
            UnitTestUtil.assertEqual(result, Value.integer(1));
        }catch (SyntaxException e){
            /* There are no syntax exceptions */
            UnitTestUtil.assertEqual(true, false);
        }
    }

    @Test
    public void shouldReturnNilOnFalseIfNodeWithNoElse(){
        IfBlockParser parser = new IfBlockParser();
        String input[] = {"if 2 == 3", "3", "1"};
        ArrayList<Either<String, ParseBlock>> block = BlockParser.parseBody(new SplitStream<String>(input));
        ParseBlock ifBlock = block.get(0).getRight().get();
        try {
            ASTNode node = parser.parseBlock(ifBlock);
            Value result = node.execute(new Context());
            UnitTestUtil.assertEqual(result, Value.nil());
        }catch (SyntaxException e){
            /* There are no syntax exceptions */
            UnitTestUtil.assertEqual(true, false);
        }
    }

    @Test
    public void shouldExecuteElseStatement(){
        IfBlockParser parser = new IfBlockParser();
        String input[] = {"if 2 == 3", "3", "1", "else", "2"};
        ArrayList<Either<String, ParseBlock>> block = BlockParser.parseBody(new SplitStream<String>(input));
        ParseBlock ifBlock = block.get(0).getRight().get();
        try {
            ASTNode node = parser.parseBlock(ifBlock);
            Value result = node.execute(new Context());
            UnitTestUtil.assertEqual(result, Value.integer(2));
        }catch (SyntaxException e){
            /* There are no syntax exceptions */
            UnitTestUtil.assertEqual(true, false);
        }
    }

    @Test
    public void shouldComplainAboutElseWithNothing(){
        IfBlockParser parser = new IfBlockParser();
        String input[] = {"if 2 == 3", "3", "1", "else"};
        ArrayList<Either<String, ParseBlock>> block = BlockParser.parseBody(new SplitStream<String>(input));
        ParseBlock ifBlock = block.get(0).getRight().get();
        try {
            parser.parseBlock(ifBlock);
            /* An error should have been thrown */
            UnitTestUtil.assertEqual(false, true);
        }catch (SyntaxException e){
            UnitTestUtil.assertEqual(e.getMessage(), "Else was the last line of an if statement!");
        }
    }

}