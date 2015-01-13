package com.blazingkin.interpreter.executor;

public class InstructionType {
	public static Instruction getInstructionType(String s){		//gets the instruction based on the function call name
		for (int i = 0; i < Instruction.values().length; i++){
			if (s.equalsIgnoreCase(Instruction.values()[i].instruction)){
				return Instruction.values()[i];
			}
		}
		return Instruction.INVALID;
	}
	
	
}
