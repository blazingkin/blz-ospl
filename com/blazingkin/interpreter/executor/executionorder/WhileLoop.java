package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;

public class WhileLoop extends LoopWrapper {
	public WhileLoop(){
		
	}
	
	public WhileLoop(String cond){
		termInstr = cond;
		startLine = Executor.getLine();
		functionContext = Executor.getCurrentContext();
	}
	
	public void run(String[] args){
		if (!Executor.getLoopStack().isEmpty() && Executor.getLoopStack().peek().functionContext == Executor.getCurrentContext()
				&& Executor.getLoopStack().peek().startLine == Executor.getLine()){	// This if statement checks if the loop is already on the stack
			LoopWrapper lw = Executor.getLoopStack().peek();
			if (!IfBlock.pureComparison(lw.termInstr.split(" "))){// If our condition is false, ignore the body of the loop
				Executor.setLoopIgnoreMode(true);
			}
		}else{
			String buildingString = "";
			for (String s : args){
				buildingString += s + " ";
			}
			Executor.getLoopStack().push(new WhileLoop(buildingString.trim()));
			if (!IfBlock.pureComparison(buildingString.split(" "))){	// If our condition is false, ignore the body of the loop
				Executor.setLoopIgnoreMode(true);
			}
		}
	}
	
	
}
