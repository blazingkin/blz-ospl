package com.blazingkin.interpreter.executor.sourcestructures;

import java.util.ArrayList;
import java.util.List;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.astnodes.BlockNode;
import com.blazingkin.interpreter.executor.astnodes.Closure;
import com.blazingkin.interpreter.executor.astnodes.MethodNode;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.parser.Either;
import com.blazingkin.interpreter.parser.ExpressionParser;
import com.blazingkin.interpreter.parser.MethodBlockParser;
import com.blazingkin.interpreter.parser.ParseBlock;
import com.blazingkin.interpreter.parser.SourceLine;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class Constructor {

	private Process parent;
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

	private void findMethods(List<Either<SourceLine, ParseBlock>> lines, MethodBlockParser parser) throws SyntaxException {
		List<Either<SourceLine,ParseBlock>> methods = new ArrayList<Either<SourceLine, ParseBlock>>();
		for (Either<SourceLine, ParseBlock> line : lines){
			if (line.isRight()){
				if (parser.shouldParse(line.getRight().get().getHeader())){
					/* Do we have a method? */
					methods.add(line);
				}
			}
		}
		for (Either<SourceLine, ParseBlock> method : methods){
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
	
	public String getName(){
		return name;
	}
	
	public Process getParent(){
		return parent;
	}

	public String toString(){
		String argNames = "";
		for (int i = 0; i < argumentNames.length; i++){
			argNames += argumentNames[i];
			if ( i != argumentNames.length - 1){
				argNames += ", ";
			}
		}

		return "<Contructor "+getName() + "(" +argNames+")>";
	}
	
	public static Value initialize(Constructor con, Value[] args, boolean passByReference) throws BLZRuntimeException{
		boolean pushedParent = false;
        if (con.parent != null && (RuntimeStack.isEmpty() || RuntimeStack.getProcessStack().peek().UUID != con.parent.UUID)){
            pushedParent = true;
			RuntimeStack.push(con.parent);
        }
		BLZObject newObj = new BLZObject(con.parent.processContext);
		setReferences(con, newObj); 
		initializeArguments(con, newObj, args, passByReference);
		try {
			con.blockNode.execute(newObj.objectContext);
		}catch(BLZRuntimeException exception) {
            if (exception.exceptionValue != null){
                throw exception;
            }
            String message = "In constructor "+con.getName()+"\n"+exception.getMessage();
            if (pushedParent){
                String fileName = RuntimeStack.getProcessStack().peek().toString();
                message = "In " + fileName + "\n"+message;
                RuntimeStack.pop();
            }
            throw new BLZRuntimeException(message, exception.alreadyCaught);
        }
		if(pushedParent){
			RuntimeStack.pop();
		}	
		return Value.obj(newObj);
	}
	
	/* Create a nil? node to add to all objects */
	private static MethodNode nilHuhNode() {
		try {
			if (nilHuh != null){
				return nilHuh;
			}
			ArrayList<Either<SourceLine, ParseBlock>> body = new ArrayList<Either<SourceLine, ParseBlock>>();
			body.add(Either.left(new SourceLine("false", -1))); // The source file doesn't exist so it is at line -1
			nilHuh = new MethodNode(":nil?", body, null);
			return nilHuh;
		}catch(Exception e){
			return null;
		}
	}


	private static MethodNode nilHuh;

	/* Set all the 'this' references 
	 * as well as global function references */
	private static void setReferences(Constructor constructor, BLZObject newObj){
		newObj.objectContext.setValueInPresent("this", Value.obj(newObj));
		newObj.objectContext.setValueInPresent("self", Value.obj(newObj));
		newObj.objectContext.setValueInPresent("constructor", Value.constructor(constructor));
		/* Normally this would be part of the standard library, but */
		/* This must be added programatically since objects won't look at primitive contexts */
		newObj.objectContext.setValue("nil?", Value.method(nilHuhNode()));
		newObj.objectContext.setValueInPresent("type", Value.string(constructor.name));
		for (MethodNode m : constructor.getParent().importedMethods){
			newObj.objectContext.setValueInPresent(m.getStoreName(), new Value(VariableTypes.Method, m));
		}
		for (Constructor c : constructor.getParent().importedConstructors) {
			newObj.objectContext.setValueInPresent(c.getName(), Value.constructor(c));
		}
		for (MethodNode m : constructor.getParent().methods){
			newObj.objectContext.setValueInPresent(m.getStoreName(), Value.method(m));
		}
		for (Constructor c : constructor.getParent().constructors){
			newObj.objectContext.setValueInPresent(c.getName(), Value.constructor(c));
		}
		for (MethodNode m : constructor.methods){
			Value closure = Value.closure(new Closure(newObj.objectContext, m, constructor.parent, "constructor " + constructor.getName()));
			newObj.objectContext.setValueInPresent(m.getStoreName(), closure);
		}
	}
	
	private static void initializeArguments(Constructor constructor, BLZObject newObj,
											Value[] args, boolean passByReference) throws BLZRuntimeException{
		if (args.length > constructor.argumentNames.length){
			throw new BLZRuntimeException("Too many arguments passed to constructor " + constructor.name);
		}
		if (constructor.takesArguments){
			if (passByReference){
				for (int i = 0; i < args.length; i++){
					newObj.objectContext.setValueInPresent(constructor.argumentNames[i], args[i]);
				}
			}else{
				for (int i = 0; i < args.length; i++){
					newObj.objectContext.setValueInPresent(constructor.argumentNames[i], args[i].clone());
				}
			}
			/* Set all unpassed arguments to nil */
			for (int i = args.length; i < constructor.argumentNames.length; i++){
				newObj.objectContext.setValueInPresent(constructor.argumentNames[i], Value.nil());
			}
		}
	}

}
