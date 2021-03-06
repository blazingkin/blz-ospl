package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class AdditionNode extends BinaryNode {

	protected long add(long left, long right){
		return left + right;
	}

	public boolean canModify() {
		return args[0].canModify() || args[1].canModify();
	}

	public AdditionNode(ASTNode[] args) throws SyntaxException {
		super(Operator.Addition, args);
		if (args.length != 2){
			throw new SyntaxException("Addition node did not have 2 arguments");
		}
	}


	
	@Override
	public Value execute(Context con) throws BLZRuntimeException { 
		return Variable.addValues(args[0].execute(con), args[1].execute(con));
	}

}
