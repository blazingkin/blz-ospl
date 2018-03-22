package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.executor.instruction.BlockInstruction;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.variables.Context;

public abstract class LoopWrapper implements BlockInstruction {
	public ASTNode init;
	public ASTNode loop;
	public ASTNode term;
	public int startLine;
	public Context functionContext;
		
}
