package com.blazingkin.interpreter.executor.sourcestructures;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStackElement;
import com.blazingkin.interpreter.expressionabstraction.ExpressionParser;
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
		for (Method m : constructor.getParent().methods){
			Variable.setValue(m.functionName, new Value(VariableTypes.Method, m), newObj.objectContext);
		}
		for (Method m : constructor.getParent().importedMethods){
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
