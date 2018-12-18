package com.blazingkin.interpreter.executor.output;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.UserTestRunner;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutor;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class FailTest implements InstructionExecutor {

    public Value run(String line, Context con) throws BLZRuntimeException {
        UserTestRunner.testFailed = true;
        return Value.nil();
    }
	

}