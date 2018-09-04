package com.blazingkin.interpreter.executor.instruction;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.parser.LineLexer;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;



public interface InstructionExecutorCommaDelimited extends InstructionExecutor {

	public default Value run(String line, Context con) throws BLZRuntimeException{
		try{
			return run(ExpressionExecutor.extractCommaDelimits(ExpressionParser.parseExpression(LineLexer.lexLine(line)), con));
		}catch(SyntaxException e){
			throw new BLZRuntimeException("Unresolved Syntax Exception\n"+e.getMessage());
		}
	}
	
	public abstract Value run(Value[] args) throws BLZRuntimeException;
	
}
