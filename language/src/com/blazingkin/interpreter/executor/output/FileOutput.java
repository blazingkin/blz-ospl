package com.blazingkin.interpreter.executor.output;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class FileOutput implements InstructionExecutorStringArray {

	public Value run(String args[]){
		String arrayVarAddress = args[0];
		String filePath = "";
		for (int i = 1; i < args.length; i++){
			filePath= filePath+args[i];
			if (i != args.length-1){
				filePath = filePath + "";
			}
		}
		try{
			filePath = Variable.getValue(filePath).value.toString();
			File f = new File(filePath);
			if (!f.exists()){
				f.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
			Value arr = Variable.getValue(arrayVarAddress);
			if (arr.type != VariableTypes.Array){
				Interpreter.throwError(arr+" was not an array");
			}
			Value[] vals = (Value[]) arr.value;
			int size = vals.length;
			for (int i = 0; i < size; i++){
				writer.write((vals[i]).value+"");
				if (i != size -1){
					writer.write("\n");
				}
			}
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
			Interpreter.throwError("there was an error writing to file "+filePath);
			return Value.bool(false);
		}
		return Value.bool(true);
	}
	
	
	
}
