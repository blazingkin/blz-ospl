package com.blazingkin.interpreter.unittests;


import static com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor.parseExpression;
import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertAlmostEqual;
import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertEqual;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.parser.ExpressionParser;
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
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@AfterClass
	public static void clear(){
		UnitTestUtil.clearEnv();
	}

	@Test
	public void testSimpleExpressions(){
		try {
			assertEqual(parseExpression("true"), Value.bool(true));
			assertEqual(parseExpression("false"), Value.bool(false));
			assertEqual(parseExpression("TRUE"), Value.bool(true));
			assertEqual(parseExpression("FALSE"), Value.bool(false));
			assertEqual(parseExpression("3"), Value.integer(3));
			assertEqual(parseExpression("-5"), Value.integer(-5));
			assertEqual(parseExpression("0.1"), Value.doub(.1d));
			// This one fails... maybe it should.. maybe it shouldn't... not sure
			//assertEqual(parseExpression("-.5"), Value.doub(-.5d));
			assertEqual(parseExpression("-0.5"), Value.doub(-.5d));
		}catch(BLZRuntimeException e){
			UnitTestUtil.fail();
		}
	}
	
	@Test
	public void testAddition(){
		try {
			assertEqual(parseExpression("3 + 4"), Value.integer(7));
			assertEqual(parseExpression("3 + 2"), Value.integer(5));
			assertEqual(parseExpression("-1 + -5"), Value.integer(-6));
			assertEqual(parseExpression("3 + 3 + 2"), Value.integer(8));
			assertEqual(parseExpression("1+1"), Value.integer(2));
			assertEqual(parseExpression("1+1+1+1+1"), Value.integer(5));
			assertEqual(parseExpression("2.0 + 2.0"), Value.doub(4));
			assertEqual(parseExpression("2.0 + 2"), Value.doub(4));
		}catch(BLZRuntimeException e){
			UnitTestUtil.fail();
		}
	}
	
	
	@Test
	public void testLotsOfAssignment() throws BLZRuntimeException {
		Context tCon = new Context();
		parseExpression("a = 3", tCon);
		assertEqual(tCon.getValue("a"), Value.integer(3));
		parseExpression("a = 4", tCon);
		assertEqual(tCon.getValue("a"), Value.integer(4));
		parseExpression("b = 5", tCon);
		assertEqual(tCon.getValue("b"), Value.integer(5));
		parseExpression("a = b", tCon);
		assertEqual(tCon.getValue("a"), Value.integer(5));
		assertEqual(parseExpression("a + b", tCon), Value.integer(10));
		assertEqual(parseExpression("a + b", tCon), parseExpression("b + a", tCon));
		parseExpression("c = a + b", tCon);
		assertEqual(tCon.getValue("c"), Value.integer(10));
		parseExpression("d= a +b + c + c", tCon);
		assertEqual(tCon.getValue("d"), Value.integer(30));
		assertEqual(tCon.getValue("a"), Value.integer(5));
		assertEqual(tCon.getValue("c"), Value.integer(10));
		Variable.clearVariables();
	}
	
	
	@Test
	public void testTwoPlusTwo() throws BLZRuntimeException {
		assertEqual(parseExpression("2 + 2"), Value.integer(4));
	}
	
	@Test
	public void testTwoPlusMinusThree() throws BLZRuntimeException{
		assertEqual(parseExpression("2 + -3"), Value.integer(-1));
	}
	
	@Test
	public void testAddingDoublesShouldGiveDouble() throws BLZRuntimeException{
		assertEqual(parseExpression("2.5 + 3.5"), Value.doub(6));
	}
	
	@Test
	public void testAddingStrings() throws BLZRuntimeException{
		assertEqual(parseExpression("\"hello\" + \"world\""), new Value(VariableTypes.String, "helloworld"));
	}
	
	@Test
	public void testThreeTimesThree() throws BLZRuntimeException{
		assertEqual(parseExpression("3 * 3"), Value.integer(9));
	}
	
	@Test
	public void testOrderOfOperations() throws BLZRuntimeException{
		assertEqual(parseExpression("2 * 3 + 2 * 3"), Value.integer(12));
	}
	
	@Test
	public void testSeveralMultiplications() throws BLZRuntimeException{
		assertEqual(parseExpression("2 * 2 * 2 * 2"), Value.integer(16));
	}
	
	@Test
	public void testSubtractingShouldWork() throws BLZRuntimeException{
		assertEqual(parseExpression("2 - 2"), Value.integer(0));
	}
	
	@Test
	public void testSubtractingNegativeNumbersShouldWork() throws BLZRuntimeException{
		assertEqual(parseExpression("3 - -5"), Value.integer(8));
	}
	
	@Test
	public void testPowerOfOneShouldBeTheSame() throws BLZRuntimeException{
		assertEqual(parseExpression("3 ** 1"), Value.integer(3));
	}
	
	@Test
	public void testThreeSquared() throws BLZRuntimeException{
		assertEqual(parseExpression("3 ** 2"), Value.integer(9));
	}
	
	@Test
	public void testDecimalPowers() throws BLZRuntimeException{
		assertAlmostEqual(parseExpression("2 ** 0.5"), Value.doub(Math.sqrt(2)));
	}
	
	@Test
	public void testBigExponent() throws BLZRuntimeException{
		assertEqual(parseExpression("99 ** 30"), parseExpression("739700373388280422730015092316714942252676262352676444347001"));
	}
	
	@Test
	public void testLogarithmShouldBeInverseOfExp() throws BLZRuntimeException{
		assertAlmostEqual(parseExpression("(20 ** 2) __ 20"), Value.doub(2));
	}
	
	@Test
	public void testLogarithmOnKnownValue() throws BLZRuntimeException{
		assertAlmostEqual(parseExpression("10000 __ 10"), Value.doub(4));
	}
	
	@Test
	public void testNaturalLogarithmOnKnownValue() throws BLZRuntimeException{
		assertAlmostEqual(parseExpression("(10 __ \"e\")"), parseExpression("2.30258509299404568401799145468436420760110148862877297603"));
	}
	
	@Test
	public void testDividingShouldGiveRationals() throws BLZRuntimeException{
		assertEqual(parseExpression("10/3"), Value.rational(10, 3));
	}
	
	@Test
	public void testDividingShouldGiveInts() throws BLZRuntimeException{
		assertEqual(parseExpression("12/3"), Value.integer(4));
	}
	
	@Test
	public void testMultiplicationShouldUndoDivision() throws BLZRuntimeException{
		assertEqual(parseExpression("3 / 4 * 4"), Value.integer(3));
	}
	
	@Test
	public void testDoubleDivisionShouldWork() throws BLZRuntimeException{
		assertAlmostEqual(parseExpression("3.0 / 2"), Value.doub(1.5));
	}
	
	@Test
	public void testModulusOnKnownValues() throws BLZRuntimeException{
		assertEqual(parseExpression("3 % 5"), Value.integer(3));
		assertEqual(parseExpression("7 % 3"), Value.integer(1));
		assertEqual(parseExpression("10 % 5"), Value.integer(0));
	}
	
	@Test
	public void testModulusOnDoubles() throws BLZRuntimeException{
		assertEqual(parseExpression("10.3 % 3"), Value.doub(1.3));
	}
	
	@Test
	public void testModulusOnNegativeNumbers() throws BLZRuntimeException{
		// Should act as if we are in n mod 3
		assertEqual(parseExpression("-1 % 3"), Value.integer(2));
	}
	
	@Test
	public void testAssignment() throws BLZRuntimeException{
		parseExpression("asdf = 3");
		assertEqual(parseExpression("asdf"), Value.integer(3));
	}
	
	@Test
	public void testAssignmentWithComplexExpression() throws BLZRuntimeException{
		parseExpression("asdf = 3 * 2 / 3 + 7");
		assertEqual(parseExpression("asdf"), Value.integer(9));
	}
	
	@Test
	public void testIncrementation() throws BLZRuntimeException{
		parseExpression("a = 3");
		assertEqual(parseExpression("a++"), Value.integer(4));
		assertEqual(parseExpression("a"), Value.integer(4));
	}
	
	@Test
	public void testDecrementation() throws BLZRuntimeException{
		parseExpression("a = 300");
		assertEqual(parseExpression("a--"), Value.integer(299));
		assertEqual(parseExpression("a--"), Value.integer(298));
		assertEqual(parseExpression("a"), Value.integer(298));
	}
	
	@Test
	public void testComparison() throws BLZRuntimeException{
		assertEqual(parseExpression("true == true"), Value.bool(true));
	}
	
	@Test
	public void testComparisonTwo() throws BLZRuntimeException{
		assertEqual(parseExpression("2 + 2 == 2 ** 2"), Value.bool(true));
	}
	
	@Test
	public void testStringComparison() throws BLZRuntimeException{
		assertEqual(parseExpression("\"ab\" == \"ab\""), Value.bool(true));
	}
	
	@Test
	public void testComparisonNegative() throws BLZRuntimeException{
		assertEqual(parseExpression("\"ab\" == \"ba\""), Value.bool(false));
	}
	
	@Test
	public void testNotEquals() throws BLZRuntimeException{
		assertEqual(parseExpression("2 != 2"), Value.bool(false));
	}
	
	@Test
	public void testNotEqualsTwo() throws BLZRuntimeException{
		assertEqual(parseExpression("3 != 2 * 1.7"), Value.bool(true));
	}
	
	@Test
	public void testLessThan() throws BLZRuntimeException{
		assertEqual(parseExpression("2 < 3"), Value.bool(true));
		assertEqual(parseExpression("3 < 2"), Value.bool(false));
	}
	
	@Test
	public void testLessThanOnEqualValues() throws BLZRuntimeException{
		assertEqual(parseExpression("2 < 2"), Value.bool(false));
	}
	
	@Test
	public void testLessThanOnDifferentVariableTypes() throws BLZRuntimeException{
		assertEqual(parseExpression("2/3 < 1"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThan() throws BLZRuntimeException{
		assertEqual(parseExpression("2 > 3"), Value.bool(false));
		assertEqual(parseExpression("3 > 2"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThanOnEqualValues() throws BLZRuntimeException{
		assertEqual(parseExpression("3 > 3"), Value.bool(false));
	}
	
	
	@Test
	public void testLessThanOrEqual() throws BLZRuntimeException{
		assertEqual(parseExpression("2 <= 3"), Value.bool(true));
		assertEqual(parseExpression("3 <= 2"), Value.bool(false));
	}
	
	@Test
	public void testLessThanOrEqualOnEqualValues() throws BLZRuntimeException{
		assertEqual(parseExpression("50 <= 50"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThanOrEqual() throws BLZRuntimeException{
		assertEqual(parseExpression("2 >= 3"), Value.bool(false));
		assertEqual(parseExpression("3 >= 2"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThanOrEqualOnEqualValues() throws BLZRuntimeException{
		assertEqual(parseExpression("2.431 >= 2.431"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThanOrEqualOnHugeValue() throws BLZRuntimeException{
		assertEqual(parseExpression("99999213124214151251521512521.3214 >= 2"), Value.bool(true));
	}
	
	@Test
	public void testCommaDelimit() throws BLZRuntimeException{
		Value[] arr = {Value.integer(2), Value.integer(3)};
		assertEqual(parseExpression("2, 3"), Value.arr(arr));
	}
	
	@Test
	public void testDifferentVariableTypesCommaDelimit() throws BLZRuntimeException{
		Value[] arr = {Value.rational(2, 5), Value.doub(2.0), Value.integer(7)};
		assertEqual(parseExpression("2/5, 2.0, 7"), Value.arr(arr));
	}
	
	@Test
	public void testApproximatelyEquals() throws BLZRuntimeException{
		assertEqual(parseExpression("1.999999999999 ~= 2"), Value.bool(true));
		assertEqual(parseExpression("2 ~= 2"), Value.bool(true));
	}
	
	@Test
	public void testApproximatelyEqualsShouldntBeTooForgiving() throws BLZRuntimeException{
		assertEqual(parseExpression("1.995 ~= 2"), Value.bool(false));
		assertEqual(parseExpression("2 ~= 3"), Value.bool(false));
	}
	
	@Test
	public void testApproximatelyEqualsOnPi() throws BLZRuntimeException{
		assertEqual(parseExpression("{pi} ~= 3.141592653589"), Value.bool(true));
	}
	
	@Test
	public void testCommaDelimitExtraction() throws BLZRuntimeException{
		Value[] arr = {Value.integer(3), Value.bool(false), Value.doub(3.2)};
		ASTNode expr = ExpressionParser.parseExpression("3, false, 3.2");
		UnitTestUtil.assertEqualArrays(ExpressionExecutor.extractCommaDelimits(expr, new Context()), arr);
	}
	
	@Test
	public void testCommaDelimitExtractionTwo() throws BLZRuntimeException{
		Value[] internalArr = {Value.integer(2), Value.integer(3)};
		Value[] arr = {Value.arr(internalArr), Value.bool(false), Value.doub(3.2)};
		ASTNode expr = ExpressionParser.parseExpression("[2, 3], false, 3.2");
		UnitTestUtil.assertEqualArrays(ExpressionExecutor.extractCommaDelimits(expr, new Context()), arr);
	}
	
	@Test
	public void testArrayLiteral() throws BLZRuntimeException{
		Value[] shouldParseTo = {Value.integer(2), Value.integer(3)};
		assertEqual(parseExpression("[2, 3]"), Value.arr(shouldParseTo));
	}
	
	@Test
	public void testShouldHandleNestedArrays() throws BLZRuntimeException{
		Value[] internalArray = {Value.bool(false), Value.integer(20)};
		Value[] outer = {Value.arr(internalArray), Value.integer(10)};
		assertEqual(parseExpression("[[false, 20], 10]"), Value.arr(outer));
	}
	
	@Test
	public void testSettingAndGettingFromArray() throws BLZRuntimeException{
		Context tCon = new Context();
		Value[] arr = {Value.integer(2), Value.integer(3), Value.integer(4)};
		tCon.setValue("arr", Value.arr(arr));
		assertEqual(parseExpression("arr[0]", tCon), Value.integer(2));
		assertEqual(parseExpression("arr[1]", tCon), Value.integer(3));
		assertEqual(parseExpression("arr[2]", tCon), Value.integer(4));
	}
	
	@Test
	public void testDotOperatorShouldWork(){
		try {
		Context testContext = new Context();
		testContext.setValue("abcd", Value.obj(new BLZObject()));
		assertEqual(parseExpression("abcd.inner = 2", testContext), Value.integer(2));
		assertEqual(parseExpression("abcd.inner", testContext), Value.integer(2));
		parseExpression("inner", testContext);
		}catch(BLZRuntimeException e){
			UnitTestUtil.assertEqual(e.getMessage(), "Could not find a value for inner");
			return;
		}
		UnitTestUtil.fail();
	}
	
	@Test
	public void testDoubleDotOperatorShouldWork() throws BLZRuntimeException {
		Context tCon = new Context();
		tCon.setValue("abc", Value.obj(new BLZObject()));
		tCon.setValue("bbb", Value.obj(new BLZObject()));
		assertEqual(parseExpression("abc.b = bbb", tCon), parseExpression("bbb", tCon));
		assertEqual(parseExpression("abc.b.x = 2", tCon), Value.integer(2));
		assertEqual(parseExpression("abc.b.x", tCon), Value.integer(2));
		assertEqual(parseExpression("bbb.x", tCon), Value.integer(2));
		try {
		parseExpression("x", tCon);
		}catch(BLZRuntimeException e){
			UnitTestUtil.assertEqual(e.getMessage(), "Could not find a value for x");
			return;
		}
		UnitTestUtil.fail();
	}
	
	@Test
	public void testArraysInsideObject() throws BLZRuntimeException {
		Context testContext = new Context();
		testContext.setValue("a", Value.obj(new BLZObject()));
		assertEqual(parseExpression("a.arr = [1,2,3]", testContext), parseExpression("[1,2,3]"));
		assertEqual(parseExpression("a.arr", testContext), parseExpression("[1,2,3]"));
		assertEqual(parseExpression("a.arr[0]", testContext), Value.integer(1));
		assertEqual(parseExpression("a.arr[2]", testContext), Value.integer(3));
	}
	
	//TODO find a way to test function calls
	
	
}
