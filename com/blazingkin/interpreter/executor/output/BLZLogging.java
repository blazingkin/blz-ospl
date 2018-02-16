package com.blazingkin.interpreter.executor.output;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Value;

public class BLZLogging implements InstructionExecutorStringArray {

	@Override
	public Value run(String[] args) {
		if (args.length == 1){
			if (args[0].toLowerCase().equals("on")){
				Interpreter.logging = true;
				return Value.bool(true);
			}else if (args[0].toLowerCase().equals("off")){
				Interpreter.logging = false;
				return Value.bool(false);
			}else{
				Interpreter.throwError("BLZLogging takes either on or off as an argument");
			}
		}else{
			Interpreter.throwError("Incorrect number of arguments for BLZLogging");
		}
		return Value.nil();
	}

}
