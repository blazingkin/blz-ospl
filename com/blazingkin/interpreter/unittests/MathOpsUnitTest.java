package com.blazingkin.interpreter.unittests;


import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertAlmostEqual;
import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertEqual;
import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertValEqual;
import static com.blazingkin.interpreter.executor.SimpleExpressionParser.parseExpression;

import org.junit.AfterClass;
import org.junit.Test;

import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.math.AddVars;
import com.blazingkin.interpreter.executor.math.Ceiling;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

@SuppressWarnings("deprecation")
public class MathOpsUnitTest {

	@Test
	public void testAddVars(){
		//AddVars Tests (Imperative)
		AddVars av = (AddVars) Instruction.ADDVARIABLE.executor;
		String[] a1 = {"1", "1", "a"};
		av.run(a1);
		assertEqual(Variable.getValue("a"), Value.integer(2));
		String[] a2 = {"2", "3", "a"};
		av.run(a2);
		assertEqual(Variable.getValue("a"), Value.integer(5));
		String[] a3 = {"1", "-2", "b"};
		av.run(a3);
		assertEqual(Variable.getValue("b"), Value.integer(-1));
		String[] a4 = {"a", "b", "a"};
		av.run(a4);
		assertEqual(Variable.getValue("a"), Value.integer(4));
		
		//AddVar Tests (Functional)
		for (int i = 0; i < 1000; i++){
			int a = UnitTestUtil.rand.nextInt() % 1500;
			int b = UnitTestUtil.rand.nextInt() % 1500;
			String[] arg = {a+"", b+""};
			assertEqual(av.evaluate(arg), Value.integer(a+b));
		}
		Variable.clearVariables();

	}
	
	
	@Test
	public void testCeiling(){
		{
			//Ceiling Tests (Imperative)
			Ceiling ceil = (Ceiling) Instruction.CEILING.executor;
			Variable.setValue("c", Value.doub(1.5));
			String[] a1 = {"c"};
			ceil.run(a1);
			assertEqual(Variable.getValue("c"), Value.integer(2));
			Variable.setValue("d", Value.doub(.01));
			String[] a2 = {"d"};
			ceil.run(a2);
			assertEqual(Variable.getValue("d"), Value.integer(1));
			Variable.setValue("e", Value.integer(1));
			String[] a3 = {"e"};
			ceil.run(a3);
			assertEqual(Variable.getValue("e"), Value.integer(1));
			Variable.setValue("f", Value.doub(-1.5));
			String[] a4 = {"f"};
			ceil.run(a4);
			assertEqual(Variable.getValue("f"), Value.integer(-1));
			
			
			//Ceiling Tests (Functional)
			for (double d = 0; d < 5000; d += .3){
				String[] arg = {d+""};
				assertEqual(ceil.evaluate(arg), Value.integer((int)Math.ceil(d)));
			}
			
		}
		
		Variable.clearVariables();
	}
	
	@Test
	public void testSimpleExpressions(){
		assertEqual(parseExpression("true"), new Value(VariableTypes.Boolean, true));
		assertEqual(parseExpression("false"), new Value(VariableTypes.Boolean, false));
		assertEqual(parseExpression("TRUE"), new Value(VariableTypes.Boolean, true));
		assertEqual(parseExpression("FALSE"), new Value(VariableTypes.Boolean, false));
		assertEqual(parseExpression("3"), Value.integer(3));
		assertEqual(parseExpression("-5"), Value.integer(-5));
		assertEqual(parseExpression(".1"), Value.doub(.1d));
		assertEqual(parseExpression("-.5"), Value.doub(-.5d));
	}
	
	
	
	@Test
	public void testAddition(){
		assertEqual(parseExpression("3 + 4"), Value.integer(7));
		assertEqual(parseExpression("3 + 2"), Value.integer(5));
		assertEqual(parseExpression("-1 + -5"), Value.integer(-6));
		assertEqual(parseExpression("3 + 3 + 2"), Value.integer(8));
		assertEqual(parseExpression("1+1"), Value.integer(2));
		assertEqual(parseExpression("1+1+1+1+1"), Value.integer(5));
		assertEqual(parseExpression("2.0 + 2.0"), Value.doub(4));
		assertEqual(parseExpression("2.0 + 2"), Value.doub(4));
	}
	
	@Test
	public void testAssignment(){
		parseExpression("a = 3");
		assertValEqual("a", Value.integer(3));
		parseExpression("a = 4");
		assertValEqual("a", Value.integer(4));
		parseExpression("b = 5");
		assertValEqual("b", Value.integer(5));
		parseExpression("a = b");
		assertValEqual("a", Value.integer(5));
		assertEqual(parseExpression("a + b"), Value.integer(10));
		assertEqual(parseExpression("a + b"), parseExpression("b + a"));
		parseExpression("c = a + b");
		assertValEqual("c", Value.integer(10));
		parseExpression("d= a +b + c + c");
		assertValEqual("d", Value.integer(30));
		assertValEqual("a", Value.integer(5));
		assertValEqual("c", Value.integer(10));
		parseExpression("arr[d] = 17.3");
		assertValEqual("arr[30]", Value.doub(17.3d));
		parseExpression("temp[a] = arr[d] + .9");
		assertValEqual("temp[5]", Value.doub(18.2d));
		Variable.clearVariables();
	}
	
	@Test
	public void testComparison(){
		assertEqual(parseExpression("1 == 1"), new Value(VariableTypes.Boolean, true));
		assertEqual(parseExpression("1 == 2"), new Value(VariableTypes.Boolean, false));
		assertEqual(parseExpression("1 == 1.0"), new Value(VariableTypes.Boolean, false));	// Not sure about how to treat this case... May change
		assertEqual(parseExpression("1 == 1 == 1"), new Value(VariableTypes.Boolean, true));
		assertEqual(parseExpression("1 + 1 == 2"), new Value(VariableTypes.Boolean, true));
		assertEqual(parseExpression("1 + 1 + 1 == 3 == 1 + 2"), new Value(VariableTypes.Boolean, true));
		assertEqual(parseExpression("1 + 1 == 1"), new Value(VariableTypes.Boolean, false));
		assertEqual(parseExpression("1/1 == 1 == 2/2 == 3/3 == 5/5 == 2 - 1 == 1/2 * 2"), new Value(VariableTypes.Boolean, true));
	}
	
	@Test
	public void testDivision(){
		assertEqual(parseExpression("1 / 2"), Value.rational(1, 2));
		assertEqual(parseExpression("2 / 4"), Value.rational(1, 2));
		assertEqual(parseExpression("2 / 1"), Value.integer(2));
		assertEqual(parseExpression("4 / 2"), Value.integer(2));
		assertEqual(parseExpression("1/2 + 1/2"), Value.integer(1));
		assertEqual(parseExpression("2/2 + 3/3 + 4/4 + 5/5"), Value.integer(4));
		assertEqual(parseExpression("1/3 + 1/2"), Value.rational(5, 6));
		assertEqual(parseExpression("1/2 + 1/2 + 1/2"), Value.rational(3, 2));
		assertEqual(parseExpression("1.1 / 2"), Value.doub(1.1 / 2));
		parseExpression("a = 3 / 2");
		parseExpression("b = 3 / 2");
		assertEqual(parseExpression("a / b"), Value.integer(1));
		assertEqual(parseExpression("-1/-1"), Value.integer(1));
		assertEqual(parseExpression("1/-1"), Value.integer(-1));
		Variable.clearVariables();
	}
	
	
	@Test
	public void testParens(){
		assertEqual(parseExpression("1 + 2/3"), Value.rational(5, 3));
		assertEqual(parseExpression("(1+2)/3"), Value.integer(1));
		assertEqual(parseExpression("(12 + 3) / 5"), Value.integer(3));
		assertEqual(parseExpression("(12 + 4) / 4 == 4"), Value.bool(true));
	}
	
	@Test
	public void testMultiplication(){
		assertEqual(parseExpression("2 * 2"), Value.integer(4));
		assertEqual(parseExpression("(1/2) * 2"), Value.integer(1));
		assertEqual(parseExpression(".5 * 2"), Value.doub(1));
		assertEqual(parseExpression("(1 + 2 / 3) * 3"), Value.integer(5));
		assertEqual(parseExpression("(2 * 3 == 5) == (2 + 2 == 1)"), Value.bool(true));
		assertEqual(parseExpression("2 * 1/2 * 1 * 1.0"), Value.doub(1.0d));
	}
	
	@Test
	public void testSubtraction(){
		assertEqual(parseExpression("2 - 2"), Value.integer(0));
		assertEqual(parseExpression("2.0 - 2.0"), Value.doub(0));
		assertEqual(parseExpression("1 - 2/3"), Value.rational(-1, 3));
		assertEqual(parseExpression("3 - (2/3)"), Value.rational(7, 3));
		assertEqual(parseExpression("1 - (2/3)"), Value.rational(1, 3));
	}
	
	@Test
	public void testExponentiation(){
		assertAlmostEqual(parseExpression("10 ** 2"), Value.doub(100));
		assertAlmostEqual(parseExpression("2 ** 3"), Value.doub(8));
		assertAlmostEqual(parseExpression("2 ** 5"), Value.doub(32));
		assertAlmostEqual(parseExpression("1 ** 100"), Value.doub(1));
		assertAlmostEqual(parseExpression(".9 ** 10000"), Value.doub(0));
		assertAlmostEqual(parseExpression(".9 ** 2"), Value.doub(.81));
		assertAlmostEqual(parseExpression(".5 ** 3"), Value.doub(1d/8d));
	}
	
	@Test
	public void testLogarithm(){
		assertAlmostEqual(parseExpression("{e} __ \"e\""), Value.doub(1));
		assertAlmostEqual(parseExpression("10 ** {pi} __ 10"), Value.doub(Math.PI));
		assertAlmostEqual(parseExpression("1000 __ 10"), Value.doub(3));
		assertAlmostEqual(parseExpression("10000 __ 10"), Value.doub(4));
		assertAlmostEqual(parseExpression("8 __ 2"), Value.doub(3));
		assertAlmostEqual(parseExpression("32 __ 2"), Value.doub(5));
		assertAlmostEqual(parseExpression("{e} __ {e}"), Value.doub(1));
	}
	
	@Test
	public void testApproxEquals(){
		assertEqual(parseExpression("2 ~= 2 =~ 2.0"), Value.bool(true));
		assertEqual(parseExpression("{pi} ~= 3.1415926534"), Value.bool(true));
		assertEqual(parseExpression("{e} =~ 2.71828182845904523"), Value.bool(true));
		assertEqual(parseExpression("1/3 ~= .33333333333"), Value.bool(true));
		assertEqual(parseExpression("8__2 ~= 3"), Value.bool(true));
		assertEqual(parseExpression(".5 + .5 ~= 1"), Value.bool(true));
		assertEqual(parseExpression(".1 + .2 ~= .3"), Value.bool(true));
		assertEqual(parseExpression(".1 + .1 ~= .25"), Value.bool(false));
		assertEqual(parseExpression(".01 + .01 ~= .025"), Value.bool(false));
		assertEqual(parseExpression(".0001 + .0001 ~= .00025"), Value.bool(false));
	}
	
	@Test
	public void testLessThan(){
		assertEqual(parseExpression("2 < 3"), Value.bool(true));
		assertEqual(parseExpression("3 < 2"), Value.bool(false));
		assertEqual(parseExpression("1 < 2 < 3"), Value.bool(true));
		assertEqual(parseExpression("1 < 3 < 2"), Value.bool(false));
		assertEqual(parseExpression("1 < 2 < 3 < 2.1"), Value.bool(false));
		assertEqual(parseExpression("1/3 < 1/2"), Value.bool(true));
		assertEqual(parseExpression("1/8 < 1/16"), Value.bool(false));
		assertEqual(parseExpression(".001 < .1"), Value.bool(true));
		assertEqual(parseExpression("1.3 < .9"), Value.bool(false));
		assertEqual(parseExpression("1 < 1"), Value.bool(false));
	}
	
	@Test
	public void testGreaterThan(){
		assertEqual(parseExpression("5 > 1"), Value.bool(true));
		assertEqual(parseExpression("{e} > {pi}"), Value.bool(false));
		assertEqual(parseExpression("0 > -1"), Value.bool(true));
		assertEqual(parseExpression("0 > 0"),Value.bool(false));
	}
	
	@Test
	public void testLessThanEqual(){
		assertEqual(parseExpression("1 <= 2"), Value.bool(true));
		assertEqual(parseExpression("1 =< 2"), Value.bool(true));
		assertEqual(parseExpression("2 <= 1"), Value.bool(false));
		assertEqual(parseExpression("2 =< 1"), Value.bool(false));
		assertEqual(parseExpression("0 <= 0"), Value.bool(true));
		assertEqual(parseExpression("0 =< 0"), Value.bool(true));
	}
	
	@Test
	public void testGreatherThanEqual(){
		assertEqual(parseExpression("1 >= 2"), Value.bool(false));
		assertEqual(parseExpression("1 => 2"), Value.bool(false));
		assertEqual(parseExpression("2 >= 1"), Value.bool(true));
		assertEqual(parseExpression("0 >= 0"), Value.bool(true));
		assertEqual(parseExpression("1 >= 1"), Value.bool(true));
	}
	
	@Test
	public void testModulus(){
		assertEqual(parseExpression("3 % 2"), Value.integer(1));
		assertEqual(parseExpression("4 % 2"), Value.integer(0));
		assertEqual(parseExpression("5 % 7"), Value.integer(5));
		assertEqual(parseExpression("0 % 999"), Value.integer(0));
		assertEqual(parseExpression("2.2 % 2"), Value.doub(.2));
		assertAlmostEqual(parseExpression("{pi} % 3"), Value.doub(Math.PI - 3));
	}
	
	@Test
	public void testPercent(){
		assertEqual(parseExpression("100%"), Value.integer(1));
		assertEqual(parseExpression("50%"), Value.rational(1, 2));
		assertEqual(parseExpression("20 %"), Value.rational(1, 5));
		assertEqual(parseExpression("10%"), Value.rational(1, 10));
		assertEqual(parseExpression("3%"), Value.rational(3, 100));
		assertEqual(parseExpression("300%"), Value.integer(3));
		assertAlmostEqual(parseExpression("{e}%"), Value.doub(Math.E / 100));
	}
	
	@Test
	public void testMathIntegration(){
		parseExpression("a = 3");
		assertEqual(parseExpression("++a + 4"), Value.integer(8));
		assertEqual(parseExpression("a"), Value.integer(4));
		assertEqual(parseExpression("2*2 < 3*3 < 3 ** 2.1"), Value.bool(true));
	}

	
	@AfterClass
	public static void teardown(){
		Variable.clearVariables();
	}
	
	
}
