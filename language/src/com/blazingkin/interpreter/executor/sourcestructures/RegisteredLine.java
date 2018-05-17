package com.blazingkin.interpreter.executor.sourcestructures;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStackElement;
import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorSemicolonDelimitedNode;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class RegisteredLine implements RuntimeStackElement{
	private ASTNode root;
	public final Instruction instr;
	public final String args;
	private String[] argsArr;
	private ASTNode[] nodes;
	public RegisteredLine(Instruction instr, String line){
		this.instr = instr;
		if (instr.executor instanceof InstructionExecutorStringArray){
			argsArr = line.split(" ");
		}else if (instr.executor instanceof InstructionExecutorValue){
			root = ExpressionParser.parseAndCollapse(line);
		}else if (instr.executor instanceof InstructionExecutorSemicolonDelimitedNode){
			nodes = ExpressionExecutor.extractSemicolonDelimitedNodes(ExpressionParser.parseAndCollapse(line));
		}
		this.args = line;
	}
	
	public RegisteredLine(ASTNode root){
		this.root = root;
		this.instr = Instruction.INVALID;
		this.args = null;
	}
	
	public Instruction getInstr(){
		return instr;
	}
	public String getArgs(){
		return args;
	}
	
	public Value run(Context con) throws BLZRuntimeException{
		if (root != null){
			if (instr != null && instr != Instruction.INVALID){
				return ((InstructionExecutorValue)instr.executor).run(root.execute(con));
			}
			return root.execute(con);
		}
		if (instr.executor instanceof InstructionExecutorStringArray){
			((InstructionExecutorStringArray) instr.executor).run(argsArr);
			return new Value(VariableTypes.Nil,null);
		}
		if (instr.executor instanceof InstructionExecutorSemicolonDelimitedNode){
			return ((InstructionExecutorSemicolonDelimitedNode) instr.executor).run(nodes);
		}
		return instr.executor.run(args, con);
	}
	
	public String toString(){
		if (root != null){
			return root.toString();
		}
		return instr.toString() + " " + args;
	}

	@Override
	public void onBlockStart() {
		
	}

	@Override
	public void onBlockEnd() {
		
	}

	@Override
	public int getLineNum() {
		return -1;
	}
	
}