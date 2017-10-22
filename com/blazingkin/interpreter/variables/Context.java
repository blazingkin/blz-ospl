package com.blazingkin.interpreter.variables;

import java.util.ArrayList;
import java.util.HashMap;

import com.blazingkin.interpreter.executor.Executor;

public class Context {
	private Context parent;
	public int contextID;
	private static int maxDepth = 500;
	public HashMap<String, Value> variables = new HashMap<String, Value>();
	
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
		contexts.add(this);
	}
	
	public Context(Context parent){
		contextID = getUID();
		this.parent = parent;
		contexts.add(this);
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
	
	public int hashCode(){
		return contextID;
	}
	
	public static ArrayList<Context> contexts = new ArrayList<Context>();
}
