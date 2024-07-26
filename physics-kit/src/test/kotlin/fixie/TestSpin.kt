package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.math.PI
import kotlin.time.Duration.Companion.seconds

class TestSpin {

	private fun assertEquals(a: Spin, b: Spin, margin: Double = 0.001) {
		assertEquals(a.value, b.value, margin.toFloat())
	}

	private fun assertNotEquals(a: Spin, b: Spin, margin: Double = 0.001) {
		assertNotEquals(a.value, b.value, margin.toFloat())
	}

	@Test
	fun testAssertEquals() {
		assertEquals(Spin(1.23f), Spin(1.23f))
		assertNotEquals(Spin(1.23f), Spin(1.24f))
		assertEquals(Spin(1.23f), Spin(1.23001f))
		assertEquals(Spin(1.5f), Spin(1.6f), 0.2)
		assertNotEquals(Spin(1.5f), Spin(1.8f), 0.2)
		assertThrows<AssertionError> { assertNotEquals(Spin(1.23f), Spin(1.23f)) }
		assertThrows<AssertionError> { assertEquals(Spin(1.23f), Spin(1.24f)) }
		assertThrows<AssertionError> { assertNotEquals(Spin(1.23f), Spin(1.23001f)) }
		assertThrows<AssertionError> { assertNotEquals(Spin(1.5f), Spin(1.6f), 0.2) }
		assertThrows<AssertionError> { assertEquals(Spin(1.5f), Spin(1.8f), 0.2) }
	}

	@Test
	fun testToDouble() {
		assertEquals(1.0, Spin.DEGREES_PER_SECOND.toDouble(SpinUnit.DEGREES_PER_SECOND), 0.002)
		assertEquals(0.234, (0.234 * Spin.DEGREES_PER_SECOND).toDouble(SpinUnit.DEGREES_PER_SECOND), 0.002)
		assertFalse(Spin.DEGREES_PER_SECOND == Spin.RADIANS_PER_SECOND)
		assertEquals(1.0, Spin.RADIANS_PER_SECOND.toDouble(SpinUnit.RADIANS_PER_SECOND), 0.002)
		assertEquals(0.234, (0.234 * Spin.RADIANS_PER_SECOND).toDouble(SpinUnit.RADIANS_PER_SECOND), 0.002)
		assertEquals(180.0, (Math.PI * Spin.RADIANS_PER_SECOND).toDouble(SpinUnit.DEGREES_PER_SECOND), 0.1)
		assertEquals(Math.PI, (180 * Spin.DEGREES_PER_SECOND).toDouble(SpinUnit.RADIANS_PER_SECOND), 0.01)
	}

	@Test
	fun testToString() {
		assertEquals("123°/s", (123.45 * Spin.DEGREES_PER_SECOND).toString(SpinUnit.DEGREES_PER_SECOND))
		assertEquals("1.23rad/s", (1.23 * Spin.RADIANS_PER_SECOND).toString(SpinUnit.RADIANS_PER_SECOND))
		assertEquals("-180°/s", (-Math.PI * Spin.RADIANS_PER_SECOND).toString(SpinUnit.DEGREES_PER_SECOND))
		assertEquals("3.14rad/s", (180 * Spin.DEGREES_PER_SECOND).toString(SpinUnit.RADIANS_PER_SECOND))
		assertEquals("123°/s", (123.45 * Spin.DEGREES_PER_SECOND).toString())
	}

	@Test
	fun testCompareTo() {
		assertTrue(Spin.DEGREES_PER_SECOND >= Spin.DEGREES_PER_SECOND)
		assertTrue(Spin.DEGREES_PER_SECOND <= Spin.DEGREES_PER_SECOND)
		assertFalse(Spin.DEGREES_PER_SECOND > Spin.DEGREES_PER_SECOND)
		assertFalse(Spin.DEGREES_PER_SECOND < Spin.DEGREES_PER_SECOND)
		assertTrue(Spin.DEGREES_PER_SECOND > Spin.DEGREES_PER_SECOND * 0.8f)
		assertFalse(Spin.DEGREES_PER_SECOND < Spin.DEGREES_PER_SECOND * 0.8)
		assertTrue(Spin.DEGREES_PER_SECOND / 2 > -Spin.DEGREES_PER_SECOND)
		assertTrue(-Spin.DEGREES_PER_SECOND / 2L > -Spin.DEGREES_PER_SECOND)
		assertTrue(Spin.RADIANS_PER_SECOND >= Spin.RADIANS_PER_SECOND)
		assertTrue(Spin.RADIANS_PER_SECOND <= Spin.RADIANS_PER_SECOND)
		assertFalse(Spin.RADIANS_PER_SECOND > Spin.RADIANS_PER_SECOND)
		assertFalse(Spin.RADIANS_PER_SECOND < Spin.RADIANS_PER_SECOND)
		assertTrue(Spin.RADIANS_PER_SECOND > Spin.RADIANS_PER_SECOND * 0.8f)
		assertFalse(Spin.RADIANS_PER_SECOND < Spin.RADIANS_PER_SECOND * 0.8)
		assertTrue(Spin.RADIANS_PER_SECOND / 2 > -Spin.RADIANS_PER_SECOND)
		assertTrue(-Spin.RADIANS_PER_SECOND / 2L > -Spin.RADIANS_PER_SECOND)
		assertTrue(Spin.RADIANS_PER_SECOND > Spin.DEGREES_PER_SECOND)
		assertTrue(55 * Spin.DEGREES_PER_SECOND < Spin.RADIANS_PER_SECOND)
		assertFalse(60 * Spin.DEGREES_PER_SECOND < Spin.RADIANS_PER_SECOND)
		assertTrue(20 * Spin.DEGREES_PER_SECOND > -Spin.RADIANS_PER_SECOND)
		assertFalse(20 * Spin.DEGREES_PER_SECOND > Spin.RADIANS_PER_SECOND)
		assertTrue(10 * Spin.RADIANS_PER_SECOND > 5 * Spin.RADIANS_PER_SECOND)
		assertFalse(10 * Spin.RADIANS_PER_SECOND > 10 * Spin.RADIANS_PER_SECOND)
		assertFalse(10 * Spin.RADIANS_PER_SECOND > 15 * Spin.RADIANS_PER_SECOND)
		assertFalse(10 * Spin.RADIANS_PER_SECOND < 5 * Spin.RADIANS_PER_SECOND)
		assertFalse(10 * Spin.RADIANS_PER_SECOND < 10 * Spin.RADIANS_PER_SECOND)
		assertTrue(10 * Spin.RADIANS_PER_SECOND < 15 * Spin.RADIANS_PER_SECOND)
	}

	@Test
	fun testArithmetic() {
		assertEquals(Spin.DEGREES_PER_SECOND / 2, Spin.DEGREES_PER_SECOND - 0.5f * Spin.DEGREES_PER_SECOND, 0.005)
		assertEquals(Spin.DEGREES_PER_SECOND, Spin.DEGREES_PER_SECOND / 4L + 0.75 * Spin.DEGREES_PER_SECOND, 0.005)
		assertEquals(0.25, Spin.DEGREES_PER_SECOND / (4 * Spin.DEGREES_PER_SECOND), 0.005)
		assertEquals(-Spin.DEGREES_PER_SECOND / 2, Spin.DEGREES_PER_SECOND / 2 - Spin.DEGREES_PER_SECOND, 0.005)
		assertEquals(Spin.RADIANS_PER_SECOND / 2, Spin.RADIANS_PER_SECOND - 0.5f * Spin.RADIANS_PER_SECOND, 0.005)
		assertEquals(Spin.RADIANS_PER_SECOND, Spin.RADIANS_PER_SECOND / 4L + 0.75 * Spin.RADIANS_PER_SECOND, 0.005)
		assertEquals(0.25, Spin.RADIANS_PER_SECOND / (4 * Spin.RADIANS_PER_SECOND), 0.005)
		assertEquals(-Spin.RADIANS_PER_SECOND / 2, Spin.RADIANS_PER_SECOND / 2 - Spin.RADIANS_PER_SECOND, 0.005)
		assertEquals(120f * Spin.DEGREES_PER_SECOND, PI * Spin.RADIANS_PER_SECOND - Spin.DEGREES_PER_SECOND * 60)
		assertEquals(5.0 * PI * Spin.RADIANS_PER_SECOND, PI * Spin.RADIANS_PER_SECOND / (1.0 / 3.0) + Spin.DEGREES_PER_SECOND * 360L)
		assertEquals(-Spin.DEGREES_PER_SECOND, Spin.DEGREES_PER_SECOND / 0.1f - Spin.DEGREES_PER_SECOND * 11f)
		assertEquals(2.0, Spin.DEGREES_PER_SECOND * 360 / (PI * Spin.RADIANS_PER_SECOND), 0.001)
		assertNotEquals(Spin.DEGREES_PER_SECOND, Spin.RADIANS_PER_SECOND)
		assertEquals(150.0, (3.seconds * (50 * Spin.DEGREES_PER_SECOND)).toDouble(AngleUnit.DEGREES), 0.5)
	}

	@Test
	fun testExtensionFunctions() {
		assertEquals(0.8 * Spin.DEGREES_PER_SECOND, 0.8.degps)
		assertEquals(0.6f * Spin.DEGREES_PER_SECOND, 0.6f.degps)
		assertEquals(Spin.DEGREES_PER_SECOND, 1.degps)
		assertEquals(Spin.DEGREES_PER_SECOND, 1L.degps)
		assertEquals(0.8 * Spin.RADIANS_PER_SECOND, 0.8.radps)
		assertEquals(0.6f * Spin.RADIANS_PER_SECOND, 0.6f.radps)
		assertEquals(Spin.RADIANS_PER_SECOND, 1.radps)
		assertEquals(Spin.RADIANS_PER_SECOND, 1L.radps)
		assertEquals(0.8 * Spin.DEGREES_PER_SECOND, Spin.DEGREES_PER_SECOND * 0.8)
		assertEquals(0.3f * Spin.DEGREES_PER_SECOND, Spin.DEGREES_PER_SECOND * 0.3f)
		assertEquals(1 * Spin.DEGREES_PER_SECOND, Spin.DEGREES_PER_SECOND * 1)
		assertEquals(2L * Spin.DEGREES_PER_SECOND, Spin.DEGREES_PER_SECOND * 2L)
		assertEquals(0.8 * Spin.RADIANS_PER_SECOND, Spin.RADIANS_PER_SECOND * 0.8)
		assertEquals(0.3f * Spin.RADIANS_PER_SECOND, Spin.RADIANS_PER_SECOND * 0.3f)
		assertEquals(1 * Spin.RADIANS_PER_SECOND, Spin.RADIANS_PER_SECOND * 1)
		assertEquals(2L * Spin.RADIANS_PER_SECOND, Spin.RADIANS_PER_SECOND * 2L)
	}

	@Test
	fun testMathFunctions() {
		assertEquals(Spin.RADIANS_PER_SECOND, abs(Spin.RADIANS_PER_SECOND))
		assertEquals(Spin.RADIANS_PER_SECOND / 2, abs(Spin.RADIANS_PER_SECOND / 2))
		assertEquals(0 * Spin.RADIANS_PER_SECOND, abs(0 * Spin.RADIANS_PER_SECOND))
		assertEquals(Spin.RADIANS_PER_SECOND, abs(-Spin.RADIANS_PER_SECOND))
		assertEquals(Spin.RADIANS_PER_SECOND / 2, min(Spin.RADIANS_PER_SECOND, Spin.RADIANS_PER_SECOND / 2))
		assertEquals(Spin.RADIANS_PER_SECOND, max(Spin.RADIANS_PER_SECOND, Spin.RADIANS_PER_SECOND / 2))
		assertEquals(-Spin.RADIANS_PER_SECOND, min(-Spin.RADIANS_PER_SECOND, -Spin.RADIANS_PER_SECOND / 2))
		assertEquals(-Spin.RADIANS_PER_SECOND / 2, max(-Spin.RADIANS_PER_SECOND, -Spin.RADIANS_PER_SECOND / 2))
		assertEquals(Spin.RADIANS_PER_SECOND * 0, min(Spin.RADIANS_PER_SECOND, Spin.RADIANS_PER_SECOND * 0))
		assertEquals(Spin.RADIANS_PER_SECOND, max(Spin.RADIANS_PER_SECOND, Spin.RADIANS_PER_SECOND * 0))
		assertEquals(-Spin.RADIANS_PER_SECOND, min(-Spin.RADIANS_PER_SECOND, -Spin.RADIANS_PER_SECOND * 0))
		assertEquals(-Spin.RADIANS_PER_SECOND * 0, max(-Spin.RADIANS_PER_SECOND, -Spin.RADIANS_PER_SECOND * 0))
		assertEquals(-Spin.RADIANS_PER_SECOND, min(Spin.RADIANS_PER_SECOND / 2, -Spin.RADIANS_PER_SECOND))
		assertEquals(Spin.RADIANS_PER_SECOND / 2, max(Spin.RADIANS_PER_SECOND / 2, -Spin.RADIANS_PER_SECOND))
		assertEquals(50 * Spin.DEGREES_PER_SECOND, min(50 * Spin.DEGREES_PER_SECOND, Spin.RADIANS_PER_SECOND))
	}
}
