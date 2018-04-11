package com.blazingkin.interpreter.expressionabstraction;

public abstract class BinaryNode extends OperatorASTNode {

	public BinaryNode(Operator op, ASTNode[] args) {
		super(op, args);
	}


}
