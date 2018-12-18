package in.blazingk.blz;

import java.util.ArrayList;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.variables.Value;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExecutorUnitTest {

	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@AfterClass
	public static void clear(){
		UnitTestUtil.clearEnv();
	}

	
	@Test
	public void testUUIDs(){
		ArrayList<Integer> uuids = new ArrayList<Integer>();
		for (int i = 0; i < 5000; i++){
			int u = Executor.getUUID();
			org.junit.Assert.assertFalse(uuids.contains(u));
			uuids.add(u);
		}
	}

	@Test
	public void testArgumentParsing(){
		ArrayList<String> args = new ArrayList<String>();
		args.add("-m");
		args.add("main_func");
		args.add("arg1");
		args.add("arg2");
		Executor.handleArgs(args);
		UnitTestUtil.assertEqual(Executor.startingMethod, "main_func");
		Value[] vargs = {Value.string("arg1"), Value.string("arg2")};
		UnitTestUtil.assertEqual(Executor.getProgramArguments(), Value.arr(vargs));
	}

	@Test
	public void defaultMainShouldBeMain(){
		Executor.cleanup();
		UnitTestUtil.assertEqual(Executor.startingMethod, "main");
	}
	


}
