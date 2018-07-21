package com.blazingkin.interpreter.unittests;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.executionorder.Throw;
import com.blazingkin.interpreter.variables.Value;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ThrowUnitTest {

    @BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@AfterClass
	public static void clear(){
		UnitTestUtil.clearEnv();
    }

    @Test
    public void testThrowingWorks() {
        Value toThrow = Value.integer(3);
        Throw t = new Throw();
        try {
            t.run(toThrow);
            UnitTestUtil.fail("An exception should have been thrown");
        }catch(BLZRuntimeException e){
            UnitTestUtil.assertEqual(e.exceptionValue, Value.integer(3));
        }
    }

}