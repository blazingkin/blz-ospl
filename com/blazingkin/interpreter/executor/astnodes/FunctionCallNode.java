package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.sourcestructures.Method;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class FunctionCallNode extends BinaryNode {

	public boolean passByReference = false;
	
	public FunctionCallNode(ASTNode[] args) {
		super(Operator.functionCall, args);
		if (args.length > 2){
			Interpreter.throwError("Somehow a function had a weird number of arguments");
		}
		if (this.args[0] instanceof ValueASTNode){
			ValueASTNode fName = (ValueASTNode) this.args[0];
			if (fName.getStoreName().contains("!")){
				passByReference = true;
				this.args[0] = new ValueASTNode(fName.getStoreName().replace("!", ""));
			}
		}
	}
	
	@Override
	public Value execute(Context con){
		Value methodVal = args[0].execute(con);
		if (methodVal.type != VariableTypes.Method && methodVal.type != VariableTypes.Closure){
			Interpreter.throwError("Tried to call a non-method "+methodVal);
		}
		Method toCall = (Method) methodVal.value;
		Value[] args;
		if (this.args.length == 1){	// No Args
			args = new Value[0];
		}else{	// Some args
			if (this.args[1].getOperator() == Operator.CommaDelimit){
				args = Variable.getValueAsArray(this.args[1].execute(con));
			}else{
				Value[] arg = {this.args[1].execute(con)};
				args = arg;
			}
		}
		return Executor.functionCall(toCall, args, passByReference);
	}

}
