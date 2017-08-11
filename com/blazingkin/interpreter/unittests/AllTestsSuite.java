package com.blazingkin.interpreter.unittests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	ExecutorUnitTest.class,
	ExpressionExecutorUnitTest.class,
	ExpressionParserUnitTest.class,
	LambdaUnitTest.class,
	MathOpsUnitTest.class,
	ProcessUnitTest.class,
	VariableUnitTest.class
})
public class AllTestsSuite {

}
