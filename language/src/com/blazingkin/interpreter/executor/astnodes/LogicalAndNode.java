package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class LogicalAndNode extends BinaryNode {

	public LogicalAndNode(ASTNode[] args) {
		super(Operator.LogicalAnd, args);
		if (args.length != 2){
			Interpreter.throwError("Logical And did not have 2 arguments");
		}
	}

	@Override
	public Value execute(Context c) {
		Value left = args[0].execute(c);
		if (left.type != VariableTypes.Boolean){
			Interpreter.throwError("Logical And given non-boolean: "+left);
		}
		if (!((boolean) left.value)){
			// Short circuit if we can
			return Value.bool(false);
		}
		Value right = args[1].execute(c);
		if (right.type != VariableTypes.Boolean){
			Interpreter.throwError("Logical And given non-boolean: "+right);
		}
		return right;
	}

}
