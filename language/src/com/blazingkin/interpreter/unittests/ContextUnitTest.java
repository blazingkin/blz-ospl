package com.blazingkin.interpreter.unittests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

import static com.blazingkin.interpreter.unittests.UnitTestUtil.assertEqual;

public class ContextUnitTest {

	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@AfterClass
	public static void clear(){
		UnitTestUtil.clearEnv();
	}

	 
	@Test
	public void anEmptyContextShouldHaveNothing() {
		Context con = new Context();
		assertEqual(false, con.inContext("asdf"));
		assertEqual(false, con.inContext("a"));
		assertEqual(false, con.inContext("print"));
		assertEqual(false, con.inContext("e"));
		assertEqual(false, con.inContext("pi"));
		assertEqual(false, con.inContext("a.b.c"));
		assertEqual(false, con.inContext("\"asdf\""));
	}
	
	@Test
	public void inContextShouldFindVariables(){
		Context testCon = new Context();
		testCon.setValue("asdf", Value.integer(600));
		assertEqual(true, testCon.inContext("asdf"));
	}
	
	@Test
	public void inContextShouldFindParentVariables(){
		Context pCon = new Context();
		Context cCon = new Context(pCon);
		pCon.setValue("asdf", Value.integer(50));
		assertEqual(true, cCon.inContext("asdf"));
	}
	
	@Test
	public void shouldBeAbleToRetrieveValueFromContext(){
		Context testCon = new Context();
		testCon.variables.put("asdf", Value.integer(3));
		assertEqual(testCon.getValue("asdf"), Value.integer(3));
		assertEqual(testCon.getValue("asdf"), Value.integer(3));
		testCon.variables.put("asdf", Value.integer(5));
		assertEqual(testCon.getValue("asdf"), Value.integer(5));
		assertEqual(testCon.getValue("asdf"), Value.integer(5));
	}
	
	@Test
	public void parentContextShouldNotSeeChildVariables(){
		Context pCon = new Context();
		Context cCon = new Context(pCon);
		cCon.variables.put("abc", Value.integer(30));
		assertEqual(false, pCon.inContext("abc"));
	}
	
	@Test
	public void childContextShouldBeAbleToSeeParentVariables(){
		Context pCon = new Context();
		Context cCon = new Context(pCon);
		pCon.setValue("random", Value.string("asdf"));
		assertEqual(pCon.getValue("random"), Value.string("asdf"));
		assertEqual(cCon.getValue("random"), Value.string("asdf"));
		cCon.setValue("random", Value.integer(20));
		assertEqual(cCon.getValue("random"), Value.integer(20));
	}
	
	@Test
	public void childContextShouldBeAbleToModifyParentVariables(){
		Context pCon = new Context();
		Context cCon = new Context(pCon);
		pCon.setValue("random", Value.integer(3));
		cCon.setValue("random", Value.integer(30));
		assertEqual(pCon.getValue("random"), Value.integer(30));
		assertEqual(cCon.getValue("random"), Value.integer(30));
	}
	
	@Test
	public void childShouldOnlyModifyLocalVariableIfDuplicate(){
		Context pCon = new Context();
		Context cCon = new Context(pCon);
		cCon.setValue("a", Value.integer(3));
		pCon.setValue("a", Value.integer(40));
		cCon.setValue("a", Value.integer(50));
		assertEqual(cCon.getValue("a"), Value.integer(50));
		assertEqual(pCon.getValue("a"), Value.integer(40));
	}

}
