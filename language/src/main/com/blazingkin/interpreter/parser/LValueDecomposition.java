package com.blazingkin.interpreter.parser;

import com.blazingkin.interpreter.executor.astnodes.ArrayLookupNode;
import com.blazingkin.interpreter.executor.astnodes.DotOperatorNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;

import java.util.List;
import java.util.ArrayList;
import org.graalvm.collections.Pair;

public class LValueDecomposition {

    /* Some example returns 
        asdf - {(Name, "asdf")}
        object.subname - {(ObjectName, "object"), (Name, "subname")}
        array[key] - {(ArrayOrHashName, "array"), (Value, ASTNode("key"))}
    */
    public static List<Pair<LValueType, Either<String, ASTNode>>> decompose(ASTNode lvalue) throws SyntaxException {
        System.out.println(lvalue);
        ArrayList<Pair<LValueType, Either<String, ASTNode>>> result = new ArrayList<>();
        ASTNode currentSubValue = lvalue;
        while (currentSubValue != null) {
            if (currentSubValue instanceof ValueASTNode) {
                ValueASTNode valueNode = (ValueASTNode) currentSubValue;
                result.add(
                    Pair.create(
                        LValueType.Name, Either.left(valueNode.getStoreName())
                    )
                );
                currentSubValue = null;
            }else if (currentSubValue instanceof DotOperatorNode) {
                DotOperatorNode dotNode = (DotOperatorNode) currentSubValue;
                // DotOperatorNode has 2 args
                result.addAll(decompose(dotNode.args[0]));

                currentSubValue = null;
            } else if (currentSubValue instanceof ArrayLookupNode) {

                currentSubValue = null;
            } else {
                throw new SyntaxException("Invalid syntax on left side of assignment\nDon't know how to assign to type "+lvalue.getClass());
            }
        }
        return result;
    }

    public enum LValueType {
        Name,
        ArrayOrHashName,
        ObjectName,
        Value
    }

}