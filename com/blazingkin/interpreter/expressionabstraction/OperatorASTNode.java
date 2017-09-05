package com.blazingkin.interpreter.expressionabstraction;

import com.blazingkin.interpreter.executor.astnodes.*;
import com.blazingkin.interpreter.variables.SystemEnv;

public abstract class OperatorASTNode extends ASTNode {

	public static double EPSILON = 1E-8; 
	
	public Operator op;
	public ASTNode[] args;
	public OperatorASTNode(Operator op, ASTNode... args){
		this.op = op;
		this.args = args;
	}
	
	public String toString(){
		String building = "";
		for (ASTNode node : args){
			building += node.toString() + ", ";
		}
		building += op.syntax;
		return "("+building+")"; 
	}
	
	public boolean equals(Object otherobj){
		if (otherobj instanceof OperatorASTNode){
			OperatorASTNode other = (OperatorASTNode) otherobj;
			if (this.op == other.op && this.args.length == other.args.length){
				for (int i = 0; i < this.args.length; i++){
					if (!this.args[i].equals(other.args[i])){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canCollapse() {
		switch(op){
			// We can't collapse things that mutate
			case Assignment: 
				return false;
			case Decrement:
				return false;
			case functionCall:
				return false;
			case Increment:
				return false;
			// Some environment variables are dynamic, others are not
			case environmentVariableLookup:
				if (args[0] instanceof ValueASTNode){
					String envName = ((ValueASTNode) args[0]).strVal;
					for (SystemEnv se : SystemEnv.values()){
						if (se.name.equals(envName)){
							return !se.dynamic;
						}
					}
				}
				return false;
			default:
				return canCollapseAll(args);
		}
	}

	@Override
	public ASTNode collapse() {
		if (canCollapse()){
			return new ValueASTNode(execute());
		}else{
			if (args != null){
				ASTNode[] newChildren = new ASTNode[args.length];
				for (int i = 0; i < args.length; i++){
					newChildren[i] = args[i].collapse();
				}
				return newNode(op, newChildren);
			}else{
				return this;
			}
		}
	}
	
	@Override
	public Operator getOperator(){
		return op;
	}

	
	public String getStoreName(){
		return null;
	}
	
	public static OperatorASTNode newNode(Operator op, ASTNode... args){
		switch (op){
		case Addition:
			return new AdditionNode(args);
		case ApproximateComparison:
			return new ApproximateComparisonNode(args);
		case arrayLiteral:
			return new ArrayLiteralNode(args);
		case arrayLookup:
			return new ArrayLookupNode(args);
		case Assignment:
			return new AssignmentNode(args);
		case CommaDelimit:
			return new CommaDelimitNode(args);
		case Comparison:
			return new ComparisonNode(args);
		case Decrement:
			return new DecrementNode(args);
		case Division:
			return new DivisionNode(args);
		case DotOperator:
			return new DotOperatorNode(args);
		case environmentVariableLookup:
			return new EnvironmentVariableLookupNode(args);
		case Exponentiation:
			return new ExponentiationNode(args);
		case functionCall:
			return new FunctionCallNode(args);
		case GreaterThan:
			return new GreaterThanNode(args);
		case GreaterThanEqual:
			return new GreaterThanEqualsNode(args);
		case Increment:
			return new IncrementNode(args);
		case LessThan:
			return new LessThanNode(args);
		case LessThanEqual:
			return new LessThanEqualsNode(args);
		case Logarithm:
			return new LogarithmNode(args);
		case Modulus:
			return new ModulusNode(args);
		case Multiplication:
			return new MultiplicationNode(args);
		case NotEqual:
			return new NotEqualNode(args);
		case Subtraction:
			return new SubtractionNode(args);
		default:
			return null;
		}
	}
	
}
