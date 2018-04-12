package com.blazingkin.interpreter.executor.sourcestructures;

import java.util.ArrayList;

import com.blazingkin.interpreter.executor.astnodes.MethodNode;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class Closure extends MethodNode {

	public Context context;
	
	public Closure(Context con, String header, ArrayList<Either<String, ParseBlock>> body, Process parent) throws SyntaxException{
		super(header, body, parent);
		context = con;
	}

	public Value execute(Context con, Value values[], boolean passByReference){
		return super.execute(context, values, passByReference);
	}
	
	
}
