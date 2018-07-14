package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Context;

public abstract interface InstructionExecutorSemicolonDelimitedNode extends InstructionExecutor {
	public default Value run(String line, Context c){
		return run(ExpressionExecutor.extractSemicolonDelimitedNodes(ExpressionParser.parseAndCollapse(line)), c);
	}
	
	public abstract Value run(ASTNode[] args, Context c);
	
}
