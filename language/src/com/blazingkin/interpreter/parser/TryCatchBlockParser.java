package com.blazingkin.interpreter.parser;

import java.util.ArrayList;
import java.util.List;

import com.blazingkin.interpreter.executor.astnodes.BlockNode;
import com.blazingkin.interpreter.executor.astnodes.TryCatchNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.variables.Value;

public class TryCatchBlockParser implements BlockParseProtocol {

    public boolean shouldParse(String header){
        return header.length() == 3 && (header.charAt(0) == 't' || header.charAt(0) == 'T') &&
                                        (header.charAt(1) == 'r' || header.charAt(1) == 'R') &&
                                        (header.charAt(2) == 'y' || header.charAt(2) == 'Y');
    }

    public ASTNode parseBlock(ParseBlock block) throws SyntaxException {
        ArrayList<Either<SourceLine, ParseBlock>> lines = block.getLines();
        int catchIndex = lines.size();
        boolean catchFound =  false;
        for (int i = 0; i < lines.size(); i++){
            if (lines.get(i).isLeft() && isCatch(lines.get(i).getLeft().get().line)){
                /* If it is an else marker */
                catchFound = true;
                catchIndex = i;
                if (i == lines.size() - 1){
                    throw new SyntaxException("Catch was the last line of an try block!");
                }
                break;
            }
        }

        
        ASTNode exceptionNode;
        List<Either<SourceLine, ParseBlock>> mainLines;
        if (catchFound){
            List<Either<SourceLine, ParseBlock>> catchBlock = lines.subList(catchIndex + 1, lines.size());
            exceptionNode = new BlockNode(catchBlock, false);
            mainLines = lines.subList(0, catchIndex);
        }else {
            mainLines = lines;
            exceptionNode = new ValueASTNode(Value.nil());
        }
        BlockNode mainNode = new BlockNode(mainLines, false);
        TryCatchNode node = new TryCatchNode(mainNode, exceptionNode);
        return node;
    }

    private boolean isCatch(String line){
        return line.toLowerCase().startsWith("catch");
    }

}