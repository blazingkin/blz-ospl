package com.blazingkin.interpreter.executor.astnodes;

import java.math.BigDecimal;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class GreaterThanNode extends BinaryNode {

	public GreaterThanNode(ASTNode[] args) {
		super(Operator.GreaterThan, args);
		if (args.length != 2){
			Interpreter.throwError("Greater Than did not have 2 arguments");
		}
	}
	
	@Override
	public Value execute(Context con){
		Value v1 = args[0].execute(con);
		Value v2 = args[1].execute(con);
		return LessThanNode.lessThan(v2, v1);
	}

}
