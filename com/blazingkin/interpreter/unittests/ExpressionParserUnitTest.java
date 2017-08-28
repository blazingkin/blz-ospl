package com.blazingkin.interpreter.unittests;

import static com.blazingkin.interpreter.expressionabstraction.ExpressionParser.parseExpression;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.OperatorASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;

public class ExpressionParserUnitTest {

	@Test
	public void testTwoPlusTwo(){
		assertEquals(parseExpression("2 + 2"), new OperatorASTNode(Operator.Addition, new ValueASTNode("2"), new ValueASTNode("2")));
	}
	
	@Test
	public void testExponentiation(){
		assertEquals(parseExpression("2 ** 2"), new OperatorASTNode(Operator.Exponentiation, new ValueASTNode("2"), new ValueASTNode("2")));
	}
	
	@Test
	public void testWhitespaceShouldntMatter(){
		assertEquals(parseExpression("2+2"), parseExpression("   2  + 2  "));
		assertEquals(parseExpression(" 5 * 2 % 6 ** 7"), parseExpression("5*2%6**7"));
		assertEquals(parseExpression("2+\n(3*4)"), parseExpression("2 \t+   ( 3*4 )"));
	}
	
	@Test
	public void testThreeTimeThreeTimesThree(){
		ASTNode threetimesthree = parseExpression("3 * 3");
		assertEquals(parseExpression("3 * 3 * 3"), new OperatorASTNode(Operator.Multiplication, threetimesthree, new ValueASTNode("3")));
	}
	
	@Test
	public void testParens(){
		ASTNode twoplustwo = parseExpression("2 + 2");
		assertEquals(parseExpression("3 * (2 + 2)"), new OperatorASTNode(Operator.Multiplication, new ValueASTNode("3"), twoplustwo));
	}
	
	@Test
	public void testOrderOfOperation(){
		ASTNode threetimestwo = parseExpression("3 * 2");
		assertEquals(parseExpression("3 * 2 + 3 * 2"), new OperatorASTNode(Operator.Addition, threetimestwo, threetimestwo));
	}
	
	@Test
	public void testFunctionCall(){
		assertEquals(parseExpression("blah(3)"), new OperatorASTNode(Operator.functionCall, new ValueASTNode("blah"), new ValueASTNode("3")));
	}
	
	@Test
	public void testFunctionCallWithNoArgs(){
		ASTNode[] arg = {new ValueASTNode("blah")};
		assertEquals(parseExpression("blah()"), new OperatorASTNode(Operator.functionCall, arg));
	}
	
	@Test
	public void testEmbeddedFunctionCalls(){
		ASTNode[] arg = {new ValueASTNode("fasd")};
		assertEquals(parseExpression("blah(asdf(fasd()))"), new OperatorASTNode(Operator.functionCall, new ValueASTNode("blah"), 
				new OperatorASTNode(Operator.functionCall, new ValueASTNode("asdf"), new OperatorASTNode(Operator.functionCall, arg))));
	}
	
	@Test
	public void testFunctionWithOtherOps(){
		ASTNode twoplustwo = parseExpression("2 + 2");
		assertEquals(parseExpression("2 + 2 + blah(2 + 2)"), new OperatorASTNode(Operator.Addition, twoplustwo,
				new OperatorASTNode(Operator.functionCall, new ValueASTNode("blah"), twoplustwo)));
	}
	
	@Test
	public void testCommasInFunctionParamsShouldWork(){
		ASTNode threetimesthree = parseExpression("3 * 3");
		assertEquals(parseExpression("funct(3 * 3,3*3)"),
				new OperatorASTNode(Operator.functionCall, new ValueASTNode("funct"),
						new OperatorASTNode(Operator.CommaDelimit, threetimesthree, threetimesthree)));
	}
	
	@Test
	public void testLongStatement(){
		ASTNode left = parseExpression("a , b");
		ASTNode threetimesthree = parseExpression("3 * 3");
		ASTNode right = new OperatorASTNode(Operator.CommaDelimit, 
				new OperatorASTNode(Operator.functionCall, new ValueASTNode("funct"), threetimesthree),
				threetimesthree);
		assertEquals(parseExpression("a, b = funct(3 * 3), 3 * 3"), new OperatorASTNode(Operator.Assignment,
				left, right));
	}
	
	@Test
	public void testNegativeNumbersShouldWork(){
		assertEquals(parseExpression("-3 + 4"), new OperatorASTNode(Operator.Addition, new ValueASTNode("-3"), new ValueASTNode("4")));
	}
	
	@Test
	public void testSubtractingNegativeNumberShouldWork(){
		assertEquals(parseExpression("3 - -2"), new OperatorASTNode(Operator.Subtraction, new ValueASTNode("3"), new ValueASTNode("-2")));
	}
	
	@Test
	public void testArrays(){
		assertEquals(parseExpression("arr[3]"), new OperatorASTNode(Operator.arrayLookup, new ValueASTNode("arr"), new ValueASTNode("3")));
	}
	
	@Test
	public void testOperationInArrayAccessors(){
		ASTNode twoplustwo = parseExpression("2 + 2");
		assertEquals(parseExpression("arr[2 + 2]"), new OperatorASTNode(Operator.arrayLookup, new ValueASTNode("arr"), twoplustwo));
	}
	
	@Test
	public void testPassingArrayLiteralInArgs(){
		ASTNode arrLiteral = parseExpression("[2, 3, 4]");
		assertEquals(parseExpression("func([2,3,4])"), new OperatorASTNode(Operator.functionCall, new ValueASTNode("func"), arrLiteral));
	}
	
	@Test
	public void testArrayLiteral(){
		ASTNode[] arg = {new OperatorASTNode(Operator.CommaDelimit, new ValueASTNode("1"), new ValueASTNode("2"))};
		assertEquals(parseExpression("[1,2]"), new OperatorASTNode(Operator.arrayLiteral, arg));
	}
	

	@Test
	public void testTwoDimensionalArrays(){
		assertEquals(parseExpression("arr[2][2]"), new OperatorASTNode(Operator.arrayLookup, new OperatorASTNode(Operator.arrayLookup, new ValueASTNode("arr"), new ValueASTNode("2")), new ValueASTNode("2")));
	}
	
	@Test
	public void testArrayAccessorExpressionShouldWork(){
		ASTNode left = parseExpression("temp[a]");
		ASTNode arrd = parseExpression("arr[d]");
		ASTNode right = new OperatorASTNode(Operator.Addition, arrd, new ValueASTNode("0.9"));
		assertEquals(parseExpression("temp[a] = arr[d] + 0.9"), new OperatorASTNode(Operator.Assignment, left, right));
	}
	
	@Test
	public void testWhiteSpaceIssueWithSubtraction(){
		assertEquals(parseExpression("a-2"), parseExpression("a - 2"));
	}
	
	/*
	 * TODO fix failing test
	@Test
	public void testSubtractionInArrayAccessors(){
		ASTNode inner = parseExpression("a - 2");
		assertEquals(parseExpression("arr[a-2]"), new ASTNode(Operator.arrayLookup, new ASTNode("arr"), inner));
	}
	*/
	
	@Test
	public void testVariableNamesWithUnderscoresShouldWork(){
		assertEquals(parseExpression("arr_thing + 2"), new OperatorASTNode(Operator.Addition, new ValueASTNode("arr_thing"), new ValueASTNode("2")));
	}
	
	@Test
	public void testIssueWithWhitespaceFailureCase(){
		// Probably shouldn't work like this?
		// I'll allow it... I guess?
		assertEquals(parseExpression("2 2 +"), new OperatorASTNode(Operator.Addition, new ValueASTNode("2"), new ValueASTNode("2")));
	}
	
	@Test
	public void testPossibleTwoDimensionalArrayFailureCase(){
		// This probably should not work like this....
		// I could throw some kind of syntax error?
		assertEquals(parseExpression("arr[2]2[]"), new OperatorASTNode(Operator.arrayLookup, new OperatorASTNode(Operator.arrayLookup, new ValueASTNode("arr"), new ValueASTNode("2")), new ValueASTNode("2")));
	}
	
	@Test
	public void testDotOperator(){
		assertEquals(parseExpression("a.b"), new OperatorASTNode(Operator.DotOperator, new ValueASTNode("a"), new ValueASTNode("b")));
	}
	
	@Test
	public void testDotOperatorShouldNotMixWithDoubles(){
		assertEquals(parseExpression("123.4"), new ValueASTNode("123.4"));
	}
	
	@Test
	public void testAssignmentOnDotOperator(){
		assertEquals(parseExpression("a.b = 2"), new OperatorASTNode(Operator.Assignment, new OperatorASTNode(Operator.DotOperator, new ValueASTNode("a"), new ValueASTNode("b")), new ValueASTNode("2")));
	}
	
	@Test
	public void testChainedDotOperators(){
		assertEquals(parseExpression("a.b.c"), new OperatorASTNode(Operator.DotOperator, new OperatorASTNode(Operator.DotOperator, new ValueASTNode("a"), new ValueASTNode("b")), new ValueASTNode("c")));
	}
	
	@Test
	public void testFunctionCallOnDotOperator(){
		assertEquals(parseExpression("a.b()"), new OperatorASTNode(Operator.functionCall, new OperatorASTNode(Operator.DotOperator, new ValueASTNode("a"), new ValueASTNode("b"))));
	}
	
	@Test
	public void testArrayAccessorOnFunctionCall(){
		ASTNode callFunc = parseExpression("a(2)");
		assertEquals(parseExpression("a(2)[3]"), new OperatorASTNode(Operator.arrayLookup, callFunc, new ValueASTNode("3")));
	}
	
	@Test
	public void testArrayAccessorOnDotOperatorAndFunctionCall(){
		ASTNode dotAndCall = parseExpression("a.b(2)");
		assertEquals(parseExpression("a.b(2)[3]"), new OperatorASTNode(Operator.arrayLookup, dotAndCall, new ValueASTNode("3")));
	}
	
	@Test
	public void testSpaceInString(){
		assertEquals(parseExpression("\"Hello there\""), new ValueASTNode("\"Hello there\""));
	}
	
	@Test
	public void testSpaceInStringInFunctionCall(){
		assertEquals(parseExpression("print(\"Hello World!\")"), new OperatorASTNode(Operator.functionCall, new ValueASTNode("print"), new ValueASTNode("\"Hello World!\"")));
	}
	
	@Test
	public void testEscapedCharactersInStrings(){
		assertEquals(parseExpression("\"Test \\\"\""), new ValueASTNode("\"Test \"\""));
	}
	

}
