package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.UnaryNode;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.SystemEnv;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class EnvironmentVariableLookupNode extends UnaryNode {

	public EnvironmentVariableLookupNode(ASTNode[] args) {
		super(Operator.environmentVariableLookup, args);
		if (args.length != 1){
			Interpreter.throwError("Environment Variable Lookup did not have 1 argument");
		}
	}
	
	@Override
	public Value execute(Context con){
		for (SystemEnv se : SystemEnv.values()){
			if (se.name.equals(args[0].getStoreName())){
				return Variable.getEnvVariable(se);
			}
		}
		System.out.println("Could Not Find Environment Variable "+args[0].getStoreName());
		return new Value(VariableTypes.Nil, null);
	}

}
