package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

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
		assertEquals(Displacement.raw(63), Displacement.raw(3) * 21L)
		assertEquals(Displacement.raw(63), Displacement.raw(3) * 21f)
		assertEquals(Displacement.raw(63), Displacement.raw(3) * 21.0)
		assertEquals(Displacement.raw(20), Displacement.raw(40) / 2)
		assertEquals(Displacement.raw(20), Displacement.raw(40) / 2L)
		assertEquals(Displacement.raw(20), Displacement.raw(50) / 2.5f)
		assertEquals(Displacement.raw(20), Displacement.raw(50) / 2.5)
		assertEquals(Displacement.raw(-43), -Displacement.raw(43))
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
}
