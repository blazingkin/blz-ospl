package com.blazingkin.interpreter.executor.math;

import java.math.BigDecimal;

import org.nevec.rjm.BigDecimalMath;

import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

@Deprecated
public class Logarithm implements InstructionExecutor, LambdaFunction {

	@Override
	public void run(String[] args) {
		BigDecimal num = Variable.getDoubleVal(Variable.getValue(args[0]));
		if (args.length == 2){
			String address = args[1];
			BigDecimal result = BigDecimalMath.log(num);
			Variable.setValue(address, new Value(VariableTypes.Double, result));
		}else{
			if (args[1].toLowerCase().equals("e")){
				String address = args[2];
				BigDecimal result = BigDecimalMath.log(num);
				Variable.setValue(address, new Value(VariableTypes.Double, result));
			}else{
				String address = args[2];
				BigDecimal base = Variable.getDoubleVal(Variable.getValue(args[1]));
				BigDecimal result = BigDecimalMath.log(num).divide(BigDecimalMath.log(base));
				Variable.setValue(address, new Value(VariableTypes.Double, result));
			}
		}
	}

	@Override
	public Value evaluate(String[] args) {
		BigDecimal num = Variable.getDoubleVal(Variable.getValue(args[0]));
		if (args.length == 1){
			BigDecimal result = BigDecimalMath.log(num);
			return new Value(VariableTypes.Double, result);
		}else{
			if (args[1].toLowerCase().equals("e")){
				BigDecimal result = BigDecimalMath.log(num);
				return new Value(VariableTypes.Double, result);
			}else{
				BigDecimal base = Variable.getDoubleVal(Variable.getValue(args[1]));
				BigDecimal result = BigDecimalMath.log(num).divide(BigDecimalMath.log(base));
				return new Value(VariableTypes.Double, result);
			}
		}
	}

}
