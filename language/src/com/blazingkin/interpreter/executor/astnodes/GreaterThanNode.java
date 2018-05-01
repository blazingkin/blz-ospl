package com.blazingkin.interpreter.executor.astnodes;

import java.math.BigDecimal;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class GreaterThanNode extends BinaryNode {

	public GreaterThanNode(ASTNode[] args) {
		super(Operator.GreaterThan, args);
		if (args.length != 2){
			Interpreter.throwError("Greater Than did not have 2 arguments");
		}
	}
	
	@Override
	public Value execute(Context con){
		Value v1 = args[0].execute(con);
		Value v2 = args[1].execute(con);
		if (!Variable.isDecimalValue(v1) || !Variable.isDecimalValue(v2)){
			if (v1.type == VariableTypes.String && v2.type == VariableTypes.String){
				String s1 = (String) v1.value;
				String s2 = (String) v2.value;
				return Value.bool(s1.compareTo(s2) > 0);
			}else if(v1.type == VariableTypes.Object){
				BLZObject obj = (BLZObject) v1.value;
				if (obj.objectContext.hasValue("<")){
					Value lessThan = obj.objectContext.getValue("<");
					if (lessThan.type == VariableTypes.Closure){
						Closure node = (Closure) lessThan.value;
						Value[] args = {v2};
						Value result = node.execute(node.context, args, false);
						if (result.type != VariableTypes.Boolean){
							Interpreter.throwError(v1 + " < " + v2+" returned non-boolean "+result);
							return result;
						}
						boolean val = (boolean) result.value;
						return Value.bool(!val);
					}
				}
			}
			Interpreter.throwError("Did not know how to compare greater than for values "+v1+" and "+v2);
		}
		BigDecimal d1 = Variable.getDoubleVal(v1);
		BigDecimal d2 = Variable.getDoubleVal(v2);
		return Value.bool(d1.compareTo(d2) > 0);
	}

}
