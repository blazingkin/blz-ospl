package in.blazingk.blz;

import java.util.ArrayList;

import com.blazingkin.interpreter.parser.BlockParser;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SourceLine;
import com.blazingkin.interpreter.parser.SplitStream;
import com.blazingkin.interpreter.parser.SyntaxException;

import org.junit.Test;

public class BlockParserUnitTest {


    @Test
    public void lineNumbersShouldBeTracked() throws SyntaxException{
        String input[] = {"1", "2", "3"};
        ArrayList<Either<SourceLine, ParseBlock>> result = BlockParser.parseBody(new SplitStream<String>(input), 1);
        UnitTestUtil.assertEqual(result.size(), 3);
        UnitTestUtil.assertEqual(result.get(0).getLeft().get(), new SourceLine("1", 1));
        UnitTestUtil.assertEqual(result.get(1).getLeft().get(), new SourceLine("2", 2));
        UnitTestUtil.assertEqual(result.get(2).getLeft().get(), new SourceLine("3", 3));
    }

    @Test
    public void shouldReturnASingleString(){
        try{
            String input[] = {"asdf"};
            ArrayList<Either<SourceLine, ParseBlock>> result = BlockParser.parseBody(new SplitStream<String>(input), 1);
            UnitTestUtil.assertEqual(result.size(), 1);
            Either<SourceLine, ParseBlock> string = result.get(0);
            UnitTestUtil.assertEqual(string.getLeft().get().line, "asdf");
            UnitTestUtil.assertEqual(string.getLeft().get().lineNumber, 1);
        }catch(SyntaxException e){
            UnitTestUtil.fail();
        }
    }

    @Test
    public void shouldDescendIntoBlock(){
        try{
            String input[] = {"if test", "end"};
            ArrayList<Either<SourceLine, ParseBlock>> result = BlockParser.parseBody(new SplitStream<String>(input), 1);
            UnitTestUtil.assertEqual(result.size(), 1);
            Either<SourceLine, ParseBlock> block = result.get(0);
            UnitTestUtil.assertEqual(block.isRight(), true);
            ParseBlock bk = block.getRight().get();
            UnitTestUtil.assertEqual(bk.getLines().size(), 0);
            UnitTestUtil.assertEqual(bk.getHeader(), "if test");
        }catch (SyntaxException e){
            UnitTestUtil.fail();
        }
    }

    @Test
    public void shouldDescendIntoMultipleBlocks(){
        try {
            String input[] = {":main", "if true", "test", "end", "end"};
            ArrayList<Either<SourceLine, ParseBlock>> result = BlockParser.parseBody(new SplitStream<String>(input), 1);
            UnitTestUtil.assertEqual(result.size(), 1);
            ParseBlock mainBlock = result.get(0).getRight().get();
            UnitTestUtil.assertEqual(mainBlock.getHeader(), ":main");
            UnitTestUtil.assertEqual(mainBlock.getLines().size(), 1);
            ParseBlock ifBlock = mainBlock.getLines().get(0).getRight().get();
            UnitTestUtil.assertEqual(ifBlock.getHeader(), "if true");
            UnitTestUtil.assertEqual(ifBlock.getLines().size(), 1);
            UnitTestUtil.assertEqual(ifBlock.getLines().get(0).getLeft().get().line, "test");
            UnitTestUtil.assertEqual(ifBlock.getLines().get(0).getLeft().get().lineNumber, 3);
        }catch(SyntaxException e){
            UnitTestUtil.fail();
        }
    }

    @Test
    public void shouldYellAboutNonClosedBlocks(){
        try {
            String input[] = {"if "};
            BlockParser.parseBody(new SplitStream<String>(input), 1);
            UnitTestUtil.fail();
        }catch (SyntaxException e){
            // Pass
        }
    }

}