package com.blazingkin.interpreter;

import com.oracle.truffle.api.TruffleLanguage.Env;
import com.oracle.truffle.api.instrumentation.AllocationReporter;

public final class BLZContext {

    private final Env env;
    private final BLZLanguage language;
    private final AllocationReporter allocReporter;

    public BLZContext(Env env, BLZLanguage language){
        this.env = env;
        this.language = language;
        this.allocReporter = env.lookup(AllocationReporter.class);
    }

}