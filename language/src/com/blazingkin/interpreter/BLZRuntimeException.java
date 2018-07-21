package com.blazingkin.interpreter;

import java.util.Optional;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.variables.Value;

public class BLZRuntimeException extends Exception {

    public static final long serialVersionUID = 8675309l;

    public boolean alreadyCaught = false;
    public Value exceptionValue = null;

    public Optional<ASTNode> failingNode;
    public BLZRuntimeException(ASTNode node, String message) {
        super(message);
        this.failingNode = Optional.of(node);
    }

    public BLZRuntimeException(String message){
        super(message);
        this.failingNode = Optional.empty();
    }

    public BLZRuntimeException(String message, boolean alreadyCaught){
        super(message);
        this.alreadyCaught = alreadyCaught;
    }

    public BLZRuntimeException(Value exceptionValue){
        super(exceptionValue.value.toString());
        this.exceptionValue = exceptionValue;
    }


}