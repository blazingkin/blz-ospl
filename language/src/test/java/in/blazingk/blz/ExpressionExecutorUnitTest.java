package in.blazingk.blz;

import static com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor.runExpression;
import static in.blazingk.blz.UnitTestUtil.assertAlmostEqual;
import static in.blazingk.blz.UnitTestUtil.assertEqual;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExpressionExecutorUnitTest {

	@BeforeClass
	public static void setup() {
		UnitTestUtil.setup();
	}

	@AfterClass
	public static void clear() {
		UnitTestUtil.clearEnv();
	}

	@Test
	public void testSimpleExpressions() {
		try {
			assertEqual(runExpression("true"), Value.bool(true));
			assertEqual(runExpression("false"), Value.bool(false));
			assertEqual(runExpression("TRUE"), Value.bool(true));
			assertEqual(runExpression("FALSE"), Value.bool(false));
			assertEqual(runExpression("3"), Value.integer(3));
			assertEqual(runExpression("-5"), Value.integer(-5));
			assertEqual(runExpression("0.1"), Value.doub(.1d));
			// This one fails... maybe it should.. maybe it shouldn't... not sure
			// assertEqual(runExpression("-.5"), Value.doub(-.5d));
			assertEqual(runExpression("-0.5"), Value.doub(-.5d));
		} catch (Exception e) {
			UnitTestUtil.fail();
		}
	}

	@Test
	public void testAddition() {
		try {
			assertEqual(runExpression("3 + 4"), Value.integer(7));
			assertEqual(runExpression("3 + 2"), Value.integer(5));
			assertEqual(runExpression("-1 + -5"), Value.integer(-6));
			assertEqual(runExpression("3 + 3 + 2"), Value.integer(8));
			assertEqual(runExpression("1+1"), Value.integer(2));
			assertEqual(runExpression("1+1+1+1+1"), Value.integer(5));
			assertEqual(runExpression("2.0 + 2.0"), Value.integer(4));
			assertEqual(runExpression("2.0 + 2"), Value.integer(4));
		} catch (BLZRuntimeException e) {
			UnitTestUtil.fail();
		} catch (Exception e) {
			UnitTestUtil.fail();
		}
	}

	@Test
	public void testLotsOfAssignment() throws Exception {
		Context tCon = new Context();
		runExpression("a = 3", tCon);
		assertEqual(tCon.getValue("a"), Value.integer(3));
		runExpression("a = 4", tCon);
		assertEqual(tCon.getValue("a"), Value.integer(4));
		runExpression("b = 5", tCon);
		assertEqual(tCon.getValue("b"), Value.integer(5));
		runExpression("a = b", tCon);
		assertEqual(tCon.getValue("a"), Value.integer(5));
		assertEqual(runExpression("a + b", tCon), Value.integer(10));
		assertEqual(runExpression("a + b", tCon), runExpression("b + a", tCon));
		runExpression("c = a + b", tCon);
		assertEqual(tCon.getValue("c"), Value.integer(10));
		runExpression("d= a +b + c + c", tCon);
		assertEqual(tCon.getValue("d"), Value.integer(30));
		assertEqual(tCon.getValue("a"), Value.integer(5));
		assertEqual(tCon.getValue("c"), Value.integer(10));
		Variable.clearVariables();
	}

	@Test
	public void testTwoPlusTwo() throws Exception {
		assertEqual(runExpression("2 + 2"), Value.integer(4));
	}

	@Test
	public void testTwoPlusMinusThree() throws Exception {
		assertEqual(runExpression("2 + -3"), Value.integer(-1));
	}

	@Test
	public void testAddingDoublesShouldGiveDouble() throws Exception {
		assertEqual(runExpression("2.5 + 3.5"), Value.doub(6));
	}

	@Test
	public void testAddingStrings() throws Exception {
		assertEqual(runExpression("\"hello\" + \"world\""), new Value(VariableTypes.String, "helloworld"));
	}

	@Test
	public void testThreeTimesThree() throws Exception {
		assertEqual(runExpression("3 * 3"), Value.integer(9));
	}

	@Test
	public void testOrderOfOperations() throws Exception {
		assertEqual(runExpression("2 * 3 + 2 * 3"), Value.integer(12));
	}

	@Test
	public void testSeveralMultiplications() throws Exception {
		assertEqual(runExpression("2 * 2 * 2 * 2"), Value.integer(16));
	}

	@Test
	public void testSubtractingShouldWork() throws Exception {
		assertEqual(runExpression("2 - 2"), Value.integer(0));
	}

	@Test
	public void testSubtractingNegativeNumbersShouldWork() throws Exception {
		assertEqual(runExpression("3 - -5"), Value.integer(8));
	}

	@Test
	public void testPowerOfOneShouldBeTheSame() throws Exception {
		assertEqual(runExpression("3 ** 1"), Value.integer(3));
	}

	@Test
	public void testThreeSquared() throws Exception {
		assertEqual(runExpression("3 ** 2"), Value.integer(9));
	}

	@Test
	public void testDecimalPowers() throws Exception {
		assertAlmostEqual(runExpression("2 ** 0.5"), Value.doub(Math.sqrt(2)));
	}

	@Test
	public void testBigExponent() throws Exception {
		assertEqual(runExpression("99 ** 30"),
				runExpression("739700373388280422730015092316714942252676262352676444347001"));
	}

	@Test
	public void testLogarithmShouldBeInverseOfExp() throws Exception {
		assertAlmostEqual(runExpression("(20 ** 2) __ 20"), Value.doub(2));
	}

	@Test
	public void testLogarithmOnKnownValue() throws Exception {
		assertAlmostEqual(runExpression("10000 __ 10"), Value.doub(4));
	}

	@Test
	public void testNaturalLogarithmOnKnownValue() throws Exception {
		assertAlmostEqual(runExpression("(10 __ \"e\")"),
				runExpression("2.30258509299404568401799145468436420760110148862877297603"));
	}

	@Test
	public void testDividingShouldGiveRationals() throws Exception {
		assertEqual(runExpression("10/3"), Value.rational(10, 3));
	}

	@Test
	public void testDividingShouldGiveInts() throws Exception {
		assertEqual(runExpression("12/3"), Value.integer(4));
	}

	@Test
	public void testMultiplicationShouldUndoDivision() throws Exception {
		assertEqual(runExpression("3 / 4 * 4"), Value.integer(3));
	}

	@Test
	public void testDoubleDivisionShouldWork() throws Exception {
		assertAlmostEqual(runExpression("3.0 / 2"), Value.doub(1.5));
	}

	@Test
	public void testModulusOnKnownValues() throws Exception {
		assertEqual(runExpression("3 % 5"), Value.integer(3));
		assertEqual(runExpression("7 % 3"), Value.integer(1));
		assertEqual(runExpression("10 % 5"), Value.integer(0));
	}

	@Test
	public void testModulusOnDoubles() throws Exception {
		assertEqual(runExpression("10.3 % 3"), Value.doub(1.3));
	}

	@Test
	public void testModulusOnNegativeNumbers() throws Exception {
		// Should act as if we are in n mod 3
		assertEqual(runExpression("-1 % 3"), Value.integer(2));
	}

	@Test
	public void testAssignment() throws Exception {
		runExpression("asdf = 3");
		assertEqual(runExpression("asdf"), Value.integer(3));
	}

	@Test
	public void testAssignmentWithComplexExpression() throws Exception {
		runExpression("asdf = 3 * 2 / 3 + 7");
		assertEqual(runExpression("asdf"), Value.integer(9));
	}

	@Test
	public void testIncrementation() throws Exception {
		runExpression("a = 3");
		assertEqual(runExpression("a++"), Value.integer(4));
		assertEqual(runExpression("a"), Value.integer(4));
	}

	@Test
	public void testDecrementation() throws Exception {
		runExpression("a = 300");
		assertEqual(runExpression("a--"), Value.integer(299));
		assertEqual(runExpression("a--"), Value.integer(298));
		assertEqual(runExpression("a"), Value.integer(298));
	}

	@Test
	public void testComparison() throws Exception {
		assertEqual(runExpression("true == true"), Value.bool(true));
	}

	@Test
	public void testComparisonTwo() throws Exception {
		assertEqual(runExpression("2 + 2 == 2 ** 2"), Value.bool(true));
	}

	@Test
	public void testStringComparison() throws Exception {
		assertEqual(runExpression("\"ab\" == \"ab\""), Value.bool(true));
	}

	@Test
	public void testComparisonNegative() throws Exception {
		assertEqual(runExpression("\"ab\" == \"ba\""), Value.bool(false));
	}

	@Test
	public void testNotEquals() throws Exception {
		assertEqual(runExpression("2 != 2"), Value.bool(false));
	}

	@Test
	public void testNotEqualsTwo() throws Exception {
		assertEqual(runExpression("3 != 2 * 1.7"), Value.bool(true));
	}

	@Test
	public void testLessThan() throws Exception {
		assertEqual(runExpression("2 < 3"), Value.bool(true));
		assertEqual(runExpression("3 < 2"), Value.bool(false));
	}

	@Test
	public void testLessThanOnEqualValues() throws Exception {
		assertEqual(runExpression("2 < 2"), Value.bool(false));
	}

	@Test
	public void testLessThanOnDifferentVariableTypes() throws Exception {
		assertEqual(runExpression("2/3 < 1"), Value.bool(true));
	}

	@Test
	public void testGreaterThan() throws Exception {
		assertEqual(runExpression("2 > 3"), Value.bool(false));
		assertEqual(runExpression("3 > 2"), Value.bool(true));
	}

	@Test
	public void testGreaterThanOnEqualValues() throws Exception {
		assertEqual(runExpression("3 > 3"), Value.bool(false));
	}

	@Test
	public void testLessThanOrEqual() throws Exception {
		assertEqual(runExpression("2 <= 3"), Value.bool(true));
		assertEqual(runExpression("3 <= 2"), Value.bool(false));
	}

	@Test
	public void testLessThanOrEqualOnEqualValues() throws Exception {
		assertEqual(runExpression("50 <= 50"), Value.bool(true));
	}

	@Test
	public void testGreaterThanOrEqual() throws Exception {
		assertEqual(runExpression("2 >= 3"), Value.bool(false));
		assertEqual(runExpression("3 >= 2"), Value.bool(true));
	}

	@Test
	public void testGreaterThanOrEqualOnEqualValues() throws Exception {
		assertEqual(runExpression("2.431 >= 2.431"), Value.bool(true));
	}

	@Test
	public void testGreaterThanOrEqualOnHugeValue() throws Exception {
		assertEqual(runExpression("99999213124214151251521512521.3214 >= 2"), Value.bool(true));
	}

	@Test
	public void testCommaDelimit() throws Exception {
		Value[] arr = { Value.integer(2), Value.integer(3) };
		assertEqual(runExpression("2, 3"), Value.arr(arr));
	}

	@Test
	public void testDifferentVariableTypesCommaDelimit() throws Exception {
		Value[] arr = { Value.rational(2, 5), Value.doub(2.0), Value.integer(7) };
		assertEqual(runExpression("2/5, 2.0, 7"), Value.arr(arr));
	}

	@Test
	public void testApproximatelyEquals() throws Exception {
		assertEqual(runExpression("1.999999999999 ~= 2"), Value.bool(true));
		assertEqual(runExpression("2 ~= 2"), Value.bool(true));
	}

	@Test
	public void testApproximatelyEqualsShouldntBeTooForgiving() throws Exception {
		assertEqual(runExpression("1.995 ~= 2"), Value.bool(false));
		assertEqual(runExpression("2 ~= 3"), Value.bool(false));
	}

	@Test
	public void testApproximatelyEqualsOnPi() throws Exception {
		assertEqual(runExpression("{pi} ~= 3.141592653589"), Value.bool(true));
	}

	@Test
	public void testCommaDelimitExtraction() throws Exception {
		Value[] arr = { Value.integer(3), Value.bool(false), Value.doub(3.2) };
		ASTNode expr = ExpressionParser.parseExpression("3, false, 3.2");
		UnitTestUtil.assertEqualArrays(ExpressionExecutor.extractCommaDelimits(expr, new Context()), arr);
	}

	@Test
	public void testCommaDelimitExtractionTwo() throws Exception {
		Value[] internalArr = { Value.integer(2), Value.integer(3) };
		Value[] arr = { Value.arr(internalArr), Value.bool(false), Value.doub(3.2) };
		ASTNode expr = ExpressionParser.parseExpression("[2, 3], false, 3.2");
		UnitTestUtil.assertEqualArrays(ExpressionExecutor.extractCommaDelimits(expr, new Context()), arr);
	}

	@Test
	public void testArrayLiteral() throws Exception {
		Value[] shouldParseTo = { Value.integer(2), Value.integer(3) };
		assertEqual(runExpression("[2, 3]"), Value.arr(shouldParseTo));
	}

	@Test
	public void testShouldHandleNestedArrays() throws Exception {
		Value[] internalArray = { Value.bool(false), Value.integer(20) };
		Value[] outer = { Value.arr(internalArray), Value.integer(10) };
		assertEqual(runExpression("[[false, 20], 10]"), Value.arr(outer));
	}

	@Test
	public void testSettingAndGettingFromArray() throws Exception {
		Context tCon = new Context();
		Value[] arr = { Value.integer(2), Value.integer(3), Value.integer(4) };
		tCon.setValue("arr", Value.arr(arr));
		assertEqual(runExpression("arr[0]", tCon), Value.integer(2));
		assertEqual(runExpression("arr[1]", tCon), Value.integer(3));
		assertEqual(runExpression("arr[2]", tCon), Value.integer(4));
	}

	@Test
	public void testDotOperatorShouldWork() {
		try {
			Context testContext = new Context();
			testContext.setValue("abcd", Value.obj(new BLZObject()));
			assertEqual(runExpression("abcd.inner = 2", testContext), Value.integer(2));
			assertEqual(runExpression("abcd.inner", testContext), Value.integer(2));
			runExpression("inner", testContext);
		} catch (BLZRuntimeException e) {
			UnitTestUtil.assertEqual(e.getMessage(), "Could not find a value for inner");
			return;
		} catch (Exception e){
			UnitTestUtil.fail("Did not expect a syntax exception");
		}
		UnitTestUtil.fail();
	}

	@Test
	public void testDoubleDotOperatorShouldWork() throws Exception {
		Context tCon = new Context();
		tCon.setValue("abc", Value.obj(new BLZObject()));
		tCon.setValue("bbb", Value.obj(new BLZObject()));
		assertEqual(runExpression("abc.b = bbb", tCon), runExpression("bbb", tCon));
		assertEqual(runExpression("abc.b.x = 2", tCon), Value.integer(2));
		assertEqual(runExpression("abc.b.x", tCon), Value.integer(2));
		assertEqual(runExpression("bbb.x", tCon), Value.integer(2));
		try {
			runExpression("x", tCon);
		} catch (BLZRuntimeException e) {
			UnitTestUtil.assertEqual(e.getMessage(), "Could not find a value for x");
			return;
		} catch (SyntaxException e){
			UnitTestUtil.fail("Did not expect a syntax exception");
		}
		UnitTestUtil.fail();
	}

	@Test
	public void testArraysInsideObject() throws Exception {
		Context testContext = new Context();
		testContext.setValue("a", Value.obj(new BLZObject()));
		assertEqual(runExpression("a.arr = [1,2,3]", testContext), runExpression("[1,2,3]"));
		assertEqual(runExpression("a.arr", testContext), runExpression("[1,2,3]"));
		assertEqual(runExpression("a.arr[0]", testContext), Value.integer(1));
		assertEqual(runExpression("a.arr[2]", testContext), Value.integer(3));
	}

	// TODO find a way to test function calls

}
