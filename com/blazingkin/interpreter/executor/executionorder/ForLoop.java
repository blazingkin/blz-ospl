package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.Instruction;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.InstructionType;
import com.blazingkin.interpreter.variables.Variable;

public class ForLoop extends LoopWrapper {
	
	public ForLoop(){
		
	}
	public ForLoop(String init, String term, String loop){
		initInstr = init.trim();
		loopInstr = loop.trim();
		termInstr = term.trim();
		startLine = (int)Variable.getValue("pc"+Executor.getCurrentProcess().UUID).value;
		functionUUID = Executor.getCurrentMethodUUID();
	}
	
	
	public void run(String[] args) {
		if (!Executor.loopStack.isEmpty() && Executor.loopStack.peek().functionUUID == Executor.getCurrentMethodUUID()
				&& Executor.loopStack.peek().startLine == 
				(int)Variable.getValue("pc"+Executor.getCurrentProcess().UUID).value){
			LoopWrapper lw = Executor.loopStack.peek();
			
			Instruction it = InstructionType.getInstructionType(lw.loopInstr.trim().split(" ")[0]);
			if (it.name.equals(Instruction.INVALID.name)){
				
				Interpreter.throwError("Invalid instruction "+lw.loopInstr.trim().split(" ")[0]);
			}
			String[] nSplits = new String[lw.loopInstr.trim().split(" ").length - 1];
			for (int i = 0; i < nSplits.length; i++){
				nSplits[i] = lw.loopInstr.split(" ")[i + 1];
			}
			it.executor.run(nSplits);
			
			
			if (!IfBlock.pureComparison(lw.termInstr.trim().split(" "))){
				Executor.loopIgnoreMode = true;
			}
			
		}else{
			String originalString = "";
			for (String s: args){
				originalString = originalString + s + " ";
			}
			originalString.trim();
			String[] splits = originalString.split(",");
			
			Executor.loopStack.push(new ForLoop(splits[0], splits[1], splits[2]));
			
			Instruction it = InstructionType.getInstructionType(splits[0].split(" ")[0]);
			if (it.name.equals(Instruction.INVALID.name)){
				
				Interpreter.throwError("Invalid instruction "+splits[0].split(" "));
			}
			String[] nSplits = new String[splits[0].split(" ").length - 1];
			for (int i = 0; i < nSplits.length; i++){
				nSplits[i] = splits[0].split(" ")[i + 1].trim();
			}
			it.executor.run(nSplits);
			if (!IfBlock.pureComparison(splits[1].trim().split(" "))){
				Executor.loopIgnoreMode = true;
			}
		}
	}

	
	
}
