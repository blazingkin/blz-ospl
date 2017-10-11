package com.blazingkin.interpreter.executor.astnodes;

import java.math.BigInteger;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.OperatorASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.variables.BLZObject;
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
	public Value execute(){
		Value object = args[0].execute();
		if (object.type != VariableTypes.Object){
			Interpreter.throwError("Did not know how to handle dot operator on non-object");
		}
		BLZObject obj = (BLZObject) object.value;
		if (args[1].getOperator() == Operator.functionCall){
			OperatorASTNode fCall = (OperatorASTNode) args[1];
			fCall.args[0] = new ValueASTNode(Variable.getValue(fCall.args[0].getStoreName(), obj.objectContext));
			return args[1].execute();
		}
		if (args[1].getOperator() == Operator.arrayLookup){
			OperatorASTNode arrLookup = (OperatorASTNode) args[1];
			BigInteger index = Variable.getIntValue(arrLookup.args[1].execute());
			return Variable.getValueOfArray(Variable.getValue(arrLookup.args[0].getStoreName(), obj.objectContext), index);
		}
		return Variable.getValue(args[1].getStoreName(), obj.objectContext);
	}

}
