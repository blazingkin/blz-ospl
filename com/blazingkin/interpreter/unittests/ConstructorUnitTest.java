package com.blazingkin.interpreter.unittests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.sourcestructures.Constructor;
import com.blazingkin.interpreter.executor.sourcestructures.Process;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class ConstructorUnitTest {

	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@AfterClass
	public static void clear(){
		UnitTestUtil.clearEnv();
	}
	
	@Test
	public void testObjectInitialization() {
		Value[] args = {};
		Value result = Constructor.initialize(getBallConstructor(), args, false);
		UnitTestUtil.assertEqual(result.type, VariableTypes.Object);
		BLZObject obj = (BLZObject) result.value;
		UnitTestUtil.assertEqual(result, Variable.getValue("this", obj.objectContext));
		UnitTestUtil.assertEqual(Value.integer(2), Variable.getValue("radius", obj.objectContext));
		UnitTestUtil.assertEqual(Value.string("red"), Variable.getValue("color", obj.objectContext));
	}
	
	
	@Test
	public void testConstructorParameters(){
		Value[] args = {Value.string("blue"), Value.integer(5)};
		Value result = Constructor.initialize(getParamatizedBallConstructor(), args, false);
		UnitTestUtil.assertEqual(result.type, VariableTypes.Object);
		BLZObject obj = (BLZObject) result.value;
		UnitTestUtil.assertEqual(result, Variable.getValue("this", obj.objectContext));
		UnitTestUtil.assertEqual(Value.integer(5), Variable.getValue("radius", obj.objectContext));
		UnitTestUtil.assertEqual(Value.string("blue"), Variable.getValue("color", obj.objectContext));
	}
	
	@Test
	public void testConstructorTooManyArguments(){
		Value[] args = {Value.integer(20)};
		/* Because we are not exiting, it runs into another error 
		 * This is an issue with the way that internal errors are thrown in the language
		 */
		try{
			Constructor.initialize(getBallConstructor(), args, false);
		}catch(Exception e){}
		UnitTestUtil.assertLastError("Too many arguments passed to constructor Ball");
	}
	
	@Test
	public void testPartialConstructorArgsShouldWork(){
		Value[] args = {Value.string("blue")};
		Value result = Constructor.initialize(getParamatizedBallConstructor(), args, false);
		UnitTestUtil.assertEqual(result.type, VariableTypes.Object);
		BLZObject obj = (BLZObject) result.value;
		UnitTestUtil.assertEqual(result, Variable.getValue("this", obj.objectContext));
		UnitTestUtil.assertEqual(Value.string("blue"), Variable.getValue("color", obj.objectContext));
		UnitTestUtil.assertNil(Variable.getValue("radius"));
	}
	
	private Constructor getBallConstructor(){
		String[] constructor = {"constructor Ball", "color = \"red\"", "radius = 2", "end"};
		Process p = new Process(constructor);
		RuntimeStack.push(p);
		return p.constructors.get(0);
	}
	
	private Constructor getParamatizedBallConstructor(){
		String[] constructor = {"constructor Ball(color, radius)", "end"};
		Process p = new Process(constructor);
		RuntimeStack.push(p);
		return p.constructors.get(0);
	}

}
