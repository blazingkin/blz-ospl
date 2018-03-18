package com.blazingkin.interpreter.variables;

import java.awt.MouseInfo;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.ArrayDeque;

import org.nevec.rjm.BigDecimalMath;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.sourcestructures.Method;

public class Variable {
	private static Context globalContext = new Context();
	
	public static Context getGlobalContext(){
		return globalContext;
	}	
	public static HashMap<BigInteger, Value> getArray(String arrayName){
		return getArray(arrayName, Executor.getCurrentContext());
	}
	public static HashMap<BigInteger, Value> getGlobalArray(String arrayName){
		return getArray(arrayName, getGlobalContext());
	}
	public static HashMap<BigInteger, Value> getArray(String arrayName, Context context){
		if (!context.variables.containsKey(arrayName)){
			setValue(arrayName, new Value(VariableTypes.Array, new HashMap<BigInteger, Value>()), context);
		}
		Value v = context.variables.get(arrayName);
		if (v.type == VariableTypes.Array && v.value instanceof HashMap<?, ?>){
			@SuppressWarnings("unchecked")
			HashMap<BigInteger, Value> arr = (HashMap<BigInteger, Value>)v.value;
			return arr;
		}else if(v.value instanceof Value[]){
			HashMap<BigInteger, Value> cast = new HashMap<BigInteger, Value>();
			Value[] vals = (Value[]) v.value;
			for (int i = 0; i < vals.length; i++){
				cast.put(BigInteger.valueOf(i), vals[i]);
			}
			setValue(arrayName, new Value(VariableTypes.Array, cast), context);
			return cast;
		}else{
			Interpreter.throwError("Attempted to get "+arrayName+" as an array, but it is not one");
			return null;
		}
	}
	
	public static void clearVariables(){
		for (Context c : Context.contexts){
			c.variables.clear();
		}
		Context.contexts = new ArrayList<Context>();
		globalContext = new Context(null);
	}
	
	public static void killContext(Context con){
		if (!con.equals(getGlobalContext())){
			con.variables.clear();
		}
	}
	
	//Gets a local value (the scope of these variables is the function they are declared in
	public static Value getValue(String key){
		return getValue(key, Executor.getCurrentContext());
	}
	
	public static Value[] getValueAsArray(Value v){
		if (v.type == VariableTypes.Array){
			return (Value[]) v.value;
		}
		Value[] ret = {v};
		return ret;
	}
	
	//This gets a global value (i.e. its scope is the entire program)
	public static Value getGlobalValue(String key){
		return getValue(key, getGlobalContext());
	}
	
	public static Value addValues(Value v1, Value v2){
		if (v1.type == VariableTypes.Integer && v2.type == VariableTypes.Integer){
			return new Value(VariableTypes.Integer, getIntValue(v1).add(getIntValue(v2)));
		}
		if ((v1.type == VariableTypes.Integer || v1.type == VariableTypes.Double) &&
				(v2.type == VariableTypes.Integer || v2.type == VariableTypes.Double)){
			return new Value(VariableTypes.Double, getDoubleVal(v1).add(getDoubleVal(v2)));
		}
		if (isValRational(v1) && isValRational(v2)){
			BLZRational rat = getRationalVal(v1).add(getRationalVal(v2));
			if (rat.den.equals(BigInteger.ONE)){
				return new Value(VariableTypes.Integer, rat.num);
			}
			return new Value(VariableTypes.Rational, rat);
		}
		if (v1.type == VariableTypes.String || v2.type == VariableTypes.String){
			String s1 = v1.toString();
			String s2 = v2.toString();
			return new Value(VariableTypes.String, s1+s2);
		}
		Interpreter.throwError("Failed Adding Variables "+v1.value+" and "+v2.value);
		return new Value(VariableTypes.Nil, null);
	}
	
	public static Value subValues(Value v1, Value v2){
		if (v1.type == VariableTypes.Integer && v2.type == VariableTypes.Integer){
			return new Value(VariableTypes.Integer, getIntValue(v1).subtract(getIntValue(v2)));
		}
		if ((v1.type == VariableTypes.Integer || v1.type == VariableTypes.Double) &&
				v2.type == VariableTypes.Integer || v2.type == VariableTypes.Double){
			return new Value(VariableTypes.Double, getDoubleVal(v1).subtract(getDoubleVal(v2)));
		}
		if (isValRational(v1) && isValRational(v2)){
			BLZRational val2 = getRationalVal(v2);
			BLZRational rat = getRationalVal(v1).subtract(val2);
			if (rat.den.equals(BigInteger.ONE)){
				return new Value(VariableTypes.Integer, rat.num);
			}
			return new Value(VariableTypes.Rational, rat);
		}
		Interpreter.throwError("Failed Subtracting Variables "+v1.value+" and "+v2.value);
		return new Value(VariableTypes.Nil, null);
	}
	
	public static Value mulValues(Value v1, Value v2){
		if (v1.type == VariableTypes.Integer && v2.type == VariableTypes.Integer){
			return new Value(VariableTypes.Integer, ((BigInteger)v1.value).multiply((BigInteger)v2.value));
		}
		if (isValRational(v1) && isValRational(v2)){
			BLZRational rat = getRationalVal(v1).multiply(getRationalVal(v2));
			if (rat.den.equals(BigInteger.ONE)){
				return new Value(VariableTypes.Integer, rat.num);
			}
			return new Value(VariableTypes.Rational, rat);
		}
		if ((isValRational(v1) || isValDouble(v1)) && (isValRational(v2) || isValDouble(v2))){
			return new Value(VariableTypes.Double, getDoubleVal(v1).multiply(getDoubleVal(v2)));
		}
		Interpreter.throwError("Failed Multiplying Variables "+v1.value+" and "+v2.value);
		return new Value(VariableTypes.Nil, null);
	}
	
	public static Value modVals(Value val, Value quo) {
		if (val.type == VariableTypes.Integer && quo.type == VariableTypes.Integer){
			return new Value(VariableTypes.Integer, ((BigInteger)val.value).mod((BigInteger)quo.value));
		}
		if (isDecimalValue(val) && isDecimalValue(quo)){
			return new Value(VariableTypes.Double, getDoubleVal(val).remainder(getDoubleVal(quo)));
		}
		Interpreter.throwError("Attempted to perform a modulus on non-integers, "+val+" and "+quo);
		return new Value(VariableTypes.Nil, null);
	}
	
	public static Value divVals(Value top, Value bottom){
		if (top.type == VariableTypes.Integer && bottom.type == VariableTypes.Integer){
			return Value.rational((BigInteger)top.value, (BigInteger)bottom.value);
		}
		if (Variable.isValRational(top) && Variable.isValRational(bottom)){
			BLZRational toprat = Variable.getRationalVal(top);
			BLZRational botrat = Variable.getRationalVal(bottom);
			BLZRational botratopp = getRationalVal(Value.rational(botrat.den, botrat.num));
			BLZRational prod = toprat.multiply(botratopp);
			if (prod.den.equals(BigInteger.ONE)){
				return new Value(VariableTypes.Integer, prod.num);
			}
			return new Value(VariableTypes.Rational, prod);
		}
		if ((Variable.isValDouble(top) || Variable.isValRational(top))
				&& (Variable.isValDouble(bottom) || Variable.isValRational(bottom))){
			BigDecimal dtop = Variable.getDoubleVal(top);
			BigDecimal dbot = Variable.getDoubleVal(bottom);
			return new Value(VariableTypes.Double, dtop.divide(dbot, MathContext.DECIMAL128));
		}
		Interpreter.throwError("Could not convert one of "+top+" or "+bottom+" to a dividable type");
		return new Value(VariableTypes.Nil, null);
	}
	
	public static Value expValues(Value v1, Value v2){
		if (isValInt(v1) && isValInt(v2)){
			return new Value(VariableTypes.Integer, ((BigInteger)v1.value).pow(((BigInteger)v2.value).intValue()));
		}
		if (isDecimalValue(v1) && isDecimalValue(v2)){
			BigDecimal d1 = getDoubleVal(v1);
			BigDecimal d2 = getDoubleVal(v2);
			return new Value(VariableTypes.Double, powerBig(d1,d2));
		}
		Interpreter.throwError("Failed Taking an Exponent with "+v1.value + " and "+v2.value);
		return new Value(VariableTypes.Nil, null);
	}
	
	public static Value logValues(Value v1, Value v2){
		if (isDecimalValue(v1) && isDecimalValue(v2)){
			BigDecimal d1 = getDoubleVal(v1);
			BigDecimal d2 = getDoubleVal(v2);
			return new Value(VariableTypes.Double, BigDecimalMath.log(d1.setScale(12, RoundingMode.HALF_UP)).divide(BigDecimalMath.log(d2.setScale(12, RoundingMode.HALF_UP)), MathContext.DECIMAL64));
		}
		if (isDecimalValue(v1) && (v2.type == VariableTypes.String && ((String) v2.value).toLowerCase().equals("e"))){
			return new Value(VariableTypes.Double, BigDecimalMath.log(getDoubleVal(v1).setScale(12, RoundingMode.HALF_UP)));
		}
		Interpreter.throwError("Failed Taking an Logarithm with "+v1.value + " and "+v2.value);
		return new Value(VariableTypes.Nil, null);
	}
	
	public static Value lnValue(Value v){
		if (isDecimalValue(v)){
			BigDecimal d1 = getDoubleVal(v);
			return new Value(VariableTypes.Double, BigDecimalMath.log(d1.setScale(12, RoundingMode.HALF_UP)));
		}
		Interpreter.throwError("Failed taking the natural log of "+v.value);
		return new Value(VariableTypes.Nil, null);
	}
	
	
	static Pattern quotePattern = Pattern.compile("^\".*\"$");
	public static Value getValue(String line, Context con){
		if (Variable.isDouble(line)){	//If its a double, then return it
			if (isInteger(line)){	//If its an integer, then return it
				return new Value(VariableTypes.Integer, new BigInteger(line));
			}
			return new Value(VariableTypes.Double, new BigDecimal(line));
		}
		if (Variable.isBool(line)){		//If its a bool, then return it
			return new Value(VariableTypes.Boolean, Variable.convertToBool(line));
		}
		if (con.hasValue(line)) {
			return con.getValue(line);
		}
		if (line.length() >= 2){
			if (line.charAt(0) == '"' && line.charAt(line.length() - 1) == '"'){
				return new Value(VariableTypes.String, line.replace("\"",""));
			}
			if (line.startsWith("{") && line.endsWith("}")) {
				String gp = line.substring(1, line.length() - 1);
				gp = gp.substring(1, gp.length()-1);
				for (SystemEnv env : SystemEnv.values()){
					if (gp.equals(env.name)){
						return Variable.getEnvVariable(env);
					}
				}
			}
		}
		return con.getValue(line);
	}
	
	public static Value getVariableValue(String line){
		return getVariableValue(line, Executor.getCurrentContext());
	}
	
	public static Value getVariableValue(String line, Context con){
		if (con.variables.containsKey(line)){
			return con.variables.get(line);
		}
		if (con.getParentContext() != getGlobalContext()){
			return getVariableValue(line, con.getParentContext());
		}
		Interpreter.throwError("Could not find a value for: "+line);
		return new Value(VariableTypes.Nil, null);
	}
	
	public static boolean canGetValue(String line){
		return isInteger(line) || isDouble(line) || isBool(line) || isString(line);
	}
	
	
	public static void setValue(String key, Value value){		//sets a local variable
		setValue(key, value, Executor.getCurrentContext());
	}
	
	public static void setValue(String key, Value value, Context con){
		con.variables.put(key, value);
	}

	/* Checks the string one character at a time to see if it is an int string */
	public static boolean isInteger(String s) {
		if (!s.isEmpty() && s.charAt(0) == '-'){
			s = s.substring(1);
		}
		for (char c : s.toCharArray()){
			if (!Character.isDigit(c)){
				return false;
			}
		}
		return !s.isEmpty();
	}
	
	/* Checks the string one character at a time to see if it is a double string */
	public static boolean isDouble(String s){
		if (!s.isEmpty() && s.charAt(0) == '-'){
			s = s.substring(1);
		}
		boolean seenDecimal = false;
		for (char c : s.toCharArray()){
			if (!Character.isDigit(c)){
				if (c != '.'){
					return false;
				}
				if (seenDecimal){
					return false;
				}
				seenDecimal = true;
			}
		}
		return !s.isEmpty();
	}
	
	
	public static boolean isBool(String s){
		/* Crazy inlining here for speed purposes */
		/* Basically it checks if the string is (case-insensitively) equal to true or false */
		return (s.length() == 4 && 	(s.charAt(0) == 't' || s.charAt(0) == 'T') &&
									(s.charAt(1) == 'r' || s.charAt(1) == 'R') &&
									(s.charAt(2) == 'u' || s.charAt(2) == 'U') &&
									(s.charAt(3) == 'e' || s.charAt(3) == 'E')) ||
			 ((s.length() == 5) && 	(s.charAt(0) == 'f' || s.charAt(0) == 'F') &&
									(s.charAt(1) == 'a' || s.charAt(1) == 'A') &&
									(s.charAt(2) == 'l' || s.charAt(2) == 'L') &&
									(s.charAt(3) == 's' || s.charAt(3) == 'S') &&
									(s.charAt(4) == 'e' || s.charAt(4) == 'E'));
	}
	
	public static boolean isString(String s){
		return s.length() >= 2 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"';
	}
	
	public static boolean convertToBool(String s){
		String lower = s.toLowerCase();
		if (lower.equals("true")){
			return true;
		}
		return false;
	}
	
	public static Value getEnvVariable(SystemEnv se){
		switch(se){
		case time:
			return Value.doub((double)System.currentTimeMillis());
		case osName:
			return new Value(VariableTypes.String, System.getProperty("os.name"));
		case osVersion:
			return new Value(VariableTypes.String, System.getProperty("os.version"));
		case cursorPosX:
			return Value.integer(MouseInfo.getPointerInfo().getLocation().x);
		case cursorPosY:
			return Value.integer(MouseInfo.getPointerInfo().getLocation().y);
		case processUUID:
			if (Executor.getCurrentProcess() == null){
				return Value.integer(-1);
			}
			return Value.integer(Executor.getCurrentProcess().UUID);
		case processesRunning:
			return Value.integer(Executor.getRunningProcesses().size());
		case methodStack:
			String stackString = "";
			ArrayDeque<Method> stck = Executor.getMethodStack().clone();
			for (Method m : stck){
				stackString = m.functionName + "\n" + stackString;
			}
			stackString = stackString.trim();
			return new Value(VariableTypes.String, stackString);
		case runtimeStack:
			return Value.string(RuntimeStack.runtimeStack.toString());
		case lineReturns:
			if (Executor.getCurrentProcess() == null){
				return Value.integer(-1);
			}
			return Value.integer(Executor.getCurrentProcess().lineReturns.size());
		case version:
			//TODO update this every time
			return new Value(VariableTypes.String, "2.6");
		case runningFileLocation:
			if (Executor.getCurrentProcess() == null || !Executor.getCurrentProcess().runningFromFile){
				return new Value(VariableTypes.String, "SOFTWARE");
			}
			return new Value(VariableTypes.String, Executor.getCurrentProcess().readingFrom.getParentFile().getAbsolutePath());
		case runningFileName:
			if (Executor.getCurrentProcess() == null || !Executor.getCurrentProcess().runningFromFile){
				return new Value(VariableTypes.String, "SOFTWARE");
			}
			return new Value(VariableTypes.String, Executor.getCurrentProcess().readingFrom.getName());
		case runningFilePath:
			if (Executor.getCurrentProcess() == null || !Executor.getCurrentProcess().runningFromFile){
				return new Value(VariableTypes.String, "SOFTWARE");
			}
			return new Value(VariableTypes.String, Executor.getCurrentProcess().readingFrom.getAbsolutePath());
		case newline:
			return new Value(VariableTypes.String, "\n");
		case alt:
			return new Value(VariableTypes.String, "ALT");
		case back:
			return new Value(VariableTypes.String, "\b");
		case control:
			return new Value(VariableTypes.String, "CONTROL");
		case shift:
			return new Value(VariableTypes.String, "SHIFT");
		case systemKey:
			return new Value(VariableTypes.String, "SYSTEM_KEY");
		case tab:
			return new Value(VariableTypes.String, "\t");
		case caps:
			return new Value(VariableTypes.String, "CAPS_LOCK");
		case space:
			return new Value(VariableTypes.String, " ");
		case euler:
			return Value.doub(Math.E);
		case pi:
			return Value.doub(Math.PI);
		case context:
			return Value.integer(Executor.getCurrentContext().getID());
		case nil:
			return new Value(VariableTypes.Nil, null);
		default:
			return new Value(VariableTypes.Nil, null);
		}
	}
	
	public static Value getValueOfArray(String arrayName, BigInteger index, Context con){
		Value v = con.getValue(arrayName);
		if (v.value instanceof Value[]){
			Value[] arr = (Value[]) v.value;
			return arr[index.intValue()];
		}
		return new Value(VariableTypes.Nil, null);
	}
	
	public static Value getValueOfArray(Value value, BigInteger index) {
		if (value.type != VariableTypes.Array){
			Interpreter.throwError("Tried to access "+value+" as an array, but it is not one.");
		}
		if (value.value instanceof Value[]){
			Value[] arr = (Value[]) value.value;
			return arr[index.intValue()];
		}
		HashMap<?, ?> arr = (HashMap<?, ?>) value.value;
		return (Value) arr.get(index);
	}
	
	public static void setValueOfArray(String arrayName,BigInteger index, Value value, Context con){
		Value arr = con.getValue(arrayName);
		if (arr.type != VariableTypes.Array){
			Interpreter.throwError(arrayName+" was not a hash ("+arr.typedToString()+" instead)");
		}
		Value[] VArr = (Value[]) arr.value;
		if (VArr.length <= index.intValue()){
			Value[] NArr = new Value[index.intValue() + 1];
			for (int i = 0; i <= index.intValue(); i++){
				if (i < VArr.length){
					NArr[i] = VArr[i];
				}else{
					NArr[i] = Value.nil();
				}
			}
			
			NArr[index.intValue()] = value;
			setValue(arrayName, new Value(VariableTypes.Array, NArr));
		}else{
			VArr[index.intValue()] = value;
		}
	}
	
	public static void setValueOfHash(String hashName, Value key, Value newVal, Context con){
		if (!con.variables.containsKey(hashName)){
			HashMap<Value, Value> newHash = new HashMap<Value, Value>();
			newHash.put(key, newVal);
			setValue(hashName, new Value(VariableTypes.Hash, newHash), con);
			return;
		}
		Value hash = getValue(hashName, con);
		if (hash.type != VariableTypes.Hash){
			Interpreter.throwError(hashName+" was not a hash ("+hash.typedToString()+" instead)");
		}
		@SuppressWarnings("unchecked")
		HashMap<Value, Value> hsh = (HashMap<Value, Value>) hash.value;
		hsh.put(key, newVal);
	}
	
	public static Value getValueOfHash(String hashName, Value key, Context con){
		Value hash = con.getValue(hashName);
		if (hash.type != VariableTypes.Hash){
			Interpreter.throwError(hashName+" was not a hash ("+hash.typedToString()+" instead)");
		}
		@SuppressWarnings("unchecked")
		HashMap<Value, Value> hsh = (HashMap<Value, Value>) hash.value;
		return hsh.get(key);
	}
	
	public static boolean isValInt(Value v){
		if (v.type == VariableTypes.Rational){
			BLZRational rat = (BLZRational) v.value;
			return rat.den.equals(BigInteger.ONE);
		}
		return v.type == VariableTypes.Integer;
	}
	public static boolean isValDouble(Value v){
		return  v.type == VariableTypes.Double;
	}
	
	
	/**
	 * @param v - The value to check
	 * @return if the value is a real number (as in the mathematical set)
	 */
	public static boolean isDecimalValue(Value v){
		return v.type == VariableTypes.Integer || v.type == VariableTypes.Rational || v.type == VariableTypes.Double;
	}
	
	public static boolean isValRational(Value v){
		return v.type == VariableTypes.Integer || v.type == VariableTypes.Rational;
	}
	
	public static BLZRational getRationalVal(Value v){
		if (v.type == VariableTypes.Rational){
			return (BLZRational) v.value;
		}
		if (v.type == VariableTypes.Integer){
			return new BLZRational((BigInteger)v.value, BigInteger.ONE);
		}
		Interpreter.throwError("Attemted an illegal cast to a rational");
		return new BLZRational(BigInteger.ZERO, BigInteger.ZERO);
	}
	
	public static BigDecimal getDoubleVal(Value v){
		try{
			if (isValInt(v) || v.value instanceof Integer){
				return new BigDecimal(((BigInteger) v.value));
			}
			if (v.type == VariableTypes.Rational){
				BLZRational rat = (BLZRational) v.value;
				BigDecimal num = new BigDecimal(rat.num);
				BigDecimal den = new BigDecimal(rat.den);
				return num.divide(den, MathContext.DECIMAL128);
			}
			return (BigDecimal)v.value;
		}catch(Exception e){
			Interpreter.throwError("Attempted to cast "+v.value+" to a double and failed!");
			return BigDecimal.ZERO;
		}
	}
	public static BigInteger getIntValue(Value v){
		try{
			if (isValDouble(v) || v.value instanceof Double){
				return BigInteger.valueOf((long)((double) v.value));
			}
			if (v.type == VariableTypes.Rational){
				BLZRational rat = (BLZRational) v.value;
				return rat.num.divide(rat.den); 
			}
			return (BigInteger) v.value;
		}catch(Exception e){
			Interpreter.throwError("Attempted to cast "+v.value+" to an int and failed!");
			return BigInteger.ZERO;
		}
	}
	
	public static BigDecimal powerBig(BigDecimal base, BigDecimal exponent) {
		return BigDecimalMath.exp(exponent.multiply(BigDecimalMath.log(base)));
	}
	
	public static VariableTypes typeOf(String name, Context con){
		return getValue(name, con).type;
	}


	
	
}
