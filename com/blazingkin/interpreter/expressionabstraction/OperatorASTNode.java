package com.blazingkin.interpreter.expressionabstraction;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.Method;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.SystemEnv;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class OperatorASTNode extends ASTNode {

	public static double EPSILON = 1E-8; 
	
	Operator op;
	ASTNode[] args;
	public OperatorASTNode(Operator op, ASTNode... args){
		this.op = op;
		this.args = args;
	}
	
	public String toString(){
		String building = "";
		for (ASTNode node : args){
			building += node.toString() + ", ";
		}
		building += op.syntax;
		return "("+building+")"; 
	}
	
	public boolean equals(Object otherobj){
		if (otherobj instanceof OperatorASTNode){
			OperatorASTNode other = (OperatorASTNode) otherobj;
			if (this.op == other.op && this.args.length == other.args.length){
				for (int i = 0; i < this.args.length; i++){
					if (!this.args[i].equals(other.args[i])){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canCollapse() {
		switch(op){
			// We can't collapse things that mutate
			case Assignment: 
				return false;
			case Decrement:
				return false;
			case functionCall:
				return false;
			case Increment:
				return false;
			// Some environment variables are dynamic, others are not
			case environmentVariableLookup:
				if (args[0] instanceof ValueASTNode){
					String envName = ((ValueASTNode) args[0]).strVal;
					for (SystemEnv se : SystemEnv.values()){
						if (se.name.equals(envName)){
							return !se.dynamic;
						}
					}
				}
				return false;
			default:
				return canCollapseAll(args);
		}
	}

	@Override
	public ASTNode collapse() {
		if (canCollapse()){
			return new ValueASTNode(execute());
		}else{
			if (args != null){
				ASTNode[] newChildren = new ASTNode[args.length];
				for (int i = 0; i < args.length; i++){
					newChildren[i] = args[i].collapse();
				}
				return new OperatorASTNode(op, newChildren);
			}else{
				return this;
			}
		}
	}
	
	@Override
	public Operator getOperator(){
		return op;
	}

	@Override
	public Value execute() {
		switch (op){
		case Addition:
		{
			if (args.length != 2){
				Interpreter.throwError("Addition did not have 2 arguments");
			}
			return Variable.addValues(args[0].execute(), args[1].execute());
		}
		case Multiplication:
		{
			if (args.length != 2){
				Interpreter.throwError("Multiplication did not have 2 arguments");
			}
			return Variable.mulValues(args[0].execute(), args[1].execute());
		}
		case Subtraction:
		{
			if (args.length != 2){
				Interpreter.throwError("Subtraction did not have 2 arguments");
			}
			return Variable.subValues(args[0].execute(), args[1].execute());
		}
		case Exponentiation:
		{
			if (args.length != 2){
				Interpreter.throwError("Exponentiation did not have 2 arguments");
			}
			return Variable.expValues(args[0].execute(), args[1].execute());
		}
		case Logarithm:
		{
			if (args.length == 1){
				return Variable.lnValue(args[0].execute());
			}
			if (args.length != 2){
				Interpreter.throwError("Logarithm had a strange number of arguments: "+this);
			}
			return Variable.logValues(args[0].execute(), args[1].execute());
		}
		case Division:
		{
			if (args.length != 2){
				Interpreter.throwError("Division did not have 2 arguments");
			}
			return Variable.divVals(args[0].execute(), args[1].execute());
		}
		case Modulus:
		{
			if (args.length == 1){
				return Variable.divVals(args[0].execute(), Value.integer(100));
			}
			if (args.length != 2){
				Interpreter.throwError("Modulus had a strange number of arguments: "+this);
			}
			return Variable.modVals(args[0].execute(), args[1].execute());
		}
		case Assignment:
		{
			if (args.length != 2){
				Interpreter.throwError("Assignment did not have 2 arguments");
			}
			// TODO handle multiple assignment (e.g a,b = 2,3)
			if (args[0].getStoreName() == null){
				if (args[0].getOperator() == Operator.arrayLookup){
					OperatorASTNode lookupNode = (OperatorASTNode) args[0];
					String arrayName = lookupNode.args[0].getStoreName();
					BigInteger index = Variable.getIntValue(lookupNode.args[1].execute());
					Value newVal = args[1].execute();
					Variable.setValueOfArray(arrayName, index, newVal);
					return newVal;
				}else if (args[0].getOperator() == Operator.DotOperator){
					OperatorASTNode dotNode = (OperatorASTNode) args[0];
					Value object = dotNode.args[0].execute();
					if (object.type != VariableTypes.Object){
						Interpreter.throwError("Tried accessing "+object+" as an object");
					}
					BLZObject obj = (BLZObject) object.value;
					Value newVal = args[1].execute();
					Variable.setValue(dotNode.args[1].getStoreName(), newVal, obj.objectContext);
					return newVal;
				}
				Interpreter.throwError("Did not know how to handle assignment of: "+args[0]);
			}
			Variable.setValue(args[0].getStoreName(), args[1].execute());
			return Variable.getValue(args[0].getStoreName());
		}
		case Increment:
		{
			if (args.length != 1){
				Interpreter.throwError("Increment had more than one variable");
			}
			if (args[0].getStoreName() == null){
				Interpreter.throwError("Did not know how to increment: "+args[0]);
			}
			Variable.setValue(args[0].getStoreName(), Variable.addValues(Variable.getValue(args[0].getStoreName()), Value.integer(1)));
			return Variable.getValue(args[0].getStoreName());
		} 
		case Decrement:
		{
			if (args.length != 1){
				Interpreter.throwError("Decrement had more than one variable");
			}
			if (args[0].getStoreName() == null){
				Interpreter.throwError("Did not know how to decrement: "+args[0]);
			}
			Variable.setValue(args[0].getStoreName(), Variable.subValues(Variable.getValue(args[0].getStoreName()), Value.integer(1)));
			return Variable.getValue(args[0].getStoreName());
		}
		case Comparison:
		{
			if (args.length != 2){
				Interpreter.throwError("Comparison did not have 2 arguments");
			}
			return Value.bool(args[0].execute().equals(args[1].execute()));
		}
		case NotEqual:
		{
			if (args.length != 2){
				Interpreter.throwError("Negative Comparison did not have 2 arguments");
			}
			return Value.bool(!args[0].execute().equals(args[1].execute()));
		}
		case LessThan:
		{
			if (args.length != 2){
				Interpreter.throwError("Less Than did not have 2 arguments");
			}
			Value v1 = args[0].execute();
			Value v2 = args[1].execute();
			if (!Variable.isDecimalValue(v1) || !Variable.isDecimalValue(v2)){
				Interpreter.throwError("One of "+v1+" or "+v2+" is not a decimal value");
			}
			BigDecimal d1 = Variable.getDoubleVal(v1);
			BigDecimal d2 = Variable.getDoubleVal(v2);
			return Value.bool(d1.compareTo(d2) < 0);
		}
		case LessThanEqual:
		{
			if (args.length != 2){
				Interpreter.throwError("Less Than Or Equal did not have 2 arguments");
			}
			Value v1 = args[0].execute();
			Value v2 = args[1].execute();
			if (!Variable.isDecimalValue(v1) || !Variable.isDecimalValue(v2)){
				Interpreter.throwError("One of "+v1+" or "+v2+" is not a decimal value");
			}
			BigDecimal d1 = Variable.getDoubleVal(v1);
			BigDecimal d2 = Variable.getDoubleVal(v2);
			return Value.bool(d1.compareTo(d2) <= 0);
		}
		case GreaterThan:
		{
			if (args.length != 2){
				Interpreter.throwError("Greater Than did not have 2 arguments");
			}
			Value v1 = args[0].execute();
			Value v2 = args[1].execute();
			if (!Variable.isDecimalValue(v1) || !Variable.isDecimalValue(v2)){
				Interpreter.throwError("One of "+v1+" or "+v2+" is not a decimal value");
			}
			BigDecimal d1 = Variable.getDoubleVal(v1);
			BigDecimal d2 = Variable.getDoubleVal(v2);
			return Value.bool(d1.compareTo(d2) > 0);
		}
		case GreaterThanEqual:
		{
			if (args.length != 2){
				Interpreter.throwError("Greater Than Or Equal did not have 2 arguments");
			}
			Value v1 = args[0].execute();
			Value v2 = args[1].execute();
			if (!Variable.isDecimalValue(v1) || !Variable.isDecimalValue(v2)){
				Interpreter.throwError("One of "+v1+" or "+v2+" is not a decimal value");
			}
			BigDecimal d1 = Variable.getDoubleVal(v1);
			BigDecimal d2 = Variable.getDoubleVal(v2);
			return Value.bool(d1.compareTo(d2) >= 0);
		}
		case CommaDelimit:
		{
			if (args.length != 2){
				Interpreter.throwError("Comma Delimiting did not have 2 arguments");
			}
			Value[] v1 = Variable.getValueAsArray(args[0].execute());
			Value[] v2 = Variable.getValueAsArray(args[1].execute());
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
			if (args.length > 2){
				Interpreter.throwError("Somehow a function had a weird numbeer of arguments");
			}
			Value methodVal = args[0].execute();
			if (methodVal.type != VariableTypes.Method){
				Interpreter.throwError("Tried to call a non-method "+methodVal);
			}
			Method toCall = (Method) methodVal.value;
			Value[] args;
			if (this.args.length == 1){	// No Args
				args = new Value[0];
			}else{	// Some args
				if (this.args[1].getOperator() == Operator.CommaDelimit){
					args = Variable.getValueAsArray(this.args[1].execute());
				}else{
					Value[] arg = {this.args[1].execute()};
					args = arg;
				}
			}
			return Executor.functionCall(toCall, args);
		}
		case ApproximateComparison:
		{
			if (args.length != 2){
				Interpreter.throwError("Greater Than Or Equal did not have 2 arguments");
			}
			Value v1 = args[0].execute();
			Value v2 = args[1].execute();
			if (!Variable.isDecimalValue(v1) || !Variable.isDecimalValue(v2)){
				Interpreter.throwError("One of "+v1+" or "+v2+" is not a decimal value");
			}
			BigDecimal d1 = Variable.getDoubleVal(v1);
			BigDecimal d2 = Variable.getDoubleVal(v2);
			return Value.bool(d1.subtract(d2).abs().doubleValue() < EPSILON);
		}
		case arrayLookup:
		{
			if (args.length != 2){
				Interpreter.throwError("Array Lookup did not have 2 arguments");
			}
			if (args[0].getStoreName() == null){
				Value arr = args[0].execute();
				if (arr.type != VariableTypes.Array){
					Interpreter.throwError("Did not know how to access "+args[0]+" as an array");
				}
				BigInteger index = Variable.getIntValue(args[1].execute());
				return Variable.getValueOfArray(arr, index);
			}
			String name = args[0].getStoreName();
			BigInteger index = Variable.getIntValue(args[1].execute());
			return Variable.getValueOfArray(name, index);
		}
		case arrayLiteral:
		{
			if (args.length != 1){
				Interpreter.throwError("Array Literal did not have 1 argument");
			}
			return Value.arr(ExpressionExecutor.extractCommaDelimits(args[0]));
		}
		case environmentVariableLookup:
		{
			
			for (SystemEnv se : SystemEnv.values()){
				if (se.name.equals(args[0].getStoreName())){
					return Variable.getEnvVariable(se);
				}
			}
			System.out.println("Could Not Find Environment Variable "+args[0].getStoreName());
			return new Value(VariableTypes.Nil, null);
		}
		case DotOperator:
		{
			if (args.length != 2){
				Interpreter.throwError("Dot Operator did not have 2 arguments");
			}
			Value object = args[0].execute();
			if (object.type != VariableTypes.Object){
				Interpreter.throwError("Did not know how to handle dot operator on non-object");
			}
			BLZObject obj = (BLZObject) object.value;
			if (args[1].getOperator() == Operator.functionCall){
				OperatorASTNode fCall = (OperatorASTNode) args[1];
				fCall.args[0] = new ValueASTNode(Variable.getValue(fCall.args[0].getStoreName(), obj.objectContext));
				return args[1].execute();
			}
			if (args[1].getOperator() == Operator.arrayLookup){
				OperatorASTNode arrLookup = (OperatorASTNode) args[1];
				BigInteger index = Variable.getIntValue(arrLookup.args[1].execute());
				return Variable.getValueOfArray(Variable.getValue(arrLookup.args[0].getStoreName(), obj.objectContext), index);
			}
			return Variable.getValue(args[1].getStoreName(), obj.objectContext);
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
	
	public String getStoreName(){
		return null;
	}
	
}
