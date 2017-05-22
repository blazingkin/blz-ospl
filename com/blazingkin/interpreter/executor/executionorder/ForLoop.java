package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.Instruction;
import com.blazingkin.interpreter.executor.InstructionType;

public class ForLoop extends LoopWrapper {
	
	public ForLoop(){
		
	}
	public ForLoop(String init, String term, String loop){
		initInstr = init.trim();
		loopInstr = loop.trim();
		termInstr = term.trim();
		startLine = Executor.getLine();
		functionContext = Executor.getCurrentContext();
	}
	
	
	public void run(String[] args) {
		if (!Executor.getLoopStack().isEmpty() && Executor.getLoopStack().peek().functionContext == Executor.getCurrentContext()
				&& Executor.getLoopStack().peek().startLine == 
				Executor.getLine()){	// This whole if statement checks if the for loop is already on the loop stack
			LoopWrapper lw = Executor.getLoopStack().peek();
			
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
				Executor.setLoopIgnoreMode(true);
			}
			
		}else{
			String originalString = "";
			for (String s: args){
				originalString = originalString + s + " ";
			}
			originalString.trim();
			String[] splits = originalString.split(",");
			
			Executor.getLoopStack().push(new ForLoop(splits[0], splits[1], splits[2]));
			
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
				
				Executor.setLoopIgnoreMode(true);
			}
		}
	}

	
	
}
