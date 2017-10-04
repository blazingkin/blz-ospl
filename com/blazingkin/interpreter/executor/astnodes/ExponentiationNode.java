package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class ExponentiationNode extends BinaryNode {

	public ExponentiationNode(ASTNode[] args) {
		super(Operator.Exponentiation, args);
		if (args.length != 2){
			Interpreter.throwError("Exponentiation did not have 2 arguments");
		}
	}
	
	@Override
	public Value execute(){
		return Variable.expValues(args[0].execute(), args[1].execute());
	}

}