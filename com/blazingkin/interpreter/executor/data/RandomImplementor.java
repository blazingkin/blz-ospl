package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class RandomImplementor implements InstructionExecutor, LambdaFunction {
	/*	Random
	 * Returns a random integer from 0-99
	 * 
	 */
	@Override
	public void run(String[] args) {
		Value v;
		if(args.length == 2){
			int range = Variable.getIntValue(Variable.getValue(args[1]));
			v = new Value(VariableTypes.Integer, (int)Math.random()*range);
		}else if (args.length == 3){
			int lowerBound = Variable.getIntValue(Variable.getValue(args[1]));
			int upperBound = Variable.getIntValue(Variable.getValue(args[2]));
			int range = upperBound-lowerBound;
			v = new Value(VariableTypes.Integer, lowerBound + (int)Math.random()*range);
		}else{
			v = new Value(VariableTypes.Integer, (int)Math.random()*100);
		}
		Variable.setValue(args[0], v);
	}


	@Override
	public Value evaluate(String[] args) {
		if(args.length == 1){
			int range = Variable.getIntValue(Variable.getValue(args[0]));
			return new Value(VariableTypes.Integer, (int)Math.random()*range);
		}else if (args.length == 2){
			int lowerBound = Variable.getIntValue(Variable.getValue(args[0]));
			int upperBound = Variable.getIntValue(Variable.getValue(args[1]));
			int range = upperBound-lowerBound;
			return new Value(VariableTypes.Integer, lowerBound + (int)Math.random()*range);
		}
		return new Value(VariableTypes.Integer, (int)Math.random()*100);
	}
}