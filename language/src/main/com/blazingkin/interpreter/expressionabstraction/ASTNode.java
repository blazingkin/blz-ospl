package com.blazingkin.interpreter.expressionabstraction;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.variables.Context;
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

	public abstract boolean canModify();
	
	public abstract ASTNode collapse();
	
	public abstract Value execute(Context c) throws BLZRuntimeException;
	
	// This is similar to the lvalue in c
	public abstract String getStoreName();
	
	public abstract Operator getOperator();
	
}
