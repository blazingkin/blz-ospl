package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.executor.lambda.LambdaFunction;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Trigonometry implements InstructionExecutor, LambdaFunction {

	public TrigFunctions function;
	public Trigonometry(TrigFunctions f){
		function = f;
	}
	@Override
	public void run(String[] args) {
		double input = Variable.getDoubleVal(Variable.getValue(args[0]));
		String address = args[1];
		switch(function){
		case COS:
			Variable.setValue(address, new Value(VariableTypes.Double, Math.cos(input)));
			break;
		case COT:
			Variable.setValue(address, new Value(VariableTypes.Double, 1/Math.tan(input)));
			break;
		case CSC:
			Variable.setValue(address, new Value(VariableTypes.Double, 1/Math.sin(input)));
			break;
		case SEC:
			Variable.setValue(address, new Value(VariableTypes.Double, 1/Math.cos(input)));
			break;
		case SIN:
			Variable.setValue(address, new Value(VariableTypes.Double, Math.sin(input)));
			break;
		case TAN:
			Variable.setValue(address, new Value(VariableTypes.Double, Math.tan(input)));
			break;
		case ARCCOS:
			Variable.setValue(address, new Value(VariableTypes.Double, Math.acos(input)));
			break;
		case ARCSIN:
			Variable.setValue(address, new Value(VariableTypes.Double, Math.asin(input)));
			break;
		case ARCTAN:
			Variable.setValue(address, new Value(VariableTypes.Double, Math.atan(input)));
			break;
		default:
			Interpreter.throwError("Attempted to do a trig function of unknown type");
			Variable.setValue(address, new Value(VariableTypes.Double, input));
			break;
		}
	}
	@Override
	public Value evaluate(String[] args) {

		double input = Variable.getDoubleVal(Variable.getValue(args[0]));
		switch(function){
		case COS:
			return new Value(VariableTypes.Double, Math.cos(input));
		case COT:
			return new Value(VariableTypes.Double, 1/Math.tan(input));
		case CSC:
			return new Value(VariableTypes.Double, 1/Math.sin(input));
		case SEC:
			return new Value(VariableTypes.Double, 1/Math.cos(input));
		case SIN:
			return new Value(VariableTypes.Double, Math.sin(input));
		case TAN:
			return new Value(VariableTypes.Double, Math.tan(input));
		case ARCCOS:
			return new Value(VariableTypes.Double, Math.acos(input));
		case ARCSIN:
			return new Value(VariableTypes.Double, Math.asin(input));
		case ARCTAN:
			return new Value(VariableTypes.Double, Math.atan(input));
		default:
			Interpreter.throwError("Attempted to do a trig function of unknown type");
			return new Value(VariableTypes.Double, input);
		}
	}

}
