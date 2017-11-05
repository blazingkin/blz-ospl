package com.blazingkin.interpreter.executor.astnodes;

import java.math.BigInteger;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.OperatorASTNode;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class AssignmentNode extends BinaryNode {

	public AssignmentNode(ASTNode[] args) {
		super(Operator.Assignment, args);
		if (args.length != 2){
			Interpreter.throwError("Assignment did not have 2 arguments");
		}
	}
	
	// TODO handle multiple assignment (e.g a,b = 2,3)
	@Override
	public Value execute(){
		if (args[0].getStoreName() == null){
			if (args[0].getOperator() == Operator.arrayLookup){
				OperatorASTNode lookupNode = (OperatorASTNode) args[0];
				String arrayName = lookupNode.args[0].getStoreName();
				VariableTypes type = Variable.typeOf(arrayName, Executor.getCurrentContext());
				if (type == VariableTypes.Array){
					BigInteger index = Variable.getIntValue(lookupNode.args[1].execute());
					Value newVal = args[1].execute();
					Variable.setValueOfArray(arrayName, index, newVal);
					return newVal;					
				}else{ // Assume it's a hash
					Value key = lookupNode.args[1].execute();
					Value newVal = args[1].execute();
					Variable.setValueOfHash(arrayName, key, newVal, Executor.getCurrentContext());
					return newVal;
				}
			}else if (args[0].getOperator() == Operator.DotOperator){
				OperatorASTNode dotNode = (OperatorASTNode) args[0];
				Value object = dotNode.args[0].execute();
				if (object.type != VariableTypes.Object){
					Interpreter.throwError("Tried accessing "+object.typedToString()+" as an object");
				}
				BLZObject obj = (BLZObject) object.value;
				Value newVal = args[1].execute();
				Variable.setValue(dotNode.args[1].getStoreName(), newVal, obj.objectContext);
				return newVal;
			}
			Interpreter.throwError("Did not know how to handle assignment of: "+args[0]);
		}
		Variable.setValue(args[0].getStoreName(), args[1].execute());
		return Variable.getValue(args[0].getStoreName());
	}

}
