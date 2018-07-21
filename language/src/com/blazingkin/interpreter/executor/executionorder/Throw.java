package com.blazingkin.interpreter.executor.executionorder;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorValue;
import com.blazingkin.interpreter.variables.*;

public class Throw implements InstructionExecutorValue {

    public Value run(Value v) throws BLZRuntimeException {
        throw new BLZRuntimeException(v);
    }

}