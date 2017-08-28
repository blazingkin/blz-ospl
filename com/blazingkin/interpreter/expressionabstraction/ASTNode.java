package com.blazingkin.interpreter.expressionabstraction;

import com.blazingkin.interpreter.executor.executionstack.RuntimeStackElement;
import com.blazingkin.interpreter.variables.SystemEnv;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public abstract class ASTNode implements RuntimeStackElement {
	
	public abstract boolean canCollapse();
	
	public static boolean canCollapseAll(ASTNode[] nodes){
		for (ASTNode node: nodes){
			if (!node.canCollapse()){
				return false;
			}
		}
		return true;
	}
	
	public abstract ASTNode collapse();
	
	public abstract Value execute();
	
	public abstract String getStoreName();
	
	public abstract Operator getOperator();

	@Override
	public void onBlockStart() {
		
	}

	@Override
	public void onBlockEnd() {
		
	}
	
}
