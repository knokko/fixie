package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.time.Duration.Companion.seconds

class TestDisplacement {

	private fun assertEquals(a: Displacement, b: Displacement, margin: Double = 0.001) {
		assertEquals(a.toDouble(DistanceUnit.METER), b.toDouble(DistanceUnit.METER), margin)
	}

	private fun assertNotEquals(a: Displacement, b: Displacement, margin: Double = 0.001) {
		assertNotEquals(a.toDouble(DistanceUnit.METER), b.toDouble(DistanceUnit.METER), margin)
	}

	@Test
	fun testAssertEquals() {
		val base = Displacement.METER
		assertEquals(1.23 * base, 1.23 * base)
		assertNotEquals(1.23 * base, 1.252 * base)
		assertEquals(1.23 * base, 1.23001 * base)
		assertEquals(1.5 * base, 1.6 * base, 0.2)
		assertNotEquals(1.5 * base, 1.8 * base, 0.2)
		assertThrows<AssertionError> { assertNotEquals(1.23 * base, 1.23 * base) }
		assertThrows<AssertionError> { assertEquals(1.23 * base, 1.252 * base) }
		assertThrows<AssertionError> { assertNotEquals(1.23 * base, 1.23001 * base) }
		assertThrows<AssertionError> { assertNotEquals(1.5 * base, 1.6 * base, 0.2) }
		assertThrows<AssertionError> { assertEquals(1.5 * base, 1.8 * base, 0.2) }
	}

	@Test
	fun testToDouble() {
		assertEquals(1.0, Displacement.MILLIMETER.toDouble(DistanceUnit.MILLIMETER), 0.02)
		assertEquals(0.234, (0.234 * Displacement.MILLIMETER).toDouble(DistanceUnit.MILLIMETER), 0.02)
		assertFalse(Displacement.MILLIMETER == Displacement.INCH)
		assertEquals(1.0, Displacement.INCH.toDouble(DistanceUnit.INCH), 7.874015748031496E-4)
		assertEquals(0.234, (0.234 * Displacement.INCH).toDouble(DistanceUnit.INCH), 7.874015748031496E-4)
		assertFalse(Displacement.INCH == Displacement.FOOT)
		assertEquals(1.0, Displacement.FOOT.toDouble(DistanceUnit.FOOT), 6.561679790026247E-5)
		assertEquals(0.234, (0.234 * Displacement.FOOT).toDouble(DistanceUnit.FOOT), 6.561679790026247E-5)
		assertFalse(Displacement.FOOT == Displacement.YARD)
		assertEquals(1.0, Displacement.YARD.toDouble(DistanceUnit.YARD), 2.1872265966754154E-5)
		assertEquals(0.234, (0.234 * Displacement.YARD).toDouble(DistanceUnit.YARD), 2.1872265966754154E-5)
		assertFalse(Displacement.YARD == Displacement.METER)
		assertEquals(1.0, Displacement.METER.toDouble(DistanceUnit.METER), 2.0E-5)
		assertEquals(0.234, (0.234 * Displacement.METER).toDouble(DistanceUnit.METER), 2.0E-5)
		assertFalse(Displacement.METER == Displacement.KILOMETER)
		assertEquals(1.0, Displacement.KILOMETER.toDouble(DistanceUnit.KILOMETER), 2.0E-5)
		assertEquals(0.234, (0.234 * Displacement.KILOMETER).toDouble(DistanceUnit.KILOMETER), 2.0E-5)
		assertFalse(Displacement.KILOMETER == Displacement.MILE)
		assertEquals(1.0, Displacement.MILE.toDouble(DistanceUnit.MILE), 2.0E-5)
		assertEquals(0.234, (0.234 * Displacement.MILE).toDouble(DistanceUnit.MILE), 2.0E-5)
	}

	@Test
	fun testToString() {
		val displacementString = (0.5 * Displacement.METER).toString(DistanceUnit.METER)
		assertTrue(displacementString.startsWith("0.5"))
		assertTrue(displacementString.endsWith("m"))
	}

	@Test
	fun testCompareTo() {
		assertTrue(Displacement.MILLIMETER >= Displacement.MILLIMETER)
		assertTrue(Displacement.MILLIMETER <= Displacement.MILLIMETER)
		assertFalse(Displacement.MILLIMETER > Displacement.MILLIMETER)
		assertFalse(Displacement.MILLIMETER < Displacement.MILLIMETER)
		assertTrue(Displacement.MILLIMETER > Displacement.MILLIMETER * 0.8f)
		assertFalse(Displacement.MILLIMETER < Displacement.MILLIMETER * 0.8)
		assertTrue(Displacement.MILLIMETER / 2 > -Displacement.MILLIMETER)
		assertTrue(-Displacement.MILLIMETER / 2L > -Displacement.MILLIMETER)
		assertTrue(Displacement.INCH >= Displacement.INCH)
		assertTrue(Displacement.INCH <= Displacement.INCH)
		assertFalse(Displacement.INCH > Displacement.INCH)
		assertFalse(Displacement.INCH < Displacement.INCH)
		assertTrue(Displacement.INCH > Displacement.INCH * 0.8f)
		assertFalse(Displacement.INCH < Displacement.INCH * 0.8)
		assertTrue(Displacement.INCH / 2 > -Displacement.INCH)
		assertTrue(-Displacement.INCH / 2L > -Displacement.INCH)
		assertTrue(Displacement.FOOT >= Displacement.FOOT)
		assertTrue(Displacement.FOOT <= Displacement.FOOT)
		assertFalse(Displacement.FOOT > Displacement.FOOT)
		assertFalse(Displacement.FOOT < Displacement.FOOT)
		assertTrue(Displacement.FOOT > Displacement.FOOT * 0.8f)
		assertFalse(Displacement.FOOT < Displacement.FOOT * 0.8)
		assertTrue(Displacement.FOOT / 2 > -Displacement.FOOT)
		assertTrue(-Displacement.FOOT / 2L > -Displacement.FOOT)
		assertTrue(Displacement.YARD >= Displacement.YARD)
		assertTrue(Displacement.YARD <= Displacement.YARD)
		assertFalse(Displacement.YARD > Displacement.YARD)
		assertFalse(Displacement.YARD < Displacement.YARD)
		assertTrue(Displacement.YARD > Displacement.YARD * 0.8f)
		assertFalse(Displacement.YARD < Displacement.YARD * 0.8)
		assertTrue(Displacement.YARD / 2 > -Displacement.YARD)
		assertTrue(-Displacement.YARD / 2L > -Displacement.YARD)
		assertTrue(Displacement.METER >= Displacement.METER)
		assertTrue(Displacement.METER <= Displacement.METER)
		assertFalse(Displacement.METER > Displacement.METER)
		assertFalse(Displacement.METER < Displacement.METER)
		assertTrue(Displacement.METER > Displacement.METER * 0.8f)
		assertFalse(Displacement.METER < Displacement.METER * 0.8)
		assertTrue(Displacement.METER / 2 > -Displacement.METER)
		assertTrue(-Displacement.METER / 2L > -Displacement.METER)
		assertTrue(Displacement.KILOMETER >= Displacement.KILOMETER)
		assertTrue(Displacement.KILOMETER <= Displacement.KILOMETER)
		assertFalse(Displacement.KILOMETER > Displacement.KILOMETER)
		assertFalse(Displacement.KILOMETER < Displacement.KILOMETER)
		assertTrue(Displacement.KILOMETER > Displacement.KILOMETER * 0.8f)
		assertFalse(Displacement.KILOMETER < Displacement.KILOMETER * 0.8)
		assertTrue(Displacement.KILOMETER / 2 > -Displacement.KILOMETER)
		assertTrue(-Displacement.KILOMETER / 2L > -Displacement.KILOMETER)
		assertTrue(Displacement.MILE >= Displacement.MILE)
		assertTrue(Displacement.MILE <= Displacement.MILE)
		assertFalse(Displacement.MILE > Displacement.MILE)
		assertFalse(Displacement.MILE < Displacement.MILE)
		assertTrue(Displacement.MILE > Displacement.MILE * 0.8f)
		assertFalse(Displacement.MILE < Displacement.MILE * 0.8)
		assertTrue(Displacement.MILE / 2 > -Displacement.MILE)
		assertTrue(-Displacement.MILE / 2L > -Displacement.MILE)
		assertTrue(Displacement.INCH > Displacement.MILLIMETER)
		assertTrue(Displacement.FOOT > Displacement.INCH)
		assertTrue(Displacement.YARD > Displacement.FOOT)
		assertTrue(Displacement.METER > Displacement.YARD)
		assertTrue(Displacement.KILOMETER > Displacement.METER)
		assertTrue(Displacement.MILE > Displacement.KILOMETER)
		assertNotEquals(Displacement.METER * 0.9, Displacement.METER)
		assertTrue(Displacement.METER > Displacement.METER * 0.9)
	}

	@Test
	fun testArithmetic() {
		assertEquals(Displacement.MILLIMETER / 2, Displacement.MILLIMETER - 0.5f * Displacement.MILLIMETER, 0.05)
		assertEquals(Displacement.MILLIMETER, Displacement.MILLIMETER / 4L + 0.75 * Displacement.MILLIMETER, 0.05)
		assertEquals(0.25, Displacement.MILLIMETER / (4 * Displacement.MILLIMETER), 0.05)
		assertEquals(-Displacement.MILLIMETER / 2, Displacement.MILLIMETER / 2 - Displacement.MILLIMETER, 0.05)
		assertEquals(Displacement.INCH / 2, Displacement.INCH - 0.5f * Displacement.INCH, 0.001968503937007874)
		assertEquals(Displacement.INCH, Displacement.INCH / 4L + 0.75 * Displacement.INCH, 0.001968503937007874)
		assertEquals(0.25, Displacement.INCH / (4 * Displacement.INCH), 0.001968503937007874)
		assertEquals(-Displacement.INCH / 2, Displacement.INCH / 2 - Displacement.INCH, 0.001968503937007874)
		assertEquals(Displacement.FOOT / 2, Displacement.FOOT - 0.5f * Displacement.FOOT, 0.001)
		assertEquals(Displacement.FOOT, Displacement.FOOT / 4L + 0.75 * Displacement.FOOT, 0.001)
		assertEquals(0.25, Displacement.FOOT / (4 * Displacement.FOOT), 0.001)
		assertEquals(-Displacement.FOOT / 2, Displacement.FOOT / 2 - Displacement.FOOT, 0.001)
		assertEquals(Displacement.YARD / 2, Displacement.YARD - 0.5f * Displacement.YARD, 0.001)
		assertEquals(Displacement.YARD, Displacement.YARD / 4L + 0.75 * Displacement.YARD, 0.001)
		assertEquals(0.25, Displacement.YARD / (4 * Displacement.YARD), 0.001)
		assertEquals(-Displacement.YARD / 2, Displacement.YARD / 2 - Displacement.YARD, 0.001)
		assertEquals(Displacement.METER / 2, Displacement.METER - 0.5f * Displacement.METER, 0.001)
		assertEquals(Displacement.METER, Displacement.METER / 4L + 0.75 * Displacement.METER, 0.001)
		assertEquals(0.25, Displacement.METER / (4 * Displacement.METER), 0.001)
		assertEquals(-Displacement.METER / 2, Displacement.METER / 2 - Displacement.METER, 0.001)
		assertEquals(Displacement.KILOMETER / 2, Displacement.KILOMETER - 0.5f * Displacement.KILOMETER, 0.001)
		assertEquals(Displacement.KILOMETER, Displacement.KILOMETER / 4L + 0.75 * Displacement.KILOMETER, 0.001)
		assertEquals(0.25, Displacement.KILOMETER / (4 * Displacement.KILOMETER), 0.001)
		assertEquals(-Displacement.KILOMETER / 2, Displacement.KILOMETER / 2 - Displacement.KILOMETER, 0.001)
		assertEquals(Displacement.MILE / 2, Displacement.MILE - 0.5f * Displacement.MILE, 0.001)
		assertEquals(Displacement.MILE, Displacement.MILE / 4L + 0.75 * Displacement.MILE, 0.001)
		assertEquals(0.25, Displacement.MILE / (4 * Displacement.MILE), 0.001)
		assertEquals(-Displacement.MILE / 2, Displacement.MILE / 2 - Displacement.MILE, 0.001)
		assertEquals(Displacement.raw(21474), Displacement.raw(1) * 21474.0)
		assertEquals(2.0, (10 * Displacement.METER / 5.seconds).toDouble(SpeedUnit.METERS_PER_SECOND), 0.001)
	}

	@Test
	fun testExtensionFunctions() {
		assertEquals(0.8 * Displacement.MILLIMETER, 0.8.mm)
		assertEquals(0.6f * Displacement.MILLIMETER, 0.6f.mm)
		assertEquals(Displacement.MILLIMETER, 1.mm)
		assertEquals(Displacement.MILLIMETER, 1L.mm)
		assertEquals(0.8 * Displacement.INCH, 0.8.inch)
		assertEquals(0.6f * Displacement.INCH, 0.6f.inch)
		assertEquals(Displacement.INCH, 1.inch)
		assertEquals(Displacement.INCH, 1L.inch)
		assertEquals(0.8 * Displacement.FOOT, 0.8.ft)
		assertEquals(0.6f * Displacement.FOOT, 0.6f.ft)
		assertEquals(Displacement.FOOT, 1.ft)
		assertEquals(Displacement.FOOT, 1L.ft)
		assertEquals(0.8 * Displacement.YARD, 0.8.yd)
		assertEquals(0.6f * Displacement.YARD, 0.6f.yd)
		assertEquals(Displacement.YARD, 1.yd)
		assertEquals(Displacement.YARD, 1L.yd)
		assertEquals(0.8 * Displacement.METER, 0.8.m)
		assertEquals(0.6f * Displacement.METER, 0.6f.m)
		assertEquals(Displacement.METER, 1.m)
		assertEquals(Displacement.METER, 1L.m)
		assertEquals(0.8 * Displacement.KILOMETER, 0.8.km)
		assertEquals(0.6f * Displacement.KILOMETER, 0.6f.km)
		assertEquals(Displacement.KILOMETER, 1.km)
		assertEquals(Displacement.KILOMETER, 1L.km)
		assertEquals(0.8 * Displacement.MILE, 0.8.mi)
		assertEquals(0.6f * Displacement.MILE, 0.6f.mi)
		assertEquals(Displacement.MILE, 1.mi)
		assertEquals(Displacement.MILE, 1L.mi)
		assertEquals(0.8 * Displacement.MILLIMETER, Displacement.MILLIMETER * 0.8)
		assertEquals(0.3f * Displacement.MILLIMETER, Displacement.MILLIMETER * 0.3f)
		assertEquals(1 * Displacement.MILLIMETER, Displacement.MILLIMETER * 1)
		assertEquals(2L * Displacement.MILLIMETER, Displacement.MILLIMETER * 2L)
		assertEquals(0.8 * Displacement.INCH, Displacement.INCH * 0.8)
		assertEquals(0.3f * Displacement.INCH, Displacement.INCH * 0.3f)
		assertEquals(1 * Displacement.INCH, Displacement.INCH * 1)
		assertEquals(2L * Displacement.INCH, Displacement.INCH * 2L)
		assertEquals(0.8 * Displacement.FOOT, Displacement.FOOT * 0.8)
		assertEquals(0.3f * Displacement.FOOT, Displacement.FOOT * 0.3f)
		assertEquals(1 * Displacement.FOOT, Displacement.FOOT * 1)
		assertEquals(2L * Displacement.FOOT, Displacement.FOOT * 2L)
		assertEquals(0.8 * Displacement.YARD, Displacement.YARD * 0.8)
		assertEquals(0.3f * Displacement.YARD, Displacement.YARD * 0.3f)
		assertEquals(1 * Displacement.YARD, Displacement.YARD * 1)
		assertEquals(2L * Displacement.YARD, Displacement.YARD * 2L)
		assertEquals(0.8 * Displacement.METER, Displacement.METER * 0.8)
		assertEquals(0.3f * Displacement.METER, Displacement.METER * 0.3f)
		assertEquals(1 * Displacement.METER, Displacement.METER * 1)
		assertEquals(2L * Displacement.METER, Displacement.METER * 2L)
		assertEquals(0.8 * Displacement.KILOMETER, Displacement.KILOMETER * 0.8)
		assertEquals(0.3f * Displacement.KILOMETER, Displacement.KILOMETER * 0.3f)
		assertEquals(1 * Displacement.KILOMETER, Displacement.KILOMETER * 1)
		assertEquals(2L * Displacement.KILOMETER, Displacement.KILOMETER * 2L)
		assertEquals(0.8 * Displacement.MILE, Displacement.MILE * 0.8)
		assertEquals(0.3f * Displacement.MILE, Displacement.MILE * 0.3f)
		assertEquals(1 * Displacement.MILE, Displacement.MILE * 1)
		assertEquals(2L * Displacement.MILE, Displacement.MILE * 2L)
	}

	@Test
	fun testMathFunctions() {
		assertEquals(Displacement.YARD, abs(Displacement.YARD))
		assertEquals(Displacement.YARD / 2, abs(Displacement.YARD / 2))
		assertEquals(0 * Displacement.YARD, abs(0 * Displacement.YARD))
		assertEquals(Displacement.YARD, abs(-Displacement.YARD))
		assertEquals(Displacement.YARD / 2, min(Displacement.YARD, Displacement.YARD / 2))
		assertEquals(Displacement.YARD, max(Displacement.YARD, Displacement.YARD / 2))
		assertEquals(-Displacement.YARD, min(-Displacement.YARD, -Displacement.YARD / 2))
		assertEquals(-Displacement.YARD / 2, max(-Displacement.YARD, -Displacement.YARD / 2))
		assertEquals(Displacement.YARD * 0, min(Displacement.YARD, Displacement.YARD * 0))
		assertEquals(Displacement.YARD, max(Displacement.YARD, Displacement.YARD * 0))
		assertEquals(-Displacement.YARD, min(-Displacement.YARD, -Displacement.YARD * 0))
		assertEquals(-Displacement.YARD * 0, max(-Displacement.YARD, -Displacement.YARD * 0))
		assertEquals(-Displacement.YARD, min(Displacement.YARD / 2, -Displacement.YARD))
		assertEquals(Displacement.YARD / 2, max(Displacement.YARD / 2, -Displacement.YARD))
	}
}
