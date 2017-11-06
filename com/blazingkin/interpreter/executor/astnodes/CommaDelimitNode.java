package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class CommaDelimitNode extends BinaryNode {

	public CommaDelimitNode(ASTNode[] args) {
		super(Operator.CommaDelimit, args);
		if (args.length != 2){
			Interpreter.throwError("Comma Delimiting did not have 2 arguments");
		}
	}
	
	@Override
	public Value execute(Context con){
		Value[] v1 = Variable.getValueAsArray(args[0].execute(con));
		Value[] v2 = Variable.getValueAsArray(args[1].execute(con));
		int size = v1.length + v2.length;
		Value[] newArr = new Value[size];
		for (int i = 0; i < v1.length; i++){
			newArr[i] = v1[i];
		}
		for (int i = 0; i < v2.length; i++){
			newArr[i + v1.length] = v2[i];
		}
		return Value.arr(newArr);
	}

}
