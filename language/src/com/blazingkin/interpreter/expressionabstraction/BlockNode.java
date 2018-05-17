package com.blazingkin.interpreter.expressionabstraction;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.sourcestructures.RegisteredLine;
import com.blazingkin.interpreter.variables.Context;
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
	public Value execute(Context con) throws BLZRuntimeException {
		for (int i = 0; i < instructions.length - 1; i++){
			instructions[i].run(con);
		}
		return instructions[instructions.length - 1].run(con); // Ruby got it right
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
