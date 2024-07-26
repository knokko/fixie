package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class TestArea {

	private fun assertEquals(a: Area, b: Area, margin: Double = 0.001) {
		assertEquals(a.value, b.value, margin)
	}

	private fun assertNotEquals(a: Area, b: Area, margin: Double = 0.001) {
		assertNotEquals(a.value, b.value, margin)
	}

	@Test
	fun testAssertEquals() {
		assertEquals(Area(1.23), Area(1.23))
		assertNotEquals(Area(1.23), Area(1.24))
		assertEquals(Area(1.23), Area(1.23001))
		assertEquals(Area(1.5), Area(1.6), 0.2)
		assertNotEquals(Area(1.5), Area(1.8), 0.2)
		assertThrows<AssertionError> { assertNotEquals(Area(1.23), Area(1.23)) }
		assertThrows<AssertionError> { assertEquals(Area(1.23), Area(1.24)) }
		assertThrows<AssertionError> { assertNotEquals(Area(1.23), Area(1.23001)) }
		assertThrows<AssertionError> { assertNotEquals(Area(1.5), Area(1.6), 0.2) }
		assertThrows<AssertionError> { assertEquals(Area(1.5), Area(1.8), 0.2) }
	}

	@Test
	fun testToDouble() {
		assertEquals(1.0, Area.SQUARE_MILLIMETER.toDouble(AreaUnit.SQUARE_MILLIMETER), 0.002)
		assertEquals(0.234, (0.234 * Area.SQUARE_MILLIMETER).toDouble(AreaUnit.SQUARE_MILLIMETER), 0.002)
		assertFalse(Area.SQUARE_MILLIMETER == Area.SQUARE_INCH)
		assertEquals(1.0, Area.SQUARE_INCH.toDouble(AreaUnit.SQUARE_INCH), 0.002)
		assertEquals(0.234, (0.234 * Area.SQUARE_INCH).toDouble(AreaUnit.SQUARE_INCH), 0.002)
		assertFalse(Area.SQUARE_INCH == Area.SQUARE_METER)
		assertEquals(1.0, Area.SQUARE_METER.toDouble(AreaUnit.SQUARE_METER), 0.002)
		assertEquals(0.234, (0.234 * Area.SQUARE_METER).toDouble(AreaUnit.SQUARE_METER), 0.002)
		assertFalse(Area.SQUARE_METER == Area.HECTARE)
		assertEquals(1.0, Area.HECTARE.toDouble(AreaUnit.HECTARE), 0.002)
		assertEquals(0.234, (0.234 * Area.HECTARE).toDouble(AreaUnit.HECTARE), 0.002)
		assertEquals(25000.0, (2.5 * Area.HECTARE).toDouble(AreaUnit.SQUARE_METER), 0.1)
		assertEquals(0.1, (1000 * Area.SQUARE_METER).toDouble(AreaUnit.HECTARE), 0.001)
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
		assertTrue(Area.SQUARE_MILLIMETER >= Area.SQUARE_MILLIMETER)
		assertTrue(Area.SQUARE_MILLIMETER <= Area.SQUARE_MILLIMETER)
		assertFalse(Area.SQUARE_MILLIMETER > Area.SQUARE_MILLIMETER)
		assertFalse(Area.SQUARE_MILLIMETER < Area.SQUARE_MILLIMETER)
		assertTrue(Area.SQUARE_MILLIMETER > Area.SQUARE_MILLIMETER * 0.8f)
		assertFalse(Area.SQUARE_MILLIMETER < Area.SQUARE_MILLIMETER * 0.8)
		assertTrue(Area.SQUARE_MILLIMETER / 2 > -Area.SQUARE_MILLIMETER)
		assertTrue(-Area.SQUARE_MILLIMETER / 2L > -Area.SQUARE_MILLIMETER)
		assertTrue(Area.SQUARE_INCH >= Area.SQUARE_INCH)
		assertTrue(Area.SQUARE_INCH <= Area.SQUARE_INCH)
		assertFalse(Area.SQUARE_INCH > Area.SQUARE_INCH)
		assertFalse(Area.SQUARE_INCH < Area.SQUARE_INCH)
		assertTrue(Area.SQUARE_INCH > Area.SQUARE_INCH * 0.8f)
		assertFalse(Area.SQUARE_INCH < Area.SQUARE_INCH * 0.8)
		assertTrue(Area.SQUARE_INCH / 2 > -Area.SQUARE_INCH)
		assertTrue(-Area.SQUARE_INCH / 2L > -Area.SQUARE_INCH)
		assertTrue(Area.SQUARE_METER >= Area.SQUARE_METER)
		assertTrue(Area.SQUARE_METER <= Area.SQUARE_METER)
		assertFalse(Area.SQUARE_METER > Area.SQUARE_METER)
		assertFalse(Area.SQUARE_METER < Area.SQUARE_METER)
		assertTrue(Area.SQUARE_METER > Area.SQUARE_METER * 0.8f)
		assertFalse(Area.SQUARE_METER < Area.SQUARE_METER * 0.8)
		assertTrue(Area.SQUARE_METER / 2 > -Area.SQUARE_METER)
		assertTrue(-Area.SQUARE_METER / 2L > -Area.SQUARE_METER)
		assertTrue(Area.HECTARE >= Area.HECTARE)
		assertTrue(Area.HECTARE <= Area.HECTARE)
		assertFalse(Area.HECTARE > Area.HECTARE)
		assertFalse(Area.HECTARE < Area.HECTARE)
		assertTrue(Area.HECTARE > Area.HECTARE * 0.8f)
		assertFalse(Area.HECTARE < Area.HECTARE * 0.8)
		assertTrue(Area.HECTARE / 2 > -Area.HECTARE)
		assertTrue(-Area.HECTARE / 2L > -Area.HECTARE)
		assertTrue(Area.SQUARE_INCH > Area.SQUARE_MILLIMETER)
		assertTrue(Area.SQUARE_METER > Area.SQUARE_INCH)
		assertTrue(Area.HECTARE > Area.SQUARE_METER)
		assertTrue(Area.SQUARE_METER > Area.SQUARE_INCH * 200)
		assertTrue(Area.SQUARE_METER < Area.HECTARE / 500)
		assertTrue(Area.SQUARE_INCH > -Area.HECTARE)
	}

	@Test
	fun testArithmetic() {
		assertEquals(Area.SQUARE_MILLIMETER / 2, Area.SQUARE_MILLIMETER - 0.5f * Area.SQUARE_MILLIMETER, 0.005)
		assertEquals(Area.SQUARE_MILLIMETER, Area.SQUARE_MILLIMETER / 4L + 0.75 * Area.SQUARE_MILLIMETER, 0.005)
		assertEquals(0.25, Area.SQUARE_MILLIMETER / (4 * Area.SQUARE_MILLIMETER), 0.005)
		assertEquals(-Area.SQUARE_MILLIMETER / 2, Area.SQUARE_MILLIMETER / 2 - Area.SQUARE_MILLIMETER, 0.005)
		assertEquals(Area.SQUARE_INCH / 2, Area.SQUARE_INCH - 0.5f * Area.SQUARE_INCH, 0.005)
		assertEquals(Area.SQUARE_INCH, Area.SQUARE_INCH / 4L + 0.75 * Area.SQUARE_INCH, 0.005)
		assertEquals(0.25, Area.SQUARE_INCH / (4 * Area.SQUARE_INCH), 0.005)
		assertEquals(-Area.SQUARE_INCH / 2, Area.SQUARE_INCH / 2 - Area.SQUARE_INCH, 0.005)
		assertEquals(Area.SQUARE_METER / 2, Area.SQUARE_METER - 0.5f * Area.SQUARE_METER, 0.005)
		assertEquals(Area.SQUARE_METER, Area.SQUARE_METER / 4L + 0.75 * Area.SQUARE_METER, 0.005)
		assertEquals(0.25, Area.SQUARE_METER / (4 * Area.SQUARE_METER), 0.005)
		assertEquals(-Area.SQUARE_METER / 2, Area.SQUARE_METER / 2 - Area.SQUARE_METER, 0.005)
		assertEquals(Area.HECTARE / 2, Area.HECTARE - 0.5f * Area.HECTARE, 0.005)
		assertEquals(Area.HECTARE, Area.HECTARE / 4L + 0.75 * Area.HECTARE, 0.005)
		assertEquals(0.25, Area.HECTARE / (4 * Area.HECTARE), 0.005)
		assertEquals(-Area.HECTARE / 2, Area.HECTARE / 2 - Area.HECTARE, 0.005)
		assertEquals(Area.SQUARE_METER, 1000 * Area.SQUARE_MILLIMETER * 1000L)
		assertNotEquals(Area.SQUARE_METER, Area.SQUARE_INCH)
		assertNotEquals(Area.SQUARE_METER, Area.HECTARE)
		assertNotEquals(Area.SQUARE_METER, Area.SQUARE_MILLIMETER)
		assertEquals(100 * 100 * Area.SQUARE_METER, Area.HECTARE)
		assertEquals(Area.HECTARE, 100 * 100 * Area.SQUARE_METER)
		assertEquals(4.0, ((10 * Area.SQUARE_INCH) / (2.5 * Displacement.INCH)).toDouble(DistanceUnit.INCH), 0.01)
	}

	@Test
	fun testExtensionFunctions() {
		assertEquals(0.8 * Area.SQUARE_MILLIMETER, 0.8.mm2)
		assertEquals(0.6f * Area.SQUARE_MILLIMETER, 0.6f.mm2)
		assertEquals(Area.SQUARE_MILLIMETER, 1.mm2)
		assertEquals(Area.SQUARE_MILLIMETER, 1L.mm2)
		assertEquals(0.8 * Area.SQUARE_INCH, 0.8.in2)
		assertEquals(0.6f * Area.SQUARE_INCH, 0.6f.in2)
		assertEquals(Area.SQUARE_INCH, 1.in2)
		assertEquals(Area.SQUARE_INCH, 1L.in2)
		assertEquals(0.8 * Area.SQUARE_METER, 0.8.m2)
		assertEquals(0.6f * Area.SQUARE_METER, 0.6f.m2)
		assertEquals(Area.SQUARE_METER, 1.m2)
		assertEquals(Area.SQUARE_METER, 1L.m2)
		assertEquals(0.8 * Area.HECTARE, 0.8.ha)
		assertEquals(0.6f * Area.HECTARE, 0.6f.ha)
		assertEquals(Area.HECTARE, 1.ha)
		assertEquals(Area.HECTARE, 1L.ha)
		assertEquals(0.8 * Area.SQUARE_MILLIMETER, Area.SQUARE_MILLIMETER * 0.8)
		assertEquals(0.3f * Area.SQUARE_MILLIMETER, Area.SQUARE_MILLIMETER * 0.3f)
		assertEquals(1 * Area.SQUARE_MILLIMETER, Area.SQUARE_MILLIMETER * 1)
		assertEquals(2L * Area.SQUARE_MILLIMETER, Area.SQUARE_MILLIMETER * 2L)
		assertEquals(0.8 * Area.SQUARE_INCH, Area.SQUARE_INCH * 0.8)
		assertEquals(0.3f * Area.SQUARE_INCH, Area.SQUARE_INCH * 0.3f)
		assertEquals(1 * Area.SQUARE_INCH, Area.SQUARE_INCH * 1)
		assertEquals(2L * Area.SQUARE_INCH, Area.SQUARE_INCH * 2L)
		assertEquals(0.8 * Area.SQUARE_METER, Area.SQUARE_METER * 0.8)
		assertEquals(0.3f * Area.SQUARE_METER, Area.SQUARE_METER * 0.3f)
		assertEquals(1 * Area.SQUARE_METER, Area.SQUARE_METER * 1)
		assertEquals(2L * Area.SQUARE_METER, Area.SQUARE_METER * 2L)
		assertEquals(0.8 * Area.HECTARE, Area.HECTARE * 0.8)
		assertEquals(0.3f * Area.HECTARE, Area.HECTARE * 0.3f)
		assertEquals(1 * Area.HECTARE, Area.HECTARE * 1)
		assertEquals(2L * Area.HECTARE, Area.HECTARE * 2L)
	}

	@Test
	fun testMathFunctions() {
		assertEquals(Area.SQUARE_METER, abs(Area.SQUARE_METER))
		assertEquals(Area.SQUARE_METER / 2, abs(Area.SQUARE_METER / 2))
		assertEquals(0 * Area.SQUARE_METER, abs(0 * Area.SQUARE_METER))
		assertEquals(Area.SQUARE_METER, abs(-Area.SQUARE_METER))
		assertEquals(Area.SQUARE_METER / 2, min(Area.SQUARE_METER, Area.SQUARE_METER / 2))
		assertEquals(Area.SQUARE_METER, max(Area.SQUARE_METER, Area.SQUARE_METER / 2))
		assertEquals(-Area.SQUARE_METER, min(-Area.SQUARE_METER, -Area.SQUARE_METER / 2))
		assertEquals(-Area.SQUARE_METER / 2, max(-Area.SQUARE_METER, -Area.SQUARE_METER / 2))
		assertEquals(Area.SQUARE_METER * 0, min(Area.SQUARE_METER, Area.SQUARE_METER * 0))
		assertEquals(Area.SQUARE_METER, max(Area.SQUARE_METER, Area.SQUARE_METER * 0))
		assertEquals(-Area.SQUARE_METER, min(-Area.SQUARE_METER, -Area.SQUARE_METER * 0))
		assertEquals(-Area.SQUARE_METER * 0, max(-Area.SQUARE_METER, -Area.SQUARE_METER * 0))
		assertEquals(-Area.SQUARE_METER, min(Area.SQUARE_METER / 2, -Area.SQUARE_METER))
		assertEquals(Area.SQUARE_METER / 2, max(Area.SQUARE_METER / 2, -Area.SQUARE_METER))
		assertEquals(3 * Area.SQUARE_METER, max(5 * Area.SQUARE_INCH, 3 * Area.SQUARE_METER))
		assertEquals(2.0, sqrt(Area.SQUARE_METER * 4).toDouble(DistanceUnit.METER), 0.01)
	}
}
