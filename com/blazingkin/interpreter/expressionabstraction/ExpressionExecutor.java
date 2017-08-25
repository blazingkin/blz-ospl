package com.blazingkin.interpreter.expressionabstraction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.Method;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.SystemEnv;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ExpressionExecutor {
	
	public static double EPSILON = 1E-8; 
	
	public static Value parseExpression(String line){
		return executeNode(ExpressionParser.parseExpression(line));
	}
	
	public static Value[] extractCommaDelimits(ASTNode root){
		ArrayList<Value> helperCall = extractCommaDelimitsHelper(root);
		Value[] newVals = new Value[helperCall.size()];
		helperCall.toArray(newVals);
		return newVals;
	}
	
	public static ArrayList<Value> extractCommaDelimitsHelper(ASTNode root){
		if (root.op != null && root.op == Operator.CommaDelimit){
			ArrayList<Value> first = extractCommaDelimitsHelper(root.args[0]);
			ArrayList<Value> second = extractCommaDelimitsHelper(root.args[1]);
			first.addAll(second);
			return first;
		}else{
			ArrayList<Value> ret = new ArrayList<Value>();
			ret.add(executeNode(root));
			return ret;
		}
	}
	
	public static ASTNode[] extractSemicolonDelimitedNodes(ASTNode root){
		ArrayList<ASTNode> helperCall = extractSemicolonDelimitedNodesHelper(root);
		ASTNode[] newVals = new ASTNode[helperCall.size()];
		helperCall.toArray(newVals);
		return newVals;
	}
	
	public static ArrayList<ASTNode> extractSemicolonDelimitedNodesHelper(ASTNode root){
		if (root.op != null && root.op == Operator.ExpressionDelimit){
			ArrayList<ASTNode> first = extractSemicolonDelimitedNodesHelper(root.args[0]);
			ArrayList<ASTNode> second = extractSemicolonDelimitedNodesHelper(root.args[1]);
			first.addAll(second);
			return first;
		}else{
			ArrayList<ASTNode> ret = new ArrayList<ASTNode>();
			ret.add(root);
			return ret;
		}
	}
	
	public static Value executeNode(ASTNode root){
		if (root.name != null){
			if (root.value != null){
				return root.value;
			}
			String expr = root.name;
			if (Variable.contains(expr)){
				return Variable.getVariableValue(expr);
			}
			if (Variable.isInteger(expr)){
				return new Value(VariableTypes.Integer, new BigInteger(expr));
			}
			if (Variable.isDouble(expr)){
				return new Value(VariableTypes.Double, new BigDecimal(expr));
			}
			if (Variable.isBool(expr)){
				return new Value(VariableTypes.Boolean, Variable.convertToBool(expr));
			}
			if (Variable.isString(expr)){
				return Variable.convertToString(expr);
			}
			Interpreter.throwError("Could not find a value for "+root.name);
			return new Value(VariableTypes.Nil, null);
		}
		switch (root.op){
			case Addition:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Addition did not have 2 arguments");
				}
				return Variable.addValues(executeNode(root.args[0]), executeNode(root.args[1]));
			}
			case Multiplication:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Multiplication did not have 2 arguments");
				}
				return Variable.mulValues(executeNode(root.args[0]), executeNode(root.args[1]));
			}
			case Subtraction:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Subtraction did not have 2 arguments");
				}
				return Variable.subValues(executeNode(root.args[0]), executeNode(root.args[1]));
			}
			case Exponentiation:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Exponentiation did not have 2 arguments");
				}
				return Variable.expValues(executeNode(root.args[0]), executeNode(root.args[1]));
			}
			case Logarithm:
			{
				if (root.args.length == 1){
					return Variable.lnValue(executeNode(root.args[0]));
				}
				if (root.args.length != 2){
					Interpreter.throwError("Logarithm had a strange number of arguments: "+root);
				}
				return Variable.logValues(executeNode(root.args[0]), executeNode(root.args[1]));
			}
			case Division:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Division did not have 2 arguments");
				}
				return Variable.divVals(executeNode(root.args[0]), executeNode(root.args[1]));
			}
			case Modulus:
			{
				if (root.args.length == 1){
					return Variable.divVals(executeNode(root.args[0]), Value.integer(100));
				}
				if (root.args.length != 2){
					Interpreter.throwError("Modulus had a strange number of arguments: "+root);
				}
				return Variable.modVals(executeNode(root.args[0]), executeNode(root.args[1]));
			}
			case Assignment:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Assignment did not have 2 arguments");
				}
				// TODO handle multiple assignment (e.g a,b = 2,3)
				if (root.args[0].name == null){
					if (root.args[0].op == Operator.arrayLookup){
						String arrayName = root.args[0].args[0].name;
						BigInteger index = Variable.getIntValue(executeNode(root.args[0].args[1]));
						Value newVal = executeNode(root.args[1]);
						Variable.setValueOfArray(arrayName, index, newVal);
						return newVal;
					}else if (root.args[0].op == Operator.DotOperator){
						Value object = executeNode(root.args[0].args[0]);
						if (object.type != VariableTypes.Object){
							Interpreter.throwError("Tried accessing "+object+" as an object");
						}
						BLZObject obj = (BLZObject) object.value;
						Value newVal = executeNode(root.args[1]);
						Variable.setValue(root.args[0].args[1].name, newVal, obj.objectContext);
						return newVal;
					}
					Interpreter.throwError("Did not know how to handle assignment of: "+root.args[0]);
				}
				Variable.setValue(root.args[0].name, executeNode(root.args[1]));
				return Variable.getValue(root.args[0].name);
			}
			case Increment:
			{
				if (root.args.length != 1){
					Interpreter.throwError("Increment had more than one variable");
				}
				if (root.args[0].name == null){
					Interpreter.throwError("Did not know how to increment: "+root.args[0]);
				}
				Variable.setValue(root.args[0].name, Variable.addValues(Variable.getValue(root.args[0].name), Value.integer(1)));
				return Variable.getValue(root.args[0].name);
			} 
			case Decrement:
			{
				if (root.args.length != 1){
					Interpreter.throwError("Decrement had more than one variable");
				}
				if (root.args[0].name == null){
					Interpreter.throwError("Did not know how to decrement: "+root.args[0]);
				}
				Variable.setValue(root.args[0].name, Variable.subValues(Variable.getValue(root.args[0].name), Value.integer(1)));
				return Variable.getValue(root.args[0].name);
			}
			case Comparison:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Comparison did not have 2 arguments");
				}
				return Value.bool(executeNode(root.args[0]).equals(executeNode(root.args[1])));
			}
			case NotEqual:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Negative Comparison did not have 2 arguments");
				}
				return Value.bool(!executeNode(root.args[0]).equals(executeNode(root.args[1])));
			}
			case LessThan:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Less Than did not have 2 arguments");
				}
				Value v1 = executeNode(root.args[0]);
				Value v2 = executeNode(root.args[1]);
				if (!Variable.isDecimalValue(v1) || !Variable.isDecimalValue(v2)){
					Interpreter.throwError("One of "+v1+" or "+v2+" is not a decimal value");
				}
				BigDecimal d1 = Variable.getDoubleVal(v1);
				BigDecimal d2 = Variable.getDoubleVal(v2);
				return Value.bool(d1.compareTo(d2) < 0);
			}
			case LessThanEqual:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Less Than Or Equal did not have 2 arguments");
				}
				Value v1 = executeNode(root.args[0]);
				Value v2 = executeNode(root.args[1]);
				if (!Variable.isDecimalValue(v1) || !Variable.isDecimalValue(v2)){
					Interpreter.throwError("One of "+v1+" or "+v2+" is not a decimal value");
				}
				BigDecimal d1 = Variable.getDoubleVal(v1);
				BigDecimal d2 = Variable.getDoubleVal(v2);
				return Value.bool(d1.compareTo(d2) <= 0);
			}
			case GreaterThan:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Greater Than did not have 2 arguments");
				}
				Value v1 = executeNode(root.args[0]);
				Value v2 = executeNode(root.args[1]);
				if (!Variable.isDecimalValue(v1) || !Variable.isDecimalValue(v2)){
					Interpreter.throwError("One of "+v1+" or "+v2+" is not a decimal value");
				}
				BigDecimal d1 = Variable.getDoubleVal(v1);
				BigDecimal d2 = Variable.getDoubleVal(v2);
				return Value.bool(d1.compareTo(d2) > 0);
			}
			case GreaterThanEqual:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Greater Than Or Equal did not have 2 arguments");
				}
				Value v1 = executeNode(root.args[0]);
				Value v2 = executeNode(root.args[1]);
				if (!Variable.isDecimalValue(v1) || !Variable.isDecimalValue(v2)){
					Interpreter.throwError("One of "+v1+" or "+v2+" is not a decimal value");
				}
				BigDecimal d1 = Variable.getDoubleVal(v1);
				BigDecimal d2 = Variable.getDoubleVal(v2);
				return Value.bool(d1.compareTo(d2) >= 0);
			}
			case CommaDelimit:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Comma Delimiting did not have 2 arguments");
				}
				Value[] v1 = Variable.getValueAsArray(executeNode(root.args[0]));
				Value[] v2 = Variable.getValueAsArray(executeNode(root.args[1]));
				int size = v1.length + v2.length;
				Value[] newArr = new Value[size];
				for (int i = 0; i < v1.length; i++){
					newArr[i] = v1[i];
				}
				for (int i = 0; i < v2.length; i++){
					newArr[i + v1.length] = v2[i];
				}
				return Value.arr(newArr);
			}
			case functionCall:
			{
				if (root.args.length > 2){
					Interpreter.throwError("Somehow a function had a weird numbeer of arguments");
				}
				Value methodVal = executeNode(root.args[0]);
				if (methodVal.type != VariableTypes.Method){
					Interpreter.throwError("Tried to call a non-method "+methodVal);
				}
				Method toCall = (Method) methodVal.value;
				Value[] args;
				if (root.args.length == 1){	// No Args
					args = new Value[0];
				}else{	// Some args
					if (root.args[1].op == Operator.CommaDelimit){
						args = Variable.getValueAsArray(executeNode(root.args[1]));
					}else{
						Value[] arg = {executeNode(root.args[1])};
						args = arg;
					}
				}
				return Executor.functionCall(toCall, args);
			}
			case ApproximateComparison:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Greater Than Or Equal did not have 2 arguments");
				}
				Value v1 = executeNode(root.args[0]);
				Value v2 = executeNode(root.args[1]);
				if (!Variable.isDecimalValue(v1) || !Variable.isDecimalValue(v2)){
					Interpreter.throwError("One of "+v1+" or "+v2+" is not a decimal value");
				}
				BigDecimal d1 = Variable.getDoubleVal(v1);
				BigDecimal d2 = Variable.getDoubleVal(v2);
				return Value.bool(d1.subtract(d2).abs().doubleValue() < EPSILON);
			}
			case arrayLookup:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Array Lookup did not have 2 arguments");
				}
				if (root.args[0].name == null){
					Value arr = executeNode(root.args[0]);
					if (arr.type != VariableTypes.Array){
						Interpreter.throwError("Did not know how to access "+root.args[0]+" as an array");
					}
					BigInteger index = Variable.getIntValue(executeNode(root.args[1]));
					return Variable.getValueOfArray(arr, index);
				}
				String name = root.args[0].name;
				BigInteger index = Variable.getIntValue(executeNode(root.args[1]));
				return Variable.getValueOfArray(name, index);
			}
			case arrayLiteral:
			{
				if (root.args.length != 1){
					Interpreter.throwError("Array Literal did not have 1 argument");
				}
				return Value.arr(extractCommaDelimits(root.args[0]));
			}
			case environmentVariableLookup:
			{
				for (SystemEnv se : SystemEnv.values()){
					if (se.name.equals(root.args[0].name)){
						return Variable.getEnvVariable(se);
					}
				}
				System.out.println("Could Not Find Environment Variable "+root.args[0].name);
				return new Value(VariableTypes.Nil, null);
			}
			case DotOperator:
			{
				if (root.args.length != 2){
					Interpreter.throwError("Dot Operator did not have 2 arguments");
				}
				Value object = executeNode(root.args[0]);
				if (object.type != VariableTypes.Object){
					Interpreter.throwError("Did not know how to handle dot operator on non-object");
				}
				BLZObject obj = (BLZObject) object.value;
				if (root.args[1].op == Operator.functionCall){
					root.args[1].args[0] = new ASTNode(Variable.getValue(root.args[1].args[0].name, obj.objectContext));
					return executeNode(root.args[1]);
				}
				if (root.args[1].op == Operator.arrayLookup){
					BigInteger index = Variable.getIntValue(executeNode(root.args[1].args[1]));
					return Variable.getValueOfArray(Variable.getValue(root.args[1].args[0].name, obj.objectContext), index);
				}
				return Variable.getValue(root.args[1].name, obj.objectContext);
			}
			case parensOpen:
			{
				Interpreter.throwError("Parenthesis were run.... You really broke something somehow");
				break;
			}
			default:
				return new Value(VariableTypes.Nil, null);
		}
		return new Value(VariableTypes.Nil, null);
	}
	


	

}
