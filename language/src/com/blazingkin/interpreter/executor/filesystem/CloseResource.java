package com.blazingkin.interpreter.executor.filesystem;

import java.io.IOException;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.BLZResource;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class CloseResource implements InstructionExecutorValue {

    public Value run(Value v) throws BLZRuntimeException {
        if (v.type != VariableTypes.Resource) {
            throw new BLZRuntimeException("Tried to close non-resource "+v);
        }
        BLZResource resource = (BLZResource) v.value;
        try {
            resource.close();
        }catch(IOException e){
            throw new BLZRuntimeException("Error when closing resource: "+e.getMessage());
        }
        return Value.bool(true);
    }

}