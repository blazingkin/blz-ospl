package com.blazingkin.interpreter.unittests;

import static com.blazingkin.interpreter.parser.ExpressionParser.parseExpression;
import static com.blazingkin.interpreter.parser.LineLexer.lexLine;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.OperatorASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.parser.Token;
import com.blazingkin.interpreter.variables.Value;

import org.junit.Test;

public class ExpressionParserUnitTest {

	@Test
	public void testTwo() throws SyntaxException {
		ArrayList<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token(Operator.Number, "2"));
		assertEquals(parseExpression(tokens), new ValueASTNode(Value.integer(2)));
	}

	@Test
	public void testTwoPlusTwo() throws SyntaxException {
		ArrayList<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token(Operator.Number, "2"));
		tokens.add(new Token(Operator.Addition));
		tokens.add(new Token(Operator.Number, "2"));
		assertEquals(parseExpression(tokens), OperatorASTNode.newNode(Operator.Addition, new ValueASTNode("2"), new ValueASTNode("2")));
	}
	
	@Test
	public void testExponentiation() throws SyntaxException {
		ArrayList<Token> tokens = lexLine("2 ** 2");
		assertEquals(parseExpression(tokens), OperatorASTNode.newNode(Operator.Exponentiation, new ValueASTNode("2"), new ValueASTNode("2")));
	}
	
	@Test
	public void testWhitespaceShouldntMatter() throws SyntaxException{
		assertEquals(parseExpression(lexLine("2+2")), parseExpression(lexLine("   2  + 2  ")));
		assertEquals(parseExpression(lexLine(" 5 * 2 % 6 ** 7")), parseExpression(lexLine("5*2%6**7")));
		assertEquals(parseExpression(lexLine("2+\n(3*4)")), parseExpression(lexLine("2 \t+   ( 3*4 )")));
	}
	
	@Test
	public void testThreeTimeThreeTimesThree() throws SyntaxException{
		ASTNode threetimesthree = parseExpression(lexLine("3 * 3"));
		assertEquals(parseExpression(lexLine("3 * 3 * 3")), OperatorASTNode.newNode(Operator.Multiplication, threetimesthree, new ValueASTNode("3")));
	}
	
	@Test
	public void testParens() throws SyntaxException {
		ArrayList<Token> full = lexLine("3 * (2 + 2)");
		ArrayList<Token> inner = lexLine("2 + 2");
		ASTNode twoplustwo = parseExpression(inner);
		assertEquals(parseExpression(full), OperatorASTNode.newNode(Operator.Multiplication, new ValueASTNode("3"), twoplustwo));
	}

	@Test
	public void testNestedParens() throws SyntaxException {
		ArrayList<Token> full = lexLine("3 * (2 + (2 + 2))");
		ArrayList<Token> inner = lexLine("2 + 2");
		ASTNode twoplustwo = parseExpression(inner);
		ASTNode parenNode = OperatorASTNode.newNode(Operator.Addition, new ValueASTNode("2"), twoplustwo);
		assertEquals(parseExpression(full), OperatorASTNode.newNode(Operator.Multiplication, new ValueASTNode("3"), parenNode));

	}
	
	@Test
	public void testOrderOfOperation() throws SyntaxException{
		ArrayList<Token> half = lexLine("3 * 2");
		ArrayList<Token> full = lexLine("3 * 2 + 3 * 2");
		ASTNode threetimestwo = parseExpression(half);
		assertEquals(parseExpression(full), OperatorASTNode.newNode(Operator.Addition, threetimestwo, threetimestwo));
	}
	
	@Test
	public void testFunctionCall() throws SyntaxException{
		ArrayList<Token> tokens = lexLine("blah(3)");
		assertEquals(parseExpression(tokens), OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("blah"), new ValueASTNode("3")));
	}
	
	@Test
	public void testFunctionCallWithNoArgs() throws SyntaxException{
		ArrayList<Token> tokens = lexLine("blah()");
		ASTNode[] arg = {new ValueASTNode("blah")};
		assertEquals(parseExpression(tokens), OperatorASTNode.newNode(Operator.functionCall, arg));
	}
	
	@Test
	public void testEmbeddedFunctionCalls() throws SyntaxException{
		ASTNode[] arg = {new ValueASTNode("fasd")};
		assertEquals(parseExpression(lexLine("blah(asdf(fasd()))")), OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("blah"), 
				OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("asdf"), OperatorASTNode.newNode(Operator.functionCall, arg))));
	}
	
	@Test
	public void testFunctionWithOtherOps() throws SyntaxException{
		ArrayList<Token> sht = lexLine("2 + 2");
		ArrayList<Token> lng = lexLine("2 + 2 + blah(2 + 2)");
		ASTNode twoplustwo = parseExpression(sht);
		assertEquals(parseExpression(lng), OperatorASTNode.newNode(Operator.Addition, twoplustwo,
				OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("blah"), twoplustwo)));
	}
	
	@Test
	public void testCommasInFunctionParamsShouldWork() throws SyntaxException{
		ASTNode threetimesthree = parseExpression(lexLine("3 * 3"));
		assertEquals(parseExpression(lexLine("funct(3 * 3,3*3)")),
				OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("funct"),
						OperatorASTNode.newNode(Operator.CommaDelimit, threetimesthree, threetimesthree)));
	}
	
	@Test
	public void testLongStatement() throws SyntaxException{
		ASTNode left = parseExpression(lexLine("a , b"));
		ASTNode threetimesthree = parseExpression(lexLine("3 * 3"));
		ASTNode right = OperatorASTNode.newNode(Operator.CommaDelimit, 
				OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("funct"), threetimesthree),
				threetimesthree);
		assertEquals(parseExpression(lexLine("a, b = funct(3 * 3), 3 * 3")), OperatorASTNode.newNode(Operator.Assignment,
				left, right));
	}
	
	@Test
	public void testNegativeNumbersShouldWork() throws SyntaxException{
		assertEquals(parseExpression(lexLine("-3 + 4")), OperatorASTNode.newNode(Operator.Addition, new ValueASTNode("-3"), new ValueASTNode("4")));
	}
	
	@Test
	public void testSubtractingNegativeNumberShouldWork() throws SyntaxException{
		assertEquals(parseExpression(lexLine("3 - -2")), OperatorASTNode.newNode(Operator.Subtraction, new ValueASTNode("3"), new ValueASTNode("-2")));
	}
	
	@Test
	public void testArrays() throws SyntaxException{
		assertEquals(parseExpression(lexLine("arr[3]")), OperatorASTNode.newNode(Operator.arrayLookup, new ValueASTNode("arr"), new ValueASTNode("3")));
	}
	
	@Test
	public void testOperationInArrayAccessors() throws SyntaxException{
		ASTNode twoplustwo = parseExpression(lexLine("2 + 2"));
		assertEquals(parseExpression(lexLine("arr[2 + 2]")), OperatorASTNode.newNode(Operator.arrayLookup, new ValueASTNode("arr"), twoplustwo));
	}
	
	@Test
	public void testPassingArrayLiteralInArgs() throws SyntaxException {
		ASTNode arrLiteral = parseExpression(lexLine("[2, 3, 4]"));
		assertEquals(parseExpression(lexLine("func([2,3,4])")), OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("func"), arrLiteral));
	}
	
	@Test
	public void testPassingTwoVariablesOneIsArray() throws SyntaxException {
		ASTNode arrLiteral = parseExpression(lexLine("[2,3,4]"));
		ASTNode commaDelimits = OperatorASTNode.newNode(Operator.CommaDelimit, new ValueASTNode("1"), arrLiteral);
		assertEquals(parseExpression(lexLine("func(1, [2,3,4])")), OperatorASTNode.newNode(Operator.functionCall, 
				new ValueASTNode("func"), commaDelimits));
	}
	
	@Test
	public void testArrayLiteral() throws SyntaxException {
		ASTNode[] arg = {OperatorASTNode.newNode(Operator.CommaDelimit, new ValueASTNode("1"), new ValueASTNode("2"))};
		assertEquals(parseExpression(lexLine("[1,2]")), OperatorASTNode.newNode(Operator.arrayLiteral, arg));
	}
	

	@Test
	public void testTwoDimensionalArrays() throws SyntaxException {
		assertEquals(parseExpression(lexLine("arr[2][2]")), OperatorASTNode.newNode(Operator.arrayLookup, OperatorASTNode.newNode(Operator.arrayLookup, new ValueASTNode("arr"), new ValueASTNode("2")), new ValueASTNode("2")));
	}
	
	@Test
	public void testArrayAccessorExpressionShouldWork() throws SyntaxException {
		ASTNode left = parseExpression(lexLine("temp[a]"));
		ASTNode arrd = parseExpression(lexLine("arr[d]"));
		ASTNode right = OperatorASTNode.newNode(Operator.Addition, arrd, new ValueASTNode("0.9"));
		assertEquals(parseExpression(lexLine("temp[a] = arr[d] + 0.9")), OperatorASTNode.newNode(Operator.Assignment, left, right));
	}
	
	@Test
	public void testWhiteSpaceIssueWithSubtraction() throws SyntaxException {
		assertEquals(parseExpression(lexLine("a-2")), parseExpression(lexLine("a - 2")));
	}
	
	@Test
	public void testSubtractionInArrayAccessors() throws SyntaxException {
		ASTNode inner = parseExpression("a - 2");
		assertEquals(parseExpression("arr[a - 2]"), OperatorASTNode.newNode(Operator.arrayLookup, new ValueASTNode("arr"), inner));
	}
	
	@Test
	public void testVariableNamesWithUnderscoresShouldWork() throws SyntaxException {
		assertEquals(parseExpression("arr_thing + 2"), OperatorASTNode.newNode(Operator.Addition, new ValueASTNode("arr_thing"), new ValueASTNode("2")));
	}
	
	@Test
	public void testIssueWithWhitespaceFailureCase() throws SyntaxException {
		// Probably shouldn't work like this?
		// I'll allow it... I guess?
		assertEquals(parseExpression(lexLine("2 2 +")), OperatorASTNode.newNode(Operator.Addition, new ValueASTNode("2"), new ValueASTNode("2")));
	}
	
	@Test
	public void testPossibleTwoDimensionalArrayFailureCase() {
		try {
			parseExpression(lexLine("arr[2]2[]"));
			UnitTestUtil.fail("Should have thrown syntax error");
		}catch(SyntaxException e) {
			UnitTestUtil.assertEqual(e.getMessage(), "No index present in array lookup");
		}
	}
	
	@Test
	public void testDotOperator() throws SyntaxException {
		assertEquals(parseExpression("a.b"), OperatorASTNode.newNode(Operator.DotOperator, new ValueASTNode("a"), new ValueASTNode("b")));
	}
	
	@Test
	public void testDotOperatorShouldNotMixWithDoubles() throws SyntaxException {
		assertEquals(parseExpression("123.4"), new ValueASTNode("123.4"));
	}
	
	@Test
	public void testAssignmentOnDotOperator() throws SyntaxException {
		assertEquals(parseExpression("a.b = 2"), OperatorASTNode.newNode(Operator.Assignment, OperatorASTNode.newNode(Operator.DotOperator, new ValueASTNode("a"), new ValueASTNode("b")), new ValueASTNode("2")));
	}
	
	@Test
	public void testChainedDotOperators() throws SyntaxException {
		assertEquals(parseExpression("a.b.c"), OperatorASTNode.newNode(Operator.DotOperator, OperatorASTNode.newNode(Operator.DotOperator, new ValueASTNode("a"), new ValueASTNode("b")), new ValueASTNode("c")));
	}
	
	@Test
	public void testFunctionCallOnDotOperator() throws SyntaxException {
		assertEquals(parseExpression("a.b()"), OperatorASTNode.newNode(Operator.functionCall, OperatorASTNode.newNode(Operator.DotOperator, new ValueASTNode("a"), new ValueASTNode("b"))));
	}
	
	@Test
	public void testArrayAccessorOnFunctionCall() throws SyntaxException {
		ASTNode callFunc = parseExpression("a(2)");
		assertEquals(parseExpression("a(2)[3]"), OperatorASTNode.newNode(Operator.arrayLookup, callFunc, new ValueASTNode("3")));
	}
	
	@Test
	public void testAssignFromMultidimensionalArray() throws SyntaxException {
		assertEquals(parseExpression(lexLine("a = arr[2][3]")),
			OperatorASTNode.newNode(Operator.Assignment, new ValueASTNode("a"),
				OperatorASTNode.newNode(Operator.arrayLookup, 
					OperatorASTNode.newNode(Operator.arrayLookup,
						new ValueASTNode("arr"),
						new ValueASTNode("2")),
					new ValueASTNode("3"))));
	}

	@Test
	public void testArrayAccessorOnDotOperatorAndFunctionCall() throws SyntaxException {
		ASTNode dotAndCall = parseExpression(lexLine("a.b(2)"));
		assertEquals(parseExpression(lexLine("a.b(2)[3]")), OperatorASTNode.newNode(Operator.arrayLookup, dotAndCall, new ValueASTNode("3")));
	}
	
	@Test
	public void testSpaceInString() throws SyntaxException {
		assertEquals(parseExpression(lexLine("\"Hello there\"")), new ValueASTNode("\"Hello there\""));
	}
	
	@Test
	public void testSpaceInStringInFunctionCall() throws SyntaxException {
		assertEquals(parseExpression(lexLine("print(\"Hello World!\")")), OperatorASTNode.newNode(Operator.functionCall, new ValueASTNode("print"), new ValueASTNode("\"Hello World!\"")));
	}
	
	@Test
	public void testEscapedCharactersInStrings() throws SyntaxException {
		assertEquals(parseExpression(lexLine("\"Test \\\"\"")), new ValueASTNode("\"Test \"\""));
	}
	
	@Test
	public void testOperandCharactersInStrings() throws SyntaxException {
		assertEquals(parseExpression(lexLine("\"Test-\"")), new ValueASTNode("\"Test-\""));
	}

	@Test
	public void testParseLambda() throws SyntaxException {
		assertEquals(parseExpression(lexLine("a -> b")), OperatorASTNode.newNode(Operator.Lambda, new ValueASTNode("a"), new ValueASTNode("b")));
	}

	@Test
	public void shouldGroupCommasAsOneNodeInLambda() throws SyntaxException {
		ASTNode commaSeperated = OperatorASTNode.newNode(Operator.CommaDelimit, new ValueASTNode("a"), new ValueASTNode("b"));
		assertEquals(parseExpression(lexLine("a,b->c")), OperatorASTNode.newNode(Operator.Lambda, commaSeperated, new ValueASTNode("c")));
	}
	
	@Test
	public void shouldEscapeCharacters() throws SyntaxException {
		/* The first expression reads "\\" when it doesnt need to be double escaped */
		assertEquals(parseExpression(lexLine("\"\\\\\"")), new ValueASTNode("\"\\\""));
		assertEquals(parseExpression(lexLine("\"#\"")), new ValueASTNode("\"#\""));
	}

	@Test
	public void testBindingParsingWithNoParenthesis() throws SyntaxException {
		String[] expected = {"func"};
		String[] result = ExpressionParser.parseBindingWithArguments("func");
		org.junit.Assert.assertArrayEquals(result, expected);
	}
	
	@Test
	public void testBindingParsingWithParenthesis() throws SyntaxException {
		String[] expected = {"func"};
		String[] result = ExpressionParser.parseBindingWithArguments("func()");
		org.junit.Assert.assertArrayEquals(expected, result);
	}
	
	@Test
	public void testBindingParsingWithOneArgument() throws SyntaxException {
		String[] expected = {"func", "arg"};
		String[] result = ExpressionParser.parseBindingWithArguments("func(arg)");
		org.junit.Assert.assertArrayEquals(expected, result);
	}

	@Test
	public void testBindingParsingWithTwoArgumentsNoSpace() throws SyntaxException {
		String[] expected = {"func", "arg1", "arg2"};
		String[] result = ExpressionParser.parseBindingWithArguments("func(arg1,arg2)");
		org.junit.Assert.assertArrayEquals(expected, result);
	}
	
	@Test
	public void testBindingParsingWithTwoArgumentsWithSpace() throws SyntaxException {
		String[] expected = {"func", "arg1", "arg2"};
		String[] result = ExpressionParser.parseBindingWithArguments("func(arg1, arg2)");
		org.junit.Assert.assertArrayEquals(expected, result);
	}
	
	@Test
	public void testBindingParsingWithThreeArguments() throws SyntaxException {
		String[] expected = {"func", "arg1", "arg2", "arg3"};
		String[] result = ExpressionParser.parseBindingWithArguments("func(arg1,arg2,arg3)");
		org.junit.Assert.assertArrayEquals(expected, result);
	}
	
}
