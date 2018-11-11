package com.blazingkin.interpreter.parser;

import com.blazingkin.interpreter.executor.astnodes.BlockNode;
import com.blazingkin.interpreter.executor.astnodes.ForNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;

public class ForBlockParser implements BlockParseProtocol {

    public boolean shouldParse(String line){
        return line.startsWith("for ");
    }

    public ASTNode parseBlock(ParseBlock block) throws SyntaxException {
        BlockNode forBlock = new BlockNode(block.getLines(), false);
        String initLine = block.getHeader().replaceFirst("for", "").trim();
        ASTNode initNode = ExpressionParser.parseExpression(initLine);
        ASTNode initNodes[] = ExpressionExecutor.extractSemicolonDelimitedNodes(initNode);
        if (initNodes.length != 3){
            throw new SyntaxException("For did not have an initiating, looping and terminating expression");
        }
        return new ForNode(initNodes[0], initNodes[2], initNodes[1], forBlock);
    }

}