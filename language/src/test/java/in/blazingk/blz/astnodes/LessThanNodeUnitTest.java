package in.blazingk.blz.astnodes;

import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.parser.LineLexer;
import in.blazingk.blz.UnitTestUtil;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class LessThanNodeUnitTest {

	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
	}
	
	@Test 
	public void StringsShouldBeSortable() throws Exception {
		UnitTestUtil.assertEqual(Value.bool(true), ExpressionParser.parseExpression("\"a\" < \"b\"").execute(new Context()));
	}
	
	@Test
	public void TwoShouldBeLessThanThree() throws Exception {
		UnitTestUtil.assertEqual(Value.bool(true), ExpressionParser.parseExpression("2 < 3").execute(new Context()));
	}
	
	@Test
	public void TwoShouldNotBeLessThanTwo() throws Exception {
		UnitTestUtil.assertEqual(Value.bool(false), ExpressionParser.parseExpression("2 < 2").execute(new Context()));
	}

}
