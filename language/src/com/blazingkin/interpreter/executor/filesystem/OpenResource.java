package com.blazingkin.interpreter.executor.filesystem;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class OpenResource implements InstructionExecutorValue {
    

    public Value run(Value v){
        if (v.type != VariableTypes.Resource){
            Interpreter.throwError("Attempted to open "+v+" as a resource, but it is not one");
            return Value.nil();
        }
        URL path = (URL) v.value;
        try {
            Scanner scanner = new Scanner(path.openStream());
            scanner.useDelimiter("");
            return Value.scanner(scanner);
        }catch (IOException e){
            Interpreter.throwError("Failed to open resource "+v+": " + e.getMessage());
            return Value.nil();
        }
    }

}