package com.blazingkin.interpreter.executor.data;

import java.math.BigInteger;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorCommaDelimited;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ArrayRebind implements InstructionExecutorCommaDelimited {
    public Value run(Value[] args){
        if (args.length != 2){
            Interpreter.throwError("Array rebind at index did not have 2 values");
        }
        if (args[0].type != VariableTypes.Array || args[1].type != VariableTypes.Array){
            Interpreter.throwError("Array rebind was passed incorrect types: " + args[0].type +" and "+args[1].type+". Expected Array and Array");
        }
        args[0].value = args[1].value;
        return args[0];
    }
}