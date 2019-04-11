package in.blazingk.blz.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.astnodes.SubtractionNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import in.blazingk.blz.UnitTestUtil;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class SubtractionNodeUnitTest {
    @BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
	}
   
	@Test
	public void shouldRequireArguments() {
		ASTNode args[] = {};
		new SubtractionNode(args);
		UnitTestUtil.assertLastError("Subtraction did not have 2 arguments");
    }
    
    @Test
	public void shouldRequireTwoArguments(){
		ASTNode args[] = {new ValueASTNode("3")};
		new SubtractionNode(args);
		UnitTestUtil.assertLastError("Subtraction did not have 2 arguments");
    }
    
    @Test
    public void shouldProperlySubtractIntegers() throws BLZRuntimeException {
        ASTNode args[] = {new ValueASTNode("3"), new ValueASTNode("1")};
        Value result = new SubtractionNode(args).execute(new Context());
        UnitTestUtil.assertEqual(result, Value.integer(2));
    }
    
    @Test
    public void shouldProperlySubtractDoubles() throws BLZRuntimeException {
        ASTNode args[] = {new ValueASTNode("2.4"), new ValueASTNode("0.9")};
        Value result = new SubtractionNode(args).execute(new Context());
        UnitTestUtil.assertAlmostEqual(result, Value.doub(1.5d));
    }

    @Test
    public void shouldFailToSubtractStrings() throws BLZRuntimeException {
        try {
        ASTNode args[] = {new ValueASTNode("\"asdf\""), new ValueASTNode("\"asd\"")};
            new SubtractionNode(args).execute(new Context());
            UnitTestUtil.fail("An error should have been thrown");
        }catch(BLZRuntimeException e){
            UnitTestUtil.assertEqual(e.getMessage(), "Failed Subtracting Values asdf and asd");
        }
    }

    @Test
    public void shouldSubtractRationals() throws BLZRuntimeException {
        Value half = Value.rational(1, 2);
        ASTNode args[] = {new ValueASTNode("1"), new ValueASTNode(half)};
        Value result = new SubtractionNode(args).execute(new Context());
        UnitTestUtil.assertEqual(result, half);
    }

}