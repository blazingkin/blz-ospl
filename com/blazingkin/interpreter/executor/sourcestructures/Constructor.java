package com.blazingkin.interpreter.executor.sourcestructures;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStackElement;

public class Constructor implements RuntimeStackElement {

	private Process parent;
	private int lineNum;
	public String name;
	
	public Constructor(Process parent, int line, String name){
		if (name.equals("")){
			Interpreter.throwError("Empty constructor name!");
		}
		this.parent = parent;
		this.lineNum = line;
		this.name = name;
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

}
