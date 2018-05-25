package com.blazingkin.interpreter.executor.filesystem;

import java.io.IOException;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorCommaDelimited;
import com.blazingkin.interpreter.variables.BLZResource;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class WriteToResource implements InstructionExecutorCommaDelimited {

    public Value run(Value[] v) throws BLZRuntimeException{
        if (v.length != 2){
            throw new BLZRuntimeException(null, "Passed incorrect number of arguments to write to resource");
        }
        if (v[0].type != VariableTypes.Resource) {
            throw new BLZRuntimeException(null, "Cannot write to non-resource "+v[0]);
        }
        if (v[1].type != VariableTypes.String) {
            throw new BLZRuntimeException(null, "Cannot write non-string "+v[1]);
        }
        BLZResource resource = (BLZResource) v[0].value;
        String toWrite = (String) v[1].value;
        try {
            resource.write(toWrite);
        }catch(IOException e){
            throw new BLZRuntimeException(null, "I/O Exception when writing to resource "+e.getMessage());
        }
        return v[0];
    }

}