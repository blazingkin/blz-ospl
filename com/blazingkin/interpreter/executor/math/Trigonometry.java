package com.blazingkin.interpreter.executor.math;

import java.math.BigDecimal;

import org.nevec.rjm.BigDecimalMath;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Trigonometry implements InstructionExecutorStringArray, LambdaFunction {

	public TrigFunctions function;
	public Trigonometry(TrigFunctions f){
		function = f;
	}
	@Override
	public void run(String[] args) {
		BigDecimal input = Variable.getDoubleVal(Variable.getValue(args[0]));
		String address = args[1];
		switch(function){
		case COS:
			Variable.setValue(address, new Value(VariableTypes.Double, BigDecimalMath.cos(input)));
			break;
		case COT:
			Variable.setValue(address, new Value(VariableTypes.Double, BigDecimal.ONE.divide(BigDecimalMath.tan(input))));
			break;
		case CSC:
			Variable.setValue(address, new Value(VariableTypes.Double, BigDecimal.ONE.divide(BigDecimalMath.sin(input))));
			break;
		case SEC:
			Variable.setValue(address, new Value(VariableTypes.Double, BigDecimal.ONE.divide(BigDecimalMath.cos(input))));
			break;
		case SIN:
			Variable.setValue(address, new Value(VariableTypes.Double, BigDecimalMath.sin(input)));
			break;
		case TAN:
			Variable.setValue(address, new Value(VariableTypes.Double, BigDecimalMath.tan(input)));
			break;
		case ARCCOS:
			Variable.setValue(address, new Value(VariableTypes.Double, BigDecimalMath.acos(input)));
			break;
		case ARCSIN:
			Variable.setValue(address, new Value(VariableTypes.Double, BigDecimalMath.asin(input)));
			break;
		case ARCTAN:
			Variable.setValue(address, new Value(VariableTypes.Double, BigDecimalMath.atan(input)));
			break;
		default:
			Interpreter.throwError("Attempted to do a trig function of unknown type");
			Variable.setValue(address, new Value(VariableTypes.Double, input));
			break;
		}
	}
	@Override
	public Value evaluate(String[] args) {

		BigDecimal input = Variable.getDoubleVal(Variable.getValue(args[0]));
		switch(function){
		case COS:
			return new Value(VariableTypes.Double, BigDecimalMath.cos(input));
		case COT:
			return new Value(VariableTypes.Double, BigDecimal.ONE.divide(BigDecimalMath.tan(input)));
		case CSC:
			return new Value(VariableTypes.Double, BigDecimal.ONE.divide(BigDecimalMath.sin(input)));
		case SEC:
			return new Value(VariableTypes.Double, BigDecimal.ONE.divide(BigDecimalMath.cos(input)));
		case SIN:
			return new Value(VariableTypes.Double, BigDecimalMath.sin(input));
		case TAN:
			return new Value(VariableTypes.Double, BigDecimalMath.tan(input));
		case ARCCOS:
			return new Value(VariableTypes.Double, BigDecimalMath.acos(input));
		case ARCSIN:
			return new Value(VariableTypes.Double, BigDecimalMath.asin(input));
		case ARCTAN:
			return new Value(VariableTypes.Double, BigDecimalMath.atan(input));
		default:
			Interpreter.throwError("Attempted to do a trig function of unknown type");
			return new Value(VariableTypes.Double, input);
		}
	}

}
