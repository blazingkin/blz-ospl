package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public abstract interface InstructionExecutorSemicolonDelimitedNode extends InstructionExecutor {
	public default Value run(String line, Context c) throws BLZRuntimeException{
		try {
			return run(ExpressionExecutor.extractSemicolonDelimitedNodes(ExpressionParser.parseAndCollapse(line)), c);
		}catch(SyntaxException e){
			throw new BLZRuntimeException("Uncaught syntax exception\n"+e.getMessage());
		}
	}
	
	public abstract Value run(ASTNode[] args, Context c);
	
}
