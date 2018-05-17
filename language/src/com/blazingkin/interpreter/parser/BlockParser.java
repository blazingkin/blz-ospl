package com.blazingkin.interpreter.parser;

import java.util.ArrayList;

import com.blazingkin.interpreter.executor.instruction.Instruction;

public class BlockParser {

    public static ArrayList<Either<String, ParseBlock>> parseBody(SplitStream<String> input) throws SyntaxException{
		boolean isStart = input.isStart();
        ArrayList<Either<String, ParseBlock>> result = new ArrayList<Either<String, ParseBlock>>();
        while (input.hasNext()){
            String line = input.next();
            if (isEnd(line)){
				if (isStart){
					throw new SyntaxException("Unexpected end of block");
				}
                return result;
            }else if (isBlockHeader(line)){
                ArrayList<Either<String, ParseBlock>> childBlock = parseBody(input);
                ParseBlock newBlock = new ParseBlock(line, childBlock);
                result.add(Either.right(newBlock));
            }else{
                result.add(Either.left(line.split("(?<!\\\\)#")[0].trim()));
            }
		}
		if (!isStart){
			throw new SyntaxException("Some blocks not closed!");
		}
        return result;        
    }

    private static boolean isEnd(String line){
		return line.length() == 3 && (line.charAt(0) == 'e' || line.charAt(0) == 'E') &&
									(line.charAt(1) == 'n' || line.charAt(1) == 'N') && 
									(line.charAt(2) == 'd' || line.charAt(2) == 'D');	
	}

    private static boolean isBlockHeader(String line){
		line = line.toLowerCase();
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
		if (line.startsWith("if")){
			return true;
		}
		if (line.startsWith("for")){
			return true;
		}
		if (line.startsWith("while")){
			return true;
		}
		return false;
    }


}