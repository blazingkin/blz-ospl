package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class Closure extends MethodNode {

	public Context context;
	public String source;
	
	public Closure(Context con, MethodNode node, String source){
		super(node);
		context = con;
		this.source = source;
	}

    public Value execute(Context c, Value[] values, boolean passByReference) throws BLZRuntimeException{
		Context methodContext = new Context(context);
		if (takesVariables){
			bindArguments(values, passByReference, methodContext);
		}
		try {
			Value result = body.execute(methodContext);
			Executor.setReturnMode(false);
			return result;
		}catch(BLZRuntimeException exception) {
			if (exception.exceptionValue != null){
					throw exception;
			}
			String message = "In " + source + "\nIn "+toString()+"\n"+exception.getMessage();
			throw new BLZRuntimeException(message, exception.alreadyCaught);
		}catch(StackOverflowError err) {
			String message = "In " + source + "\nIn "+toString()+"\n Stack Overflow";
			throw new BLZRuntimeException(message);
		}
	}
	
	
}
