package in.blazingk.blz;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.executionstack.RuntimeStack;
import com.blazingkin.interpreter.expressionabstraction.ExpressionExecutor;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.NilSingleton;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class UnitTestUtil {
	
	
	public static Queue<String> inputBuffer = new LinkedList<String>();
	public static ArrayList<String> outputLog = new ArrayList<String>();
	public static ArrayList<String> exitLog = new ArrayList<String>();
	public static ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	
	public static void setup(){
		try{
			in.blazingk.blz.packagemanager.Package.importCore();
		}catch(Exception e){
			fail("Failed to import core");
		}
		Executor.setEventHandler(new TestEventHandler());
		System.setErr(new PrintStream(outStream));
		clearEnv();
	}
	
	public static void clearEnv(){
		Executor.cleanup();
		Variable.clearVariables();
		RuntimeStack.cleanup();
		inputBuffer = new LinkedList<String>();
		outputLog = new ArrayList<String>();
		exitLog = new ArrayList<String>();
		outStream.reset();
	}
	
	public static void addInput(String input){
		inputBuffer.add(input);
	}
	
	public static void log(String event){
		outputLog.add(event);
	}
	
	public static void exitLog(String event){
		exitLog.add(event);
	}
	
	public static String getInputFromBuffer(){
		return inputBuffer.remove();
	}
	
	public static Random rand = new Random();
	
	public static boolean checkExpect(Object o1, Object o2){
		return o1.equals(o2);
	}
	
	public static void printArr(String[] a){
		for (String s: a){
			System.out.println(s);
		}
	}

	public static void assertEqual(Object a, Object b){
		if (a instanceof Value){
			Value aVal = (Value) a;
			assertEqual((aVal).type.dataType, aVal.value.getClass());
		}
		if (b instanceof Value){
			Value bVal = (Value) b;
			assertEqual((bVal).type.dataType, bVal.value.getClass());
		}
		org.junit.Assert.assertEquals(b, a);
		if (!a.equals(b)){
			System.err.println("An assertion was false!");
			System.err.println(a + "!=" + b);
		}
		assert a.equals(b);
	}
	
	public static void assertEqual(Value a, Value b){
		org.junit.Assert.assertEquals(b, a);
		if (!a.equals(b)){
			System.err.println(a.value + " != "+ b.value);
			new Exception().printStackTrace();
			assert false;
		}
		assert a.equals(b);
	}

	public static void fail(){
		new Exception().printStackTrace();
		org.junit.Assert.fail();
		assert false;
	}

	public static void fail(String message){
		System.err.println(message);
		fail();
	}
	

	private static final double EPSILON = 1E-10;
	public static void assertAlmostEqual(Value a, Value b){

		if (a.equals(b)){
			assert a.equals(b);
			return;
		}
		if (Variable.isDecimalValue(a) && Variable.isDecimalValue(b)){
			BigDecimal v1 = Variable.getDoubleVal(a);
			BigDecimal v2 = Variable.getDoubleVal(b);
			double diff = (v2.subtract(v1)).doubleValue();
			if (diff > EPSILON){
				System.out.println(a.value + " !~= " + b.value);
				new Exception().printStackTrace();
			}
			org.junit.Assert.assertTrue(diff < EPSILON);
			assert diff < EPSILON;
			return;
		}
		System.err.println("Assert Almost Equal can't handle the two values "+ a.value+" and "+b.value);
		new Exception().printStackTrace();
	}
	
	public static void assertValEqual(String name, Value b, Context con) throws Exception {
		Value a = ExpressionExecutor.runExpression(name);
		org.junit.Assert.assertEquals(a, b);
		if (!a.equals(b)){
			System.err.println(name + " != "+ b.value);
			new Exception().printStackTrace();
		}
		assert con.getValue(name).equals(b);
	}
	
	public static void assertEqualArrays(Object[] a, Object[] b){
		if (a.length != b.length){
			System.err.println("Two 'equal' arrays were not the same length");
			System.err.println("len of: "+a+" != len of: "+b);
		}
		for (int i = 0; i < a.length; i++){
			org.junit.Assert.assertEquals(a[i], b[i]);
			assert a[i].equals(b[i]);
		}
	}

	public static void assertEqualArrays(ArrayList<?> a, ArrayList<?> b){
		if (a.size() != b.size()){
			System.err.println("Two 'equal' arrays were not the same length");
			System.err.println("len of: "+a+" != len of: "+b);
		}
		for (int i = 0; i < a.size(); i++){
			org.junit.Assert.assertEquals(a.get(i), b.get(i));
			assert a.get(i).equals(b.get(i));
		}
	}
	
	public static void assertLastOutput(String output){
		String out = outputLog.get(outputLog.size()-1);
		org.junit.Assert.assertEquals(output, out);
		assert output.equals(out);
	}
	
	public static void assertLastExit(String err){
		String out = exitLog.get(exitLog.size()-1);
		org.junit.Assert.assertEquals(err, out);
		assert err.equals(out);
	}
	
	public static void assertLastError(String err){
		String[] splits = outStream.toString().split("\n");
		String last = splits[splits.length-1].trim();
		org.junit.Assert.assertEquals(err, last);
		assert err.equals(last);
	}

	public static void assertLastErrorContains(String err){
		String[] splits = outStream.toString().split("\n");
		String last = splits[splits.length-1].trim();
		org.junit.Assert.assertTrue(last+" should contain "+err, last.contains(err));
	}
	
	public static void assertNoErrors(){
		org.junit.Assert.assertEquals("", outStream.toString());
		assert "".equals(outStream.toString());
		org.junit.Assert.assertTrue(exitLog.isEmpty());
		assert exitLog.isEmpty();
	}
	
	public static Value getIntValue(int v){
		return new Value(VariableTypes.Integer, v);
	}
	
	public static Value getDoubleValue(double v){
		return new Value(VariableTypes.Double, v);
	}

	public static void assertNil(Value v) {
		org.junit.Assert.assertEquals(VariableTypes.Nil, v.type);
		org.junit.Assert.assertEquals(v.value, NilSingleton.SINGLETON);
		assert v.type.equals(VariableTypes.Nil);
	}
	
}
