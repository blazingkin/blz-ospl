package com.blazingkin.interpreter.expressionabstraction;

import java.util.Stack;

public class ExpressionParser {


	
	// Use the shunting yard algorithm to parse a line
	public static ASTNode parseExpression(String line){
		Stack<Operator> operatorStack = new Stack<Operator>();
		Stack<ASTNode> operandStack = new Stack<ASTNode>();
		Stack<String> functionNames = new Stack<String>();
		char[] lne = line.toCharArray();
		String building = "";
		for (int i = 0; i < lne.length; i++){
			if (Operator.symbols.contains(building + lne[i])){
				building += lne[i];
			}else if (Operator.symbols.contains(""+lne[i])){
				if (!Operator.symbolLookup.keySet().contains(""+lne[i])){	// lookahead to check for multicharacter expressoins
					String subBuilding = ""+lne[i];
					boolean found = false;
					for (int y = i + 1; y < lne.length; y++){
						subBuilding += lne[y];
						if (Operator.symbolLookup.keySet().contains(subBuilding)){	// It is an operator
							found = true;
							break;
						}
					}
					if (!found){
						building += lne[i];
						continue;
					}
				}
				operandStack.push(new ASTNode(building));
				building = ""+lne[i];
			}else{
				if (Operator.symbols.contains(building)){
					boolean isNegativeNumber = building.equals("-") && Character.isDigit(lne[i]); // make sure it fits the format for being a negative number
					isNegativeNumber = isNegativeNumber && ((operandStack.empty() && operatorStack.empty()) || (!operatorStack.empty() && !operandStack.empty()));
					if (!isNegativeNumber){
						pushNewOperator(operatorStack, operandStack, Operator.symbolLookup.get(building));
						building = "";
					}
				}
				switch(lne[i]){	// Switch is faster than else-ifs
				case '(':
					if (building.isEmpty()){
						operatorStack.push(Operator.parensOpen);
					}else{
						operatorStack.push(Operator.functionCall);
						operandStack.push(new ASTNode(building));
						functionNames.push(building);
						building = "";
					}
					break;
				case ')':
					if (!building.isEmpty()){
						operandStack.push(new ASTNode(building));
						building = "";
					}
					while (operatorStack.peek() != Operator.parensOpen && operatorStack.peek() != Operator.functionCall){
						pushNewExpression(operatorStack, operandStack);
					}
					if (operatorStack.peek() == Operator.parensOpen){
						operatorStack.pop(); // We don't care about the open parens
					}else{	// If it is a function call
						if (operandStack.size() > 0 && functionNames.peek().equals(operandStack.peek().name)){
							pushUnaryExpression(operatorStack, operandStack);
						}else{
							combineBinaryExpression(operatorStack, operandStack); // Add the function call to the stack
						}
						functionNames.pop();
					}
					break;
				case '[':
					if (operatorStack.empty() && operandStack.size() > 0 && operandStack.peek().op == Operator.arrayLookup){ // Multidimensional arrays
						// TODO handle if the building string is not empty, there is a test that shows why this is broken
						operatorStack.push(Operator.arrayLookup);
					}else if (building.isEmpty()){
						operatorStack.push(Operator.arrayLiteral);
					}else{
						operatorStack.push(Operator.arrayLookup);
						operandStack.push(new ASTNode(building));
						building = "";
					}
					break;
				case ']':
					if (!building.isEmpty()){
						operandStack.push(new ASTNode(building));
						building = "";
					}
					while (operatorStack.peek() != Operator.arrayLookup && operatorStack.peek() != Operator.arrayLiteral){
						combineBinaryExpression(operatorStack, operandStack);
					}
					if (operatorStack.peek() == Operator.arrayLiteral){
						pushUnaryExpression(operatorStack, operandStack);
					}else{
						combineBinaryExpression(operatorStack, operandStack);
					}
					break;
				default:
					if (Character.isWhitespace(lne[i])){
						if (building.length() > 0){
							operandStack.push(new ASTNode(building));
							building = "";
						}
					}else{
						building += lne[i];
					}
				}
			}
		}
		if (Operator.symbols.contains(building)){	// It probably shouldn't end on an operator, but let's just handle it anyways
			pushNewOperator(operatorStack, operandStack, Operator.symbolLookup.get(building));
			building = "";
		}
		if (!building.isEmpty()){
			operandStack.push(new ASTNode(building));
		}
		while (!operatorStack.isEmpty()){
			pushNewExpression(operatorStack, operandStack);
		}
		return operandStack.pop();
	}
	
	public static void pushNewOperator(Stack<Operator> operatorStack, Stack<ASTNode> operandStack, Operator opToPush){
		while (!operatorStack.empty() && operatorStack.peek().precedence <= opToPush.precedence){
			pushNewExpression(operatorStack, operandStack);
		}
		operatorStack.push(opToPush);
	}
	
	public static void pushNewExpression(Stack<Operator> operatorStack, Stack<ASTNode> operandStack){
		Operator op = operatorStack.peek();
		switch(op.type){
		case Binary:
			combineBinaryExpression(operatorStack, operandStack);
			break;
		case Unary:
			pushUnaryExpression(operatorStack, operandStack);
			break;
		case UnaryOrBinary:
			if (operandStack.size() == 1){
				pushUnaryExpression(operatorStack, operandStack);
			}
			combineBinaryExpression(operatorStack, operandStack);
			break;
		case None:
			break;
		}
	}
	
	public static void combineBinaryExpression(Stack<Operator> operatorStack, Stack<ASTNode> operandStack){
		Operator op = operatorStack.pop();
		ASTNode arg2 = operandStack.pop();
		ASTNode arg1 = operandStack.pop();
		ASTNode[] args = {arg1, arg2};
		operandStack.push(new ASTNode(op, args));
	}
	
	public static void pushUnaryExpression(Stack<Operator> operatorStack, Stack<ASTNode> operandStack){
		Operator op = operatorStack.pop();
		ASTNode arg = operandStack.pop();
		ASTNode[] args = {arg};
		operandStack.push(new ASTNode(op, args));
	}
	
	
}