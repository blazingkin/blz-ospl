package com.blazingkin.interpreter.executor.async;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorSemicolonDelimitedNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class Async implements InstructionExecutorSemicolonDelimitedNode {

    public Value run(ASTNode[] nodes, Context context) {
        for (ASTNode node : nodes) {
            new Thread(new Runnable() {
                public void run(){
                    try {
                        node.execute(context);
                    }catch(BLZRuntimeException e) {
                        System.err.println("Don't have a solution for async errors yet: "+e.getMessage());
                    }
                }
            }).start();
        }
        return Value.nil();
    }

}