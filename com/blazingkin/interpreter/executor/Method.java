package com.blazingkin.interpreter.executor;

import java.util.ArrayList;

public class Method {
	
	public int UUID = Executor.getUUID();
	public Process parent;
	public int lineNumber;
	public String functionName;
	
	public Method(Process parent, int line, String name){
		this.parent = parent;
		lineNumber = line;
		functionName = name;
	}
	
	public boolean isItThis(String name, int parentID){
		return (functionName.equals(name) && (parentID == parent.UUID));
	}
	
	public static Method contains(ArrayList<Method> ar, String s){
		for (int i = 0; i < ar.size(); i++){
			if (ar.get(i).functionName.equals(s)){
				return ar.get(i);
			}
				
		}
		return null;
	}
	
}
