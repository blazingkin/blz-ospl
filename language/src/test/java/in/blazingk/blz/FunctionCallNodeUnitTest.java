package in.blazingk.blz;

import org.junit.Test;


import com.blazingkin.interpreter.executor.astnodes.FunctionCallNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import static in.blazingk.blz.UnitTestUtil.assertEqual;

public class FunctionCallNodeUnitTest {

	@Test
	public void shouldMarkAsPassByReference() {
		ASTNode args[] = {new ValueASTNode("call!"),new ValueASTNode("2")};
		FunctionCallNode fNode = new FunctionCallNode(args);
		assertEqual(true, fNode.passByReference);
		assertEqual("call", fNode.args[0].getStoreName());
	}
	
	@Test
	public void shouldMarkAsPassByValue() {
		ASTNode args[] = {new ValueASTNode("call"),new ValueASTNode("2")};
		FunctionCallNode fNode = new FunctionCallNode(args);
		assertEqual(false, fNode.passByReference);
		assertEqual("call", fNode.args[0].getStoreName());
	}

}
