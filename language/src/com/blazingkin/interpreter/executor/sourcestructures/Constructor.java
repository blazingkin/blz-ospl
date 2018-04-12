package com.blazingkin.interpreter.executor.sourcestructures;

import java.util.ArrayList;
import java.util.List;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.astnodes.MethodNode;
import com.blazingkin.interpreter.executor.astnodes.BlockNode;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStackElement;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.parser.MethodBlockParser;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Constructor implements RuntimeStackElement {

	private Process parent;
	private int lineNum;
	public String name;
	private boolean takesArguments;
	private String[] argumentNames = {};
	private ArrayList<MethodNode> methods = new ArrayList<MethodNode>();
	private ASTNode blockNode;
	
	public Constructor(Process parent, ParseBlock block) throws SyntaxException{
		String line = block.getHeader().replaceFirst("constructor", "").trim();
		if (line.equals("")){
			throw new SyntaxException("Empty constructor name!");
		}
		String[] parsed = ExpressionParser.parseBindingWithArguments(line);
		this.name = parsed[0];
		if (parsed.length > 1){
			takesArguments = true;
			argumentNames = new String[parsed.length - 1];
			for (int i = 0; i < argumentNames.length; i++){
				argumentNames[i] = parsed[i + 1];
			}
		}
		this.parent = parent;
		MethodBlockParser parser = new MethodBlockParser(parent);
		findMethods(block.getLines(), parser);
		blockNode = new BlockNode(block.getLines(), true);
	}

	private void findMethods(List<Either<String, ParseBlock>> lines, MethodBlockParser parser) throws SyntaxException {
		List<Either<String,ParseBlock>> methods = new ArrayList<Either<String, ParseBlock>>();
		for (Either<String, ParseBlock> line : lines){
			if (line.isRight()){
				if (parser.shouldParse(line.getRight().get().getHeader())){
					/* Do we have a method? */
					methods.add(line);
				}
			}
		}
		for (Either<String, ParseBlock> method : methods){
			lines.remove(method);
			try {
				this.methods.add((MethodNode) parser.parseBlock(method.getRight().get()));
			}catch (SyntaxException exception) {
				String message = exception.getMessage();
				message = "In constructor "+ this.name + "\n" + message;
				throw new SyntaxException(message);
			}
		}
	}

	public Constructor(Process parent, int lineNum, String line){
		if (line.equals("")){
			Interpreter.throwError("Empty constructor name!");
		}
		String[] parsed = ExpressionParser.parseBindingWithArguments(line);
		this.name = parsed[0];
		if (parsed.length > 1){
			takesArguments = true;
			argumentNames = new String[parsed.length - 1];
			for (int i = 0; i < argumentNames.length; i++){
				argumentNames[i] = parsed[i + 1];
			}
		}
		this.parent = parent;
		this.lineNum = lineNum;
	}
	
	public String getName(){
		return name;
	}
	
	public Process getParent(){
		return parent;
	}
	
	@Override
	public void onBlockStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBlockEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLineNum() {
		return lineNum;
	}
	
	public static Value initialize(Constructor con, Value[] args, boolean passByReference){
		BLZObject newObj = new BLZObject();
		boolean differentProcess = !con.parent.equals(Executor.getCurrentProcess());
		

		int startLine = Executor.getLine();
		if (differentProcess) {
			RuntimeStack.push(con.parent);
		}
			Executor.setLine(con.getLineNum());
			setReferences(con, newObj);
			initializeArguments(con, newObj, args, passByReference);
			RuntimeStack.pushContext(newObj.objectContext);
				int depth = RuntimeStack.runtimeStack.size();
				RuntimeStack.push(con);
				while (RuntimeStack.runtimeStack.size() > depth){
					Executor.executeCurrentLine();
				}
			RuntimeStack.popContext();
		if (differentProcess) {
			RuntimeStack.pop();
		}
		Executor.setLine(startLine);
		return Value.obj(newObj);
	}
	
	/* Set all the 'this' references 
	 * as well as global function references */
	private static void setReferences(Constructor constructor, BLZObject newObj){
		Variable.setValue("this", Value.obj(newObj), newObj.objectContext);
		Variable.setValue("constructor", Value.constructor(constructor), newObj.objectContext);
		for (Method m : constructor.getParent().importedMethods){
			Variable.setValue(m.functionName, new Value(VariableTypes.Method, m), newObj.objectContext);
		}
		for (Constructor c : constructor.getParent().importedConstructors) {
			Variable.setValue(c.getName(), Value.constructor(c));
		}
		for (Method m : constructor.getParent().methods){
			Variable.setValue(m.functionName, new Value(VariableTypes.Method, m), newObj.objectContext);
		}
		for (Constructor c : constructor.getParent().constructors){
			Variable.setValue(c.getName(), Value.constructor(c));
		}
	}
	
	private static void initializeArguments(Constructor constructor, BLZObject newObj,
											Value[] args, boolean passByReference){
		if (args.length > constructor.argumentNames.length){
			Interpreter.throwError("Too many arguments passed to constructor " + constructor.name);
		}
		if (constructor.takesArguments){
			if (passByReference){
				for (int i = 0; i < args.length; i++){
					Variable.setValue(constructor.argumentNames[i], args[i], newObj.objectContext);
				}
			}else{
				for (int i = 0; i < args.length; i++){
					Variable.setValue(constructor.argumentNames[i], args[i].clone(), newObj.objectContext);
				}
			}
		}
	}

}
