package com.blazingkin.interpreter;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;

public class BLZRuntimeException extends Exception {

    public static final long serialVersionUID = 8675309l;

    public ASTNode failingNode;
    public BLZRuntimeException(ASTNode node, String message) {
        super(message);
        this.failingNode = node;
    }


}