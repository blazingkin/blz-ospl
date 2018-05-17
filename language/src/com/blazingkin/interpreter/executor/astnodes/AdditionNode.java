package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class AdditionNode extends BinaryNode {

	protected long add(long left, long right){
		return left + right;
	}

	public AdditionNode(ASTNode[] args) {
		super(Operator.Addition, args);
		if (args.length != 2){
			Interpreter.throwError("Addition did not have 2 arguments");
		}
	}
	
	@Override
	public Value execute(Context con) throws BLZRuntimeException { 
		return Variable.addValues(args[0].execute(con), args[1].execute(con));
	}

}
