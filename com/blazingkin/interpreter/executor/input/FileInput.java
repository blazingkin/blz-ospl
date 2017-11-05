package com.blazingkin.interpreter.executor.input;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class FileInput implements InstructionExecutorStringArray {

	public void run(String args[]){
		String arrayVarAddress = args[0];
		String filePath = "";
		ArrayList<Value> lines = new ArrayList<Value>();
		for (int i = 1; i < args.length; i++){
			filePath= filePath+args[i];
			if (i != args.length-1){
				filePath = filePath + "";
			}
		}
		filePath = (String) Variable.getValue(filePath).value;
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
		Variable.setValue(arrayVarAddress, Value.arr(lnes));
	}
	
	
}
