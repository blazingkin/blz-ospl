package com.blazingkin.interpreter.executor;

import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorSemicolonDelimitedNode;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.expressionabstraction.ExpressionParser;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class RegisteredLine{
	private ASTNode root;
	public final Instruction instr;
	public final String args;
	private String[] argsArr;
	private ASTNode[] nodes;
	public RegisteredLine(Instruction instr, String line){
		this.instr = instr;
		if (instr.executor instanceof InstructionExecutorStringArray){
			argsArr = Executor.parseExpressions(line);
		}else if (instr.executor instanceof InstructionExecutorValue){
			root = ExpressionParser.parseExpression(line);
		}else if (instr.executor instanceof InstructionExecutorSemicolonDelimitedNode){
			nodes = ExpressionExecutor.extractSemicolonDelimitedNodes(ExpressionParser.parseExpression(line));
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
	
	public Value run(){
		if (root != null){
			if (instr != null && instr != Instruction.INVALID){
				return ((InstructionExecutorValue)instr.executor).run(ExpressionExecutor.executeNode(root));
			}
			return ExpressionExecutor.executeNode(root);
		}
		if (instr.executor instanceof InstructionExecutorStringArray){
			((InstructionExecutorStringArray) instr.executor).run(argsArr);
			return new Value(VariableTypes.Nil,null);
		}
		if (instr.executor instanceof InstructionExecutorSemicolonDelimitedNode){
			return ((InstructionExecutorSemicolonDelimitedNode) instr.executor).run(nodes);
		}
		return instr.executor.run(args);
	}
	
}