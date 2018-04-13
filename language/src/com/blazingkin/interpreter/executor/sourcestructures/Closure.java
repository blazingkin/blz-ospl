package com.blazingkin.interpreter.executor.sourcestructures;

import com.blazingkin.interpreter.executor.astnodes.MethodNode;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.executor.Executor;

public class Closure extends MethodNode {

	public Context context;
	
	public Closure(Context con, MethodNode node){
		super(node);
		context = con;
	}

    public Value execute(Context c, Value[] values, boolean passByReference){
		Context methodContext = new Context(context);
		if (takesVariables){
            bindArguments(values, passByReference, methodContext);
		}
		Value result = body.execute(methodContext);
		Executor.setReturnMode(false);
        return result;
    }
	
	
}
