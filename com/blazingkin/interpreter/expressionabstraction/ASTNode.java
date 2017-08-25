package com.blazingkin.interpreter.expressionabstraction;

import com.blazingkin.interpreter.variables.SystemEnv;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class ASTNode {
	public Operator op;
	public ASTNode[] args;
	public ASTNode(Operator op, ASTNode[] args){
		this.op = op;
		this.args = args;
	}
	
	public ASTNode(Operator op, ASTNode arg1, ASTNode arg2){
		this.op = op;
		args = new ASTNode[2];
		args[0] = arg1;
		args[1] = arg2;
	}
	
	public ASTNode(Operator op, ASTNode arg){
		this.op = op;
		args = new ASTNode[1];
		args[0] = arg;
	}
	
	public String name;
	public ASTNode(String name){
		this.name = name;
	}
	
	public Value value;
	public ASTNode(Value val){
		this.name = "";
		this.value = val;
	}
	
	public String toString(){
		if (this.name == null){
			String building = "";
			for (ASTNode node : args){
				building += node.toString() + ", ";
			}
			building += op.syntax;
			return "("+building+")"; 
		}
		if (this.value != null){
			return value.toString();
		}
		return name;
	}
	
	public boolean canCollapse(){
		if (name != null){
			if (value != null){
				return true;
			}
			return Variable.canGetValue(name);
		}
		switch (op){
		case arrayLookup:
			return false;
		case functionCall:
			return false;
		case Increment:
			return false;
		case Decrement:
			return false;
		case Assignment:
			return false;
		case ExpressionDelimit:
			return false;
		case environmentVariableLookup:
			for (SystemEnv se : SystemEnv.values()){
				if (se.name.equals(args[0].name)){
					return !se.dynamic;
				}
			}
			return false;
		case DotOperator:
			return false;
		default:
			return canCollapseAll(args);
		}
	}
	
	public static boolean canCollapseAll(ASTNode[] nodes){
		for (ASTNode node: nodes){
			if (!node.canCollapse()){
				return false;
			}
		}
		return true;
	}
	
	public ASTNode collapse(){
		if (canCollapse()){
			return new ASTNode(ExpressionExecutor.executeNode(this));
		}else{
			if (args != null){
				ASTNode[] newChildren = new ASTNode[args.length];
				for (int i = 0; i < args.length; i++){
					newChildren[i] = args[i].collapse();
				}
				return new ASTNode(op, newChildren);
			}else{
				return this;
			}
		}
	}
	
	
	public boolean equals(Object other){
		if (!(other instanceof ASTNode)){
			return false;
		}
		ASTNode otherNode = (ASTNode) other;
		if (otherNode.name != null && this.name != null){
			return otherNode.name.equals(this.name);
		}
		if (otherNode.args == null || this.args == null || otherNode.args.length != this.args.length){
			return false;
		}
		boolean flag = this.op.equals(otherNode.op);
		for (int i = 0; i < args.length; i++){
			flag = flag && args[i].equals(otherNode.args[i]);
		}
		return flag;
	}
	
}
