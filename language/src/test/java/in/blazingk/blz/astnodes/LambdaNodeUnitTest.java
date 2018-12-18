package in.blazingk.blz.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.astnodes.AdditionNode;
import com.blazingkin.interpreter.executor.astnodes.Closure;
import com.blazingkin.interpreter.executor.astnodes.CommaDelimitNode;
import com.blazingkin.interpreter.executor.astnodes.LambdaNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.parser.ExpressionParser;
import in.blazingk.blz.UnitTestUtil;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class LambdaNodeUnitTest {

    @BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
    }

    @Test
    public void shouldComplainAboutExpressionInArguments(){
        ASTNode addArgs[] = {new ValueASTNode("a"), new ValueASTNode("b")};
        ASTNode args[] = {new AdditionNode(addArgs), new ValueASTNode("a")};
        new LambdaNode(args);
        UnitTestUtil.assertLastError("Lambda expression given bad parameter list (a, b, +)");
    }

    @Test
    public void lambdaShouldProduceCorrectFunction() throws BLZRuntimeException{
        ASTNode args[] = {new ValueASTNode("a"), new ValueASTNode("a")};
        Value v = new LambdaNode(args).execute(new Context());
        UnitTestUtil.assertEqual(v.type, VariableTypes.Closure);
        Closure c = (Closure) v.value;
        Value arg[] = {Value.integer(2)};
        Value result = c.execute(new Context(), arg, false);
        UnitTestUtil.assertEqual(result, Value.integer(2));
    }

    @Test
    public void lambdaShouldProduceCorrectFunctionWithMultipleParams() throws Exception{
        ASTNode cArgs[] = {new ValueASTNode("a"), new ValueASTNode("b")};
        ASTNode commaSeperated = new CommaDelimitNode(cArgs);
        ASTNode args[] = {commaSeperated, ExpressionParser.parseAndCollapse("b ** a")};
        Value v = new LambdaNode(args).execute(new Context());
        UnitTestUtil.assertEqual(v.type, VariableTypes.Closure);
        Closure c = (Closure) v.value;
        Value arg[] = {Value.integer(2), Value.integer(4)};
        Value result = c.execute(new Context(), arg, false);
        UnitTestUtil.assertEqual(result, Value.integer(16));
    }

}