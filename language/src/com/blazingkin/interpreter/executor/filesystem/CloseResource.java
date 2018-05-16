package com.blazingkin.interpreter.executor.filesystem;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;
import java.util.Scanner;

public class CloseResource implements InstructionExecutorValue {

    public Value run(Value v){
        if (v.type != VariableTypes.Scanner) {
            Interpreter.throwError("Tried to close non-resource "+v);
            return Value.nil();
        }
        Scanner scanner = (Scanner) v.value;
        scanner.close();
        return Value.nil();
    }

}