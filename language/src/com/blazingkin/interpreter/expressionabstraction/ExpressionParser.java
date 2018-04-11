package com.blazingkin.interpreter.expressionabstraction;

import java.util.Stack;

import com.blazingkin.interpreter.variables.Variable;

public class ExpressionParser {
	
	private static Stack<Operator> operatorStack = new Stack<Operator>();
	private static Stack<ASTNode> operandStack = new Stack<ASTNode>();
	private static Stack<ASTNode> functionNames = new Stack<ASTNode>();
	
	public static String[] parseBindingWithArguments(String line){
		int firstParensIndex = line.indexOf('(');
		if (firstParensIndex != -1) {
			/* Get the name as the part before ( */
			String name = line.substring(0,firstParensIndex).trim();
			
			/* Get the index of ) */
			int lastParensIndex = line.lastIndexOf(')');
			if (lastParensIndex == -1) {
				lastParensIndex = line.length();
			}
			
			/* Split out the part that is just variables */
			String vars = line.substring(firstParensIndex + 1, lastParensIndex);
			
			/* If there are none, then just return the function name */
			if (vars.length() == 0) {
				String result[] = {name};
				return result;
			}
			
			/* Split out the variables and return the whole list */
			String vNames[] = vars.split(",");
			String result[] = new String[vNames.length + 1];
			for (int i = 1; i <= vNames.length; i++) {
				result[i] = vNames[i - 1].trim();
			}
			result[0] = name;
			return result;
		}else {
			/* It has no (, so we can return the name and we're done */
			String[] result = {line};
			return result;
		}
	}
	
	public static ASTNode parseAndCollapse(String line){
		return parseExpression(line).collapse();
	}
	
	// Use the shunting yard algorithm to parse a line
	public static ASTNode parseExpression(String line){
		operatorStack.clear();
		operandStack.clear();
		functionNames.clear();
		char[] lne = line.toCharArray();
		boolean ignoreMode = false;
		String building = "";
		boolean inQuotes = false;
		for (int i = 0; i < lne.length; i++){
			if (Operator.symbols.contains(building + lne[i]) || (ignoreMode && lne[i] != '}') || (inQuotes && lne[i] != '\"')){
				building += lne[i];
			}else if (Operator.symbols.contains(""+lne[i]) && !(lne[i] == '.' && Variable.isInteger(building))){
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
				operandStack.push(new ValueASTNode(building));
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
						if (!operatorStack.empty() && operatorStack.peek() == Operator.DotOperator){
							operandStack.push(new ValueASTNode(building));
							building = "";
							combineBinaryExpression(operatorStack, operandStack);
							functionNames.push(operandStack.peek());
							operatorStack.push(Operator.functionCall);
						}else{
							operatorStack.push(Operator.functionCall);
							ASTNode functionName = new ValueASTNode(building);
							operandStack.push(functionName);
							functionNames.push(functionName);
							building = "";
						}
					}
					break;
				case ')':
					if (!building.isEmpty()){
						operandStack.push(new ValueASTNode(building));
						building = "";
					}
					while (operatorStack.peek() != Operator.parensOpen && operatorStack.peek() != Operator.functionCall){
						pushNewExpression(operatorStack, operandStack);
					}
					if (operatorStack.peek() == Operator.parensOpen){
						operatorStack.pop(); // We don't care about the open parens
					}else{	// If it is a function call
						if (operandStack.size() > 0 && functionNames.peek().equals(operandStack.peek())){
							pushUnaryExpression(operatorStack, operandStack);
						}else{
							combineBinaryExpression(operatorStack, operandStack); // Add the function call to the stack
						}
						functionNames.pop();
					}
					break;
				case '[':
					if (operatorStack.empty() && operandStack.size() > 0 && checkTopExpressionOperator(operandStack) == Operator.arrayLookup){ // Multidimensional arrays
						// TODO handle if the building string is not empty, there is a test that shows why this is broken
						operatorStack.push(Operator.arrayLookup);
					}else if (building.isEmpty() && !(i != 0 && lne[i-1] == ')')){
						operatorStack.push(Operator.arrayLiteral);
					}else{
						if (!building.isEmpty()){
							operandStack.push(new ValueASTNode(building));
							building = "";
						}
						operatorStack.push(Operator.arrayLookup);
					}
					break;
				case ']':
					if (!building.isEmpty()){
						operandStack.push(new ValueASTNode(building));
						building = "";
					}else{
						if (operatorStack.peek() == Operator.arrayLiteral){
							// For empty arrays
							// i.e. a = []
							operandStack.push(OperatorASTNode.newNode(operatorStack.pop(), (ASTNode)null));
							break;
						}
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
				case '{':
					if (!building.isEmpty()){
						operandStack.push(new ValueASTNode(building));
						building = "";
					}
					operatorStack.push(Operator.environmentVariableLookup);
					ignoreMode = true;
					break;
				case '}':
					operandStack.push(new ValueASTNode(building));
					building = "";
					ignoreMode = false;
					pushUnaryExpression(operatorStack, operandStack);
					break;
				case '"':
					building += '"';
					if (inQuotes){
						if (lne[i-1] == '\\'){
							building = building.substring(0,building.length() - 2) + '"';
							break;
						}
					}
					inQuotes = !inQuotes;
					break;
				default:
					if (!inQuotes && Character.isWhitespace(lne[i])){
						if (building.length() > 0){
							operandStack.push(new ValueASTNode(building));
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
			operandStack.push(new ValueASTNode(building));
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
		operandStack.push(OperatorASTNode.newNode(op, arg1, arg2));
	}
	
	public static void pushUnaryExpression(Stack<Operator> operatorStack, Stack<ASTNode> operandStack){
		Operator op = operatorStack.pop();
		ASTNode arg = operandStack.pop();
		operandStack.push(OperatorASTNode.newNode(op, arg));
	}
	
	private static Operator checkTopExpressionOperator(Stack<ASTNode> stack){
		if (stack.size() != 0){
			ASTNode top = stack.peek();
			if (top instanceof OperatorASTNode){
				OperatorASTNode otop = (OperatorASTNode) top;
				return otop.op;
			}
			return null;
		}
		return null;
	}
	
	
}
