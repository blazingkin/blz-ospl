package com.blazingkin.interpreter.unittests;

import java.util.ArrayList;

import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.parser.LineLexer;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.parser.Token;

import org.junit.Test;

public class LexerUnitTest {

    @Test
    public void shouldFindOneToken() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.Addition));

        ArrayList<Token> result = LineLexer.lexLine("+");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }

    @Test
    public void shouldExpectEndingCurlyBracket() throws SyntaxException{
        try {
            LineLexer.lexLine("{");
        }catch(SyntaxException e){
            UnitTestUtil.assertEqual(e.getMessage(), "Closing } not found");
            return;
        }
        UnitTestUtil.fail("Should have thrown syntax exception");
    }

    @Test
    public void shouldFindOneTokenEnvVariable() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.environmentVariableLookup, "test"));

        ArrayList<Token> result = LineLexer.lexLine("{test}");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }

    @Test
    public void shouldFindOneTokenMinus() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.Subtraction));

        ArrayList<Token> result = LineLexer.lexLine("-");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }

    @Test
    public void shouldFindOneTokenComparison() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.Comparison));

        ArrayList<Token> result = LineLexer.lexLine("==");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }

    @Test
    public void shouldFindTwoTokens() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.Assignment));
        expectedResult.add(new Token(Operator.Assignment));

        ArrayList<Token> result = LineLexer.lexLine("= =");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }

    @Test
    public void shouldFindOneLambda() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.Lambda));

        ArrayList<Token> result = LineLexer.lexLine("->");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }

    @Test
    public void shouldFindTwoTokensSubDec() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.Decrement));
        expectedResult.add(new Token(Operator.Subtraction));

        ArrayList<Token> result = LineLexer.lexLine("---");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }


    @Test
    public void shouldFindBangAsSeperate() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.Ident, "func"));
        expectedResult.add(new Token(Operator.Exclam));
        expectedResult.add(new Token(Operator.parensOpen));
        expectedResult.add(new Token(Operator.parensClose));

        ArrayList<Token> result = LineLexer.lexLine("func!()");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }

    @Test
    public void shouldFindComparison() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.LessThanEqual));

        ArrayList<Token> result = LineLexer.lexLine("=<");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }

    @Test
    public void shouldFindReadIdent() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.Ident, "abc:def-ghi_jkl"));

        ArrayList<Token> result = LineLexer.lexLine("abc:def-ghi_jkl");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }

    @Test
    public void shouldFindString() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.String, "abc:def-ghi_jkl"));

        ArrayList<Token> result = LineLexer.lexLine("\"abc:def-ghi_jkl\"");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }

    @Test
    public void escapingStringShouldWork() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.String, "ab#c:def\"-ghi_jkl"));

        ArrayList<Token> result = LineLexer.lexLine("\"ab\\#c:def\\\"-ghi_jkl\"");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }

    @Test
    public void newlineShouldBeTranslated() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.String, "abc:def\n-ghi_jkl"));

        ArrayList<Token> result = LineLexer.lexLine("\"abc:def\\n-ghi_jkl\"");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }

    @Test
    public void shouldReadNumber() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.Number, "8675"));
        expectedResult.add(new Token(Operator.DotOperator));
        expectedResult.add(new Token(Operator.Number, "123"));
        
        ArrayList<Token> result = LineLexer.lexLine("8675.123");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }

    @Test
    public void shouldReadLotsOfTokens() throws SyntaxException{
        ArrayList<Token> expectedResult = new ArrayList<Token>();
        expectedResult.add(new Token(Operator.Ident, "arr"));
        expectedResult.add(new Token(Operator.sqBracketOpen));
        expectedResult.add(new Token(Operator.Ident, "i"));
        expectedResult.add(new Token(Operator.sqBracketClose));
        expectedResult.add(new Token(Operator.Assignment));
        expectedResult.add(new Token(Operator.Ident, "arr"));
        expectedResult.add(new Token(Operator.sqBracketOpen));
        expectedResult.add(new Token(Operator.Ident, "i"));
        expectedResult.add(new Token(Operator.Addition));
        expectedResult.add(new Token(Operator.Number, "1"));
        expectedResult.add(new Token(Operator.sqBracketClose));
        expectedResult.add(new Token(Operator.Addition));
        expectedResult.add(new Token(Operator.String, "test"));

        ArrayList<Token> result = LineLexer.lexLine("arr[i] = arr[i + 1] + \"test\"");
        UnitTestUtil.assertEqualArrays(expectedResult, result);
    }
    
    @Test
    public void expectErrorFromSinglePipe() {
        try {
            LineLexer.lexLine("|");
            UnitTestUtil.fail("A single pipe is not valid");
        }catch(SyntaxException e){

        }
    }

    @Test
    public void expectErrorFromSingleAnd() {
        try {
            LineLexer.lexLine("&");
            UnitTestUtil.fail("A single and is not valid");
        }catch(SyntaxException e){

        }
    }

    @Test
    public void expectErrorFromSingleTilde() {
        try {
            LineLexer.lexLine("~");
            UnitTestUtil.fail("A single tilde is not valid");
        }catch(SyntaxException e){

        }
    }

}