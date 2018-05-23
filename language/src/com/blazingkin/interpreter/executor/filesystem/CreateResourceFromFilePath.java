package com.blazingkin.interpreter.executor.filesystem;

import java.io.File;
import java.io.IOException;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class CreateResourceFromFilePath implements InstructionExecutorValue {


    public Value run(Value v) throws BLZRuntimeException{
        if (v.type != VariableTypes.String){
            throw new BLZRuntimeException(null, "Tried to find file at non-string path: "+v.value);
        }
        String path = (String) v.value;
        File f = new File(path);
        return Value.resource(f.toURI());
        
    }

}