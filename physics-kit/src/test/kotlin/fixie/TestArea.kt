package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class TestArea {

	private fun assertEquals(expected: Area, actual: Area, margin: Area = 0.001 * Area.SQUARE_METER) {
		if (abs(expected - actual) > margin) org.junit.jupiter.api.Assertions.assertEquals(expected, actual)
	}

	private fun assertNotEquals(expected: Area, actual: Area, margin: Area = 0.001 * Area.SQUARE_METER) {
		if (abs(expected - actual) <= margin) org.junit.jupiter.api.Assertions.assertNotEquals(expected, actual)
	}

	@Test
	fun testAssertEquals() {
		assertEquals(Area.SQUARE_METER, Area.SQUARE_METER)
		assertEquals(Area.SQUARE_METER, Area.SQUARE_METER * 1.000001)
		assertThrows<AssertionError> { assertEquals(Area.SQUARE_METER, Area.SQUARE_METER * 1.1) }
		assertNotEquals(Area.SQUARE_METER, Area.SQUARE_METER * 1.1)
		assertThrows<AssertionError> { assertNotEquals(Area, Area) }
	}

	@Test
	fun testEquals() {
		val m2 = Area.SQUARE_METER
		assertEquals(m2 * 1.1, m2 * 1.1)
		assertEquals(m2 * 1.1, m2 * 1.1000001)
		assertEquals(m2, 1000 * Area.SQUARE_MILLIMETER * 1000L)
		assertNotEquals(m2 * 1.2, m2 * 1.1)
		assertNotEquals(m2, Area.SQUARE_INCH)
		assertEquals(100 * 100 * m2, Area.HECTARE)
		assertFalse(Area.HECTARE == 1.0001 * Area.HECTARE)
	}

	@Test
	fun testToDouble() {
		assertEquals(25000.0, (2.5 * Area.HECTARE).toDouble(AreaUnit.SQUARE_METER), 0.1)
		assertEquals(0.1, (1000 * Area.SQUARE_METER).toDouble(AreaUnit.HECTARE), 0.001)
		assertEquals(1.23, (1.23 * Area.SQUARE_INCH).toDouble(AreaUnit.SQUARE_INCH), 0.001)
	}

	@Test
	fun testToString() {
		assertEquals("2.34ha", (Area.HECTARE * 2.34).toString(AreaUnit.HECTARE))
		assertEquals("5.67in^2", (5.67 * Area.SQUARE_INCH).toString(AreaUnit.SQUARE_INCH))
		assertEquals("8.91m^2", (8.91 * Area.SQUARE_METER).toString(AreaUnit.SQUARE_METER))
		assertEquals("0.12m^2", (0.1234 * Area.SQUARE_METER).toString())
	}

	@Test
	fun testCompareTo() {
		assertFalse(Area.SQUARE_METER > Area.SQUARE_METER)
		assertFalse(Area.SQUARE_METER < Area.SQUARE_METER)
		assertTrue(Area.SQUARE_METER > Area.SQUARE_INCH * 200)
		assertTrue(Area.SQUARE_METER < Area.HECTARE / 500)
		assertTrue(Area.SQUARE_INCH > -Area.SQUARE_INCH)
		assertTrue(Area.SQUARE_INCH > -Area.HECTARE)
		assertFalse(-2 * Area.SQUARE_METER > -Area.SQUARE_METER)
		assertTrue(2 * Area.SQUARE_METER > Area.SQUARE_METER)
		assertFalse(Area.SQUARE_METER * -1.2 > Area.SQUARE_METER * -1)
		assertTrue(Area.HECTARE * -1.2 < Area.HECTARE * -1)
	}

	@Test
	fun testArithmetic() {
		assertEquals(3L * Area.SQUARE_INCH, 2f * Area.SQUARE_INCH + Area.SQUARE_INCH)
		assertEquals(Area.HECTARE * 0.3, Area.HECTARE - 0.7f * Area.HECTARE)
		assertEquals(Area.HECTARE / 0.4, 2.5 * Area.HECTARE)
		assertEquals(Area.HECTARE, 100 * 100 * Area.SQUARE_METER)
		assertEquals(0.25, Area.SQUARE_METER / (Area.SQUARE_METER * 4), 0.001)
		assertEquals(4.0, ((10 * Area.SQUARE_INCH) / (2.5 * Displacement.INCH)).toDouble(DistanceUnit.INCH), 0.01)
	}

	@Test
	fun testExtensionFunctions() {
		assertEquals(2.m2, 2 * Area.SQUARE_METER)
		assertNotEquals(2.1.m2, 2 * Area.SQUARE_METER)
		assertEquals(4 * Area.SQUARE_METER, Area.SQUARE_METER * 4f)
		assertEquals(4L * Area.SQUARE_METER, Area.SQUARE_METER * 4.0)
	}

	@Test
	fun testMathFunctions() {
		assertEquals(Area.SQUARE_METER, abs(Area.SQUARE_METER))
		assertEquals(Area.HECTARE, abs(-Area.HECTARE))
		assertEquals(0 * Area.HECTARE, abs(0 * Area.HECTARE))
		assertEquals(2 * Area.HECTARE, min(2 * Area.HECTARE, 3 * Area.HECTARE))
		assertEquals(3 * Area.SQUARE_METER, max(5 * Area.SQUARE_INCH, 3 * Area.SQUARE_METER))
		assertEquals(-3 * Area.HECTARE, min(2 * Area.HECTARE, -3 * Area.HECTARE))
		assertEquals(2 * Area.SQUARE_INCH, max(2 * Area.SQUARE_INCH, -3 * Area.SQUARE_INCH))
		assertEquals(2.0, sqrt(Area.SQUARE_METER * 4).toDouble(DistanceUnit.METER), 0.01)
	}
}
