package com.blazingkin.interpreter.executor.filesystem;

import java.util.Scanner;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.BLZResource;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ScannerHasNext implements InstructionExecutorValue {

    public Value run(Value v){
        if (v.type != VariableTypes.Resource){
            Interpreter.throwError("Tried to see if a scanner has next byte, but " + v+" is not a scanner");
            return Value.bool(false);
        }
        BLZResource s = (BLZResource) v.value;
        return Value.bool(s.hasNext());
    }

}