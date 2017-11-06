package com.blazingkin.interpreter.variables;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;

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
	
	public boolean hasValue(String s){
		return variables.containsKey(s);
	}
	
	private static Pattern curlyBracketPattern = Pattern.compile("^\\{\\S*\\}$");
	static Pattern quotePattern = Pattern.compile("^\".*\"$");
	public Value getValue(String s){
		if (hasValue(s)){
			return variables.get(s);
		}
		if (Variable.isInteger(s)){	//If its an integer, then return it
			return new Value(VariableTypes.Integer, new BigInteger(s));
		}
		if (Variable.isDouble(s)){	//If its a double, then return it
			return new Value(VariableTypes.Double, new BigDecimal(s));
		}
		if (Variable.isBool(s)){		//If its a bool, then return it
			return new Value(VariableTypes.Boolean, Variable.convertToBool(s));
		}
		
		Matcher quoteMatcher = quotePattern.matcher(s);
		if (quoteMatcher.find()){
			return new Value(VariableTypes.String, s.replace("\"",""));
		}
		Matcher curlyBracketMatcher = curlyBracketPattern.matcher(s);
		if (curlyBracketMatcher.find()){
			String gp = curlyBracketMatcher.group();
			gp = gp.substring(1, gp.length()-1);
			for (SystemEnv env : SystemEnv.values()){
				if (gp.equals(env.name)){
					return Variable.getEnvVariable(env);
				}
			}
			Interpreter.throwError("Failed to find an environment variable to match: "+gp);
		}
		
		if (parent != null && getParentContext() != Variable.getGlobalContext()){
			return parent.getValue(s);
		}else{
			Interpreter.throwError("Could not find a value for "+s);
			return Value.nil();
		}
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
		variables.put(storeName, value);
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
