package com.blazingkin.interpreter.variables;

import com.blazingkin.interpreter.executor.Executor;

public class Context {
	private Context parent;
	private int contextID;
	private static int maxDepth = 500;
	public Context(){
		parent = Executor.getCurrentContext();
		Context p = parent;
		int depth = 0;
		while (! (p == Variable.getGlobalContext()) && depth < maxDepth){
			p = p.parent;
			depth ++;
		}
		if (depth == maxDepth){
			parent = Variable.getGlobalContext();
		}
		
		contextID = getUID();
	}
	public int getID(){
		return contextID;
	}
	
	public Context getParentContext(){
		return parent;
	}
	
	
	private static int getUID(){
		return contextCounter++;
	}
	private static int contextCounter = 0;
	
	
}
