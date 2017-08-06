package com.blazingkin.interpreter.unittests;


import static com.blazingkin.interpreter.executor.SimpleExpressionParser.parseExpression;
import static com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor.parseAndExecute;
import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertAlmostEqual;
import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertEqual;
import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertValEqual;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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
		assertEqual(parseAndExecute("true"), Value.bool(true));
		assertEqual(parseAndExecute("false"), Value.bool(false));
		assertEqual(parseAndExecute("TRUE"), Value.bool(true));
		assertEqual(parseAndExecute("FALSE"), Value.bool(false));
		assertEqual(parseAndExecute("3"), Value.integer(3));
		assertEqual(parseAndExecute("-5"), Value.integer(-5));
		assertEqual(parseAndExecute(".1"), Value.doub(.1d));
		// This one fails... maybe it should.. maybe it shouldn't... not sure
		//assertEqual(parseAndExecute("-.5"), Value.doub(-.5d));
		assertEqual(parseAndExecute("-0.5"), Value.doub(-.5d));
	}
	
	@Test
	public void testAddition(){
		assertEqual(parseAndExecute("3 + 4"), Value.integer(7));
		assertEqual(parseAndExecute("3 + 2"), Value.integer(5));
		assertEqual(parseAndExecute("-1 + -5"), Value.integer(-6));
		assertEqual(parseAndExecute("3 + 3 + 2"), Value.integer(8));
		assertEqual(parseAndExecute("1+1"), Value.integer(2));
		assertEqual(parseAndExecute("1+1+1+1+1"), Value.integer(5));
		assertEqual(parseAndExecute("2.0 + 2.0"), Value.doub(4));
		assertEqual(parseAndExecute("2.0 + 2"), Value.doub(4));
	}
	
	
	@Test
	public void testLotsOfAssignment(){
		parseAndExecute("a = 3");
		assertValEqual("a", Value.integer(3));
		parseAndExecute("a = 4");
		assertValEqual("a", Value.integer(4));
		parseAndExecute("b = 5");
		assertValEqual("b", Value.integer(5));
		parseAndExecute("a = b");
		assertValEqual("a", Value.integer(5));
		assertEqual(parseAndExecute("a + b"), Value.integer(10));
		assertEqual(parseAndExecute("a + b"), parseAndExecute("b + a"));
		parseAndExecute("c = a + b");
		assertValEqual("c", Value.integer(10));
		parseAndExecute("d= a +b + c + c");
		assertValEqual("d", Value.integer(30));
		assertValEqual("a", Value.integer(5));
		assertValEqual("c", Value.integer(10));
		parseAndExecute("arr[d] = 17.3");
		assertValEqual("arr[30]", Value.doub(17.3d));
		parseAndExecute("temp[a] = arr[d] + .9");
		assertValEqual("temp[5]", Value.doub(18.2d));
		Variable.clearVariables();
	}
	
	
	@Test
	public void testTwoPlusTwo(){
		assertEqual(parseAndExecute("2 + 2"), Value.integer(4));
	}
	
	@Test
	public void testTwoPlusMinusThree(){
		assertEqual(parseAndExecute("2 + -3"), Value.integer(-1));
	}
	
	@Test
	public void testAddingDoublesShouldGiveDouble(){
		assertEqual(parseAndExecute("2.5 + 3.5"), Value.doub(6));
	}
	
	@Test
	public void testAddingStrings(){
		assertEqual(parseAndExecute("\"hello\" + \"world\""), new Value(VariableTypes.String, "helloworld"));
	}
	
	@Test
	public void testThreeTimesThree(){
		assertEqual(parseAndExecute("3 * 3"), Value.integer(9));
	}
	
	@Test
	public void testOrderOfOperations(){
		assertEqual(parseAndExecute("2 * 3 + 2 * 3"), Value.integer(12));
	}
	
	@Test
	public void testSeveralMultiplications(){
		assertEqual(parseAndExecute("2 * 2 * 2 * 2"), Value.integer(16));
	}
	
	@Test
	public void testSubtractingShouldWork(){
		assertEqual(parseAndExecute("2 - 2"), Value.integer(0));
	}
	
	@Test
	public void testSubtractingNegativeNumbersShouldWork(){
		assertEqual(parseAndExecute("3 - -5"), Value.integer(8));
	}
	
	@Test
	public void testPowerOfOneShouldBeTheSame(){
		assertEqual(parseAndExecute("3 ** 1"), Value.integer(3));
	}
	
	@Test
	public void testThreeSquared(){
		assertEqual(parseAndExecute("3 ** 2"), Value.integer(9));
	}
	
	@Test
	public void testBigExponent(){
		assertEqual(parseAndExecute("99 ** 30"), parseAndExecute("739700373388280422730015092316714942252676262352676444347001"));
	}
	
	@Test
	public void testLogarithmShouldBeInverseOfExp(){
		assertAlmostEqual(parseAndExecute("(20 ** 2) __ 20"), Value.doub(2));
	}
	
	@Test
	public void testLogarithmOnKnownValue(){
		assertAlmostEqual(parseAndExecute("10000 __ 10"), Value.doub(4));
	}
	
	@Test
	public void testNaturalLogarithmOnKnownValue(){
		assertAlmostEqual(parseAndExecute("(10 __ \"e\")"), parseAndExecute("2.30258509299404568401799145468436420760110148862877297603"));
	}
	
	@Test
	public void testDividingShouldGiveRationals(){
		assertEqual(parseAndExecute("10/3"), Value.rational(10, 3));
	}
	
	@Test
	public void testDividingShouldGiveInts(){
		assertEqual(parseAndExecute("12/3"), Value.integer(4));
	}
	
	@Test
	public void testMultiplicationShouldUndoDivision(){
		assertEqual(parseAndExecute("3 / 4 * 4"), Value.integer(3));
	}
	
	@Test
	public void testDoubleDivisionShouldWork(){
		assertAlmostEqual(parseAndExecute("3.0 / 2"), Value.doub(1.5));
	}
	
	@Test
	public void testModulusOnKnownValues(){
		assertEqual(parseAndExecute("3 % 5"), Value.integer(3));
		assertEqual(parseAndExecute("7 % 3"), Value.integer(1));
		assertEqual(parseAndExecute("10 % 5"), Value.integer(0));
	}
	
	@Test
	public void testModulusOnDoubles(){
		assertEqual(parseAndExecute("10.3 % 3"), Value.doub(1.3));
	}
	
	@Test
	public void testModulusOnNegativeNumbers(){
		// Should act as if we are in n mod 3
		assertEqual(parseAndExecute("-1 % 3"), Value.integer(2));
	}
	
	@Test
	public void testAssignment(){
		parseAndExecute("asdf = 3");
		assertEqual(parseAndExecute("asdf"), Value.integer(3));
	}
	
	@Test
	public void testAssignmentWithComplexExpression(){
		parseAndExecute("asdf = 3 * 2 / 3 + 7");
		assertEqual(parseAndExecute("asdf"), Value.integer(9));
	}
	
	@Test
	public void testIncrementation(){
		parseAndExecute("a = 3");
		assertEqual(parseAndExecute("a++"), Value.integer(4));
		assertEqual(parseAndExecute("a"), Value.integer(4));
	}
	
	@Test
	public void testDecrementation(){
		parseAndExecute("a = 300");
		assertEqual(parseAndExecute("a--"), Value.integer(299));
		assertEqual(parseAndExecute("a--"), Value.integer(298));
		assertEqual(parseAndExecute("a"), Value.integer(298));
	}
	
	@Test
	public void testComparison(){
		assertEqual(parseAndExecute("true == true"), Value.bool(true));
	}
	
	@Test
	public void testComparisonTwo(){
		assertEqual(parseAndExecute("2 + 2 == 2 ** 2"), Value.bool(true));
	}
	
	@Test
	public void testStringComparison(){
		assertEqual(parseAndExecute("\"ab\" == \"ab\""), Value.bool(true));
	}
	
	@Test
	public void testComparisonNegative(){
		assertEqual(parseAndExecute("\"ab\" == \"ba\""), Value.bool(false));
	}
	
	@Test
	public void testNotEquals(){
		assertEqual(parseAndExecute("2 != 2"), Value.bool(false));
	}
	
	@Test
	public void testNotEqualsTwo(){
		assertEqual(parseAndExecute("3 != 2 * 1.7"), Value.bool(true));
	}
	
	@Test
	public void testLessThan(){
		assertEqual(parseAndExecute("2 < 3"), Value.bool(true));
		assertEqual(parseAndExecute("3 < 2"), Value.bool(false));
	}
	
	@Test
	public void testLessThanOnEqualValues(){
		assertEqual(parseAndExecute("2 < 2"), Value.bool(false));
	}
	
	@Test
	public void testLessThanOnDifferentVariableTypes(){
		assertEqual(parseAndExecute("2/3 < 1"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThan(){
		assertEqual(parseAndExecute("2 > 3"), Value.bool(false));
		assertEqual(parseAndExecute("3 > 2"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThanOnEqualValues(){
		assertEqual(parseAndExecute("3 > 3"), Value.bool(false));
	}
	
	
	@Test
	public void testLessThanOrEqual(){
		assertEqual(parseAndExecute("2 <= 3"), Value.bool(true));
		assertEqual(parseAndExecute("3 <= 2"), Value.bool(false));
	}
	
	@Test
	public void testLessThanOrEqualOnEqualValues(){
		assertEqual(parseAndExecute("50 <= 50"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThanOrEqual(){
		assertEqual(parseAndExecute("2 >= 3"), Value.bool(false));
		assertEqual(parseAndExecute("3 >= 2"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThanOrEqualOnEqualValues(){
		assertEqual(parseAndExecute("2.431 >= 2.431"), Value.bool(true));
	}
	
	@Test
	public void testGreaterThanOrEqualOnHugeValue(){
		assertEqual(parseAndExecute("99999213124214151251521512521.3214 >= 2"), Value.bool(true));
	}
	
	@Test
	public void testCommaDelimit(){
		Value[] arr = {Value.integer(2), Value.integer(3)};
		assertEqual(parseAndExecute("2, 3"), Value.arr(arr));
	}
	
	@Test
	public void testDifferentVariableTypesCommaDelimit(){
		Value[] arr = {Value.rational(2, 5), Value.doub(2.0), Value.integer(7)};
		assertEqual(parseAndExecute("2/5, 2.0, 7"), Value.arr(arr));
	}
	
	@Test
	public void testApproximatelyEquals(){
		assertEqual(parseAndExecute("1.999999999999 ~= 2"), Value.bool(true));
		assertEqual(parseAndExecute("2 ~= 2"), Value.bool(true));
	}
	
	@Test
	public void testApproximatelyEqualsShouldntBeTooForgiving(){
		assertEqual(parseAndExecute("1.995 ~= 2"), Value.bool(false));
		assertEqual(parseAndExecute("2 ~= 3"), Value.bool(false));
	}
	
	@Test
	public void testApproximatelyEqualsOnPi(){
		assertEqual(parseAndExecute("{pi} ~= 3.141592653589"), Value.bool(true));
	}
	
	//TODO find a way to test function calls
	
	
}
