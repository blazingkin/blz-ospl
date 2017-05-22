package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class IfBlock implements InstructionExecutor, LambdaFunction {

	public void run(String[] args) {
		boolean flag = false;
		Value v1 = Variable.getValue(args[0]);
		Value v2 = Variable.getValue(args[2]);
		String operant = args[1];
		switch(operant){
		case "==":
			if (v1.value.equals(v2.value)){
				flag = true;
			}
			break;
		case ">":
			if (compare(v1, v2) > 0){
				flag = true;
			}
			break;
		case "<":
			if (compare(v1,v2) < 0){
				flag = true;
			}
			break;
		case "!=":
			if (!v1.value.equals(v2.value)){
				flag = true;
			}
			break;
		case "<=":
			if (compare(v1,v2) <= 0){
				flag = true;
			}
			break;
		case ">=":
			if (compare(v1,v2) >= 0){
				flag = true;
			}
			break;
		default:
			break;
		}
		if (!flag){
			Executor.setLine(Executor.getLine()+1);
		}
		
	}
	
	public static double compare(Value v1, Value v2){
		if ((v1.type == VariableTypes.Integer || v1.type == VariableTypes.Double) 
				&& (v2.type == VariableTypes.Integer || v2.type == VariableTypes.Double)){
			double d1;
			double d2;
			if (v1.value instanceof Integer){
				d1 = (double)((int)v1.value);
			}else{
				d1 = (double)v1.value;
			}
			if (v2.value instanceof Integer){
				d2 = (double)((int)v2.value);
			}else{
				d2 = (double) v2.value;
			}
			return d1 - d2;
		}
		return 0;
	}
	
	public static boolean pureComparison(String[] args){
		boolean flag = false;
		Value v1 = null;
		Value v2 = null;
		String operant = "";
		if (args.length == 3){
			v1 = Variable.getValue(args[0]);
			v2 = Variable.getValue(args[2]);
			operant = args[1];
		}else{
			String buildingString = "";
			for (String s : args){
				buildingString += s;
			}
			String splts[] = buildingString.split("==|!=|<=|>=|=<|=>|>|<");
			v1 = Variable.getValue(splts[0]);
			v2 = Variable.getValue(splts[1]);
			operant = buildingString.substring(splts[0].length(),buildingString.length()-splts[1].length());
		}
		switch(operant){
		case "==":
			if (v1.value.equals(v2.value)){
				flag = true;
			}
			break;
		case ">":
			if (compare(v1, v2) > 0){
				flag = true;
			}
			break;
		case "<":
			if (compare(v1,v2) < 0){
				flag = true;
			}
			break;
		case "!=":
			if (!v1.value.equals(v2.value)){
				flag = true;
			}
			break;
		case "<=":	//Same case as =<
			if (compare(v1,v2) <= 0){
				flag = true;
			}
			break;
		case "=<":	//Same case as <=
			if (compare(v1,v2) <= 0){
				flag = true;
			}
			break;
		case ">=": //Same case as =>
			if (compare(v1,v2) >= 0){
				flag = true;
			}
			break;
		case "=>": //Same case as >=
			if (compare(v1,v2) >= 0){
				flag = true;
			}
			break;
		default:
			break;
		}
		return flag;
	}
	
	public static boolean isIfStatement(String statement){
		return statement.contains("==") || statement.contains("!=") || statement.contains("<")
				|| statement.contains(">") || statement.contains("=<") || statement.contains(">=")
				|| statement.contains("=>") || statement.contains("<=");
	}
	
	public static boolean pureComparison(String statement){
		String[] arr = new String[1];
		arr[0] = statement;
		return pureComparison(arr);
	}

	//First variable is the condition
	//Second condition is the value returned if the condition is true
	//Third condition is the else-value
	@Override
	public Value evaluate(String[] args) {
		if (isIfStatement(args[0])){
			if (pureComparison(args[0])){
				return Variable.getValue(args[1]);
			}else{
				return Variable.getValue(args[2]);
			}
		}
		Value cond = Variable.getValue(args[0]);
		if (cond.type == VariableTypes.Boolean){
			boolean co = (boolean) cond.value;
			if (co){
				return Variable.getValue(args[1]);
			}else{
				return Variable.getValue(args[2]);
			}
		}
		Interpreter.throwError("If block expected a boolean or a comparison as first statement - not found");
		return new Value(VariableTypes.Nil, null);
		
	}
	

	
	
}
