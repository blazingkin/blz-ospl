package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.UnaryNode;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class ArrayLiteralNode extends UnaryNode {

	public ArrayLiteralNode(ASTNode[] args) {
		super(Operator.arrayLiteral, args);
		if (args.length != 1){
			Interpreter.throwError("Array Literal did not have 1 argument");
		}
	}

	public boolean canModify() {
		return args[0].canModify();
	}
	
	@Override
	public Value execute(Context con) throws BLZRuntimeException {
		return Value.arr(ExpressionExecutor.extractCommaDelimits(args[0], con));
	}

}
