package com.blazingkin.interpreter.expressionabstraction;

public abstract class UnaryNode extends OperatorASTNode {

	public UnaryNode(Operator op, ASTNode[] args) {
		super(op, args);
	}

}
