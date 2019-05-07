package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class ExpressionDelimitNode extends BinaryNode {

	public ExpressionDelimitNode(ASTNode[] args) {
		super(Operator.ExpressionDelimit, args);
	}

	public boolean canModify() {
		for (ASTNode arg : args) {
			if (arg.canModify()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Value execute(Context con){
		Interpreter.throwError("Somehow an expression delimit node was executed... This is a bug \n"
				+ "Please report it on GitHub with what you did to cause it :)");
		return null;
	}

}
