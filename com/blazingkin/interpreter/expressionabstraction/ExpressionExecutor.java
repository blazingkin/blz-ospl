package com.blazingkin.interpreter.expressionabstraction;

import java.util.ArrayList;

import com.blazingkin.interpreter.variables.Value;

public class ExpressionExecutor {
	
	public static double EPSILON = 1E-8; 
	
	public static Value parseExpression(String line){
		return executeNode(ExpressionParser.parseExpression(line));
	}
	
	public static Value[] extractCommaDelimits(ASTNode root){
		ArrayList<Value> helperCall = extractCommaDelimitsHelper(root);
		Value[] newVals = new Value[helperCall.size()];
		helperCall.toArray(newVals);
		return newVals;
	}
	
	public static ArrayList<Value> extractCommaDelimitsHelper(ASTNode root){
		if (root == null){
			return new ArrayList<Value>();
		}
		if (root.getOperator() == Operator.CommaDelimit){
			OperatorASTNode oroot = (OperatorASTNode) root;
			ArrayList<Value> first = extractCommaDelimitsHelper(oroot.args[0]);
			ArrayList<Value> second = extractCommaDelimitsHelper(oroot.args[1]);
			first.addAll(second);
			return first;
		}else{
			ArrayList<Value> ret = new ArrayList<Value>();
			ret.add(root.execute());
			return ret;
		}
	}
	
	public static ASTNode[] extractSemicolonDelimitedNodes(ASTNode root){
		ArrayList<ASTNode> helperCall = extractSemicolonDelimitedNodesHelper(root);
		ASTNode[] newVals = new ASTNode[helperCall.size()];
		helperCall.toArray(newVals);
		return newVals;
	}
	
	public static ArrayList<ASTNode> extractSemicolonDelimitedNodesHelper(ASTNode root){
		if (root.getOperator() == Operator.ExpressionDelimit){
			OperatorASTNode oroot = (OperatorASTNode) root;
			ArrayList<ASTNode> first = extractSemicolonDelimitedNodesHelper(oroot.args[0]);
			ArrayList<ASTNode> second = extractSemicolonDelimitedNodesHelper(oroot.args[1]);
			first.addAll(second);
			return first;
		}else{
			ArrayList<ASTNode> ret = new ArrayList<ASTNode>();
			ret.add(root);
			return ret;
		}
	}
	
	public static Value executeNode(ASTNode root){
		return root.execute();
	}
	
}
