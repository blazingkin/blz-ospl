package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.sourcestructures.Constructor;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.variables.BLZPrimitiveMethod;
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
		} else if (this.args[0] instanceof DotOperatorNode) { 
			DotOperatorNode left = (DotOperatorNode) this.args[0];
			ASTNode name = left.args[1];			
			if (name != null) {
				String sName = name.getStoreName();
				if (sName != null && sName.contains("!")) {
					passByReference = true;
				}
			}
		}
	}

	public boolean canModify() {
		/* This can be optimized further */
		/* Technically this is dependent on what the left side, but this is not trivial */
		return true;
	}
	
	@Override
	public Value execute(Context con) throws BLZRuntimeException {
		Value methodVal = args[0].execute(con);
		if (methodVal.type != VariableTypes.Method &&
			methodVal.type != VariableTypes.PrimitiveMethod &&
			methodVal.type != VariableTypes.Closure &&
			methodVal.type != VariableTypes.Constructor){
				throw new BLZRuntimeException(this, "Tried to call a non-method "+methodVal);
		}
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
		if (methodVal.type == VariableTypes.Constructor){
			Constructor constructor = (Constructor) methodVal.value;
			return Constructor.initialize(constructor, args, passByReference);
		}else if(methodVal.type == VariableTypes.PrimitiveMethod) {
			BLZPrimitiveMethod pm = (BLZPrimitiveMethod) methodVal.value;
			passByReference = pm.passByReference;
			Value[] nargs = new Value[args.length + 1];
			nargs[0] = pm.v;
			for (int i = 1; i < nargs.length; i++) {
				nargs[i] = args[i - 1];
			}
			args = nargs;
			methodVal = new Value(VariableTypes.Method, pm.m);
		}
		MethodNode toCall = (MethodNode) methodVal.value;
		return toCall.execute(con, args, passByReference || !toCall.canModify());
	}

}
