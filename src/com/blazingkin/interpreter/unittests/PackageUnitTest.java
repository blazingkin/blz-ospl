package com.blazingkin.interpreter.unittests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;

import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class PackageUnitTest {

	@After
	public void teardown() {
		Executor.cleanup();
	}
	
	@Test
	public void importingCoreShouldWork() {
		assertThat(Variable.getGlobalContext().variables.size(), is(0));
		try {
			in.blazingk.blz.packagemanager.Package.importCore();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertThat(Variable.getGlobalContext().variables.size(), is(not(0)));
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
		assertThat(Variable.getGlobalContext().variables.size(), is(0));
		try {
			in.blazingk.blz.packagemanager.Package.importCore();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		int count = Variable.getGlobalContext().variables.size();
		assertThat(count, is(not(0)));
		try {
			in.blazingk.blz.packagemanager.Package.importCore();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		/* The total size should not have changed */
		assertThat(Variable.getGlobalContext().variables.size(), is(count));
	}

}
