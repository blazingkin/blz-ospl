package com.blazingkin.interpreter.unittests;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blazingkin.interpreter.executor.sourcestructures.Process;

public class ProcessUnitTest {

	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@Test
	public void TestMethodRegistration() {
		String[] twoMethods = {":main", "end", ":other", "end"};
		Process p = new Process(twoMethods);
		UnitTestUtil.assertNoErrors();
		UnitTestUtil.assertEqual(p.methods.size(), 2);
		UnitTestUtil.assertEqual(p.methods.get(0).getStoreName(), "main");
		UnitTestUtil.assertEqual(p.methods.get(1).getStoreName(), "other");
	}
	
	@Test
	public void TestIncompleteBlockOne() {
		String[] incompleteBlock = {"for i=0; i<20; ++i", "echo i"};
		new Process(incompleteBlock);
		UnitTestUtil.assertLastErrorContains("Some blocks not closed!");
	}
	
	@Test
	public void TestIncompleteBlockTwo(){
		String[] incompleteBlock = {"if true", "echo \"bad\""};
		new Process(incompleteBlock);
		UnitTestUtil.assertLastErrorContains("Some blocks not closed!");
	}
	
	@Test
	public void TestIncompleteBlockThree(){
		String[] incompleteBlock = {":main", "a = 3"};
		new Process(incompleteBlock);
		UnitTestUtil.assertLastErrorContains("Some blocks not closed!");
	}
	
	@Test
	public void TestIncompleteBlockFour(){
		String[] incompleteBlock = {":main", "if true", "a = 3", "end"};
		new Process(incompleteBlock);
		UnitTestUtil.assertLastErrorContains("Some blocks not closed!");
	}
	
	@Test
	public void TestCompleteBlockOne(){
		String[] completeBlock = {"a = 2", "echo a"};
		new Process(completeBlock);
		UnitTestUtil.assertNoErrors();
	}
	
	@Test
	public void TestCompleteBlockTwo(){
		String[] completeBlock = {"if true", "a = 3", "end"};
		new Process(completeBlock);
		UnitTestUtil.assertNoErrors();
	}
	
	@Test
	public void TestCompleteBlockThree(){
		String[] completeBlock = {":main", "if true", "a = 3", "end", "end"};
		new Process(completeBlock);
		UnitTestUtil.assertNoErrors();
	}
	
	@Test
	public void TestIncompleteConstructorBlock(){
		String[] code = { "constructor Ball", "thing = 2" };
		new Process(code);
		UnitTestUtil.assertLastErrorContains("Some blocks not closed!");
	}
	
	@Test
	public void TestConstructorRegistration(){
		String[] code = {"constructor Ball", "color = \"red\"", "end" };
		Process p = new Process(code);
		UnitTestUtil.assertNoErrors();
		UnitTestUtil.assertEqual(p.constructors.size(), 1);
		UnitTestUtil.assertEqual(p.constructors.get(0).name, "Ball");
	}
	
	@Test
	public void TestUnnamedConstructor(){
		String[] code = {"constructor", "color = \"red\"", "end"};
		new Process(code);
		UnitTestUtil.assertLastErrorContains("Empty constructor name!");
	}
	
	@Test
	public void TestMainAndConstructor() {
		String[] code = {"constructor Blah(a, b)", "end", ":main", "print(Blah(2,3).a)", "end"};
		Process p = new Process(code);
		UnitTestUtil.assertNoErrors();
		UnitTestUtil.assertEqual(p.constructors.size(), 1);
		UnitTestUtil.assertEqual(p.constructors.get(0).name, "Blah");
		UnitTestUtil.assertEqual(p.methods.size(), 1);
		UnitTestUtil.assertEqual(p.methods.get(0).getStoreName(), "main");
	}

	@Test
	public void TestExtraEnd(){
		try{
			String[] code = {"if blah", "end", "end"};
		new Process(code);
		}catch(Exception e){}
		UnitTestUtil.assertLastErrorContains("Unexpected end of block");
	}
	
	@After
	public void clearEnv(){
		UnitTestUtil.clearEnv();
	}

}
