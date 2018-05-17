package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.UnaryNode;
import com.blazingkin.interpreter.variables.Context;
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
	public Value execute(Context con) throws BLZRuntimeException{
		String storeName = args[0].getStoreName();
		if (storeName == null){
			Interpreter.throwError("Did not know how to decrement: "+args[0]);
		}
		Value sub = Variable.subValues(con.getValue(storeName), Value.integer(1));
		con.setValue(storeName, sub);
		return sub;
	}

}
