package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class LambdaNode extends BinaryNode {
    
    String[] parameterNames;
    public LambdaNode(ASTNode[] args){
        super(Operator.Lambda, args);
        if (args.length != 2){
            Interpreter.throwError("Lamda Node Not Given 2 Arguments");
        }
        if (args[0] instanceof CommaDelimitNode){
            ASTNode[] parameters = ExpressionExecutor.extractCommaDelimitedNodes(args[0]);
            parameterNames = new String[parameters.length];
            for (int i = 0; i < parameters.length; i++){
                if (parameters[i] instanceof ValueASTNode) {
                    parameterNames[i] = ((ValueASTNode) parameters[i]).getStoreName();
                } else {
                    Interpreter.throwError("Lambda expression given bad parameter name: "+parameters[i]);
                }
            }
        }else if (args[0] instanceof ValueASTNode){
            ValueASTNode arg = (ValueASTNode) args[0];
            parameterNames = new String[1];
            parameterNames[0] = arg.getStoreName();
        } else {
            Interpreter.throwError("Lambda expression given bad parameter list "+args[0]);
        }
    }

    public Value execute(Context con){
        // TODO Fix lambdas always registering line number as -1
        MethodNode node = new MethodNode(parameterNames, this.args[1], -1);
        return Value.closure(new Closure(con, node, "Lambda expression"));
    }


}