package in.blazingk.blz;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class PackageUnitTest {

	@After
	public void teardown() {
		Executor.cleanup();
	}
	
	@Test
	public void importingCoreShouldWork() {
		assertThat(Context.globalSingleton().variables.size(), is(1));
		try {
			in.blazingk.blz.packagemanager.Package.importCore();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertThat(Context.globalSingleton().variables.size(), is(not(1)));
		assertThat(VariableTypes.primitiveContexts.size(), is(not(0)));
		assertThat(VariableTypes.primitiveContexts.get(VariableTypes.Array).variables.size(), is(not(0)));
		assertThat(VariableTypes.primitiveContexts.get(VariableTypes.String).variables.size(), is(not(0)));
		assertThat(VariableTypes.primitiveContexts.get(VariableTypes.Nil).variables.size(), is(not(0)));
		assertThat(VariableTypes.primitiveContexts.get(VariableTypes.Hash).variables.size(), is(not(0)));
		assertThat(VariableTypes.primitiveContexts.get(VariableTypes.Integer).variables.size(), is(not(0)));
		assertThat(VariableTypes.primitiveContexts.get(VariableTypes.Rational).variables.size(), is(not(0)));
		assertThat(VariableTypes.primitiveContexts.get(VariableTypes.Boolean).variables.size(), is(not(0)));
	}
	
	@Test
	public void coreShouldOnlyBeImportedOnce() {
		assertThat(Context.globalSingleton().variables.size(), is(1));
		try {
			in.blazingk.blz.packagemanager.Package.importCore();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		int count = Context.globalSingleton().variables.size();
		assertThat(count, is(not(1)));
		try {
			in.blazingk.blz.packagemanager.Package.importCore();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		/* The total size should not have changed */
		assertThat(Context.globalSingleton().variables.size(), is(count));
	}

}
