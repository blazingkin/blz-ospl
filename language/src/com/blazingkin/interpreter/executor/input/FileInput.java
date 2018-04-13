package com.blazingkin.interpreter.executor.input;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class FileInput implements InstructionExecutorValue {

	public Value run(Value arg){
		if (arg.type != VariableTypes.String){
			Interpreter.throwError("FileInput was given a non-string: "+arg);
		}
		String filePath = (String) arg.value;
		ArrayList<Value> lines = new ArrayList<Value>();
		try{
			File f = new File(filePath);
			Scanner s = new Scanner(f);
			while (s.hasNextLine()){
				lines.add(Value.string(s.nextLine()));
			}
			s.close();
		}catch(Exception e){
			Interpreter.throwError("File "+filePath+" not found");
		}
		Value[] lnes = new Value[lines.size()];
		lines.toArray(lnes);
		return Value.arr(lnes);
	}
	
	
}
