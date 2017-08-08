package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorSemicolonDelimitedNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.variables.Value;

public class WhileLoop extends LoopWrapper implements InstructionExecutorSemicolonDelimitedNode {
	private static final Value TRUE_VAL = Value.bool(true);
	private static final Value FALSE_VAL = Value.bool(false);
	
	public WhileLoop(){
		
	}
	
	public WhileLoop(ASTNode cond){
		this.term = cond;
		startLine = Executor.getLine();
		functionContext = Executor.getCurrentContext();
	}
	
	@Override
	public Value run(ASTNode[] args) {
		if (ExpressionExecutor.executeNode(args[0]).equals(TRUE_VAL)){
			Executor.pushToRuntimeStack(new WhileLoop(args[0]));
			return TRUE_VAL;
		}else{
			Executor.setLine(Executor.getCurrentBlockEnd());
			return FALSE_VAL;
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
		if (ExpressionExecutor.executeNode(term).equals(TRUE_VAL)){
			Executor.pushToRuntimeStack(this);
		}
	}


	
	
}
