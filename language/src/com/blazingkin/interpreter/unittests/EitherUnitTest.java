package com.blazingkin.interpreter.unittests;

import java.util.Optional;

import com.blazingkin.interpreter.parser.Either;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class EitherUnitTest {

	@BeforeClass
	public static void setup(){
		UnitTestUtil.setup();
	}
	
	@AfterClass
	public static void clear(){
		UnitTestUtil.clearEnv();
	}

    @Test
    public void shouldSayLeftIsCorrect(){
        Either<Integer, String> e = Either.left(20);
        UnitTestUtil.assertEqual(e.isLeft(), true);
        UnitTestUtil.assertEqual(e.getLeft().get(), 20);
    }

    @Test
    public void shouldGetRight(){
        Either<Integer, Integer> e = Either.right(40);
        UnitTestUtil.assertEqual(e.getLeft(), Optional.empty());
        UnitTestUtil.assertEqual(e.getRight().get(), 40);
    }

}