package com.blazingkin.interpreter.executor.filesystem;

import java.io.IOException;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorCommaDelimited;
import com.blazingkin.interpreter.variables.BLZResource;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class OpenResource implements InstructionExecutorCommaDelimited {
    
    public Value run(Value[] v) throws BLZRuntimeException{
        if (v.length != 2){
            throw new BLZRuntimeException(null, "Was not given the correct number of arguments to open a resource");
        }
        if (v[0].type != VariableTypes.Resource) {
            throw new BLZRuntimeException(null, "Attempted to open "+v[0]+" as a resource, but it is not one");
        }
        if (v[1].type != VariableTypes.String) {
            throw new BLZRuntimeException(null, "File mode was not a string: "+v[1]);
        }
        BLZResource resource = (BLZResource) v[0].value;
        String mode = (String) v[1].value;
        try {
            if (mode.contains("c")){
                resource.open(BLZResource.FileMode.Create);
            }
            if (mode.contains("r")){
                resource.open(BLZResource.FileMode.Read);
            }
            if (mode.contains("w")){
                resource.open(BLZResource.FileMode.Write);
            }
        }catch(IOException e){
            throw new BLZRuntimeException(null, "Failed to open file: "+e.getMessage());
        }
        return v[0];
    }

}