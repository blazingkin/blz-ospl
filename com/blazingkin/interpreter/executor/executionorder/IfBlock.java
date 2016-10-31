package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;

public class IfBlock implements InstructionExecutor {

	public void run(String[] args) {
		boolean flag = false;
		String v1 = Variable.parseString(args[0]);
		String v2 = Variable.parseString(args[2]);
		String operant = args[1];
		switch(operant){
		case "==":
			if (compare(v1,v2) == 0){
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
			if (compare(v1, v2) != 0){
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
			Executor.setLine((Integer)Variable.getValue("pc"+Executor.getCurrentProcess().UUID).value+3);
		}
		
	}
	
	public static int compare(String v1, String v2){
		if (Variable.isDouble(v1) && Variable.isDouble(v2)){
			return ((Double)Double.parseDouble(v1)).compareTo(Double.parseDouble(v2));
		}
		return v1.compareTo(v2);
	}
	
	public static boolean pureComparison(String[] args){
		boolean flag = false;
		String v1 = Variable.parseString(args[0]);
		String v2 = Variable.parseString(args[2]);
		String operant = args[1];
		switch(operant){
		case "==":
			if (compare(v1,v2) == 0){
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
			if (compare(v1, v2) != 0){
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
		return flag;
	}

	
	
}
