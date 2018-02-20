package com.blazingkin.interpreter.unittests;

import java.util.ArrayList;

import org.junit.Test;

import com.blazingkin.interpreter.executor.Executor;
public class ExecutorUnitTest {


	
	@Test
	public void testUUIDs(){
		ArrayList<Integer> uuids = new ArrayList<Integer>();
		for (int i = 0; i < 5000; i++){
			int u = Executor.getUUID();
			org.junit.Assert.assertFalse(uuids.contains(u));
			uuids.add(u);
		}
	}
	


}
