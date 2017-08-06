package com.blazingkin.interpreter.unittests;

import static com.blazingkin.interpreter.expressionabstraction.ExpressionParser.parseExpression;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;

public class ExpressionParserUnitTest {

	@Test
	public void testTwoPlusTwo(){
		assertEquals(parseExpression("2 + 2"), new ASTNode(Operator.Addition, new ASTNode("2"), new ASTNode("2")));
	}
	
	@Test
	public void testExponentiation(){
		assertEquals(parseExpression("2 ** 2"), new ASTNode(Operator.Exponentiation, new ASTNode("2"), new ASTNode("2")));
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
		assertEquals(parseExpression("3 * 3 * 3"), new ASTNode(Operator.Multiplication, threetimesthree, new ASTNode("3")));
	}
	
	@Test
	public void testParens(){
		ASTNode twoplustwo = parseExpression("2 + 2");
		assertEquals(parseExpression("3 * (2 + 2)"), new ASTNode(Operator.Multiplication, new ASTNode("3"), twoplustwo));
	}
	
	@Test
	public void testOrderOfOperation(){
		ASTNode threetimestwo = parseExpression("3 * 2");
		assertEquals(parseExpression("3 * 2 + 3 * 2"), new ASTNode(Operator.Addition, threetimestwo, threetimestwo));
	}
	
	@Test
	public void testFunctionCall(){
		assertEquals(parseExpression("blah(3)"), new ASTNode(Operator.functionCall, new ASTNode("blah"), new ASTNode("3")));
	}
	
	@Test
	public void testFunctionCallWithNoArgs(){
		ASTNode[] arg = {new ASTNode("blah")};
		assertEquals(parseExpression("blah()"), new ASTNode(Operator.functionCall, arg));
	}
	
	@Test
	public void testEmbeddedFunctionCalls(){
		ASTNode[] arg = {new ASTNode("fasd")};
		assertEquals(parseExpression("blah(asdf(fasd()))"), new ASTNode(Operator.functionCall, new ASTNode("blah"), 
				new ASTNode(Operator.functionCall, new ASTNode("asdf"), new ASTNode(Operator.functionCall, arg))));
	}
	
	@Test
	public void testFunctionWithOtherOps(){
		ASTNode twoplustwo = parseExpression("2 + 2");
		assertEquals(parseExpression("2 + 2 + blah(2 + 2)"), new ASTNode(Operator.Addition, twoplustwo,
				new ASTNode(Operator.functionCall, new ASTNode("blah"), twoplustwo)));
	}
	
	@Test
	public void testCommasInFunctionParamsShouldWork(){
		ASTNode threetimesthree = parseExpression("3 * 3");
		assertEquals(parseExpression("funct(3 * 3,3*3)"),
				new ASTNode(Operator.functionCall, new ASTNode("funct"),
						new ASTNode(Operator.CommaDelimit, threetimesthree, threetimesthree)));
	}
	
	@Test
	public void testLongStatement(){
		ASTNode left = parseExpression("a , b");
		ASTNode threetimesthree = parseExpression("3 * 3");
		ASTNode right = new ASTNode(Operator.CommaDelimit, 
				new ASTNode(Operator.functionCall, new ASTNode("funct"), threetimesthree),
				threetimesthree);
		assertEquals(parseExpression("a, b = funct(3 * 3), 3 * 3"), new ASTNode(Operator.Assignment,
				left, right));
	}
	
	@Test
	public void testNegativeNumbersShouldWork(){
		assertEquals(parseExpression("-3 + 4"), new ASTNode(Operator.Addition, new ASTNode("-3"), new ASTNode("4")));
	}
	
	@Test
	public void testArrays(){
		assertEquals(parseExpression("arr[3]"), new ASTNode(Operator.arrayLookup, new ASTNode("arr"), new ASTNode("3")));
	}
	
	@Test
	public void testOperationInArrayAccessors(){
		ASTNode twoplustwo = parseExpression("2 + 2");
		assertEquals(parseExpression("arr[2 + 2]"), new ASTNode(Operator.arrayLookup, new ASTNode("arr"), twoplustwo));
	}
	
	@Test
	public void testPassingArrayLiteralInArgs(){
		ASTNode arrLiteral = parseExpression("[2, 3, 4]");
		assertEquals(parseExpression("func([2,3,4])"), new ASTNode(Operator.functionCall, new ASTNode("func"), arrLiteral));
	}
	
	@Test
	public void testArrayLiteral(){
		ASTNode[] arg = {new ASTNode(Operator.CommaDelimit, new ASTNode("1"), new ASTNode("2"))};
		assertEquals(parseExpression("[1,2]"), new ASTNode(Operator.arrayLiteral, arg));
	}
	

	@Test
	public void testTwoDimensionalArrays(){
		assertEquals(parseExpression("arr[2][2]"), new ASTNode(Operator.arrayLookup, new ASTNode(Operator.arrayLookup, new ASTNode("arr"), new ASTNode("2")), new ASTNode("2")));
	}
	
	@Test
	public void testArrayAccessorExpressionShouldWork(){
		ASTNode left = parseExpression("temp[a]");
		ASTNode arrd = parseExpression("arr[d]");
		ASTNode right = new ASTNode(Operator.Addition, arrd, new ASTNode(".9"));
		assertEquals(parseExpression("temp[a] = arr[d] + .9"), new ASTNode(Operator.Assignment, left, right));
	}
	
	@Test
	public void testIssueWithWhitespace(){
		// Probably shouldn't work like this?
		// I'll allow it... I guess?
		assertEquals(parseExpression("2 2 +"), new ASTNode(Operator.Addition, new ASTNode("2"), new ASTNode("2")));
	}
	
	@Test
	public void testPossibleTwoDimensionalArrayFailureCase(){
		// This probably should not work like this....
		// I could throw some kind of syntax error?
		assertEquals(parseExpression("arr[2]2[]"), new ASTNode(Operator.arrayLookup, new ASTNode(Operator.arrayLookup, new ASTNode("arr"), new ASTNode("2")), new ASTNode("2")));
	}

}
