package com.blazingkin.interpreter.parser;

import java.util.ArrayList;

public class BlockParser {

	public static String blocksUnclosedErrorMessage = "Some blocks not closed!";

    public static ArrayList<Either<SourceLine, ParseBlock>> parseBody(SplitStream<String> input, int lineOffset) throws SyntaxException{
		boolean isStart = input.isStart(); // Base case for recursion. This should be true only in the initial call
        ArrayList<Either<SourceLine, ParseBlock>> result = new ArrayList<Either<SourceLine, ParseBlock>>();
        while (input.hasNext()){
			int index = input.getIndex() + lineOffset;
            String line = input.next();
            if (isEnd(line)){
				if (isStart){
					throw new SyntaxException("Unexpected end of block");
				}
                return result;
            }else if (isBlockHeader(line)){
                ArrayList<Either<SourceLine, ParseBlock>> childBlock = parseBody(input, lineOffset);
                ParseBlock newBlock = new ParseBlock(line, childBlock, index);
                result.add(Either.right(newBlock));
            }else{

				SourceLine sourceLine = new SourceLine(line.split("(?<!\\\\)#")[0].trim(), index);
                result.add(Either.left(sourceLine));
            }
		}
		if (!isStart){
			throw new SyntaxException(blocksUnclosedErrorMessage);
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
		if (line.startsWith("try")){
			return true;
		}
		return false;
    }


}