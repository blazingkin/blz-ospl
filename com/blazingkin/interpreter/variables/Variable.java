package com.blazingkin.interpreter.variables;

import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.output.graphics.GraphicsExecutor;
//DO NOT MESS WITH THIS CLASS, IT DOES SOME INTERESTING VOODOO MAGIC AND IF YOU MESS WITH IT YOU WILL SCREW IT UP!!!!

public class Variable {
	public static HashMap<String, Value> variables = new HashMap<String,Value>();
	public static HashMap<String, HashMap<Integer, Value>> lists = new HashMap<String, HashMap<Integer, Value>>();
	public static HashMap<Integer, Value> getArray(String k){
		if (Executor.getCurrentMethod() == null || k.charAt(0) == '*'){
			return getGlobalArray(k);
		}
		return lists.get(Executor.getCurrentMethod().UUID+k);
	}
	public static HashMap<Integer, Value> getGlobalArray(String k){
		return lists.get(k);
	}
	
	
	public static Value getGlobalValue(String k){
		if (k.contains("[") && k.charAt(k.length()-1) == ']'){
			return   getValueOfArray(k);
		}
		
		if (variables.containsKey(k))
		{
			return variables.get(k);
		}else{
			return new Value(VariableTypes.Integer, 0);
		}
		
	}
	public static void setGlobalValue(String key, Value value){
		if (key.contains("[") && key.charAt(key.length()-1) == ']'){
			setValueOfArray(key, value);
			return;
		}
		variables.put(key, value);
	}
	public static Value getValue(String key){	//gets local only
		if (Executor.getCurrentMethod() == null || key.charAt(0) == '*'){
			return getGlobalValue(key);
		}
		String k = Executor.getCurrentMethodUUID() + key;
		if (k.contains("|")){
			k = parseString(k);
		}
		if (k.contains("[") && k.charAt(k.length()-1) == ']'){
			return getValueOfArray(k);
		}
		
		if (variables.containsKey(k))
		{
			return variables.get(k);
			
		}else{
			return new Value(VariableTypes.Integer, 0);
		}
	}
	
	public static String[] getArguments(String[] args){
		String[] done = new String[args.length];
		for (int i = 0; i < args.length; i++){
			done[i] = args[i].replace(",", "").replace("(", "").replace(")", "").trim();
		}
		return done;
	}
	
	public static Value getValueOfArray(String key){

		if (key.contains("[")){
			if (key.contains("|")){
				key = parseString(key);
			}
			if (key.charAt(0) == '|'){
				key = key.substring(1, key.length()-1);
			}
			String[] split = key.toLowerCase().split("\\[");
			String buildingString = "";
			for (int i = 1; i < split.length; i++){
				buildingString = buildingString + split[i];
				if (i != split.length-1){
					buildingString = buildingString+"[";
				}
			}
			split[1] = buildingString;
			if (lists.containsKey(split[0])){
				if (split[1].contains("[")){
					return lists.get(split[0]).get((Integer) getValueOfArray(split[1].substring(0,split[1].length()-1)).value);
				}
				if (lists.get(split[0]).containsKey(Integer.parseInt(parseString(split[1].substring(0, split[1].length()-1))))){
					return lists.get(split[0]).get(Integer.parseInt(parseString(split[1].substring(0, split[1].length()-1))));
				}else{
					lists.get(split[0]).put(Integer.parseInt(parseString(split[1].substring(0, split[1].length()-1))), new Value(VariableTypes.Integer, 0));
					return new Value(VariableTypes.Integer, 0);
				}
				
			}
			lists.put(split[0], new HashMap<Integer, Value>());
			return new Value(VariableTypes.Integer, 0);
		}
		return getValue(key);
	}
	
	
	
	public static void setValueOfArray(String key, Value value){
		if (key.contains("[") && key.charAt(key.length()-1) == ']'){
			String[] split = key.toLowerCase().split("\\[");
			String buildingString = "";
			for (int i = 1; i < split.length; i++){
				buildingString = buildingString + split[i];
				if (i != split.length-1){
					buildingString = buildingString+"[";
				}
			}
			split[1] = buildingString;
			if (lists.containsKey(split[0])){
				if (split[1].contains("[")){
					if (split[1].charAt(0) == '|'){
						split[1] = split[1].substring(1,split[1].length()-1);
					}
					int val = (Integer)getValueOfArray(split[1].substring(0,split[1].length()-1)).value;
					lists.get(split[0]).put(val, value);
					return;
				}
				lists.get(split[0]).put(Integer.parseInt(parseString(split[1].substring(0, split[1].length()-1))), value);
				return;
			}
			lists.put(split[0], new HashMap<Integer, Value>());
			lists.get(split[0]).put(Integer.parseInt(parseString(split[1].substring(0,split[1].length()-1))), value);
			return;
		}
		setValue(key, value);
	}

	
	public static boolean contains(String key){
		if (key.contains("[") && key.charAt(key.length()-1) == ']'){
			String[] split = key.toLowerCase().split("\\[");
			return lists.containsKey(split[0]);
		}
		return variables.containsKey(key.toLowerCase());
	}
	
	public static void setValue(String key, Value value){		//sets a local variable
		if (Executor.getCurrentMethod() == null || key.charAt(0) == '*'){
			setGlobalValue(key, value);
			return;
		}
		String k = Executor.getCurrentMethodUUID() + key;
		if (k.contains("[") && k.charAt(k.length()-1) == ']'){
			setValueOfArray(k, value);
			return;
		}
		variables.put(k.toLowerCase(), value);
	}
	
	public static String[] splits(String parse){
		ArrayList<String> a = new ArrayList<String>();
		String currentString = "";
		int bracketCount = 0;
		for (int i = 0; i < parse.length(); i++){
			if ((parse.charAt(i)=='|') && bracketCount == 0){
				a.add(currentString);
				currentString = "";
			}else{
				if (parse.charAt(i) == '['){
					bracketCount++;
				}
				if (parse.charAt(i) == ']'){
					bracketCount--;
				}
				currentString = currentString+parse.charAt(i);
			}
		}

		if (!currentString.equals("")){
			a.add(currentString);
		}
		String[] str = new String[a.size()];
		for (int i = 0; i < str.length; i++){
			str[i] = a.get(i);
		}
		return str;
		
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	public static boolean isDouble(String s){
		try{
			Double.parseDouble(s);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public static String getEnvVariable(SystemEnv se){
		String replaced = "";
		switch(se){
		case windowSizeX:
			replaced = GraphicsExecutor.jf.getWidth()+"";
			break;
		case windowSizeY:
			replaced = GraphicsExecutor.jf.getHeight()+"";
			break;
		case FPS:
			replaced = GraphicsExecutor.lastFPS+"";
			break;
		case time:
			replaced = System.currentTimeMillis()+"";
			break;
		case osName:
			replaced = System.getProperty("os.name");
			break;
		case osVersion:
			replaced = System.getProperty("os.version");
			break;
		case windowPosX:
			replaced = GraphicsExecutor.jf.getLocation().x+"";
			break;
		case windowPosY:
			replaced = GraphicsExecutor.jf.getLocation().y+"";
			break;
		case screenResX:
			replaced = ((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth())+"";
			break;
		case screenResY:
			replaced = ((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight())+"";
			break;
		case cursorPosX:
			replaced = MouseInfo.getPointerInfo().getLocation().x+"";
			break;
		case cursorPosY:
			replaced = MouseInfo.getPointerInfo().getLocation().y+"";
			break;
		case processUUID:
			replaced = Executor.getCurrentProcess().UUID+"";
			break;
		case processesRunning:
			replaced = Executor.runningProcesses.size()+"";
			break;
		case lineReturns:
			replaced = Executor.getCurrentProcess().lineReturns.size()+"";
			break;
		case version:
			//TODO update this everytime
			replaced = "Release Build 1.1 R1 12/19/16";
			break;
		case runningFileLocation:
			if (!Executor.getCurrentProcess().runningFromFile){
				replaced = "SOFTWARE";
				break;
			}
			replaced = Executor.getCurrentProcess().readingFrom.getParentFile().getAbsolutePath();
			break;
		case runningFileName:
			if (!Executor.getCurrentProcess().runningFromFile){
				replaced = "SOFTWARE";
				break;
			}
			replaced = Executor.getCurrentProcess().readingFrom.getName();
			break;
		case runningFilePath:
			if (!Executor.getCurrentProcess().runningFromFile){
				replaced = "SOFTWARE";
				break;
			}
			replaced = Executor.getCurrentProcess().readingFrom.getAbsolutePath();
			break;
		case newline:
			replaced = "\n";
			break;
		case alt:
			replaced = "ALT";
			break;
		case back:
			replaced = "BACKSPACE";
			break;
		case control:
			replaced = "CONTROL";
			break;
		case shift:
			replaced = "SHIFT";
			break;
		case systemKey:
			replaced = "SYSTEM_KEY";
			break;
		case tab:
			replaced = "\t";
			break;
		case caps:
			replaced = "CAPS_LOCK";
			break;
		case boundCursorPosX:
			replaced = (MouseInfo.getPointerInfo().getLocation().x - GraphicsExecutor.jf.getLocation().x - GraphicsExecutor.jf.getInsets().left)+"";
			break;
		case boundCursorPosY:
			replaced = (MouseInfo.getPointerInfo().getLocation().y - GraphicsExecutor.jf.getLocation().y -  GraphicsExecutor.jf.getInsets().top)+"";
			break;
		case space:
			replaced = " ";
			break;
		case euler:
			replaced = Math.E+"";
			break;
		case pi:
			replaced = Math.PI+"";
			break;
		default:
			break;
		}
		
		return replaced;
	}
	
	public static void clearLocalVariables(int f){
		ArrayList<String> toBeCleared = new ArrayList<String>();
		for (int i = 0; i < variables.keySet().size(); i++){
			if ((variables.keySet().toArray()[i]+"").contains(f+"")){
				toBeCleared.add(variables.keySet().toArray()[i]+"");
			}
		}
		for (int i = 0; i < toBeCleared.size(); i++){
			variables.remove(toBeCleared.get(i));
		}
	}
	
	public static String parseString(String parse){
		if (parse.length() < 2 || ((!parse.contains("|")) && (!(parse.contains("{") && parse.contains("}"))))  ){
			return parse;										//If small or doesn't have | or ({ and })
		}
		if (parse.contains("{") && parse.contains("}")){
			Matcher bracketMatcher = bracketPattern.matcher(parse);
			while (bracketMatcher.find()){
				String replacing = bracketMatcher.group();
				replacing = replacing.substring(1, replacing.length()-1);

				SystemEnv se = null;
				try{
					for (int j = 0; j < SystemEnv.values().length; j++){
						if (replacing.equals(SystemEnv.values()[j].name)){
							se = SystemEnv.values()[j];
						}
					}
					if (se == null){
						throw new Exception();
					}
				}catch(Exception e){Interpreter.throwError("Invalid System Variable: "+replacing);}
				parse = parse.substring(0, bracketMatcher.start()) + getEnvVariable(se) + parse.substring(bracketMatcher.end());
				bracketMatcher = bracketPattern.matcher(parse);
			}
		}
		
		

		String veryFinalString = parse;
		if (parse.contains("|")){
			boolean flag = false;
			if (parse.charAt(0) == '|'){
				flag = true;
				parse = " "+parse;
			}
			String split[] = splits(parse);
			for (int i = 1; i < split.length; i+=2){
				split[i] = split[i].substring(0,split[i].length());
				String stra;
				Value v = getValue(split[i]);
				stra = v.value+"";
				split[i] = stra;
			}
			if (flag){
				split[0] = split[0].substring(1);
			}
			String finalString = "";
			for (int i = 0; i < split.length; i++){
				finalString = finalString+split[i];
			}
			veryFinalString = finalString;
		}
		return veryFinalString;
			
	}
	static Pattern bracketPattern = Pattern.compile("[{][^{|}]*[}]");
	
}
