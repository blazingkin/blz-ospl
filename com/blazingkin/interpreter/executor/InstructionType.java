package com.blazingkin.interpreter.executor;

import java.util.HashMap;

public class InstructionType {
	public static Instruction getInstructionType(String s){		//gets the instruction based on the function call name
		try{
			return instructions.get(s);
		}catch(Exception e){
		}
		try{
			return instructions.get(s.toUpperCase());
		}catch(Exception e){}
		return Instruction.INVALID;
	}
	
	// This hashmap is statically loaded with all instructions from the Instruction enum
	public static HashMap<String, Instruction> instructions;
	static {
		instructions = new HashMap<String, Instruction>();
		for (Instruction i: Instruction.values()){
			instructions.put(i.instruction, i);
		}
	}
	
	
	
}
