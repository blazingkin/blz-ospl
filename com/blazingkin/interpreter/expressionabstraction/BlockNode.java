package com.blazingkin.interpreter.expressionabstraction;

import com.blazingkin.interpreter.executor.RegisteredLine;
import com.blazingkin.interpreter.variables.Value;

public class BlockNode extends ASTNode {
	
	public RegisteredLine[] instructions;
	public BlockNode(RegisteredLine... instructions){
		this.instructions = instructions;
	}

	@Override
	public boolean canCollapse() {
		return false;
	}

	@Override
	public ASTNode collapse() {
		return this;
	}

	@Override
	public Value execute() {
		for (int i = 0; i < instructions.length - 1; i++){
			instructions[i].run();
		}
		return instructions[instructions.length - 1].run(); // Ruby got it right
	}

	@Override
	public String getStoreName() {
		return null;
	}

	@Override
	public Operator getOperator() {
		return null;
	}

}
