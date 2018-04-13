package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ForNode extends ASTNode {
	private final static Value TRUE_VAL = Value.bool(true);
	private final static Value NULL_VAL = new Value(VariableTypes.Nil, null);
	
	ASTNode init, loop, term, block;
	public ForNode(ASTNode init, ASTNode loop, ASTNode term, ASTNode block){
		this.init = init;
		this.loop = loop;
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
		Value cache = NULL_VAL;
		init.execute(con);
		while (term.execute(con).equals(TRUE_VAL)){
			cache = block.execute(con);
			loop.execute(con);
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
