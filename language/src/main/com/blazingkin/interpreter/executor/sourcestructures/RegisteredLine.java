package com.blazingkin.interpreter.executor.sourcestructures;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorSemicolonDelimitedNode;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;
import com.blazingkin.interpreter.executor.astnodes.FunctionCallNode;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;

public interface RegisteredLine{

	public static RegisteredLine build(ASTNode root, int lineNum) {
		return new RegisteredLineASTNode(root, lineNum);
	}

	public static RegisteredLine build(Instruction instr, String line, int lineNum) throws SyntaxException {
		return new RegisteredLineInstructionExecutor(instr, line, lineNum);
	}
	
	public Instruction getInstr();
	public String getArgs();
	
	public Value run(Context con) throws BLZRuntimeException;
	
	public boolean canModify();
	
}

class RegisteredLineInstructionExecutor implements RegisteredLine {
	private ASTNode root;
	public final Instruction instr;
	public final String args;
	private String[] argsArr;
	private ASTNode[] nodes;
	private int lineNum;
	public RegisteredLineInstructionExecutor(Instruction instr, String line, int lineNum) throws SyntaxException{
		this.instr = instr;
		if (instr.executor instanceof InstructionExecutorStringArray){
			argsArr = line.split(" ");
		}else if (instr.executor instanceof InstructionExecutorValue){
			root = ExpressionParser.parseAndCollapse(line);
		}else if (instr.executor instanceof InstructionExecutorSemicolonDelimitedNode){
			nodes = ExpressionExecutor.extractSemicolonDelimitedNodes(ExpressionParser.parseAndCollapse(line));
		}
		this.args = line;
		this.lineNum = lineNum;
	}

	public Value run(Context con) throws BLZRuntimeException{
		try {
			if (root != null){
				if (instr != Instruction.INVALID){
					return ((InstructionExecutorValue)instr.executor).run(root.execute(con));
				}
				return root.execute(con);
			}
			if (instr.executor instanceof InstructionExecutorStringArray){
				((InstructionExecutorStringArray) instr.executor).run(argsArr);
				return new Value(VariableTypes.Nil,null);
			}
			if (instr.executor instanceof InstructionExecutorSemicolonDelimitedNode){
				return ((InstructionExecutorSemicolonDelimitedNode) instr.executor).run(nodes, con);
			}
			return instr.executor.run(args, con);
		}catch(BLZRuntimeException exception){
			if (!exception.alreadyCaught && exception.exceptionValue == null){
				String filePath = RuntimeStack.getProcessStack().peek().getLocation();
				throw new BLZRuntimeException("In " + filePath + ":" + lineNum +"Error occurred on line "+lineNum+"\n"+exception.getMessage(), true);
			}else{
				throw exception;
			}
		}
	}
	
	public String toString(){
		if (root != null){
			return root.toString();
		}
		return instr.toString() + " " + args;
	}

	public Instruction getInstr(){
		return instr;
	}
	public String getArgs(){
		return args;
	}

	public boolean canModify() {
		return instr == Instruction.ARRAYREBIND || instr == Instruction.SOCKET ||
				instr == Instruction.OPENRESOURCE;
	}

}

class RegisteredLineASTNode implements RegisteredLine {
	ASTNode n;
	int line;
	public RegisteredLineASTNode(ASTNode n, int line) {
		this.n = n;
		this.line = line;
	}

	public boolean canModify() {
		return true;
	}

	public Value run(Context c) throws BLZRuntimeException {
		try {
			return n.execute(c);
		}catch(BLZRuntimeException exception){
			if (RuntimeStack.getProcessStack().isEmpty()){
				throw exception;
			}else if (n instanceof FunctionCallNode && exception.alreadyCaught) {
				String filePath = RuntimeStack.getProcessStack().peek().getLocation();
				throw new BLZRuntimeException("In " + filePath + ":" + this.line + "\n" + exception.getMessage(), exception.alreadyCaught);
			}
			if (!exception.alreadyCaught && exception.exceptionValue == null){
				String filePath = RuntimeStack.getProcessStack().peek().getLocation();
				throw new BLZRuntimeException("In " + filePath + ":" + this.line +"\nError occurred on line "+line+"\n"+exception.getMessage(), true);
			}else{
				throw exception;
			}
		}
	}

	public Instruction getInstr(){
		return null;
	}
	public String getArgs(){
		return null;
	}

	public String toString() {
		return n.toString();
	}
}

