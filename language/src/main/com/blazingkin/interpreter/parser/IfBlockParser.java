package com.blazingkin.interpreter.parser;

import java.util.ArrayList;
import java.util.List;

import com.blazingkin.interpreter.executor.astnodes.BlockNode;
import com.blazingkin.interpreter.executor.astnodes.IfNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.variables.Value;
 
public class IfBlockParser implements BlockParseProtocol {

    public boolean shouldParse(String header){
        return header.length() > 2 && (header.charAt(0) == 'i') &&
                                        (header.charAt(1) == 'f') &&
                                        (header.charAt(2) == ' ' || header.charAt(2) == '\t');
    }

    public ASTNode parseBlock(ParseBlock block) throws SyntaxException {
        String initLine = block.getHeader().replaceFirst("if", "").replaceFirst("IF", "").trim();
        ASTNode initCondition = ExpressionParser.parseExpression(initLine);
        ArrayList<Either<SourceLine, ParseBlock>> lines = block.getLines();
        int elseIndex = lines.size();
        boolean elseFound =  false;
        boolean elseIfFound = false;
        String elseIfLine = "";
        for (int i = 0; i < lines.size(); i++){
            if (lines.get(i).isLeft() && isElse(lines.get(i).getLeft().get().line)){
                /* If it is an else marker */
                elseFound = true;
                elseIndex = i;
                if (isElseIf(lines.get(i).getLeft().get().line)) {
                    elseIfFound = true;
                    elseIfLine = lines.get(i).getLeft().get().line.replaceFirst("else if", "if").trim();
                }
                if (i == lines.size() - 1){
                    throw new SyntaxException("Else was the last line of an if statement!");
                }
                break;
            }
        }

        
        ASTNode elseNode;
        List<Either<SourceLine, ParseBlock>> mainLines;
        if (elseIfFound){
            ArrayList<Either<SourceLine, ParseBlock>> elseIfBody = new ArrayList<Either<SourceLine, ParseBlock>>(lines.subList(elseIndex + 1, lines.size()));
            ParseBlock elseIfBlock = new ParseBlock(elseIfLine, elseIfBody, block.lineNumber + elseIndex);
            elseNode = parseBlock(elseIfBlock);
            mainLines = lines.subList(0, elseIndex);
        }else if (elseFound){
            List<Either<SourceLine, ParseBlock>> elseBlock = lines.subList(elseIndex + 1, lines.size());
            elseNode = new BlockNode(elseBlock, false);
            mainLines = lines.subList(0, elseIndex);
        }else {
            mainLines = lines;
            elseNode = new ValueASTNode(Value.nil());
        }
        BlockNode mainNode = new BlockNode(mainLines, false);
        IfNode node = new IfNode(initCondition, mainNode, elseNode);
        return node;
    }

    private boolean isElse(String line){
        return line.toLowerCase().startsWith("else");
    }

    private boolean isElseIf(String line){
        return line.toLowerCase().startsWith("else if");
    }

}