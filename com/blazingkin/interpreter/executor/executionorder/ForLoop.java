package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.SimpleExpressionParser;
import com.blazingkin.interpreter.variables.Value;

public class ForLoop extends LoopWrapper {
	private static final Value TRUE_VAL = Value.bool(true);
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
			String originalString = "";
			for (String s: args){
				originalString = originalString + s + " ";
			}
			originalString.trim();
			String[] splits = originalString.split(",");
			
			SimpleExpressionParser.parseExpression(splits[0]);
			if (SimpleExpressionParser.parseExpression(splits[1]).equals(TRUE_VAL)){
				Executor.pushToRuntimeStack(new ForLoop(splits[0], splits[1], splits[2]));
			}else{
				Executor.setLine(Executor.getCurrentBlockEnd());
			}
	}
	@Override
	public void onBlockStart() {
		Executor.setLine(startLine);
	}
	@Override
	public void onBlockEnd() {
		if (Executor.isBreakMode()){
			Executor.setBreakMode(false);
			return;
		}
		SimpleExpressionParser.parseExpression(loopInstr);
		if (SimpleExpressionParser.parseExpression(termInstr).equals(TRUE_VAL)){
			Executor.pushToRuntimeStack(this);
		}
	}

	
	
}
