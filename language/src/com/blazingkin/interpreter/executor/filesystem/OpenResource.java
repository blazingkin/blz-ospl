package com.blazingkin.interpreter.executor.filesystem;

import java.io.IOException;
import java.util.Scanner;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.BLZResource;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class OpenResource implements InstructionExecutorValue {
    

    public Value run(Value v){
        if (v.type != VariableTypes.Resource){
            Interpreter.throwError("Attempted to open "+v+" as a resource, but it is not one");
            return Value.nil();
        }
        BLZResource resource = (BLZResource) v.value;
        try {
            resource.open(BLZResource.FileMode.Read);
            return v;
        }catch (IOException e){
            Interpreter.throwError("Failed to open resource "+v+": " + e.getMessage());
            return Value.nil();
        }
    }

}