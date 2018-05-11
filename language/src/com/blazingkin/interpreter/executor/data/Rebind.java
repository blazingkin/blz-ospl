package com.blazingkin.interpreter.executor.data;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorCommaDelimited;
import com.blazingkin.interpreter.variables.Value;

public class Rebind implements InstructionExecutorCommaDelimited {
    public Value run(Value[] args){
        if (args.length != 2){
            Interpreter.throwError("Rebind at index did not have 2 values");
        }
        args[0].value = args[1].value;
        return args[0];
    }
}