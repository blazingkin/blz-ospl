package com.blazingkin.interpreter.executor.sourcestructures;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.variables.Context;

public class Closure extends Method {

	public Context context;
	public Closure(Process parent, int line, String initLine) {
		super(parent, line, initLine);
	}

	public Closure(Process parent, int line, String initLine, Context con){
		super(parent, line, initLine);
		context = con;
	}
	
	public Closure(Context con, Method met){
		super(met.parent, met.lineNumber, met.functionName);
		context = con;
	}
	
	@Override
	public void onBlockEnd() {
		RuntimeStack.popContext();
		if (!Executor.getCurrentProcess().lineReturns.empty()){
			Executor.setLine(Executor.getCurrentProcess().lineReturns.pop());
		}else{
			RuntimeStack.pop();	// If there is nothing else in the current process to run, return to the previous process
		}
	}
	
}
