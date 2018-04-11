package com.blazingkin.interpreter.parser;

import java.util.ArrayList;

import com.blazingkin.interpreter.executor.executionorder.End;
import com.blazingkin.interpreter.executor.instruction.BlockInstruction;
import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.instruction.InstructionType;

public class BlockParser {

    public static ArrayList<Either<String, ParseBlock>> parseBody(SplitStream<String> input){
        ArrayList<Either<String, ParseBlock>> result = new ArrayList<Either<String, ParseBlock>>();
        while (input.hasNext()){
            String line = input.next();
            if (isEnd(line)){
                return result;
            }else if (isBlockHeader(line)){
                ArrayList<Either<String, ParseBlock>> childBlock = parseBody(input);
                ParseBlock newBlock = new ParseBlock(line, childBlock);
                result.add(Either.right(newBlock));
            }else{
                result.add(Either.left(line));
            }
        }
        return result;        
    }

    private static boolean isEnd(String line){
		Instruction instruction = getInstructionFromString(line);
		return instruction != null && instruction.executor instanceof End;		
	}

    private static boolean isBlockHeader(String line){
        if (line.length() < 1) {
			return false;
		}
		/* A method is a block */
		if (line.charAt(0) == ':'){
			return true;
		}
		/* A constructor is a block */
		if (line.startsWith("constructor")){
			return true;
		}
		Instruction instruction = getInstructionFromString(line);
		return instruction != null && instruction.executor instanceof BlockInstruction;
    }

    private static Instruction getInstructionFromString(String line) {
		/* We need to check the `instruction` type */
		int firstSpace = line.indexOf(" ");
		
		/* Just get the part before the space */
		String instructionString;
		if (firstSpace == -1) {
			instructionString = line;
		}else {
			instructionString = line.substring(0, firstSpace);
		}
		return InstructionType.getInstructionType(instructionString);
	}


}