package com.blazingkin.interpreter.executor.filesystem;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;
import java.util.Scanner;

public class ScannerReadNext implements InstructionExecutorValue {

    public Value run(Value v){
        if (v.type != VariableTypes.Scanner){
            Interpreter.throwError("Tried to read byte from "+v+" which is not a scanner");
            return Value.nil();
        }
        Scanner s = (Scanner) v.value;
        return Value.string(s.next());
    }

}