package in.blazingk.blz.astnodes;

import java.util.ArrayList;

import com.blazingkin.interpreter.executor.astnodes.MethodNode;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SourceLine;
import in.blazingk.blz.UnitTestUtil;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class MethodNodeUnitTest {
    @BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
    }

    @Test
    public void shouldParseMethodHeader(){
        try {
            ArrayList<Either<SourceLine, ParseBlock>> empty = new ArrayList<Either<SourceLine, ParseBlock>>();
            MethodNode node = new MethodNode(":blah", empty, null);
            UnitTestUtil.assertEqual(node.getStoreName(), "blah");
            UnitTestUtil.assertEqual(node.takesVariables, false);
        }catch (Exception e){
            UnitTestUtil.assertEqual(true, false);
        }
    }

    @Test
    public void shouldParseMethodHeaderWithVariables(){
        try {
            ArrayList<Either<SourceLine, ParseBlock>> empty = new ArrayList<Either<SourceLine, ParseBlock>>();
            MethodNode node = new MethodNode(":blah(a,b,c)", empty, null);
            UnitTestUtil.assertEqual(node.getStoreName(), "blah");
            UnitTestUtil.assertEqual(node.takesVariables, true);
            UnitTestUtil.assertEqual(node.variables.length, 3);
            UnitTestUtil.assertEqual(node.variables[0], "a");
            UnitTestUtil.assertEqual(node.variables[1], "b");
            UnitTestUtil.assertEqual(node.variables[2], "c");
        }catch (Exception e){
            UnitTestUtil.assertEqual(true, false);
        }
    }

    @Test
    public void shouldRunSimpleMethod(){
        try {
            ArrayList<Either<SourceLine, ParseBlock>> code = new ArrayList<Either<SourceLine, ParseBlock>>();
            code.add(Either.left(new SourceLine("3", 2)));
            MethodNode node = new MethodNode(":blah", code, null);
            Value[] args = {};
            Value result = node.execute(new Context(), args, false);
            UnitTestUtil.assertEqual(result, Value.integer(3));
        }catch (Exception e){
            UnitTestUtil.assertEqual(true, false);
        }
    }


}