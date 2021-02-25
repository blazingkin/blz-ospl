package com.blazingkin.interpreter.variables;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.blazingkin.interpreter.BLZRuntimeException;


import com.blazingkin.interpreter.BLZNoVariableFoundException;

public class Context {
	private Context parent;
	public int contextID;
	private static int maxDepth = 500;
	public HashMap<String, Value> variables = new HashMap<String, Value>();
	

	private Context(int unused) {
		parent    = null;
		contextID = getUID();
		contexts.add(this);
	}

	static Context theGlobalContext = null;
	public static Context globalSingleton() {
		if (theGlobalContext == null) {
			theGlobalContext = new Context(1);
			theGlobalContext.setValue("nil", Value.nil());
		}
		return theGlobalContext;
	}

	public static void clearTheGlobalContext() {
		theGlobalContext = null;
	}

	public Context(){
		parent = Context.globalSingleton();
		Context p = parent;
		int depth = 0;
		while ((p != Context.globalSingleton()) && depth < maxDepth){
			p = p.parent;
			depth++;
		}
		if (depth == maxDepth){
			parent = Context.globalSingleton();
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

	public Context duplicate(int depth) {
		if (depth > maxDepth) {
			return this;
		}
		Context newParent;
		if (this.parent == null) {
			newParent = new Context();
		}else{
			newParent = this.parent.duplicate(depth + 1);
		}
		Context newContext = new Context(newParent);
		for (String key : variables.keySet()) {
			newContext.setValueInPresent(key, variables.get(key));
		}
		return newContext;
	}
	
	public Context getParentContext(){
		return parent;
	}
	
	public boolean hasValue(String s){
		return variables.containsKey(s);
	}
	

	public Value getValue(String s) throws BLZRuntimeException {
		if (hasValue(s)){
			return variables.get(s);
		}
		
		if (parent != null){
			try {
				return parent.getValue(s);
			}catch(BLZNoVariableFoundException e){
				throw new BLZNoVariableFoundException(e, getAllBoundNames(), s);
			}
		}else{
			throw new BLZNoVariableFoundException("Could not find a value for "+s, getAllBoundNames(), s);
		}
	}

	public Set<String> getAllBoundNames() {
		if (parent == null || this == parent) {
			return variables.keySet();
		}
		Set<String> result = new HashSet<String>();
		for (String s : variables.keySet()) {
			result.add(s);
		}
		for (String s : parent.getAllBoundNames()) {
			result.add(s);
		}
		return result;
	}


	
	public boolean inContext(String storeName){
		if (variables.containsKey(storeName)){
			return true;
		}
		if (parent == null || this == parent){
			return false;
		}
		return parent.inContext(storeName);
	}
	
	public void setValue(String storeName, Value value){
		if (hasValue(storeName) || !inContext(storeName) ||
			this == Context.globalSingleton() || parent == null){
			variables.put(storeName, value);
		}else{
			parent.setValue(storeName, value);
		}
	}

	public void setValueInPresent(String storeName, Value value){
		variables.put(storeName, value);
	}
	
	
	private static int getUID(){
		return contextCounter++;
	}
	private static int contextCounter = 0;
	
	public int hashCode(){
		return contextID;
	}
	
	public static List<Context> contexts = Collections.synchronizedList(new ArrayList<Context>());
}
