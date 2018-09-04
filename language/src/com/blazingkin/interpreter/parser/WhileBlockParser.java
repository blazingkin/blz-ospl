package com.blazingkin.interpreter.parser;

import com.blazingkin.interpreter.executor.astnodes.BlockNode;
import com.blazingkin.interpreter.executor.astnodes.WhileNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;

public class WhileBlockParser implements BlockParseProtocol {


    public boolean shouldParse(String line){
        return line.toLowerCase().startsWith("while ");
    }

    public ASTNode parseBlock(ParseBlock block) throws SyntaxException {
        String condition = block.getHeader().replaceFirst("while", "").trim();
        ASTNode conditionNode = ExpressionParser.parseExpression(LineLexer.lexLine(condition));
        ASTNode whileBlock = new BlockNode(block.getLines(), false);
        return new WhileNode(conditionNode, whileBlock);
    }

}