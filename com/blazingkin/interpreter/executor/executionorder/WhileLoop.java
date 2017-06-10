package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.SimpleExpressionParser;
import com.blazingkin.interpreter.variables.Value;

public class WhileLoop extends LoopWrapper {
	private static Value TRUE_VAL = Value.bool(true);
	
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
			
			if (!SimpleExpressionParser.parseExpression(lw.termInstr).equals(TRUE_VAL)){// If our condition is false, ignore the body of the loop
				Executor.setLoopIgnoreMode(true);
			}
		}else{
			String buildingString = "";
			for (String s : args){
				buildingString += s + " ";
			}
			Executor.getLoopStack().push(new WhileLoop(buildingString.trim()));
			if (!SimpleExpressionParser.parseExpression(buildingString).equals(TRUE_VAL)){	// If our condition is false, ignore the body of the loop
				Executor.setLoopIgnoreMode(true);
			}
		}
	}
	
	
}
