package com.blazingkin.interpreter.executor.output;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class RawEcho implements InstructionExecutorValue {
    
    public Value run(Value input){
        if (Variable.isValInt(input)){
            char val = (char) Variable.getIntValue(input).intValue();
            Executor.getEventHandler().print(val + "");
        }
        return input;
    }

}