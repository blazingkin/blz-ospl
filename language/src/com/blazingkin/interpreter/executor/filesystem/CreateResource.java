package com.blazingkin.interpreter.executor.filesystem;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

import java.net.MalformedURLException;
import java.net.URL;

public class CreateResource implements InstructionExecutorValue {

    public Value run(Value in){
        if (in.type != VariableTypes.String){
            Interpreter.throwError("Create Resource was passed non-string "+in);
            return Value.nil();
        }
        String resourceString = (String) in.value;
        try {
            URL resource = new URL(resourceString);
            return Value.resource(resource);
        }catch(MalformedURLException exception){
            Interpreter.throwError("Malformed resource pointer " + exception.getMessage());
            return Value.nil();
        }
    }

}