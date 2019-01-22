package com.blazingkin.interpreter.parser;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;

public interface BlockParseProtocol {
    public abstract ASTNode parseBlock(ParseBlock block) throws SyntaxException;
    public abstract boolean shouldParse(String header);
}