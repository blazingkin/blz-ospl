package com.blazingkin.interpreter.executor;

import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class RegisteredLine{
	public final ASTNode root;
	public final Instruction instr;
	public final String args;
	private String[] argsArr;
	public RegisteredLine(Instruction instr, String args){
		this.instr = instr;
		if (instr.executor instanceof InstructionExecutorStringArray){
			argsArr = Executor.parseExpressions(args);
		}
		this.args = args;
		root = null;
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
	
	public Value run(){
		if (root != null){
			return ExpressionExecutor.executeNode(root);
		}
		if (instr.executor instanceof InstructionExecutorStringArray){
			((InstructionExecutorStringArray) instr.executor).run(argsArr);
			return new Value(VariableTypes.Nil,null);
		}
		return instr.executor.run(args);
	}
	
}