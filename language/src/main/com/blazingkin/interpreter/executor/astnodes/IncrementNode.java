package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.UnaryNode;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class IncrementNode extends UnaryNode {

	public IncrementNode(ASTNode[] args) {
		super(Operator.Increment, args);
		if (args.length != 1){
			Interpreter.throwError("Increment had more than one variable");
		}
	}
	
	@Override
	public Value execute(Context con) throws BLZRuntimeException{
		String storeName = args[0].getStoreName();
		if (storeName == null){
			Interpreter.throwError("Did not know how to increment: "+args[0]);
		}
		Value add = Variable.addValues(con.getValue(storeName), Value.integer(1));
		con.setValue(storeName, add);
		return add;
	}

}
