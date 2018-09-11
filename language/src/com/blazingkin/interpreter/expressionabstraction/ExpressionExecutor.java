package com.blazingkin.interpreter.expressionabstraction;

import java.util.ArrayList;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class ExpressionExecutor {
	
	public static Value runExpression(String line) throws Exception {
		return ExpressionParser.parseExpression(line).execute(Variable.getGlobalContext());
	}

	public static Value runExpression(String line, Context con) throws Exception {
		return ExpressionParser.parseExpression(line).execute(con);
	}

	public static Value[] extractCommaDelimits(ASTNode root, Context con) throws BLZRuntimeException{
		ArrayList<Value> helperCall = extractCommaDelimitsHelper(root, con);
		Value[] newVals = new Value[helperCall.size()];
		helperCall.toArray(newVals);
		return newVals;
	}
	
	private static ArrayList<Value> extractCommaDelimitsHelper(ASTNode root, Context con) throws BLZRuntimeException{
		if (root == null){
			return new ArrayList<Value>();
		}
		if (root.getOperator() == Operator.CommaDelimit){
			OperatorASTNode oroot = (OperatorASTNode) root;
			ArrayList<Value> first = extractCommaDelimitsHelper(oroot.args[0], con);
			ArrayList<Value> second = extractCommaDelimitsHelper(oroot.args[1], con);
			first.addAll(second);
			return first;
		}else{
			ArrayList<Value> ret = new ArrayList<Value>();
			ret.add(root.execute(con));
			return ret;
		}
	}

	public static ASTNode[] extractCommaDelimitedNodes(ASTNode root) {
		ArrayList<ASTNode> helperCall = extractCommaDelimitedNodesHelper(root);
		ASTNode[] newVals = new ASTNode[helperCall.size()];
		helperCall.toArray(newVals);
		return newVals;
	}

	private static ArrayList<ASTNode> extractCommaDelimitedNodesHelper(ASTNode root){
		if (root.getOperator() == Operator.CommaDelimit) {
			OperatorASTNode oroot = (OperatorASTNode) root;
			ArrayList<ASTNode> first = extractCommaDelimitedNodesHelper(oroot.args[0]);
			ArrayList<ASTNode> second = extractCommaDelimitedNodesHelper(oroot.args[1]);
			first.addAll(second);
			return first;
		} else {
			ArrayList<ASTNode> ret = new ArrayList<ASTNode>();
			ret.add(root);
			return ret;
		}
	}
	
	public static ASTNode[] extractSemicolonDelimitedNodes(ASTNode root){
		ArrayList<ASTNode> helperCall = extractSemicolonDelimitedNodesHelper(root);
		ASTNode[] newVals = new ASTNode[helperCall.size()];
		helperCall.toArray(newVals);
		return newVals;
	}
	
	private static ArrayList<ASTNode> extractSemicolonDelimitedNodesHelper(ASTNode root){
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
	
	
}
