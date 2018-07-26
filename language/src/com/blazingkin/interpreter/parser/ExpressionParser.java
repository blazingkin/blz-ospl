package com.blazingkin.interpreter.parser;

import java.util.Optional;
import java.util.Stack;

import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.sourcestructures.RegisteredLine;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.OperatorASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.variables.Variable;

public class ExpressionParser {
	
	private static ThreadLocal<Stack<Operator>> operatorStack = new ThreadLocal<Stack<Operator>>(){
		@Override
		protected Stack<Operator> initialValue(){
			return new Stack<Operator>();
		}
	};
	private static ThreadLocal<Stack<ASTNode>> operandStack = new ThreadLocal<Stack<ASTNode>>() {
		@Override
		protected Stack<ASTNode> initialValue(){
			return new Stack<ASTNode>();
		}
	};
	private static ThreadLocal<Stack<ASTNode>> functionNames = new ThreadLocal<Stack<ASTNode>>(){
		@Override
		protected Stack<ASTNode> initialValue(){
			return new Stack<ASTNode>();
		}
	};
	
	public static Optional<RegisteredLine> parseLine(SourceLine source) throws SyntaxException {
		String splits[] = source.line.split(" ");
			if (splits.length == 0){
				return Optional.empty();
			}
			try{
				Instruction instr = Instruction.getInstructionType(splits[0]);
				if (instr == null || instr == Instruction.INVALID){
					if (source.line.trim().isEmpty() || source.line.trim().charAt(0) == ':'){
						return Optional.empty();
					}
					return Optional.of(new RegisteredLine(ExpressionParser.parseExpression(source.line), source.lineNumber));
				}
				String newStr = source.line.replaceFirst(splits[0], "").trim();
				return Optional.of(new RegisteredLine(instr, newStr, source.lineNumber));
			}catch(Exception e){
				throw new SyntaxException("Did not know how to parse line: "+source.line+" on line "+source.lineNumber);
			}
	}

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
		Stack<Operator> opStack = operatorStack.get();
		Stack<ASTNode> opandStack = operandStack.get();
		Stack<ASTNode> funcNames = functionNames.get();
		opStack.clear();
		opandStack.clear();
		funcNames.clear();
		char[] lne = line.toCharArray();
		boolean ignoreMode = false;
		String building = "";
		boolean inQuotes = false;
		for (int i = 0; i < lne.length; i++){
			if (Operator.symbols.contains(building + lne[i]) || (ignoreMode && lne[i] != '}') || (inQuotes && lne[i] != '\"')){
				if (inQuotes && lne[i] == '\\'){
					building += lne[++i]; // Don't try to parse the next character, simply add it and ignore the \
				}else{
					building += lne[i];
				}
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
				opandStack.push(new ValueASTNode(building));
				building = ""+lne[i];
			}else{
				if (Operator.symbols.contains(building)){
					boolean isNegativeNumber = building.equals("-") && Character.isDigit(lne[i]); // make sure it fits the format for being a negative number
					isNegativeNumber = isNegativeNumber && ((opandStack.empty() && opStack.empty()) || (!opStack.empty() && !opandStack.empty()));
					if (!isNegativeNumber){
						pushNewOperator(opStack, opandStack, Operator.symbolLookup.get(building));
						building = "";
					}
				}
				switch(lne[i]){	// Switch is faster than else-ifs
				case '(':
					if (building.isEmpty()){
						opStack.push(Operator.parensOpen);
					}else{
						if (!opStack.empty() && opStack.peek() == Operator.DotOperator){
							opandStack.push(new ValueASTNode(building));
							building = "";
							combineBinaryExpression(opStack, opandStack);
							funcNames.push(opandStack.peek());
							opStack.push(Operator.functionCall);
						}else{
							opStack.push(Operator.functionCall);
							ASTNode functionName = new ValueASTNode(building);
							opandStack.push(functionName);
							funcNames.push(functionName);
							building = "";
						}
					}
					break;
				case ')':
					if (!building.isEmpty()){
						opandStack.push(new ValueASTNode(building));
						building = "";
					}
					while (opStack.peek() != Operator.parensOpen && opStack.peek() != Operator.functionCall){
						pushNewExpression(opStack, opandStack);
					}
					if (opStack.peek() == Operator.parensOpen){
						opStack.pop(); // We don't care about the open parens
					}else{	// If it is a function call
						if (opandStack.size() > 0 && funcNames.peek().equals(opandStack.peek())){
							pushUnaryExpression(opStack, opandStack);
						}else{
							combineBinaryExpression(opStack, opandStack); // Add the function call to the stack
						}
						funcNames.pop();
					}
					break;
				case '[':
					if (opStack.empty() && opandStack.size() > 0 && checkTopExpressionOperator(opandStack) == Operator.arrayLookup){ // Multidimensional arrays
						// TODO handle if the building string is not empty, there is a test that shows why this is broken
						opStack.push(Operator.arrayLookup);
					}else if (building.isEmpty() && !(i != 0 && lne[i-1] == ')')){
						opStack.push(Operator.arrayLiteral);
					}else{
						if (!building.isEmpty()){
							opandStack.push(new ValueASTNode(building));
							building = "";
						}
						opStack.push(Operator.arrayLookup);
					}
					break;
				case ']':
					if (!building.isEmpty()){
						opandStack.push(new ValueASTNode(building));
						building = "";
					}else{
						if (opStack.peek() == Operator.arrayLiteral){
							// For empty arrays
							// i.e. a = []
							opandStack.push(OperatorASTNode.newNode(opStack.pop(), (ASTNode)null));
							break;
						}
					}
					while (opStack.peek() != Operator.arrayLookup && opStack.peek() != Operator.arrayLiteral){
						combineBinaryExpression(opStack, opandStack);
					}
					if (opStack.peek() == Operator.arrayLiteral){
						pushUnaryExpression(opStack, opandStack);
					}else{
						combineBinaryExpression(opStack, opandStack);
					}
					break;
				case '{':
					if (!building.isEmpty()){
						opandStack.push(new ValueASTNode(building));
						building = "";
					}
					opStack.push(Operator.environmentVariableLookup);
					ignoreMode = true;
					break;
				case '}':
					opandStack.push(new ValueASTNode(building));
					building = "";
					ignoreMode = false;
					pushUnaryExpression(opStack, opandStack);
					break;
				case '"':
					building += '"';
					inQuotes = !inQuotes;
					break;
				case '\\':
					building += lne[++i]; // Add the next character, don't try to parse it
					break;
				default:
					if (!inQuotes && Character.isWhitespace(lne[i])){
						if (building.length() > 0){
							opandStack.push(new ValueASTNode(building));
							building = "";
						}
					}else{
						building += lne[i];
					}
				}
			}
		}
		if (Operator.symbols.contains(building)){	// It probably shouldn't end on an operator, but let's just handle it anyways
			pushNewOperator(opStack, opandStack, Operator.symbolLookup.get(building));
			building = "";
		}
		if (!building.isEmpty()){
			opandStack.push(new ValueASTNode(building));
		}
		while (!opStack.isEmpty()){
			pushNewExpression(opStack, opandStack);
		}
		return opandStack.pop();
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
