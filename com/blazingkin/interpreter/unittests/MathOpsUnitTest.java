package com.blazingkin.interpreter.unittests;

import com.blazingkin.interpreter.executor.Instruction;
import com.blazingkin.interpreter.executor.math.AddVars;
import com.blazingkin.interpreter.executor.math.Ceiling;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class MathOpsUnitTest {

	public static void main(String args[]){
		//Tests separated into separate contexts so I can reuse variable names :)
		{
			//AddVars Tests (Imperative)
			AddVars av = (AddVars) Instruction.ADDVARIABLE.executor;
			String[] a1 = {"1", "1", "a"};
			av.run(a1);
			UnitTestUtil.assertEqual(Variable.getValue("a").value, 2);
			String[] a2 = {"2", "3", "a"};
			av.run(a2);
			UnitTestUtil.assertEqual(Variable.getValue("a").value, 5);
			String[] a3 = {"1", "-2", "b"};
			av.run(a3);
			UnitTestUtil.assertEqual(Variable.getValue("b").value, -1);
			String[] a4 = {"a", "b", "a"};
			av.run(a4);
			UnitTestUtil.assertEqual(Variable.getValue("a").value, 4);
			
			//AddVar Tests (Functional)
			for (int i = 0; i < 1000; i++){
				int a = UnitTestUtil.rand.nextInt();
				int b = UnitTestUtil.rand.nextInt();
				String[] arg = {a+"", b+""};
				UnitTestUtil.assertEqual(av.evaluate(arg), new Value(VariableTypes.Integer, a+b));
			}
		}
		Variable.clearVariables();
		
		{
			//Ceiling Tests (Imperative)
			Ceiling ceil = (Ceiling) Instruction.CEILING.executor;
			Variable.setValue("c", UnitTestUtil.getDoubleValue(1.5));
			String[] a1 = {"c"};
			ceil.run(a1);
			UnitTestUtil.assertEqual(Variable.getValue("c").value, 2);
			Variable.setValue("d", UnitTestUtil.getDoubleValue(.01));
			String[] a2 = {"d"};
			ceil.run(a2);
			UnitTestUtil.assertEqual(Variable.getValue("d").value, 1);
			Variable.setValue("e", UnitTestUtil.getIntValue(1));
			String[] a3 = {"e"};
			ceil.run(a3);
			UnitTestUtil.assertEqual(Variable.getValue("e").value, 1);
			Variable.setValue("f", UnitTestUtil.getDoubleValue(-1.5));
			String[] a4 = {"f"};
			ceil.run(a4);
			UnitTestUtil.assertEqual(Variable.getValue("f").value, -1);
			
			
			//Ceiling Tests (Functional)
			for (double d = 0; d < 5000; d += .42){
				String[] arg = {d+""};
				UnitTestUtil.assertEqual(ceil.evaluate(arg), UnitTestUtil.getIntValue((int)Math.ceil(d)));
			}
			
		}
		
		Variable.clearVariables();
		
		
		
	}
	
	
}
