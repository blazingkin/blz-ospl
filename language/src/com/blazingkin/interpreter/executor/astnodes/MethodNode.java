package com.blazingkin.interpreter.executor.astnodes;

import java.util.ArrayList;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.sourcestructures.Process;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class MethodNode extends ASTNode {

    String name;
    public String[] variables = {};
    public boolean takesVariables = false;
    public Process parent;
    public BlockNode body;

    public MethodNode(String header, ArrayList<Either<String, ParseBlock>> body, Process parent) throws SyntaxException {
        this.parent = parent;
        String ln = header.split(":")[header.split(":").length - 1];
		String[] nameAndArgs = ExpressionParser.parseBindingWithArguments(ln);
		name = nameAndArgs[0];
		if (nameAndArgs.length > 1){
			takesVariables = true;
			variables = new String[nameAndArgs.length - 1];
			for (int i = 0; i < variables.length; i++){
				variables[i] = nameAndArgs[i + 1];
			}
        }
        this.body = new BlockNode(body, true);
    }

    public Value execute(Context c){
        Interpreter.throwError("Method node was executed without arguments");
        Value empty[] = {};
        return execute(c, empty, false);
    }

    public Value execute(Context c, Value[] values, boolean passByReference){
        boolean pushedParent = false;
        if (parent != null && RuntimeStack.processStack.peek().UUID != parent.UUID){
            pushedParent = true;
            RuntimeStack.push(parent);
        }
        Context methodContext = new Context(RuntimeStack.processContextStack.peek());
        if (takesVariables){
            bindArguments(values, passByReference, methodContext);
        }
        Value result = body.execute(methodContext);
        if(pushedParent){
            RuntimeStack.pop();
        }
        return result;
    }

    private void bindArguments(Value values[], boolean passByReference, Context methodContext){
        int variableCount = (variables.length > values.length?values.length:variables.length);
        if (passByReference){
            for (int i = 0; i < variableCount; i++){
                methodContext.setValue(variables[i], values[i]);
            }
        }else{
            for (int i = 0; i < variableCount; i++){
                methodContext.setValue(variables[i], (values[i]).clone());
            }
        }
        /* Bind variables that weren't passed to nil */
        for (int i = variableCount; i < variables.length; i++) {
            methodContext.setValue(variables[i], Value.nil());
        }
    }



    public Operator getOperator(){
        return Operator.functionCall;
    }

    public String getStoreName(){
        return name;
    }

    public ASTNode collapse(){
        return this;
    }

    public boolean canCollapse(){
        return false;
    }

}