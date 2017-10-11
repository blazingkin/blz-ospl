package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.Method;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class FunctionCallNode extends BinaryNode {

	public FunctionCallNode(ASTNode[] args) {
		super(Operator.functionCall, args);
		if (args.length > 2){
			Interpreter.throwError("Somehow a function had a weird number of arguments");
		}
	}
	
	@Override
	public Value execute(){
		Value methodVal = args[0].execute();
		if (methodVal.type != VariableTypes.Method){
			Interpreter.throwError("Tried to call a non-method "+methodVal);
		}
		Method toCall = (Method) methodVal.value;
		Value[] args;
		if (this.args.length == 1){	// No Args
			args = new Value[0];
		}else{	// Some args
			if (this.args[1].getOperator() == Operator.CommaDelimit){
				args = Variable.getValueAsArray(this.args[1].execute());
			}else{
				Value[] arg = {this.args[1].execute()};
				args = arg;
			}
		}
		return Executor.functionCall(toCall, args);
	}

}
