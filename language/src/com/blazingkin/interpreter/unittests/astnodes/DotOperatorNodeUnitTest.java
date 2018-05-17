package com.blazingkin.interpreter.unittests.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.astnodes.DotOperatorNode;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.ValueASTNode;
import com.blazingkin.interpreter.unittests.UnitTestUtil;
import com.blazingkin.interpreter.variables.BLZObject;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class DotOperatorNodeUnitTest {
    @BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@After
	public void clear(){
		UnitTestUtil.clearEnv();
    }

    @Test
    public void shouldRequireTwoArgs(){
        ASTNode args[] = {};
        new DotOperatorNode(args);
        UnitTestUtil.assertLastError("Dot Operator did not have 2 arguments");
    }

    @Test
    public void shouldFindPropertyOfObject() throws BLZRuntimeException{
        BLZObject x = new BLZObject(new Context());
        x.objectContext.setValue("val", Value.integer(3));
        ASTNode args[] = {new ValueASTNode(Value.obj(x)), new ValueASTNode("val")};
        Value result = new DotOperatorNode(args).execute(new Context());
        UnitTestUtil.assertEqual(result, Value.integer(3));
        UnitTestUtil.assertNoErrors();
    }

    @Test
    public void shouldFindPropertyOfPrimitive() throws BLZRuntimeException {
        try {
            in.blazingk.blz.packagemanager.Package.importCore();
        }catch(Exception e){
            UnitTestUtil.fail();
        }
        ASTNode args[] = {new ValueASTNode("20"), new ValueASTNode("nil?")};
        Value result = new DotOperatorNode(args).execute(new Context());
        UnitTestUtil.assertEqual(result.type, VariableTypes.PrimitiveMethod);
    }

}