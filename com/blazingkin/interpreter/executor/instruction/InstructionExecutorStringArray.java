package com.blazingkin.interpreter.executor.instruction;

import java.util.ArrayList;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public abstract interface InstructionExecutorStringArray extends InstructionExecutor {
	
	public default Value run(String line){
		run(parseExpressions(line));
		return new Value(VariableTypes.Nil, null);
	}
	
	//Passes a whole expression
	public static String[] parseExpressions(String exp){
		ArrayList<String> expressions = new ArrayList<String>();
		int start = 0;
		int parensCount = 0;
		String buildingString = "";
		boolean inQuotes = false;
		for (int i = 0; i < exp.length(); i++){
			if (exp.charAt(i) == '(' && !inQuotes){
				if (parensCount == 0){
					start = i;
					if (!buildingString.trim().equals("")){
						expressions.add(buildingString.trim());
						buildingString = "";
					}
				}
				parensCount++;
			}
			

			if (parensCount == 0){
				if (exp.charAt(i) == '\"'){
					if (inQuotes){
						expressions.add(buildingString+"\"");
						buildingString = "";
						inQuotes = !inQuotes;
						continue;
					}
					buildingString = "";
					inQuotes = !inQuotes;
				}
				if (inQuotes){
					buildingString += exp.charAt(i);
					continue;
				}
				if (exp.charAt(i) == ' ' && !buildingString.trim().equals("")){
					expressions.add(buildingString.trim());
					buildingString = "";
					continue;
				}
				if (exp.charAt(i) != '"'){
					buildingString += exp.charAt(i);
				}
			}
			
			if (exp.charAt(i) == ')' && !inQuotes){
				parensCount--;
				if (parensCount == 0){
					expressions.add(exp.substring(start, i+1));
				}
			}
		}
		if (parensCount != 0){
			Interpreter.throwError("Unmatched parens on lambda expression: "+exp);
		}else if(!buildingString.trim().equals("")){
			expressions.add(buildingString.trim());
		}
		String[] express = new String[expressions.size()];
		for (int i = 0; i < express.length; i++){
			express[i] = expressions.get(i);
		}
		return express;
	}
	
	public abstract void run(String args[]);		//All of the Executors Implement this interface so that they can be referenced from an enum
}
