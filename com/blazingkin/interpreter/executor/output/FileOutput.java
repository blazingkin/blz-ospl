package com.blazingkin.interpreter.executor.output;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class FileOutput implements InstructionExecutor {

	public void run(String args[]){
		String arrayVarAddress = args[0];
		String filePath = "";
		for (int i = 1; i < args.length; i++){
			filePath= filePath+args[i];
			if (i != args.length-1){
				filePath = filePath + "";
			}
		}
		filePath = Variable.parseString(filePath);	
		try{
			File f = new File(filePath);
			System.out.println(filePath);
			if (!f.exists()){
				f.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
			int size = Variable.getArray(arrayVarAddress).size();
			for (int i = 0; i < size; i++){
				writer.write(((Value)Variable.getArray(arrayVarAddress).values().toArray()[i]).value+"");
				if (i != size -1){
					writer.write("\n");
				}
			}
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
			Interpreter.throwError("there was an error writing to file "+filePath);
		}
		
	}
	
	
	
}
