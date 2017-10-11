package com.blazingkin.interpreter.expressionabstraction;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ValueASTNode extends ASTNode {

	Value val;
	String strVal;
	boolean isValSet = false;
	public ValueASTNode(String val){
		if (Variable.canGetValue(val)){
			this.val = Variable.getValue(val);
			isValSet = true;
		}else{
			this.strVal = val;
		}
	}
	
	public ValueASTNode(Value val){
		this.val = val;
		isValSet = true;
	}
	
	public String toString(){
		if (isValSet){
			return val.toString();
		}else{
			return strVal;
		}
	}
	
	public boolean equals(Object otherobj){
		if (otherobj instanceof ValueASTNode){
			ValueASTNode node = (ValueASTNode) otherobj;
			if (isValSet){
				return this.val.equals(node.val);
			}else{
				return this.strVal.equals(node.strVal);
			}
		}else if (otherobj instanceof Value){
			
		}
		return false;
	}

	@Override
	public boolean canCollapse() {
		return isValSet || Variable.canGetValue(strVal);
	}

	@Override
	public ASTNode collapse() {
		return this;
	}

	@Override
	public Value execute() {
		if (isValSet){
			return val;
		}
		if (Variable.contains(strVal)){
			return Variable.getVariableValue(strVal);
		}
		if (Variable.isInteger(strVal)){
			return new Value(VariableTypes.Integer, new BigInteger(strVal));
		}
		if (Variable.isDouble(strVal)){
			return new Value(VariableTypes.Double, new BigDecimal(strVal));
		}
		if (Variable.isBool(strVal)){
			return new Value(VariableTypes.Boolean, Variable.convertToBool(strVal));
		}
		if (Variable.isString(strVal)){
			return Variable.convertToString(strVal);
		}
		Interpreter.throwError("Could not find a value for "+strVal);
		return new Value(VariableTypes.Nil, null);
	}

	@Override
	public String getStoreName() {
		if (isValSet){
			return null;
		}else{
			return strVal;
		}
	}
	
	@Override
	public Operator getOperator(){
		return null;
	}
	
}
