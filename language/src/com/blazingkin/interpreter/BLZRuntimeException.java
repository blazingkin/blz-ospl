package com.blazingkin.interpreter;

import java.util.Optional;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;

public class BLZRuntimeException extends Exception {

    public static final long serialVersionUID = 8675309l;

    public Optional<ASTNode> failingNode;
    public BLZRuntimeException(ASTNode node, String message) {
        super(message);
        this.failingNode = Optional.of(node);
    }

    public BLZRuntimeException(String message){
        super(message);
        this.failingNode = Optional.empty();
    }


}