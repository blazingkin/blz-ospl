package com.blazingkin.interpreter.executor.filesystem;

import java.io.IOException;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.BLZResource;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ScannerReadNext implements InstructionExecutorValue {

    public Value run(Value v) throws BLZRuntimeException{
        if (v.type != VariableTypes.Resource){
            Interpreter.throwError("Tried to read byte from "+v+" which is not a scanner");
            return Value.nil();
        }
        BLZResource s = (BLZResource) v.value;
        try {
            return Value.string(s.read());
        }catch(IOException e){
            throw new BLZRuntimeException(null, e.getMessage());
        }
    }

}