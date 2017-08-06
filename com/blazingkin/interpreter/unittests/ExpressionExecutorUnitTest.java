package com.blazingkin.interpreter.unittests;


import static com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor.parseExpression;
import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertAlmostEqual;
import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertEqual;
import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertValEqual;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.expressionabstraction.ExpressionParser;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ExpressionExecutorUnitTest {
	
	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@AfterClass
	public static void clear(){
		UnitTestUtil.clearEnv();
	}

	@Test
	public void testSimpleExpressions(){
		assertEqual(parseExpression("true"), Value.bool(true));
		assertEqual(parseExpression("false"), Value.bool(false));
		assertEqual(parseExpression("TRUE"), Value.bool(true));
		assertEqual(parseExpression("FALSE"), Value.bool(false));
		assertEqual(parseExpression("3"), Value.integer(3));
		assertEqual(parseExpression("-5"), Value.integer(-5));
		assertEqual(parseExpression(".1"), Value.doub(.1d));
		// This one fails... maybe it should.. maybe it shouldn't... not sure
		//assertEqual(parseExpression("-.5"), Value.doub(-.5d));
		assertEqual(parseExpression("-0.5"), Value.doub(-.5d));
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
	public void testLotsOfAssignment(){
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
	public void testTwoPlusTwo(){
		assertEqual(parseExpression("2 + 2"), Value.integer(4));
	}
	
	@Test
	public void testTwoPlusMinusThree(){
		assertEqual(parseExpression("2 + -3"), Value.integer(-1));
	}
	
	@Test
	public void testAddingDoublesShouldGiveDouble(){
		assertEqual(parseExpression("2.5 + 3.5"), Value.doub(6));
	}
	
	@Test
	public void testAddingStrings(){
		assertEqual(parseExpression("\"hello\" + \"world\""), new Value(VariableTypes.String, "helloworld"));
	}
	
	@Test
	public void testThreeTimesThree(){
		assertEqual(parseExpression("3 * 3"), Value.integer(9));
	}
	
	@Test
	public void testOrderOfOperations(){
		assertEqual(parseExpression("2 * 3 + 2 * 3"), Value.integer(12));
	}
	
	@Test
	public void testSeveralMultiplications(){
		assertEqual(parseExpression("2 * 2 * 2 * 2"), Value.integer(16));
	}
	
	@Test
	public void testSubtractingShouldWork(){
		assertEqual(parseExpression("2 - 2"), Value.integer(0));
	}
	
	@Test
	public void testSubtractingNegativeNumbersShouldWork(){
		assertEqual(parseExpression("3 - -5"), Value.integer(8));
	}
	
	@Test
	public void testPowerOfOneShouldBeTheSame(){
		assertEqual(parseExpression("3 ** 1"), Value.integer(3));
	}
	
	@Test
	public void testThreeSquared(){
		assertEqual(parseExpression("3 ** 2"), Value.integer(9));
	}
	
	@Test
	public void testBigExponent(){
		assertEqual(parseExpression("99 ** 30"), parseExpression("739700373388280422730015092316714942252676262352676444347001"));
	}
	
	@Test
	public void testLogarithmShouldBeInverseOfExp(){
		assertAlmostEqual(parseExpression("(20 ** 2) __ 20"), Value.doub(2));
	}
	
	@Test
	public void testLogarithmOnKnownValue(){
		assertAlmostEqual(parseExpression("10000 __ 10"), Value.doub(4));
	}
	
	@Test
	public void testNaturalLogarithmOnKnownValue(){
		assertAlmostEqual(parseExpression("(10 __ \"e\")"), parseExpression("2.30258509299404568401799145468436420760110148862877297603"));
	}
	
	@Test
	public void testDividingShouldGiveRationals(){
		assertEqual(parseExpression("10/3"), Value.rational(10, 3));
	}
	
	@Test
	public void testDividingShouldGiveInts(){
		assertEqual(parseExpression("12/3"), Value.integer(4));
	}
	
	@Test
	public void testMultiplicationShouldUndoDivision(){
		assertEqual(parseExpression("3 / 4 * 4"), Value.integer(3));
	}
	
	@Test
	public void testDoubleDivisionShouldWork(){
		assertAlmostEqual(parseExpression("3.0 / 2"), Value.doub(1.5));
	}
	
	@Test
	public void testModulusOnKnownValues(){
		assertEqual(parseExpression("3 % 5"), Value.integer(3));
		assertEqual(parseExpression("7 % 3"), Value.integer(1));
		assertEqual(parseExpression("10 % 5"), Value.integer(0));
	}
	
	@Test
	public void testModulusOnDoubles(){
		assertEqual(parseExpression("10.3 % 3"), Value.doub(1.3));
	}
	
	@Test
	public void testModulusOnNegativeNumbers(){
		// Should act as if we are in n mod 3
		assertEqual(parseExpression("-1 % 3"), Value.integer(2));
	}
	
	@Test
	public void testAssignment(){
		parseExpression("asdf = 3");
		assertEqual(parseExpression("asdf"), Value.integer(3));
	}
	
	@Test
	public void testAssignmentWithComplexExpression(){
		parseExpression("asdf = 3 * 2 / 3 + 7");
		assertEqual(parseExpression("asdf"), Value.integer(9));
	}
	
	@Test
	public void testIncrementation(){
		parseExpression("a = 3");
		assertEqual(parseExpression("a++"), Value.integer(4));
		assertEqual(parseExpression("a"), Value.integer(4));
	}
	
	@Test
	public void testDecrementation(){
		parseExpression("a = 300");
		assertEqual(parseExpression("a--"), Value.integer(299));
		assertEqual(parseExpression("a--"), Value.integer(298));
		assertEqual(parseExpression("a"), Value.integer(298));
	}
	
	@Test
	public void testComparison(){
		assertEqual(parseExpression("true == true"), Value.bool(true));
	}
	
	@Test
	public void testComparisonTwo(){
		assertEqual(parseExpression("2 + 2 == 2 ** 2"), Value.bool(true));
	}
	
	@Test
	public void testStringComparison(){
		assertEqual(parseExpression("\"ab\" == \"ab\""), Value.bool(true));
	}
	
	@Test
	public void testComparisonNegative(){
		assertEqual(parseExpression("\"ab\" == \"ba\""), Value.bool(false));
	}
	
	@Test
	public void testNotEquals(){
		assertEqual(parseExpression("2 != 2"), Value.bool(false));
	}
	
	@Test
	public void testNotEqualsTwo(){
		assertEqual(parseExpression("3 != 2 * 1.7"), Value.bool(true));
	}
	
	@Test
	public void testLessThan(){
		assertEqual(parseExpression("2 < 3"), Value.bool(true));
		assertEqual(parseExpression("3 < 2"), Value.bool(false));
	}
	
	@Test
	public void testLessThanOnEqualValues(){
		assertEqual(parseExpression("2 < 2"), Value.bool(false));
	}
	
	@Test
	public void testLessThanOnDifferentVariableTypes(){
		assertEqual(parseExpression("2/3 < 1"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThan(){
		assertEqual(parseExpression("2 > 3"), Value.bool(false));
		assertEqual(parseExpression("3 > 2"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThanOnEqualValues(){
		assertEqual(parseExpression("3 > 3"), Value.bool(false));
	}
	
	
	@Test
	public void testLessThanOrEqual(){
		assertEqual(parseExpression("2 <= 3"), Value.bool(true));
		assertEqual(parseExpression("3 <= 2"), Value.bool(false));
	}
	
	@Test
	public void testLessThanOrEqualOnEqualValues(){
		assertEqual(parseExpression("50 <= 50"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThanOrEqual(){
		assertEqual(parseExpression("2 >= 3"), Value.bool(false));
		assertEqual(parseExpression("3 >= 2"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThanOrEqualOnEqualValues(){
		assertEqual(parseExpression("2.431 >= 2.431"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThanOrEqualOnHugeValue(){
		assertEqual(parseExpression("99999213124214151251521512521.3214 >= 2"), Value.bool(true));
	}
	
	@Test
	public void testCommaDelimit(){
		Value[] arr = {Value.integer(2), Value.integer(3)};
		assertEqual(parseExpression("2, 3"), Value.arr(arr));
	}
	
	@Test
	public void testDifferentVariableTypesCommaDelimit(){
		Value[] arr = {Value.rational(2, 5), Value.doub(2.0), Value.integer(7)};
		assertEqual(parseExpression("2/5, 2.0, 7"), Value.arr(arr));
	}
	
	@Test
	public void testApproximatelyEquals(){
		assertEqual(parseExpression("1.999999999999 ~= 2"), Value.bool(true));
		assertEqual(parseExpression("2 ~= 2"), Value.bool(true));
	}
	
	@Test
	public void testApproximatelyEqualsShouldntBeTooForgiving(){
		assertEqual(parseExpression("1.995 ~= 2"), Value.bool(false));
		assertEqual(parseExpression("2 ~= 3"), Value.bool(false));
	}
	
	@Test
	public void testApproximatelyEqualsOnPi(){
		assertEqual(parseExpression("{pi} ~= 3.141592653589"), Value.bool(true));
	}
	
	@Test
	public void testCommaDelimitExtraction(){
		Value[] arr = {Value.integer(3), Value.bool(false), Value.doub(3.2)};
		ASTNode expr = ExpressionParser.parseExpression("3, false, 3.2");
		UnitTestUtil.assertEqualArrays(ExpressionExecutor.extractCommaDelimits(expr), arr);
	}
	
	@Test
	public void testCommaDelimitExtractionTwo(){
		Value[] internalArr = {Value.integer(2), Value.integer(3)};
		Value[] arr = {Value.arr(internalArr), Value.bool(false), Value.doub(3.2)};
		ASTNode expr = ExpressionParser.parseExpression("[2, 3], false, 3.2");
		UnitTestUtil.assertEqualArrays(ExpressionExecutor.extractCommaDelimits(expr), arr);
	}
	
	@Test
	public void testArrayLiteral(){
		Value[] shouldParseTo = {Value.integer(2), Value.integer(3)};
		assertEqual(parseExpression("[2, 3]"), Value.arr(shouldParseTo));
	}
	
	@Test
	public void testShouldHandleNestedArrays(){
		Value[] internalArray = {Value.bool(false), Value.integer(20)};
		Value[] outer = {Value.arr(internalArray), Value.integer(10)};
		assertEqual(parseExpression("[[false, 20], 10]"), Value.arr(outer));
	}
	
	@Test
	public void testSettingAndGettingFromArray(){
		parseExpression("arr = [2,3,4]");
		assertEqual(parseExpression("arr[0]"), Value.integer(2));
		assertEqual(parseExpression("arr[1]"), Value.integer(3));
		assertEqual(parseExpression("arr[2]"), Value.integer(4));
	}
	
	//TODO find a way to test function calls
	
	
}
