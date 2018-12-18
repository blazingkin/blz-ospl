package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class Closure extends MethodNode {

	public Context context;
	
	public Closure(Context con, MethodNode node){
		super(node);
		context = con;
	}

    public Value execute(Context c, Value[] values, boolean passByReference) throws BLZRuntimeException{
		Context methodContext = new Context(context);
		if (takesVariables){
            bindArguments(values, passByReference, methodContext);
		}
		Value result = body.execute(methodContext);
		Executor.setReturnMode(false);
        return result;
	}
	
	
}
