package in.blazingk.blz.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.astnodes.AdditionNode;
import com.blazingkin.interpreter.executor.astnodes.ExclusiveOrNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import in.blazingk.blz.UnitTestUtil;

public class ExclusiveOrNodeUnitTest {
	
	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
	}

	@Test
	public void shouldRequireArguments() throws SyntaxException {
		ASTNode args[] = {};
		try {
			new ExclusiveOrNode(args);
			UnitTestUtil.fail("Should have raised an exception in the constructor");
		}catch(SyntaxException e){
			UnitTestUtil.assertEqual(e.getMessage(), "Exclusive or did not have 2 arguments");
		}
	}
	
	@Test
	public void shouldRequireTwoArguments() throws SyntaxException{
		ASTNode args[] = {new ValueASTNode("3")};
		try {
			new ExclusiveOrNode(args);
			UnitTestUtil.fail("Should have raised an exception in the constructor");
		}catch(SyntaxException e){
			UnitTestUtil.assertEqual(e.getMessage(), "Exclusive or did not have 2 arguments");
		}
	}
	
	@Test
	public void shouldXORTwoAndTwo() throws BLZRuntimeException, SyntaxException {
		ASTNode args[] = {new ValueASTNode("2"), new ValueASTNode("2")};
		ExclusiveOrNode xor = new ExclusiveOrNode(args);
		UnitTestUtil.assertEqual(Value.integer(0), xor.execute(new Context()));
	}
	
	
	@Test
	public void shouldFailWithDouble() throws SyntaxException {
		ASTNode args[] = {new ValueASTNode("1"), new ValueASTNode("0.2")};
        ExclusiveOrNode xor = new ExclusiveOrNode(args);
        try {
            xor.execute(new Context());
            UnitTestUtil.fail("Should have thrown an error on a double");
        }catch(BLZRuntimeException e){
            UnitTestUtil.assertEqual(e.getMessage(), "0.2 cannot be exclusive or-ed as it is not an integer (Double instead)");
        }
    }
    
    @Test
	public void shouldFailWithDoubleOnLeft() throws SyntaxException {
		ASTNode args[] = {new ValueASTNode("0.1"), new ValueASTNode("0.2")};
        ExclusiveOrNode xor = new ExclusiveOrNode(args);
        try {
            xor.execute(new Context());
            UnitTestUtil.fail("Should have thrown an error on a double");
        }catch(BLZRuntimeException e){
            UnitTestUtil.assertEqual(e.getMessage(), "0.1 cannot be exclusive or-ed as it is not an integer (Double instead)");
        }
	}
	
	
}
