package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.sourcestructures.Process;

public class Closure extends MethodNode {

	public Context context;
	public String source;
	
	
	public Closure(Context con, MethodNode node, Process parent, String source){
		super(node);
		context = con;
		this.parent = parent;
		this.source = source;
	}


	public Value execute(Context c, Value[] values, boolean passByReference) throws BLZRuntimeException{
		boolean pushedParent = false;
        if (parent != null && (RuntimeStack.isEmpty() || RuntimeStack.getProcessStack().peek().UUID != parent.UUID)){
            pushedParent = true;
            RuntimeStack.push(parent);
        }
		Context methodContext = new Context(context);
		if (takesVariables){
			bindArguments(values, passByReference, methodContext);
		}
		try {
			Value result = body.execute(methodContext);
			if(pushedParent){
                RuntimeStack.pop();
            }
			Executor.setReturnMode(false);
			return result;
		}catch(BLZRuntimeException exception) {
			if (exception.exceptionValue != null){
					throw exception;
			}
			String message = "In " + source + "\nIn "+toString()+"\n"+exception.getMessage();
			if (pushedParent){
                String fileName = RuntimeStack.getProcessStack().peek().toString();
                message = "In " + fileName + "\n"+message;
                RuntimeStack.pop();
            }
			throw new BLZRuntimeException(message, exception.alreadyCaught);
		}catch(StackOverflowError err) {
			String message = "In " + source + "\nIn "+toString()+"\n Stack Overflow";
			if (pushedParent){
                String fileName = RuntimeStack.getProcessStack().peek().toString();
                message = "In " + fileName + "\n"+message;
                RuntimeStack.pop();
            }
			throw new BLZRuntimeException(message);
		}
	}
	
	
}
