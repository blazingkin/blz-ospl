package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class WhileNode extends ASTNode {
	private final static Value TRUE_VAL = Value.bool(true);
	private final static Value NULL_VAL = new Value(VariableTypes.Nil, null);
	
	ASTNode term, block;
	public WhileNode(ASTNode term, ASTNode block){
		this.term = term;
		this.block = block;
	}
	
	@Override
	public boolean canCollapse() {
		return false;
	}

	@Override
	public ASTNode collapse() {
		return this;
	}

	@Override
	public Value execute(Context con) {
		Context closure = new Context(con);
		Value cache = NULL_VAL;
		while (term.execute(closure).equals(TRUE_VAL)){
			cache = block.execute(closure);
		}
		return cache;
	}

	@Override
	public String getStoreName() {
		return null;
	}

	@Override
	public Operator getOperator() {
		return null;
	}
}
