package com.blazingkin.interpreter.executor.astnodes;

import java.math.BigInteger;
import java.lang.StringBuilder;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.OperatorASTNode;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class AssignmentNode extends BinaryNode {

	public AssignmentNode(ASTNode[] args) throws SyntaxException {
		super(Operator.Assignment, args);
		if (args.length != 2){
			Interpreter.throwError("Assignment did not have 2 arguments");
		}
	}
	
	// TODO handle multiple assignment (e.g a,b = 2,3)
	@Override
	public Value execute(Context con) throws BLZRuntimeException {
		String storeName = args[0].getStoreName();
		if (storeName == null){
			if (args[0].getOperator() == Operator.arrayLookup){
				OperatorASTNode lookupNode = (OperatorASTNode) args[0];
				String arrayName = lookupNode.args[0].getStoreName();
				VariableTypes type = Variable.typeOf(arrayName, con);
				if (type == VariableTypes.Array){
					BigInteger index = Variable.getIntValue(lookupNode.args[1].execute(con));
					if (index.intValue() < 0) {
						throw new BLZRuntimeException("Negative array index "+index.intValue());
					}
					Value newVal = args[1].execute(con);
					Variable.setValueOfArray(arrayName, index, newVal, con);
					return newVal;					
				}else if (type == VariableTypes.String){
					Value newVal = args[1].execute(con);
					if (newVal.type != VariableTypes.String){
						throw new BLZRuntimeException(this, "Expected "+ newVal +" to be a string");
					}
					String newString = (String) newVal.value;
					if (newString.length() != 1){
						throw new BLZRuntimeException(this, "Expected "+newVal+" to be a string of length 1");
					}
					String oldString = (String) con.getValue(arrayName).value;
					int index = Variable.getIntValue(lookupNode.args[1].execute(con)).intValue();
					if (index < 0 || oldString.length() <= index){
						throw new BLZRuntimeException(this, "Out of bounds! Tried to set index: "+index+" of string: "+oldString);
					}
					StringBuilder build = new StringBuilder(oldString);
					build.setCharAt(index, newString.charAt(0));
					Value newStrVal = Value.string(build.toString());
					Variable.setValue(arrayName, newStrVal, con);
					return newStrVal;
				}else{ // Assume it's a hash
					Value key = lookupNode.args[1].execute(con);
					Value newVal = args[1].execute(con);
					Value hash = lookupNode.args[0].execute(con);
					Variable.setValueOfHash(hash, key, newVal);
					return newVal;
				}
			}else if (args[0].getOperator() == Operator.DotOperator){
				OperatorASTNode dotNode = (OperatorASTNode) args[0];
				Value object = dotNode.args[0].execute(con);
				if (object.type != VariableTypes.Object){
					throw new BLZRuntimeException(this, "Tried accessing "+object.typedToString()+" as an object");
				}
				BLZObject obj = (BLZObject) object.value;
				Value newVal = args[1].execute(con);
				obj.objectContext.setValue(dotNode.args[1].getStoreName(), newVal);
				return newVal;
			}
			throw new BLZRuntimeException(this, "Did not know how to handle assignment of: "+args[0]);
		}
		con.setValue(storeName, args[1].execute(con));
		return con.getValue(storeName);
	}

}
