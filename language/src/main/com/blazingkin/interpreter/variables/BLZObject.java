package com.blazingkin.interpreter.variables;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.astnodes.Closure;

public class BLZObject implements Cloneable {

	public Context objectContext;
	public BLZObject(Context parent){
		objectContext = new Context(parent);
	}
	
	public BLZObject(){
		objectContext = new Context();
	}
	
	@Override
	public BLZObject clone(){
		try {
			if (objectContext.hasValue("copy")) {
				Value copyVal = objectContext.getValue("copy");
				if (copyVal.value instanceof Closure) {
					Closure copyMethod = (Closure) copyVal.value;
					Value[] args = {};
					Value result = copyMethod.execute(copyMethod.context, args, false);
					if (result.type == VariableTypes.Object) {
						return (BLZObject) result.value;
					}
				}
			}
		}catch(BLZRuntimeException e){
			
		}
		BLZObject newObj = new BLZObject();
		for (String s : objectContext.variables.keySet()){
			newObj.objectContext.setValue(s, objectContext.variables.get(s));
		}
		return newObj;
	}

	public boolean equals(Object other){
		try {
		if (other instanceof BLZObject){
			if (objectContext.hasValue("==")){
				Value eqValue = objectContext.getValue("==");
				if (eqValue.value instanceof Closure){
					Closure eqMethod = (Closure) eqValue.value;
					Value[] args = {Value.obj((BLZObject) other)};
					Value result =  eqMethod.execute(eqMethod.context, args, false);
					if (result.type == VariableTypes.Boolean){
						return (boolean) result.value;
					}else if (result.type == VariableTypes.Nil){
						return false;
					}else{
						return true;
					}
				}
			}
		}
		return this == other;
		}catch(BLZRuntimeException e){
			return this == other;
		}
	}
	
	public String toString(){
		try{
			if (objectContext.hasValue("show")){
				Value showAs = objectContext.getValue("show");
				if (showAs.value instanceof Closure){
					Closure showMethod = (Closure) showAs.value;
					Value[] args = {};
					Value result = showMethod.execute(showMethod.context, args, false);
					if (result.value != this){
						return result.toString();
					}
				}
			}
			if (objectContext.hasValue("type")) {
				return "<" + objectContext.getValue("type") + " " + hashCode() + ">";
			}
		}catch(BLZRuntimeException e){
			
		}
		return "<Object "+hashCode()+">";
	}

}
