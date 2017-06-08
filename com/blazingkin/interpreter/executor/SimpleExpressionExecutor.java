package com.blazingkin.interpreter.executor;

import java.util.regex.Matcher;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.variables.BLZRational;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class SimpleExpressionExecutor {

	public static double EPSILON = 1E-8; 
	
	public static Value evaluate(SimpleExpression type, String expr){
		switch(type){
		case addition:
		{	String[] splits = expr.split("\\+");
			if (splits.length < 2){
				Interpreter.throwError("Not enough arguments for addition");
			}
			Value accumulator = SimpleExpressionParser.parseExpression(splits[0]);
			for (int i = 1; i < splits.length; i++){
				accumulator = Variable.addValues(accumulator, SimpleExpressionParser.parseExpression(splits[i]));
			}
			return accumulator;
		}
		case assignment:
		{	
			String[] splits = expr.split(type.syntax.pattern());
			if (splits.length != 2){
				Interpreter.throwError("Unable to parse assignment operation: "+expr);
			}
			Value right = SimpleExpressionParser.parseExpression(splits[1]);
			Variable.setValue(splits[0].trim(), right);
			return right;
		}
		case comparison:
		{	String[] splits = expr.split(type.syntax.pattern());
			if (splits.length < 2){
				Interpreter.throwError("Not enough arguments for comparison");
			}
			boolean flag = true;
			Value toCompare = SimpleExpressionParser.parseExpression(splits[0]);
			for (int i = 1; i < splits.length; i++){
				flag = flag && toCompare.equals(SimpleExpressionParser.parseExpression(splits[i]));
			}
			return new Value(VariableTypes.Boolean, flag);
		}
		case division:
		{	String[] splits = expr.split("/");
			if (splits.length != 2){
				Interpreter.throwError("Too many arguments for division");
			}
			Value top = SimpleExpressionParser.parseExpression(splits[0]);
			Value bottom = SimpleExpressionParser.parseExpression(splits[1]);
			if (top.type == VariableTypes.Integer && bottom.type == VariableTypes.Integer){
				return Value.rational((int)top.value, (int)bottom.value);
			}
			if (Variable.isValRational(top) && Variable.isValRational(bottom)){
				BLZRational toprat = Variable.getRationalVal(top);
				BLZRational botrat = Variable.getRationalVal(bottom);
				BLZRational botratopp = (BLZRational) Value.rational(botrat.den, botrat.num).value;
				BLZRational prod = toprat.multiply(botratopp);
				if (prod.den == 1){
					return new Value(VariableTypes.Integer, (int) prod.num);
				}
				return new Value(VariableTypes.Rational, prod);
			}
			if ((Variable.isValDouble(top) || Variable.isValRational(top))
					&& (Variable.isValDouble(bottom) || Variable.isValRational(bottom))){
				double dtop = Variable.getDoubleVal(top);
				double dbot = Variable.getDoubleVal(bottom);
				return new Value(VariableTypes.Double, dtop/dbot);
			}

			break;
		}
		case multiplication:
		{	String[] splits = expr.split("\\*");
			if (splits.length < 2){
				Interpreter.throwError("Not enough arguments for multiplication");
			}
			Value accumulator = SimpleExpressionParser.parseExpression(splits[0]);
			for (int i = 1; i < splits.length; i++){
				accumulator = Variable.mulValues(accumulator, SimpleExpressionParser.parseExpression(splits[i]));
			}
			return accumulator;
		}
		case parenthesis:
		{	
			String replString = expr;
			Matcher mat =  type.syntax.matcher(replString);
			while (mat.find()){
				String toReplace = mat.group().replace("(", "").replace(")", "");
				String newVal = SimpleExpressionParser.parseExpression(toReplace).value.toString();
				replString = type.syntax.matcher(replString).replaceFirst(newVal);
				mat =  type.syntax.matcher(replString);
			}
			return SimpleExpressionParser.parseExpression(replString);
		}
		case subtraction:
		{	
			String[] splits = expr.split(type.syntax.pattern());
			if (splits.length != 2){
				Interpreter.throwError("Incorrect number of arguments for subtraction");
			}
			
			return Variable.subValues(SimpleExpressionParser.parseExpression(splits[0]), SimpleExpressionParser.parseExpression(splits[1]));
		}
		case exponentiation:
		{
			String[] splits = expr.split(type.syntax.pattern());
			if (splits.length != 2){
				Interpreter.throwError("Unable to parse exponentiation, it should be base ** exponent");
			}
			return Variable.expValues(SimpleExpressionParser.parseExpression(splits[0]), SimpleExpressionParser.parseExpression(splits[1]));
		}
		case logarithm:
		{
			String[] splits = expr.split(type.syntax.pattern());
			if (splits.length != 2){
				Interpreter.throwError("Unable to parse logarithm, it should be input __ base");
			}
			return Variable.logValues(SimpleExpressionParser.parseExpression(splits[0]), SimpleExpressionParser.parseExpression(splits[1]));
		}
		case approximateComparison:
		{
			String[] splits = expr.split(type.syntax.pattern());
			if (splits.length < 2){
				Interpreter.throwError("Incorrect number of arguments for approximate comparison");
			}
			Value first = SimpleExpressionParser.parseExpression(splits[0]);
			if (!Variable.isDecimalValue(first)){
				return evaluate(SimpleExpression.comparison, expr);
			}
			double frst = Variable.getDoubleVal(first);
			for (int i = 1; i < splits.length; i++){
				Value val = SimpleExpressionParser.parseExpression(splits[i]);
				if (!Variable.isDecimalValue(val)){
					return evaluate(SimpleExpression.comparison, expr);
				}
				double v = Variable.getDoubleVal(val);
				if (Math.abs(frst - v) > EPSILON){
					return Value.bool(false);
				}
			}
			return Value.bool(true);
		}
		default:
			System.err.println(expr);
			Interpreter.throwError("Simple Expression Executor Inititialized with invalid type");
			break;
			
		
		}
		System.err.println(expr);
		Interpreter.throwError("Simple Expression Executor Inititialized with invalid type");
		
		return new Value(VariableTypes.Nil, null);
	}
	
	
}
