package com.blazingkin.interpreter.executor.input;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class FileInput implements InstructionExecutor {

	public void run(String args[]){
		String arrayVarAddress = args[0];
		String filePath = "";
		ArrayList<String> lines = new ArrayList<String>();
		for (int i = 1; i < args.length; i++){
			filePath= filePath+args[i];
			if (i != args.length-1){
				filePath = filePath + "";
			}
		}
		filePath = Variable.parseString(filePath);
		try{
			File f = new File(filePath);
			Scanner s = new Scanner(f);
			while (s.hasNextLine()){
				lines.add(s.nextLine());
			}
			s.close();
		}catch(Exception e){
			Interpreter.throwError("File "+filePath+" not found");
		}
		for (int i = 0; i < lines.size(); i++){
			Variable.setValue(arrayVarAddress+"["+i+"]", new Value(VariableTypes.String, lines.get(i)));
		}
	}
	
	
}
