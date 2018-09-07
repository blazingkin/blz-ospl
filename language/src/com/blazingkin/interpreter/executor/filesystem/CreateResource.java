package com.blazingkin.interpreter.executor.filesystem;

import java.net.URI;
import java.net.URISyntaxException;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class CreateResource implements InstructionExecutorValue {

    public Value run(Value in){
        if (in.type != VariableTypes.String){
            Interpreter.throwError("Create Resource was passed non-string "+in);
            return Value.nil();
        }
        String resourceString = (String) in.value;
        try {
            URI resource = new URI(resourceString);
            return Value.resource(resource);
        }catch(URISyntaxException exception){
            Interpreter.throwError("Malformed resource pointer " + exception.getMessage());
            return Value.nil();
        }
    }

}