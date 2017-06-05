package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.SimpleExpressionParser;

public class ForLoop extends LoopWrapper {
	
	public ForLoop(){
		
	}
	public ForLoop(String init, String term, String loop){
		initInstr = init.trim();
		loopInstr = loop.trim();
		termInstr = term.trim();
		startLine = Executor.getLine();
		functionContext = Executor.getCurrentContext();
	}
	
	
	public void run(String[] args) {
		if (!Executor.getLoopStack().isEmpty() && Executor.getLoopStack().peek().functionContext == Executor.getCurrentContext()
				&& Executor.getLoopStack().peek().startLine == 
				Executor.getLine()){	// This whole if statement checks if the for loop is already on the loop stack
			LoopWrapper lw = Executor.getLoopStack().peek();
			SimpleExpressionParser.parseExpression(lw.loopInstr);
			if (!IfBlock.pureComparison(lw.termInstr.trim().split(" "))){
				Executor.setLoopIgnoreMode(true);
			}
			
		}else{
			String originalString = "";
			for (String s: args){
				originalString = originalString + s + " ";
			}
			originalString.trim();
			String[] splits = originalString.split(",");
			
			Executor.getLoopStack().push(new ForLoop(splits[0], splits[1], splits[2]));
			SimpleExpressionParser.parseExpression(splits[0]);
			
			if (!IfBlock.pureComparison(splits[1].trim().split(" "))){
				
				Executor.setLoopIgnoreMode(true);
			}
		}
	}

	
	
}
