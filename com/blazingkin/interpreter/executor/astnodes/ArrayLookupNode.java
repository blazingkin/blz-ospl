package com.blazingkin.interpreter.executor.astnodes;

import java.math.BigInteger;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ArrayLookupNode extends BinaryNode {

	public ArrayLookupNode(ASTNode[] args) {
		super(Operator.arrayLookup, args);
		if (args.length != 2){
			Interpreter.throwError("Array Lookup did not have 2 arguments");
		}
	}
	
	@Override
	public Value execute(){
		if (args[0].getStoreName() == null){
			Value arr = args[0].execute();
			if (arr.type != VariableTypes.Array){
				Interpreter.throwError("Did not know how to access "+args[0]+" as an array");
			}
			BigInteger index = Variable.getIntValue(args[1].execute());
			return Variable.getValueOfArray(arr, index);
		}
		String name = args[0].getStoreName();
		BigInteger index = Variable.getIntValue(args[1].execute());
		return Variable.getValueOfArray(name, index);
	}

}
