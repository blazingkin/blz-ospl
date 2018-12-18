package in.blazingk.blz.astnodes;

import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import in.blazingk.blz.UnitTestUtil;
import com.blazingkin.interpreter.variables.Value;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class AssignmentNodeUnitTest {

	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
	}
	
	@Test
	public void emptyBracketsShouldBeEmptyArray() throws Exception {
		Value[] arr = {};
		UnitTestUtil.assertEqual(Value.arr(arr), ExpressionExecutor.runExpression("a = []"));
	}

}
