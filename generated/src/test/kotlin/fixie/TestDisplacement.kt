package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.time.Duration.Companion.seconds

class TestDisplacement {

	@Test
	fun testEquals() {
		assertEquals(Displacement.raw(10), Displacement.raw(10))
		assertNotEquals(Displacement.raw(10), Displacement.raw(11))
	}

	@Test
	fun testToDouble() {
		assertEquals(0.7, (0.7 * Displacement.METER).toDouble(DistanceUnit.METER), 0.001)
	}

	@Test
	fun testToString() {
		assertEquals("0.5m", (0.5 * Displacement.METER).toString(DistanceUnit.METER))
	}

	@Test
	fun testCompanionConstructors() {
		assertEquals(Displacement.METER, Displacement.METER)
		assertNotEquals(Displacement.METER * 0.9, Displacement.METER)
		assertTrue(Displacement.METER > Displacement.METER * 0.9)
	}

	@Test
	fun testCompareTo() {
		assertTrue(Displacement.raw(40) < Displacement.raw(41))
		assertTrue(Displacement.raw(40) <= Displacement.raw(41))
		assertTrue(Displacement.raw(40) <= Displacement.raw(40))
		assertTrue(Displacement.raw(40) >= Displacement.raw(40))
		assertFalse(Displacement.raw(40) >= Displacement.raw(41))
		assertFalse(Displacement.raw(40) > Displacement.raw(41))

		assertFalse(Displacement.raw(40) < Displacement.raw(39))
		assertFalse(Displacement.raw(40) <= Displacement.raw(39))
		assertTrue(Displacement.raw(40) >= Displacement.raw(39))
		assertTrue(Displacement.raw(40) > Displacement.raw(39))
	}

	@Test
	fun testArithmetic() {
		assertEquals(Displacement.raw(73), Displacement.raw(70) + Displacement.raw(3))
		assertEquals(Displacement.raw(20), Displacement.raw(61) - Displacement.raw(41))
		assertEquals(Displacement.raw(63), Displacement.raw(3) * 21)
		assertEquals(Displacement.raw(63), Displacement.raw(3) * FixDisplacement.from(21))
		assertEquals(Displacement.raw(63), Displacement.raw(3) * 21L)
		assertEquals(Displacement.raw(63), Displacement.raw(3) * 21f)
		assertEquals(Displacement.raw(63), Displacement.raw(3) * 21.0)
		assertEquals(Displacement.raw(20), Displacement.raw(40) / 2)
		assertEquals(Displacement.raw(20), Displacement.raw(40) / FixDisplacement.from(2))
		assertEquals(FixDisplacement.from(20), Displacement.raw(40) / Displacement.raw(2))
		assertEquals(Displacement.raw(20), Displacement.raw(40) / 2L)
		assertEquals(Displacement.raw(20), Displacement.raw(50) / 2.5f)
		assertEquals(Displacement.raw(20), Displacement.raw(50) / 2.5)
		assertEquals(Displacement.raw(-43), -Displacement.raw(43))
		assertEquals(2.0, (10 * Displacement.METER / 5.seconds).toDouble(SpeedUnit.METERS_PER_SECOND))
	}

	@Test
	fun testAreaClass() {
		val one = Displacement.METER
		assertEquals((2 * one) * (2 * one), one * (2 * one) + (2 * one) * one)
		assertEquals(one * one, (2 * one) * one - one * one)
		assertEquals(5 * one, sqrt((3 * one) * (3 * one) + (4 * one) * (4 * one)))
		assertEquals(FixDisplacement.from(2), (2 * one * one) / (one * one))
		assertTrue(2 * one * one <= one * 3 * one)
		assertTrue(4 * one * one >= one * 3 * one)
	}

	@Test
	fun testExtensionFunctions() {
		assertEquals(Displacement.raw(63), 21 * Displacement.raw(3))
		assertEquals(Displacement.raw(63), 21L * Displacement.raw(3))
		assertEquals(Displacement.raw(63), 21f * Displacement.raw(3))
		assertEquals(Displacement.raw(63), 21.0 * Displacement.raw(3))
		assertEquals(Displacement.METER, 1.m)
		assertEquals(Displacement.METER, 1L.m)
		assertEquals(0.5 * Displacement.METER, 0.5f.m)
		assertEquals(0.5 * Displacement.METER, 0.5.m)
	}

	@Test
	fun testMathFunctions() {
		assertEquals(Displacement.raw(0), abs(Displacement.raw(0)))
		assertEquals(Displacement.raw(5), abs(Displacement.raw(-5)))
		assertEquals(Displacement.raw(5), abs(Displacement.raw(5)))
		assertEquals(Displacement.raw(4), min(Displacement.raw(6), Displacement.raw(4)))
		assertEquals(Displacement.raw(6), max(Displacement.raw(6), Displacement.raw(4)))
	}
}
