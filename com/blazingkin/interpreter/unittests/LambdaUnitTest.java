package com.blazingkin.interpreter.unittests;

import static com.blazingkin.interpreter.executor.lambda.LambdaParser.isLambdaExpression;
import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertEqual;

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
	
	
}
