package com.blazingkin.interpreter.unittests;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.executor.sourcestructures.Constructor;
import com.blazingkin.interpreter.executor.sourcestructures.Process;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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
		try {
			Value[] args = {};
			Value result = Constructor.initialize(getBallConstructor(), args, false);
			UnitTestUtil.assertEqual(result.type, VariableTypes.Object);
			BLZObject obj = (BLZObject) result.value;
			UnitTestUtil.assertEqual(result, obj.objectContext.getValue("this"));
			UnitTestUtil.assertEqual(Value.integer(2), obj.objectContext.getValue("radius"));
			UnitTestUtil.assertEqual(Value.string("red"), obj.objectContext.getValue("color"));
		}catch(BLZRuntimeException e){
			UnitTestUtil.fail();
		}
	}
	
	
	@Test
	public void testConstructorParameters(){
		try {
			Value[] args = {Value.string("blue"), Value.integer(5)};
			Value result = Constructor.initialize(getParamatizedBallConstructor(), args, false);
			UnitTestUtil.assertEqual(result.type, VariableTypes.Object);
			BLZObject obj = (BLZObject) result.value;
			UnitTestUtil.assertEqual(result, obj.objectContext.getValue("this"));
			UnitTestUtil.assertEqual(true, obj.objectContext.hasValue("nil?"));
			UnitTestUtil.assertEqual(VariableTypes.Method, obj.objectContext.getValue("nil?").type);
			UnitTestUtil.assertEqual(Value.integer(5), obj.objectContext.getValue("radius"));
			UnitTestUtil.assertEqual(Value.string("blue"), obj.objectContext.getValue("color"));
		}catch(BLZRuntimeException e){
			UnitTestUtil.fail();
		}
	}
	
	@Test
	public void testConstructorTooManyArguments(){
		Value[] args = {Value.integer(20)};
		try{
			Constructor.initialize(getBallConstructor(), args, false);
		}catch(BLZRuntimeException e){
			UnitTestUtil.assertEqual(e.getMessage(), "Too many arguments passed to constructor Ball");
			return;
		}
		// Should fall into catch block
		UnitTestUtil.fail();
	}
	
	@Test
	public void testPartialConstructorArgsShouldWork(){
		try {
			Value[] args = {Value.string("blue")};
			Value result = Constructor.initialize(getParamatizedBallConstructor(), args, false);
			UnitTestUtil.assertEqual(result.type, VariableTypes.Object);
			BLZObject obj = (BLZObject) result.value;
			UnitTestUtil.assertEqual(result, obj.objectContext.getValue("this"));
			UnitTestUtil.assertEqual(Value.string("blue"), obj.objectContext.getValue("color"));
			UnitTestUtil.assertNil(obj.objectContext.getValue("radius"));
		}catch(BLZRuntimeException e) {
			System.out.println(e.getMessage());
			UnitTestUtil.fail();
		}
	}
	
	private Constructor getBallConstructor() throws BLZRuntimeException{
		String[] constructor = {"constructor Ball", "color = \"red\"", "radius = 2", "end"};
		Process p = new Process(constructor);
		RuntimeStack.push(p);
		return p.constructors.get(0);
	}
	
	private Constructor getParamatizedBallConstructor() throws BLZRuntimeException{
		String[] constructor = {"constructor Ball(color, radius)", "end"};
		Process p = new Process(constructor);
		RuntimeStack.push(p);
		return p.constructors.get(0);
	}

}
