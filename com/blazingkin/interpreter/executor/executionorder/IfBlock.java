package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Variable;

public class IfBlock implements InstructionExecutor {
	/*	If statements
	 * 	The appositive will matter if it checks for true or false
	 * 	If the statement is not true then it will skip to the line past the executed one
	 * 	If the statement is true then it will continue and this will not do anything
	 */
	private final boolean appositive;
	public IfBlock(boolean appositive){
		this.appositive = appositive;
	}
	
	public void run(String args[]){
		if (!Variable.parseString(args[0]).equals(Variable.parseString(args[1])) == appositive){
			Executor.setLine((Integer)Variable.getValue("pc"+Executor.getCurrentProcess().UUID).value+3);
		}
	}
	
}
