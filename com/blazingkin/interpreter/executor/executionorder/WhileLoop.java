package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.SimpleExpressionParser;
import com.blazingkin.interpreter.variables.Value;

public class WhileLoop extends LoopWrapper {
	private static final Value TRUE_VAL = Value.bool(true);
	
	public WhileLoop(){
		
	}
	
	public WhileLoop(String cond){
		termInstr = cond;
		startLine = Executor.getLine();
		functionContext = Executor.getCurrentContext();
	}
	
	public void run(String[] args){
			String buildingString = "";
			for (String s : args){
				buildingString += s + " ";
			}
			if (SimpleExpressionParser.parseExpression(buildingString).equals(TRUE_VAL)){	// If our condition is false, ignore the body of the loop
				Executor.pushToRuntimeStack(new WhileLoop(buildingString.trim()));
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
		if (SimpleExpressionParser.parseExpression(termInstr).equals(TRUE_VAL)){
			Executor.pushToRuntimeStack(this);
		}
	}
	
	
}
