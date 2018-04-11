package com.blazingkin.interpreter.executor.astnodes;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.sourcestructures.RegisteredLine;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class BlockNode extends ASTNode {

    RegisteredLine body[];
    boolean shouldClearReturns;
    public BlockNode(List<Either<String, ParseBlock>> body, boolean shouldClearReturns) throws SyntaxException {
        ArrayList<RegisteredLine> lines = new ArrayList<RegisteredLine>();
        for (Either<String, ParseBlock> line : body){
            if (line.isLeft()){
                /* It is some string, parse it to a RegisteredLine */
                Optional<RegisteredLine> parsed = ExpressionParser.parseLine(line.getLeft().get());
                if (parsed.isPresent()){
                    lines.add(parsed.get());
                }
            } else {
                /* It is some block, parse it to a blocknode */
                // TODO implement some behavior to parse different blocks (i.e. if, method, for, while, etc..)
            }
        }
        this.body = new RegisteredLine[lines.size()];
        this.shouldClearReturns = shouldClearReturns;
        lines.toArray(this.body);
    }

    public Value execute(Context c){
        for (int i = 0; i < body.length - 1; i++){
            if (Executor.isBreakMode()){
                if (shouldClearReturns){
                    Executor.setBreakMode(false);
                }
                return Executor.getReturnBuffer();
            }
            body[i].run(c);
        }
        if (Executor.isBreakMode()){
            if (shouldClearReturns){
                Executor.setBreakMode(false);
            }
            return Executor.getReturnBuffer();
        }
        return body[body.length - 1].run(c);
    }

    public String getStoreName(){
        return "";
    }

    public Operator getOperator(){
        return null;
    }

    public boolean canCollapse(){
        return false;
    }

    public ASTNode collapse(){
        return this;
    }

}