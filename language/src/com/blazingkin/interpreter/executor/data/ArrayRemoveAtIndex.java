package com.blazingkin.interpreter.executor.data;

import java.math.BigInteger;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorCommaDelimited;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ArrayRemoveAtIndex implements InstructionExecutorCommaDelimited {
    public Value run(Value[] args){
        if (args.length != 2){
            Interpreter.throwError("Array remove at index did not have 2 values");
        }
        if (args[0].type != VariableTypes.Array || args[1].type != VariableTypes.Integer){
            Interpreter.throwError("Array remove at index was passed incorrect types: " + args[0].type +" and "+args[1].type+". Expected Array and Int");
        }
        Value arr[] = (Value[]) args[0].value;
        if (arr.length == 0){
            return args[0];
        }
        Value newArr[] = new Value[arr.length - 1];
        int replacing = ((BigInteger)args[1].value).intValue();
        for (int i = 0; i < newArr.length; i++){
            if (i >= replacing){
               newArr[i] = arr[i + 1];
            }else{
                newArr[i] = arr[i];
            }
        }
        args[0].value = newArr;
        return args[0];
    }
}