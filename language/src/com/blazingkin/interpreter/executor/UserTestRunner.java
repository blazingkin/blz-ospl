package com.blazingkin.interpreter.executor;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.astnodes.MethodNode;
import com.blazingkin.interpreter.executor.sourcestructures.Process;
import com.blazingkin.interpreter.variables.Value;

public class UserTestRunner {

    public static boolean testFailed = false;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static void runTests(String[] args){
        Executor.importCore();
        boolean passedAll = true;
        for (String arg : args){
            if (!arg.startsWith("-")){
                passedAll = passedAll && loadAndRunTests(arg);
            }
        }
        if (passedAll){
            Executor.getEventHandler().print(ANSI_GREEN+"Passed All Tests"+ANSI_RESET+"\n");
            Interpreter.exitCode = 0;
            Executor.getEventHandler().exitProgram("");
        }else{
            Executor.getEventHandler().print(ANSI_RED+"Some tests failed!"+ANSI_RESET+"\n");
            Interpreter.exitCode = 1;
            Executor.getEventHandler().exitProgram("Some tests failed!");
        }
    }

    private static boolean loadAndRunTests(String name){
        try {
            Process p = new Process(Paths.get(name));
            Executor.getEventHandler().print("Running tests in file "+p.readingFrom.getName()+"\n");
            for (MethodNode method : p.methods){
                if (method.getStoreName().startsWith("test")){
                    Executor.getEventHandler().print("Running "+method.getStoreName()+"\n");
                    try {
                        Value[] emptyArgs = {};
                        UserTestRunner.testFailed = false;
                        method.execute(p.processContext, emptyArgs, false);
                        if (UserTestRunner.testFailed) {
                            Executor.getEventHandler().print(ANSI_RED+"Failed!\n"+ANSI_RESET);
                            return false;
                        }
                    }catch(BLZRuntimeException exception) {
                        Executor.getEventHandler().err("Unhandled exception in "+method.getStoreName()+"\n");
                        Executor.getEventHandler().err(exception.getMessage()+"\n");
                        return false;
                    }
                }
            }
        }catch(FileNotFoundException exception) {
            Executor.getEventHandler().err("Could not find file "+name+"\n");
            return false;
        }
        return true;
    }

}