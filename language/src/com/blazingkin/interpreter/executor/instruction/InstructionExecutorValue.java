package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.parser.LineLexer;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public abstract interface InstructionExecutorValue extends InstructionExecutor {
	public default Value run(String line, Context con) throws BLZRuntimeException{
		try {
			return run(ExpressionParser.parseExpression(LineLexer.lexLine(line)).execute(con));
		}catch(SyntaxException e){
			throw new BLZRuntimeException("Uncaught syntax exception\n"+e.getMessage());
		}
	}
	
	public abstract Value run(Value val) throws BLZRuntimeException;		//All of the Executors Implement this interface so that they can be referenced from an enum

}
