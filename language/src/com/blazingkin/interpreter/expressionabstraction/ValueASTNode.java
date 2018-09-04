package com.blazingkin.interpreter.expressionabstraction;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ValueASTNode extends ASTNode {

	public Value val;
	String strVal;
	boolean isValSet = false;
	public ValueASTNode(String val){
		try {
			if (Variable.canGetValue(val)){
				this.val = Variable.getValue(val);
				isValSet = true;
			}else{
				this.strVal = val;
			}
		}catch(BLZRuntimeException e){
			this.strVal = val;
		}
	}
	
	public ValueASTNode(Value val){
		this.val = val;
		isValSet = true;
		if (val.type == VariableTypes.String) {
			strVal = (String) val.value;
		}
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
		return false;
	}

	@Override
	public ASTNode collapse() {
		return this;
	}

	@Override
	public Value execute(Context con) throws BLZRuntimeException {
		if (isValSet){
			return val;
		}
		return con.getValue(strVal);
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
