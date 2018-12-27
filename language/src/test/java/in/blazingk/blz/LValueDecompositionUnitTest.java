package in.blazingk.blz;

import static com.blazingkin.interpreter.parser.ExpressionParser.parseExpression;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.parser.LValueDecomposition;
import com.blazingkin.interpreter.parser.SyntaxException;

import org.junit.Test;


public class LValueDecompositionUnitTest {

    @Test
    public void testSingleName() throws SyntaxException {
        ASTNode simple = parseExpression("abc");
        System.out.println(LValueDecomposition.decompose(simple));
    }

    @Test
    public void testArrayLookup() throws SyntaxException {
        ASTNode lookup = parseExpression("abc[123]");
        LValueDecomposition.decompose(lookup);
    }

    @Test
    public void testHashLookup() throws SyntaxException {
        ASTNode hashLookup = parseExpression("hash[\"abc\"]");
        LValueDecomposition.decompose(hashLookup);
    }

    @Test
    public void testArrayLookupWithLValue() throws SyntaxException {
        ASTNode lookup = parseExpression("abc[def[12]]");
        LValueDecomposition.decompose(lookup);
    }

    @Test
    public void testDotOperator() throws SyntaxException {
        ASTNode objectLookup = parseExpression("first.second.third");
        LValueDecomposition.decompose(objectLookup);
    }

    @Test
    public void testDotOperatorThenArrayLookup() throws SyntaxException {
        ASTNode objectLookupThenArray = parseExpression("first.second.third[123]");
        LValueDecomposition.decompose(objectLookupThenArray);
    }

}