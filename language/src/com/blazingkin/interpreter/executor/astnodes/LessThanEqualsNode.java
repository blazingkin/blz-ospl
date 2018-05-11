package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class LessThanEqualsNode extends BinaryNode {

	public LessThanEqualsNode(ASTNode[] args) {
		super(Operator.LessThanEqual, args);
		if (args.length != 2){
			Interpreter.throwError("Less Than Or Equal did not have 2 arguments");
		}
	}
	
	@Override
	public Value execute(Context con){
		Value v1 = args[0].execute(con);
		Value v2 = args[1].execute(con);
		if (v1.equals(v2)){
			return Value.bool(true);
		}
		return LessThanNode.lessThan(v1, v2);
	}

}
