package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class GreaterThanEqualsNode extends BinaryNode {

	public GreaterThanEqualsNode(ASTNode[] args) {
		super(Operator.GreaterThanEqual, args);
		if (args.length != 2){
			Interpreter.throwError("Greater Than Or Equal did not have 2 arguments");
		}
	}

	public boolean canModify() {
		return args[0].canModify() || args[1].canModify();
	}
	
	@Override
	public Value execute(Context con) throws BLZRuntimeException {
		Value v1 = args[0].execute(con);
		Value v2 = args[1].execute(con);
		if (v1.equals(v2)){
			return Value.bool(true);
		}
		return LessThanNode.lessThan(v2, v1);
	}

}
