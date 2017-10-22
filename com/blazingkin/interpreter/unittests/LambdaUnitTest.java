package com.blazingkin.interpreter.unittests;

import static com.blazingkin.interpreter.executor.lambda.LambdaParser.isLambdaExpression;
import static com.blazingkin.interpreter.executor.lambda.LambdaParser.parseLambdaExpression;
import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertEqual;
import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertEqualArrays;

import org.junit.Test;

public class LambdaUnitTest {

	@Test
	public void testIsLambdaExpression(){
		assertEqual(isLambdaExpression("FAKE"), false);
		assertEqual(isLambdaExpression("ECHO"), true);
		assertEqual(isLambdaExpression("SECHO"), true);
		assertEqual(isLambdaExpression("DEFINE"), true);
		assertEqual(isLambdaExpression("LAMBDA"), true);
		assertEqual(isLambdaExpression("PUSH"), true);
		assertEqual(isLambdaExpression("POP"), true);
		assertEqual(isLambdaExpression("NIN"), true);
		assertEqual(isLambdaExpression("WHILE"), false);
		assertEqual(isLambdaExpression("FOR"), false);
		assertEqual(isLambdaExpression("ENDLOOP"), false);
		assertEqual(isLambdaExpression("END"), false);
		assertEqual(isLambdaExpression("CHANGEPROCESS"), false);
	}
	
	
	@Test
	public void testParseLambdaExpression(){
	//	assertEqual(parseLambdaExpression("FJASf 3 1").getFunction(), Instruction.INVALID.executor);
	//	TODO make the above test work with an INVALID executor that just throws an error and prints an error message
		String[] args = {"3", "3"};
		assertEqualArrays(parseLambdaExpression("ADD 3 3").getArgs(), args);
		args[0] = "x";
		args[1] = "y";
		assertEqualArrays(parseLambdaExpression("MUL x y").getArgs(), args);
		args[0] = "(ADD 3 3)";
		assertEqualArrays(parseLambdaExpression("DIV (ADD 3 3) y").getArgs(), args);
	}
	
	
}
