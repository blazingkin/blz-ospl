package com.blazingkin.interpreter.executor.data;

import java.math.BigInteger;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutorCommaDelimited;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorSemicolonDelimitedNode;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class RandomImplementor implements InstructionExecutorCommaDelimited {
	/*	Random
	 * Returns a random integer from 0-99
	 * 
	 */
	@Override
	public Value run(Value[] args) {
		Value v;
		if(args.length == 1){
			BigInteger range = Variable.getIntValue(args[0]);
			v = new Value(VariableTypes.Integer, BigInteger.valueOf((long)(Math.random()*range.intValue())));
		}else if (args.length == 2){
			BigInteger lowerBound = Variable.getIntValue(args[0]);
			BigInteger upperBound = Variable.getIntValue(args[1]);
			BigInteger range = upperBound.subtract(lowerBound);
			v = new Value(VariableTypes.Integer, lowerBound.add(BigInteger.valueOf((long)(Math.random()*range.intValue()))));
		}else{
			v = new Value(VariableTypes.Integer, BigInteger.valueOf((long)(Math.random()*100)));
		}
		return v;
	}

}