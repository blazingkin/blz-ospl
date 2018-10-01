package com.blazingkin.interpreter.unittests;

import java.util.Optional;

import com.blazingkin.interpreter.executor.sourcestructures.ImportStatement;
import com.blazingkin.interpreter.parser.SyntaxException;

import org.junit.Test;

public class ImportStatementUnitTest {

    @Test
    public void importShouldFindPackageName() throws SyntaxException {
        ImportStatement statement = new ImportStatement("package");
        UnitTestUtil.assertEqual(statement.packageName, "package");
    }

    @Test
    public void importShouldComplainAboutExtraWords() throws SyntaxException {
        try {
            new ImportStatement("package extra");
            UnitTestUtil.fail("An exception should have been thrown");
        }catch(SyntaxException e){

        }
    }

    @Test
    public void importStatementShouldFindAllParts() throws SyntaxException {
        ImportStatement statement = new ImportStatement("package tagged 1.2.3 as blah");
        UnitTestUtil.assertEqual(statement.packageName, "package");
        UnitTestUtil.assertEqual(statement.packageVersion, "1.2.3");
        UnitTestUtil.assertEqual(statement.alias, Optional.of("blah"));
    }

}