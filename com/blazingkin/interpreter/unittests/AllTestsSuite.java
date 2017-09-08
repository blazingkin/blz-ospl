package com.blazingkin.interpreter.unittests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.blazingkin.interpreter.unittests.astnodes.AdditionNodeUnitTest;

// This is not scalable.... TODO Find a better way
@RunWith(Suite.class)
@Suite.SuiteClasses({
	AdditionNodeUnitTest.class,
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
