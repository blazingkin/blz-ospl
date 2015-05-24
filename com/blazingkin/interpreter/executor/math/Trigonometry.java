package com.blazingkin.interpreter.executor.math;

import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Trigonometry implements InstructionExecutor {

	public TrigFunctions function;
	public Trigonometry(TrigFunctions f){
		function = f;
	}
	@Override
	public void run(String[] args) {
		Double input = Double.parseDouble(Variable.parseString(args[0]));
		String address = Variable.parseString(args[1]);
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
			Variable.setValue(address, new Value(VariableTypes.Double, input));
			break;
		}
	}

}
