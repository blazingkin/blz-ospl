package com.blazingkin.interpreter.parser;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.variables.VariableTypes;

import org.graalvm.collections.Pair;

public class LValueDecomposition {

    public Pair<VariableTypes, String>[] decompose(ASTNode lvalue) {
        System.out.println(lvalue);
        return null;
    }

}