package com.blazingkin.interpreter.expressionabstraction;

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
	
	public String name;
	public ASTNode(String name){
		this.name = name;
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
		return name;
	}
	
	public boolean equals(Object other){
		if (!(other instanceof ASTNode)){
			return false;
		}
		ASTNode otherNode = (ASTNode) other;
		if (otherNode.name != null && this.name != null){
			return otherNode.name.equals(this.name);
		}
		if (otherNode.args.length != this.args.length){
			return false;
		}
		boolean flag = this.op.equals(otherNode.op);
		for (int i = 0; i < args.length; i++){
			flag = flag && args[i].equals(otherNode.args[i]);
		}
		return flag;
	}
	
}
