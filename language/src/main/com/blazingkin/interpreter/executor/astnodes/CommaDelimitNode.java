package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
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
	public Value execute(Context con) throws BLZRuntimeException {
		Value[] v1, v2;
		
		// This is not the cleanest way to do this
		// However it fixes a bug if one of the args evaluates to an array
		if (args[0].getOperator() == Operator.CommaDelimit){
			v1 = Variable.getValueAsArray(args[0].execute(con));
		}else{
			v1 = new Value[1];
			v1[0] = args[0].execute(con);
		}
		if (args[1].getOperator() == Operator.CommaDelimit){
			v2 = Variable.getValueAsArray(args[1].execute(con));
		}else{
			v2 = new Value[1];
			v2[0] = args[1].execute(con);
		}
		
		
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
