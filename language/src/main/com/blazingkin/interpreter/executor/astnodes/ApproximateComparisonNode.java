package com.blazingkin.interpreter.executor.astnodes;

import java.math.BigDecimal;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class ApproximateComparisonNode extends BinaryNode {
	private static final double EPSILON = 1E-8; 
	public ApproximateComparisonNode(ASTNode[] args) {
		super(Operator.ApproximateComparison, args);
		if (args.length != 2){
			Interpreter.throwError("Approximate Comparison did not have 2 arguments.");
		}
	}

	public boolean canModify() {
		return args[0].canModify() || args[1].canModify();
	}
	
	@Override
	public Value execute(Context con) throws BLZRuntimeException {
		Value v1 = args[0].execute(con);
		Value v2 = args[1].execute(con);
		if (!Variable.isDecimalValue(v1) || !Variable.isDecimalValue(v2)){
			throw new BLZRuntimeException(this, "When comparing approximately, one of "+v1+" or "+v2+" is not a decimal value.");
		}
		BigDecimal d1 = Variable.getDoubleVal(v1);
		BigDecimal d2 = Variable.getDoubleVal(v2);
		return Value.bool(d1.subtract(d2).abs().doubleValue() < EPSILON);
	}

}
