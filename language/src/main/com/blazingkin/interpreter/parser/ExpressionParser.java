package com.blazingkin.interpreter.parser;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

import com.blazingkin.interpreter.executor.astnodes.EnvironmentVariableLookupNode;
import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.sourcestructures.RegisteredLine;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.expressionabstraction.OperatorASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ExpressionParser {
	
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
					return Optional.of(RegisteredLine.build(ExpressionParser.parseExpression(source.line), source.lineNumber));
				}
				String newStr = source.line.replaceFirst(splits[0], "").trim();
				return Optional.of(RegisteredLine.build(instr, newStr, source.lineNumber));
			}catch(SyntaxException e){
				throw new SyntaxException(e.getMessage()+"\nDid not know how to parse line: "+source.line+" on line "+source.lineNumber);
			}
			catch(Exception e){
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
	
	public static ASTNode parseAndCollapse(String line) throws SyntaxException{
		return parseExpression(line).collapse();
	}

	public static ASTNode parseExpression(String line) throws SyntaxException {
		return parseExpression(LineLexer.lexLine(line));
	}

	public static ASTNode parseExpression(List<Token> tokens) throws SyntaxException {
		Stack<Operator> opStack = new Stack<Operator>();
		Stack<ASTNode> opandStack = new Stack<ASTNode>();
		boolean lastPushedIdent = false;
		for (int i = 0; i < tokens.size(); i++){
			Token current = tokens.get(i);
			switch (current.op){
				// Binary Operations
				case Addition:
				case Multiplication:
				case Division:
				case Exponentiation:
				case Logarithm:
				case Modulus:
				case Comparison:
				case LessThan:
				case GreaterThan:
				case NotEqual:
				case ExclusiveOr:
				case BitwiseAnd:
				case LeftShift:
				case RightShift:
				case ApproximateComparison:
				case LessThanEqual:
				case GreaterThanEqual:
				case LogicalAnd:
				case LogicalOr:
				case Assignment:
				case CommaDelimit:
				case ExpressionDelimit:
				case DotOperator:
				case Lambda:
					if (opandStack.empty()) {
						throw new SyntaxException("Binary operator "+current.op+" was the first part of the expression (It needs something before it)");
					}
					pushNewOperator(opStack, opandStack, current.op);
					lastPushedIdent = false;
				break;

				case Increment:
				case Decrement:
					pushNewOperator(opStack, opandStack, current.op);
					lastPushedIdent = false;	
				break;

				// Values
				case Ident:
					opandStack.push(new ValueASTNode(current.meta));
					lastPushedIdent = true;
				break;
				case String:
					opandStack.push(new ValueASTNode(Value.string(current.meta)));
					lastPushedIdent = true;
				break;
				case Number:
					// There are 2 more tokens and they are . and a number
					if (i + 2 < tokens.size() && tokens.get(i + 1).op == Operator.DotOperator && tokens.get(i + 2).op == Operator.Number){
						Value val = Value.doub(Double.parseDouble(current.meta + "." + tokens.get(i + 2).meta));
						opandStack.push(new ValueASTNode(val));
						i += 2; // Consume the next 2 tokens
					}else{
						opandStack.push(new ValueASTNode(current.meta));
					}
					lastPushedIdent = true;
				break;
				case environmentVariableLookup:
					ASTNode envName[] = {new ValueASTNode(current.meta)};
					opandStack.push(new EnvironmentVariableLookupNode(envName));
					lastPushedIdent = true;
				break;

				// Special Case
				case Subtraction:
					if (lastPushedIdent) {
						// Binary Subtraction
						if (opandStack.empty()) {
							throw new SyntaxException("Binary operator "+current.op+" was the first part of the expression (It needs something before it)");
						}
						pushNewOperator(opStack, opandStack, current.op);
						lastPushedIdent = false;
					} else {
						if (i + 1 < tokens.size() && tokens.get(i + 1).op == Operator.Number) {
							tokens.get(i + 1).meta = "-" + tokens.get(i + 1).meta;
						}else{
							throw new SyntaxException("Unexpected - in line");
						}
					}
				break;
				case Exclam:
					if (!opandStack.empty() && opandStack.peek() instanceof ValueASTNode) {
						ValueASTNode top = (ValueASTNode) opandStack.peek();
						if (top.val == null || top.val.type != VariableTypes.String) {
							throw new SyntaxException("Expected ! to follow an identifier name");
						}
						top.val = Value.string(top.val + "!");
					} else {
						throw new SyntaxException("Unexpected !, it should only follow a function name");
					}
				break;
				case parensOpen:
				{
					boolean foundEndParen = false;
					int depth = 0;
					Optional<ASTNode> inner = Optional.empty();
					for (int j = i + 1; j < tokens.size() && !foundEndParen; j++){
						if (tokens.get(j).op == Operator.parensClose){
							if (depth == 0){
								// Make the recursive call
								if (i + 1 != j){
									// If there is anything inside the parenthesis
									// subList is [i + 1, j)
									inner = Optional.of(parseExpression(tokens.subList(i + 1, j)));
								}
								i = j;
								foundEndParen = true;
							}else{
								depth--;
							}
						}else if (tokens.get(j).op == Operator.parensOpen) {
							depth++;
						}
					}
					if (!foundEndParen) {
						throw new SyntaxException("Opening parenthesis not closed!");
					}

					if (lastPushedIdent) {
						// Function call
						if (!opStack.empty() && opStack.peek() == Operator.DotOperator) {
							combineBinaryExpression(opStack, opandStack);
						}
						opStack.push(Operator.functionCall);
						if (inner.isPresent()) {
							// Arguments
							opandStack.push(inner.get());
							combineBinaryExpression(opStack, opandStack); // Add the function call to the stack
						} else {
							// No arguments
							pushUnaryExpression(opStack, opandStack);
						}
					} else {
						if (inner.isPresent()) {
							opandStack.push(inner.get());
						}else {
							throw new SyntaxException("Unexpected empty parenthesis ()");
						}
					}
					lastPushedIdent = true;
				}
				break;
				case sqBracketOpen:
					boolean foundEndParen = false;
					int depth = 0;
					Optional<ASTNode> inner = Optional.empty();
					for (int j = i + 1; j < tokens.size() && !foundEndParen; j++){
						if (tokens.get(j).op == Operator.sqBracketClose){
							if (depth == 0){
								// Make the recursive call
								// subList is [i + 1, j)
								if (i + 1 != j){
									inner = Optional.of(parseExpression(tokens.subList(i + 1, j)));
								}
								i = j;
								foundEndParen = true;
							}else{
								depth--;
							}
						}else if (tokens.get(j).op == Operator.sqBracketOpen) {
							depth++;
						}
					}

					if (lastPushedIdent) {
						// Array indexing
						if (inner.isPresent()){
							opandStack.push(inner.get());
						}else{
							throw new SyntaxException("No index present in array lookup");
						}
						opStack.push(Operator.arrayLookup);						
						combineBinaryExpression(opStack, opandStack);
					}else{
						// Array Literal
						opStack.push(Operator.arrayLiteral);
						if (inner.isPresent()){
							opandStack.push(inner.get());
							pushUnaryExpression(opStack, opandStack);
						}else{
							opandStack.push(OperatorASTNode.newNode(opStack.pop(), (ASTNode)null));
						}

					}

				break;
				default:
					throw new SyntaxException("Unknown token type: "+current);
			}
		}
		while (!opStack.empty()){
			pushNewExpression(opStack, opandStack);
		}
		if (opandStack.size() != 1){
			if (opandStack.size() == 0){
				throw new SyntaxException("Line contains no expressions");
			}else{
				throw new SyntaxException("Line contained multiple statements");
			}
		}
		return opandStack.get(0);
	}
	
	public static void pushNewOperator(Stack<Operator> operatorStack, Stack<ASTNode> operandStack, Operator opToPush) throws SyntaxException{
		while (!operatorStack.empty() && operatorStack.peek().precedence <= opToPush.precedence){
			pushNewExpression(operatorStack, operandStack);
		}
		operatorStack.push(opToPush);
	}
	
	public static void pushNewExpression(Stack<Operator> operatorStack, Stack<ASTNode> operandStack) throws SyntaxException{
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
	
	public static void combineBinaryExpression(Stack<Operator> operatorStack, Stack<ASTNode> operandStack) throws SyntaxException{
		Operator op = operatorStack.pop();
		ASTNode arg2 = operandStack.pop();
		ASTNode arg1 = operandStack.pop();
		operandStack.push(OperatorASTNode.newNode(op, arg1, arg2));
	}
	
	public static void pushUnaryExpression(Stack<Operator> operatorStack, Stack<ASTNode> operandStack) throws SyntaxException{
		Operator op = operatorStack.pop();
		ASTNode arg = operandStack.pop();
		operandStack.push(OperatorASTNode.newNode(op, arg));
	}
	
	
}
