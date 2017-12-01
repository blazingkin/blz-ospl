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
		assertEquals(parseExpression("2 + 2"), OperatorASTNode.newNode(Operator.Addition, new ValueASTNode("2"), new ValueASTNode("2")));
	}
	
	@Test
	public void testExponentiation(){
		assertEquals(parseExpression("2 ** 2"), OperatorASTNode.newNode(Operator.Exponentiation, new ValueASTNode("2"), new ValueASTNode("2")));
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
		assertEquals(parseExpression("3 * 3 * 3"), OperatorASTNode.newNode(Operator.Multiplication, threetimesthree, new ValueASTNode("3")));
	}
	
	@Test
	public void testParens(){
		ASTNode twoplustwo = parseExpression("2 + 2");
		assertEquals(parseExpression("3 * (2 + 2)"), OperatorASTNode.newNode(Operator.Multiplication, new ValueASTNode("3"), twoplustwo));
	}
	
	@Test
	public void testOrderOfOperation(){
		ASTNode threetimestwo = parseExpression("3 * 2");
		assertEquals(parseExpression("3 * 2 + 3 * 2"), OperatorASTNode.newNode(Operator.Addition, threetimestwo, threetimestwo));
	}
	
	@Test
	public void testFunctionCall(){
		assertEquals(parseExpression("blah(3)"), OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("blah"), new ValueASTNode("3")));
	}
	
	@Test
	public void testFunctionCallWithNoArgs(){
		ASTNode[] arg = {new ValueASTNode("blah")};
		assertEquals(parseExpression("blah()"), OperatorASTNode.newNode(Operator.functionCall, arg));
	}
	
	@Test
	public void testEmbeddedFunctionCalls(){
		ASTNode[] arg = {new ValueASTNode("fasd")};
		assertEquals(parseExpression("blah(asdf(fasd()))"), OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("blah"), 
				OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("asdf"), OperatorASTNode.newNode(Operator.functionCall, arg))));
	}
	
	@Test
	public void testFunctionWithOtherOps(){
		ASTNode twoplustwo = parseExpression("2 + 2");
		assertEquals(parseExpression("2 + 2 + blah(2 + 2)"), OperatorASTNode.newNode(Operator.Addition, twoplustwo,
				OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("blah"), twoplustwo)));
	}
	
	@Test
	public void testCommasInFunctionParamsShouldWork(){
		ASTNode threetimesthree = parseExpression("3 * 3");
		assertEquals(parseExpression("funct(3 * 3,3*3)"),
				OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("funct"),
						OperatorASTNode.newNode(Operator.CommaDelimit, threetimesthree, threetimesthree)));
	}
	
	@Test
	public void testLongStatement(){
		ASTNode left = parseExpression("a , b");
		ASTNode threetimesthree = parseExpression("3 * 3");
		ASTNode right = OperatorASTNode.newNode(Operator.CommaDelimit, 
				OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("funct"), threetimesthree),
				threetimesthree);
		assertEquals(parseExpression("a, b = funct(3 * 3), 3 * 3"), OperatorASTNode.newNode(Operator.Assignment,
				left, right));
	}
	
	@Test
	public void testNegativeNumbersShouldWork(){
		assertEquals(parseExpression("-3 + 4"), OperatorASTNode.newNode(Operator.Addition, new ValueASTNode("-3"), new ValueASTNode("4")));
	}
	
	@Test
	public void testSubtractingNegativeNumberShouldWork(){
		assertEquals(parseExpression("3 - -2"), OperatorASTNode.newNode(Operator.Subtraction, new ValueASTNode("3"), new ValueASTNode("-2")));
	}
	
	@Test
	public void testArrays(){
		assertEquals(parseExpression("arr[3]"), OperatorASTNode.newNode(Operator.arrayLookup, new ValueASTNode("arr"), new ValueASTNode("3")));
	}
	
	@Test
	public void testOperationInArrayAccessors(){
		ASTNode twoplustwo = parseExpression("2 + 2");
		assertEquals(parseExpression("arr[2 + 2]"), OperatorASTNode.newNode(Operator.arrayLookup, new ValueASTNode("arr"), twoplustwo));
	}
	
	@Test
	public void testPassingArrayLiteralInArgs(){
		ASTNode arrLiteral = parseExpression("[2, 3, 4]");
		assertEquals(parseExpression("func([2,3,4])"), OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("func"), arrLiteral));
	}
	
	@Test
	public void testPassingTwoVariablesOneIsArray(){
		ASTNode arrLiteral = parseExpression("[2,3,4]");
		ASTNode commaDelimits = OperatorASTNode.newNode(Operator.CommaDelimit, new ValueASTNode("1"), arrLiteral);
		assertEquals(parseExpression("func(1, [2,3,4])"), OperatorASTNode.newNode(Operator.functionCall, 
				new ValueASTNode("func"), commaDelimits));
	}
	
	@Test
	public void testArrayLiteral(){
		ASTNode[] arg = {OperatorASTNode.newNode(Operator.CommaDelimit, new ValueASTNode("1"), new ValueASTNode("2"))};
		assertEquals(parseExpression("[1,2]"), OperatorASTNode.newNode(Operator.arrayLiteral, arg));
	}
	

	@Test
	public void testTwoDimensionalArrays(){
		assertEquals(parseExpression("arr[2][2]"), OperatorASTNode.newNode(Operator.arrayLookup, OperatorASTNode.newNode(Operator.arrayLookup, new ValueASTNode("arr"), new ValueASTNode("2")), new ValueASTNode("2")));
	}
	
	@Test
	public void testArrayAccessorExpressionShouldWork(){
		ASTNode left = parseExpression("temp[a]");
		ASTNode arrd = parseExpression("arr[d]");
		ASTNode right = OperatorASTNode.newNode(Operator.Addition, arrd, new ValueASTNode("0.9"));
		assertEquals(parseExpression("temp[a] = arr[d] + 0.9"), OperatorASTNode.newNode(Operator.Assignment, left, right));
	}
	
	@Test
	public void testWhiteSpaceIssueWithSubtraction(){
		assertEquals(parseExpression("a-2"), parseExpression("a - 2"));
	}
	
	@Test
	public void testSubtractionInArrayAccessors(){
		ASTNode inner = parseExpression("a - 2");
		assertEquals(parseExpression("arr[a - 2]"), OperatorASTNode.newNode(Operator.arrayLookup, new ValueASTNode("arr"), inner));
	}
	
	@Test
	public void testVariableNamesWithUnderscoresShouldWork(){
		assertEquals(parseExpression("arr_thing + 2"), OperatorASTNode.newNode(Operator.Addition, new ValueASTNode("arr_thing"), new ValueASTNode("2")));
	}
	
	@Test
	public void testIssueWithWhitespaceFailureCase(){
		// Probably shouldn't work like this?
		// I'll allow it... I guess?
		assertEquals(parseExpression("2 2 +"), OperatorASTNode.newNode(Operator.Addition, new ValueASTNode("2"), new ValueASTNode("2")));
	}
	
	@Test
	public void testPossibleTwoDimensionalArrayFailureCase(){
		// This probably should not work like this....
		// I could throw some kind of syntax error?
		assertEquals(parseExpression("arr[2]2[]"), OperatorASTNode.newNode(Operator.arrayLookup, OperatorASTNode.newNode(Operator.arrayLookup, new ValueASTNode("arr"), new ValueASTNode("2")), new ValueASTNode("2")));
	}
	
	@Test
	public void testDotOperator(){
		assertEquals(parseExpression("a.b"), OperatorASTNode.newNode(Operator.DotOperator, new ValueASTNode("a"), new ValueASTNode("b")));
	}
	
	@Test
	public void testDotOperatorShouldNotMixWithDoubles(){
		assertEquals(parseExpression("123.4"), new ValueASTNode("123.4"));
	}
	
	@Test
	public void testAssignmentOnDotOperator(){
		assertEquals(parseExpression("a.b = 2"), OperatorASTNode.newNode(Operator.Assignment, OperatorASTNode.newNode(Operator.DotOperator, new ValueASTNode("a"), new ValueASTNode("b")), new ValueASTNode("2")));
	}
	
	@Test
	public void testChainedDotOperators(){
		assertEquals(parseExpression("a.b.c"), OperatorASTNode.newNode(Operator.DotOperator, OperatorASTNode.newNode(Operator.DotOperator, new ValueASTNode("a"), new ValueASTNode("b")), new ValueASTNode("c")));
	}
	
	@Test
	public void testFunctionCallOnDotOperator(){
		assertEquals(parseExpression("a.b()"), OperatorASTNode.newNode(Operator.functionCall, OperatorASTNode.newNode(Operator.DotOperator, new ValueASTNode("a"), new ValueASTNode("b"))));
	}
	
	@Test
	public void testArrayAccessorOnFunctionCall(){
		ASTNode callFunc = parseExpression("a(2)");
		assertEquals(parseExpression("a(2)[3]"), OperatorASTNode.newNode(Operator.arrayLookup, callFunc, new ValueASTNode("3")));
	}
	
	@Test
	public void testArrayAccessorOnDotOperatorAndFunctionCall(){
		ASTNode dotAndCall = parseExpression("a.b(2)");
		assertEquals(parseExpression("a.b(2)[3]"), OperatorASTNode.newNode(Operator.arrayLookup, dotAndCall, new ValueASTNode("3")));
	}
	
	@Test
	public void testSpaceInString(){
		assertEquals(parseExpression("\"Hello there\""), new ValueASTNode("\"Hello there\""));
	}
	
	@Test
	public void testSpaceInStringInFunctionCall(){
		assertEquals(parseExpression("print(\"Hello World!\")"), OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("print"), new ValueASTNode("\"Hello World!\"")));
	}
	
	@Test
	public void testEscapedCharactersInStrings(){
		assertEquals(parseExpression("\"Test \\\"\""), new ValueASTNode("\"Test \"\""));
	}
	
	@Test
	public void testOperandCharactersInStrings(){
		assertEquals(parseExpression("\"Test-\""), new ValueASTNode("\"Test-\""));
	}
	

}
