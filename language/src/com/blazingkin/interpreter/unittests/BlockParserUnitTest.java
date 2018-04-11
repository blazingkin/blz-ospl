package com.blazingkin.interpreter.unittests;

import java.util.ArrayList;
import java.util.Arrays;

import com.blazingkin.interpreter.parser.BlockParser;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SplitStream;

import org.junit.Test;

public class BlockParserUnitTest {

    @Test
    public void shouldReturnASingleString(){
        String input[] = {"asdf"};
        ArrayList<Either<String, ParseBlock>> result = BlockParser.parseBody(new SplitStream<String>(input));
        UnitTestUtil.assertEqual(result.size(), 1);
        Either<String, ParseBlock> string = result.get(0);
        UnitTestUtil.assertEqual(string.getLeft().get(), "asdf");
    }

    @Test
    public void shouldDescendIntoBlock(){
        String input[] = {"if test", "end"};
        ArrayList<Either<String, ParseBlock>> result = BlockParser.parseBody(new SplitStream<String>(input));
        UnitTestUtil.assertEqual(result.size(), 1);
        Either<String, ParseBlock> block = result.get(0);
        UnitTestUtil.assertEqual(block.isRight(), true);
        ParseBlock bk = block.getRight().get();
        UnitTestUtil.assertEqual(bk.getLines().size(), 0);
        UnitTestUtil.assertEqual(bk.getHeader(), "if test");
    }

    @Test
    public void shouldDescendIntoMultipleBlocks(){
        String input[] = {":main", "if true", "test", "end", "end"};
        ArrayList<Either<String, ParseBlock>> result = BlockParser.parseBody(new SplitStream<String>(input));
        UnitTestUtil.assertEqual(result.size(), 1);
        ParseBlock mainBlock = result.get(0).getRight().get();
        UnitTestUtil.assertEqual(mainBlock.getHeader(), ":main");
        UnitTestUtil.assertEqual(mainBlock.getLines().size(), 1);
        ParseBlock ifBlock = mainBlock.getLines().get(0).getRight().get();
        UnitTestUtil.assertEqual(ifBlock.getHeader(), "if true");
        UnitTestUtil.assertEqual(ifBlock.getLines().size(), 1);
        UnitTestUtil.assertEqual(ifBlock.getLines().get(0).getLeft().get(), "test");
    }

}