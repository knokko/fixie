package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.time.Duration.Companion.seconds

class TestAcceleration {

	private fun assertEquals(a: Acceleration, b: Acceleration, margin: Double = 0.001) {
		assertEquals(a.value, b.value, margin.toFloat())
	}

	private fun assertNotEquals(a: Acceleration, b: Acceleration, margin: Double = 0.001) {
		assertNotEquals(a.value, b.value, margin.toFloat())
	}

	@Test
	fun testAssertEquals() {
		assertEquals(Acceleration(1.23f), Acceleration(1.23f))
		assertNotEquals(Acceleration(1.23f), Acceleration(1.24f))
		assertEquals(Acceleration(1.23f), Acceleration(1.23001f))
		assertEquals(Acceleration(1.5f), Acceleration(1.6f), 0.2)
		assertNotEquals(Acceleration(1.5f), Acceleration(1.8f), 0.2)
		assertThrows<AssertionError> { assertNotEquals(Acceleration(1.23f), Acceleration(1.23f)) }
		assertThrows<AssertionError> { assertEquals(Acceleration(1.23f), Acceleration(1.24f)) }
		assertThrows<AssertionError> { assertNotEquals(Acceleration(1.23f), Acceleration(1.23001f)) }
		assertThrows<AssertionError> { assertNotEquals(Acceleration(1.5f), Acceleration(1.6f), 0.2) }
		assertThrows<AssertionError> { assertEquals(Acceleration(1.5f), Acceleration(1.8f), 0.2) }
	}

	@Test
	fun testToDouble() {
		assertEquals(0.234, (0.234 * Acceleration.MPS2).toDouble(), 2.0E-4)
	}

	@Test
	fun testToString() {
		assertEquals("2.34m/s^2", (Acceleration.MPS2 * 2.34).toString())
	}

	@Test
	fun testCompareTo() {
		assertTrue(Acceleration.MPS2 >= Acceleration.MPS2)
		assertTrue(Acceleration.MPS2 <= Acceleration.MPS2)
		assertFalse(Acceleration.MPS2 > Acceleration.MPS2)
		assertFalse(Acceleration.MPS2 < Acceleration.MPS2)
		assertTrue(Acceleration.MPS2 > Acceleration.MPS2 * 0.8f)
		assertFalse(Acceleration.MPS2 < Acceleration.MPS2 * 0.8)
		assertTrue(Acceleration.MPS2 / 2 > -Acceleration.MPS2)
		assertTrue(-Acceleration.MPS2 / 2L > -Acceleration.MPS2)
	}

	@Test
	fun testArithmetic() {
		assertEquals(Acceleration.MPS2 / 2, Acceleration.MPS2 - 0.5f * Acceleration.MPS2, 0.001)
		assertEquals(Acceleration.MPS2, Acceleration.MPS2 / 4L + 0.75 * Acceleration.MPS2, 0.001)
		assertEquals(0.25, Acceleration.MPS2 / (4 * Acceleration.MPS2), 0.001)
		assertEquals(-Acceleration.MPS2 / 2, Acceleration.MPS2 / 2 - Acceleration.MPS2, 0.001)
		assertEquals(0.8, ((Acceleration.MPS2 * 0.2) * 4.seconds).toDouble(SpeedUnit.METERS_PER_SECOND), 0.01)
	}

	@Test
	fun testExtensionFunctions() {
		assertEquals(0.8 * Acceleration.MPS2, 0.8.mps2)
		assertEquals(0.6f * Acceleration.MPS2, 0.6f.mps2)
		assertEquals(Acceleration.MPS2, 1.mps2)
		assertEquals(Acceleration.MPS2, 1L.mps2)
		assertEquals(0.8 * Acceleration.MPS2, Acceleration.MPS2 * 0.8)
		assertEquals(0.3f * Acceleration.MPS2, Acceleration.MPS2 * 0.3f)
		assertEquals(1 * Acceleration.MPS2, Acceleration.MPS2 * 1)
		assertEquals(2L * Acceleration.MPS2, Acceleration.MPS2 * 2L)
	}

	@Test
	fun testMathFunctions() {
		assertEquals(Acceleration.MPS2, abs(Acceleration.MPS2))
		assertEquals(Acceleration.MPS2 / 2, abs(Acceleration.MPS2 / 2))
		assertEquals(0 * Acceleration.MPS2, abs(0 * Acceleration.MPS2))
		assertEquals(Acceleration.MPS2, abs(-Acceleration.MPS2))
		assertEquals(Acceleration.MPS2 / 2, min(Acceleration.MPS2, Acceleration.MPS2 / 2))
		assertEquals(Acceleration.MPS2, max(Acceleration.MPS2, Acceleration.MPS2 / 2))
		assertEquals(-Acceleration.MPS2, min(-Acceleration.MPS2, -Acceleration.MPS2 / 2))
		assertEquals(-Acceleration.MPS2 / 2, max(-Acceleration.MPS2, -Acceleration.MPS2 / 2))
		assertEquals(Acceleration.MPS2 * 0, min(Acceleration.MPS2, Acceleration.MPS2 * 0))
		assertEquals(Acceleration.MPS2, max(Acceleration.MPS2, Acceleration.MPS2 * 0))
		assertEquals(-Acceleration.MPS2, min(-Acceleration.MPS2, -Acceleration.MPS2 * 0))
		assertEquals(-Acceleration.MPS2 * 0, max(-Acceleration.MPS2, -Acceleration.MPS2 * 0))
		assertEquals(-Acceleration.MPS2, min(Acceleration.MPS2 / 2, -Acceleration.MPS2))
		assertEquals(Acceleration.MPS2 / 2, max(Acceleration.MPS2 / 2, -Acceleration.MPS2))
	}
}
