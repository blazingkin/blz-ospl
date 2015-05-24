package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;
//TODO FIX
//NONFUNCTIONAL
public class While implements InstructionExecutor {

	@Override
	public void run(String[] args) {
		boolean shouldRun = false;
		switch (args[1]){
		case ">":
			if (Integer.parseInt(Variable.parseString(args[0])) > Integer.parseInt(Variable.parseString(args[2]))){
				shouldRun = true;
			}
			break;
		case "<":
			if (Integer.parseInt(Variable.parseString(args[0])) < Integer.parseInt(Variable.parseString(args[2]))){
				shouldRun = true;
			}
			break;
		case "=":
			if (Integer.parseInt(Variable.parseString(args[0])) == Integer.parseInt(Variable.parseString(args[2]))){
				shouldRun = true;
			}
			break;
		case "!=":
			if (Integer.parseInt(Variable.parseString(args[0])) != Integer.parseInt(Variable.parseString(args[2]))){
				shouldRun = true;
			}
			break;
		default:
			break;
		}
		if (!shouldRun){
			//System.out.println("skipped");
			Executor.setLine((Integer)Variable.getValue("pc"+Executor.getCurrentProcess().UUID).value+3);
		}else{
			Executor.executeLineInCurrentProcess((Integer)Variable.getValue("pc"+Executor.getCurrentProcess().UUID).value+2,(Integer)Variable.getValue("pc"+Executor.getCurrentProcess().UUID).value);
			run(args);
		}
		
	}

	
	
	
}
