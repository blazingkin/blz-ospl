package in.blazingk.blz;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class VariableUnitTest {

	@Test
	public void testIsInteger() {
		assertTrue(Variable.isInteger("124214"));
		assertTrue(Variable.isInteger("35125214"));
		assertTrue(Variable.isInteger("90891535214"));
		assertTrue(Variable.isInteger("-52315904"));
		assertTrue(Variable.isInteger("-9801235120"));
		assertFalse(Variable.isInteger("12.4214"));
		assertFalse(Variable.isInteger("12a4214"));
		assertFalse(Variable.isInteger(""));
		assertFalse(Variable.isInteger("124214."));
	}
	
	@Test
	public void testIsDouble(){
		assertTrue(Variable.isDouble("21215125"));
		assertTrue(Variable.isDouble("80126"));
		assertTrue(Variable.isDouble("-8903515.9805231"));
		assertTrue(Variable.isDouble(".9315209"));
		assertTrue(Variable.isDouble("-.912509"));
		assertFalse(Variable.isDouble("90421a809412"));
		assertFalse(Variable.isDouble("90421.809.412"));
		assertFalse(Variable.isDouble(""));
		assertFalse(Variable.isDouble("..940214"));
	}
	
	@Test
	public void testIsBool() {
		assertTrue(Variable.isBool("truE"));
		assertTrue(Variable.isBool("true"));
		assertTrue(Variable.isBool("TruE"));
		assertTrue(Variable.isBool("True"));
		assertTrue(Variable.isBool("False"));
		assertTrue(Variable.isBool("false"));
		assertTrue(Variable.isBool("FALSE"));
		assertTrue(Variable.isBool("FaLsE"));
		assertFalse(Variable.isBool("tru"));
		assertFalse(Variable.isBool("trueish"));
		assertFalse(Variable.isBool("truce"));
		assertFalse(Variable.isBool("1False"));
	}
	
	@Test
	public void testAddValuesInt() {
		UnitTestUtil.assertEqual(Value.integer(3), Variable.addValues(Value.integer(3), Value.integer(0)));
		UnitTestUtil.assertEqual(Value.integer(0), Variable.addValues(Value.integer(3), Value.integer(-3)));
		UnitTestUtil.assertEqual(Value.integer(30), Variable.addValues(Value.integer(3), Value.integer(27)));
	}
	
	@Test
	public void testAddValuesString() {
		UnitTestUtil.assertEqual(Value.string("asdf"), Variable.addValues(Value.string("asd"), Value.string("f")));
		UnitTestUtil.assertEqual(Value.string("asdf123"), Variable.addValues(Value.string("asdf"), Value.integer(123)));
		UnitTestUtil.assertEqual(Value.string("123asdf"), Variable.addValues(Value.integer(123), Value.string("asdf")));
	}

	@Test
	public void testAddingRationals() {
		UnitTestUtil.assertEqual(Value.rational(3, 4), Variable.addValues(Value.rational(1, 2), Value.rational(1, 4)));
		UnitTestUtil.assertEqual(Value.integer(1), Variable.addValues(Value.rational(1, 2), Value.rational(1, 2)));
	}
	
	@Test
	public void testAddingDoubles() {
		UnitTestUtil.assertEqual(Value.doub(0.25d), Variable.addValues(Value.doub(0.25d), Value.integer(0)));
		UnitTestUtil.assertAlmostEqual(Value.doub(0.50d), Variable.addValues(Value.doub(0.25d), Value.doub(0.25d)));
	}
	
	@Test
	public void testIsValInt() {
		assertTrue(Variable.isValInt(Value.integer(3)));
		assertTrue(Variable.isValInt(Value.rational(2, 2)));
		assertFalse(Variable.isValInt(Value.rational(1, 2)));
		assertFalse(Variable.isValInt(Value.doub(2.3)));
		assertFalse(Variable.isValInt(Value.string("Hi!")));
	}
	
	@Test
	public void testIsValRational() {
		assertTrue(Variable.isValRational(Value.integer(3)));
		assertTrue(Variable.isValRational(Value.rational(2, 2)));
		assertTrue(Variable.isValRational(Value.rational(1, 2)));
		assertFalse(Variable.isValRational(Value.doub(2.3)));
		assertFalse(Variable.isValRational(Value.string("Hi!")));
	}
	
	@Test
	public void testIsValDecimal() {
		assertTrue(Variable.isDecimalValue(Value.integer(3)));
		assertTrue(Variable.isDecimalValue(Value.rational(2, 2)));
		assertTrue(Variable.isDecimalValue(Value.rational(1, 2)));
		assertTrue(Variable.isDecimalValue(Value.doub(2.3)));
		assertFalse(Variable.isDecimalValue(Value.string("Hi!")));
	}
	
	@Test
	public void testIsValDouble() {
		assertTrue(Variable.isValDouble(Value.doub(2.3)));
		assertFalse(Variable.isValDouble(Value.integer(3)));
		assertFalse(Variable.isValDouble(Value.rational(2, 3)));
	}
	
	@Test
	public void testGetDoubleValue() {
		UnitTestUtil.assertAlmostEqual(Value.doub(1.0), Value.doub(Variable.getDoubleVal(Value.integer(1)).doubleValue()));
		UnitTestUtil.assertAlmostEqual(Value.doub(0.5), Value.doub(Variable.getDoubleVal(Value.rational(1, 2)).doubleValue()));
	}
	
	@Test
	public void testGetIntValue() {
		UnitTestUtil.assertEqual(Value.integer(2).value, Variable.getIntValue(Value.rational(4, 2)));
	}
	
}
