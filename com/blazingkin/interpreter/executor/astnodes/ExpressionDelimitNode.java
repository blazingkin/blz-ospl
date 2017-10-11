package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Value;

public class ExpressionDelimitNode extends BinaryNode {

	public ExpressionDelimitNode(ASTNode[] args) {
		super(Operator.ExpressionDelimit, args);
	}
	
	@Override
	public Value execute(){
		Interpreter.throwError("Somehow an expression delimit node was executed... This is a bug \n"
				+ "Please report it on GitHub with what you did to cause it :)");
		return null;
	}

}
