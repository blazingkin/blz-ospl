package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class IfNode extends ASTNode {
	private final static Value TRUE_VAL = Value.bool(true);
	
	ASTNode condition, mainBlock, elseBlock;
	public IfNode(ASTNode condition, ASTNode mainBlock, ASTNode elseBlock){
		this.condition = condition;
		this.mainBlock = mainBlock;
		this.elseBlock = elseBlock;
	}

	@Override
	public boolean canCollapse() {
		return condition.canCollapse();
	}

	@Override
	public ASTNode collapse() {
		try {
			if (condition.execute(new Context()).equals(TRUE_VAL)){
				return mainBlock;
			}else{
				return elseBlock;
			}
		}catch (BLZRuntimeException e){
			return this;
		}
	}

	@Override
	public Value execute(Context con) throws BLZRuntimeException {
		if (condition.execute(con).equals(TRUE_VAL)){
			return mainBlock.execute(con);
		}else{
			return elseBlock.execute(con);
		}
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
