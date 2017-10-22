package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
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
		if (condition.execute().equals(TRUE_VAL)){
			return mainBlock;
		}else{
			return elseBlock;
		}
	}

	@Override
	public Value execute() {
		if (condition.execute().equals(TRUE_VAL)){
			return mainBlock.execute();
		}else{
			return elseBlock.execute();
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
