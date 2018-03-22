package com.blazingkin.interpreter.executor.astnodes;

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
		if (condition.execute(new Context()).equals(TRUE_VAL)){
			return mainBlock;
		}else{
			return elseBlock;
		}
	}

	@Override
	public Value execute(Context con) {
		Context closure = new Context(con);
		if (condition.execute(closure).equals(TRUE_VAL)){
			return mainBlock.execute(closure);
		}else{
			return elseBlock.execute(closure);
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
