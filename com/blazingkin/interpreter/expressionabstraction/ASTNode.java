package com.blazingkin.interpreter.expressionabstraction;

import com.blazingkin.interpreter.variables.Value;

public abstract class ASTNode {
	
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
	
	// This is similar to the lvalue in c
	public abstract String getStoreName();
	
	public abstract Operator getOperator();
	
}
