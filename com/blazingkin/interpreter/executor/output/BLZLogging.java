package com.blazingkin.interpreter.executor.output;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.InstructionExecutor;

public class BLZLogging implements InstructionExecutor {

	@Override
	public void run(String[] args) {
		if (args.length == 1){
			if (args[0].toLowerCase().equals("on")){
				Interpreter.logging = true;
			}else if (args[0].toLowerCase().equals("off")){
				Interpreter.logging = false;
			}else{
				Interpreter.throwError("BLZLogging takes either on or off as an argument");
			}
		}else{
			Interpreter.throwError("Incorrect number of arguments for BLZLogging");
		}
	}

}
