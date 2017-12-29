package com.blazingkin.interpreter.executor.astnodes;

import java.math.BigInteger;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.OperatorASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class DotOperatorNode extends BinaryNode {

	public DotOperatorNode(ASTNode[] args) {
		super(Operator.DotOperator, args);
		if (args.length != 2){
			Interpreter.throwError("Dot Operator did not have 2 arguments");
		}
	}
	
	@Override
	public Value execute(Context con){
		Value object = args[0].execute(con);
		if (object.type != VariableTypes.Object){
			Interpreter.throwError("Did not know how to handle dot operator on non-object");
		}
		BLZObject obj = (BLZObject) object.value;
		if (args[1].getOperator() == Operator.arrayLookup){
			OperatorASTNode arrLookup = (OperatorASTNode) args[1];
			BigInteger index = Variable.getIntValue(arrLookup.args[1].execute(con));
			return Variable.getValueOfArray(obj.objectContext.getValue(arrLookup.args[0].getStoreName()), index);
		}
		return obj.objectContext.getValue(args[1].getStoreName());
	}

}
