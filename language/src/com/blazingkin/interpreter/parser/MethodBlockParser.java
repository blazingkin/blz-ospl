package com.blazingkin.interpreter.parser;

import com.blazingkin.interpreter.executor.astnodes.MethodNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.executor.sourcestructures.Process;

public class MethodBlockParser implements BlockParseProtocol {
     
    private Process parent = null;
    public MethodBlockParser(){

    }

    public MethodBlockParser(Process parent){
        this.parent = parent;
    }

    public boolean shouldParse(String line){
        return line.length() >= 1 && line.charAt(0) == ':';
    }

    public ASTNode parseBlock(ParseBlock block) throws SyntaxException {
        return new MethodNode(block.getHeader(), block.getLines(), parent);
    }


}