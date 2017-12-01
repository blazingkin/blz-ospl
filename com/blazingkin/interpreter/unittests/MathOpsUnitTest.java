package com.blazingkin.interpreter.unittests;


import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertEqual;

import org.junit.AfterClass;
import org.junit.Test;

import com.blazingkin.interpreter.executor.instruction.Instruction;
import com.blazingkin.interpreter.executor.math.Ceiling;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class MathOpsUnitTest {


	
	
	@Test
	public void testCeiling(){
		{
			//Ceiling Tests (Imperative)
			Ceiling ceil = (Ceiling) Instruction.CEILING.executor;
			Variable.setValue("c", Value.doub(1.5));
			String[] a1 = {"c"};
			ceil.run(a1);
			assertEqual(Variable.getValue("c"), Value.integer(2));
			Variable.setValue("d", Value.doub(.01));
			String[] a2 = {"d"};
			ceil.run(a2);
			assertEqual(Variable.getValue("d"), Value.integer(1));
			Variable.setValue("e", Value.integer(1));
			String[] a3 = {"e"};
			ceil.run(a3);
			assertEqual(Variable.getValue("e"), Value.integer(1));
			Variable.setValue("f", Value.doub(-1.5));
			String[] a4 = {"f"};
			ceil.run(a4);
			assertEqual(Variable.getValue("f"), Value.integer(-1));
			
			
			//Ceiling Tests (Functional)
			for (double d = 0; d < 5000; d += .3){
				String[] arg = {d+""};
				assertEqual(ceil.evaluate(arg), Value.integer((int)Math.ceil(d)));
			}
			
		}
		
		Variable.clearVariables();
	}
	
	@AfterClass
	public static void teardown(){
		Variable.clearVariables();
	}
	
	
}
