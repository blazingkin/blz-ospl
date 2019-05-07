package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class WhileNode extends ASTNode {
	private final static Value TRUE_VAL = Value.bool(true);
	private final static Value NULL_VAL = Value.nil();
	
	ASTNode term, block;
	public WhileNode(ASTNode term, ASTNode block){
		this.term = term;
		this.block = block;
	}

	public boolean canModify() {
		return term.canModify() || block.canModify();
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
	public Value execute(Context con) throws BLZRuntimeException {
		Value cache = NULL_VAL;
		while (!shouldStop() && term.execute(con).equals(TRUE_VAL)){
			cache = block.execute(con);
		}
		Executor.setBreakMode(false);
		Executor.setContinueMode(false);
		return cache;
	}

	private boolean shouldStop(){
		return Executor.isBreakMode() || Executor.isReturnMode();
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
