package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.expressionabstraction.ExpressionParser;
import com.blazingkin.interpreter.variables.Value;

public abstract interface InstructionExecutorSemicolonDelimitedNode extends InstructionExecutor {
	public default Value run(String line){
		return run(ExpressionExecutor.extractSemicolonDelimitedNodes(ExpressionParser.parseAndCollapse(line)));
	}
	
	public abstract Value run(ASTNode[] args);
	
}
