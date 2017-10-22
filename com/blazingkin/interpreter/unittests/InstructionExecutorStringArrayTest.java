package com.blazingkin.interpreter.unittests;

import static com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray.parseExpressions;

import org.junit.Test;
public class InstructionExecutorStringArrayTest {

	@Test
	public void testParseExpressions(){
		String[] p1 = parseExpressions("THIS (is an (expression \"that\" takes) arguments)");
		String[] p1Check = {"THIS", "(is an (expression \"that\" takes) arguments)"};
		UnitTestUtil.assertEqualArrays(p1, p1Check);
		String[] p2 = parseExpressions("test \"dafdf adsf\" dafds (dfsadf fdsf \"fdsf fsdaf\")");
		String[] p2Check = {"test", "\"dafdf adsf\"", "dafds", "(dfsadf fdsf \"fdsf fsdaf\")"};
		UnitTestUtil.assertEqualArrays(p2, p2Check);
		String[] p3 = parseExpressions("this is a test that should be completely split");
		String[] p3Check = {"this", "is", "a", "test", "that", "should", "be", "completely", "split"};
		UnitTestUtil.assertEqualArrays(p3, p3Check);
	}

}
