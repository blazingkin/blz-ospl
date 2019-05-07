package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class LogicalOrNode extends BinaryNode {
	
	public LogicalOrNode(ASTNode[] args) {
		super(Operator.LogicalOr, args);
		if (args.length != 2){
			Interpreter.throwError("Logical Or did not have 2 arguments");
		}
	}

	public boolean canModify() {
		return args[0].canModify() || args[1].canModify();
	}

	@Override
	public Value execute(Context c) throws BLZRuntimeException {
		Value left = args[0].execute(c);
		if (left.type != VariableTypes.Boolean){
			throw new BLZRuntimeException(this, "Logical Or given non-boolean: "+left);
		}
		if (((boolean) left.value)){
			// Short circuit if we can
			return Value.bool(true);
		}
		Value right = args[1].execute(c);
		if (right.type != VariableTypes.Boolean){
			throw new BLZRuntimeException(this, "Logical Or given non-boolean: "+right);
		}
		return right;
	}

	
}
