package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.UnaryNode;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class DecrementNode extends UnaryNode {

	public DecrementNode(ASTNode[] args) {
		super(Operator.Decrement, args);
		if (args.length != 1){
			Interpreter.throwError("Decrement had more than one variable");
		}
	}
	
	@Override
	public Value execute(){
		if (args[0].getStoreName() == null){
			Interpreter.throwError("Did not know how to decrement: "+args[0]);
		}
		Variable.setValue(args[0].getStoreName(), Variable.subValues(Variable.getValue(args[0].getStoreName()), Value.integer(1)));
		return Variable.getValue(args[0].getStoreName());
	}

}
