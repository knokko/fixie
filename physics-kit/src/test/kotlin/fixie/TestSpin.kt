package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.math.PI
import kotlin.time.Duration.Companion.seconds

class TestSpin {

	private val rps = Spin.RADIANS_PER_SECOND
	private val dps = Spin.DEGREES_PER_SECOND

	private fun assertNearlyEquals(expected: Spin, actual: Spin) {
		val difference = (expected - actual).toDouble(SpinUnit.DEGREES_PER_SECOND)
		if (difference > 0.1 || difference < -0.1) assertEquals(expected, actual)
	}

	@Test
	fun testAssertNearlyEquals() {
		assertNearlyEquals(123 * dps, dps * 123)
		assertNearlyEquals(123 * dps, dps * 123.0001)
		assertNearlyEquals(180 * dps, rps * Math.PI)
		assertThrows<AssertionError> { assertNearlyEquals(dps, dps * 5) }
		assertThrows<AssertionError> { assertNearlyEquals(dps * 355, dps * 355.5) }
	}

	@Test
	fun testToString() {
		assertEquals("123°/s", (123.45 * dps).toString(SpinUnit.DEGREES_PER_SECOND))
		assertEquals("1.23rad/s", (1.23 * rps).toString(SpinUnit.RADIANS_PER_SECOND))
		assertEquals("-180°/s", (-Math.PI * rps).toString(SpinUnit.DEGREES_PER_SECOND))
		assertEquals("3.14rad/s", (180 * dps).toString(SpinUnit.RADIANS_PER_SECOND))
		assertEquals("123°/s", (123.45 * dps).toString())
	}

	@Test
	fun testCompanionConstructors() {
		assertTrue(55 * dps < rps)
		assertFalse(60 * dps < rps)
	}

	@Test
	fun testCompareTo() {
		assertTrue(20 * dps > -rps)
		assertFalse(20 * dps > rps)
		assertTrue(10 * rps > 5 * rps)
		assertFalse(10 * rps > 10 * rps)
		assertFalse(10 * rps > 15 * rps)
		assertFalse(10 * rps < 5 * rps)
		assertFalse(10 * rps < 10 * rps)
		assertTrue(10 * rps < 15 * rps)
	}

	@Test
	fun testArithmetic() {
		assertNearlyEquals(120f * dps, PI * rps - dps * 60)
		assertNearlyEquals(5.0 * PI * rps, PI * rps / (1.0 / 3.0) + dps * 360L)
		assertNearlyEquals(-dps, dps / 0.1f - dps * 11f)
		assertEquals(2.0f, dps * 360 / (PI * rps), 0.001f)
		assertEquals(150.0, (3.seconds * (50 * dps)).toDouble(AngleUnit.DEGREES), 0.5)
	}

	@Test
	fun testExtensionFunctions() {
		assertNearlyEquals(rps / 10, 0.1.radps)
		assertNearlyEquals(dps / 2L, 0.5.degps)
	}

	@Test
	fun testMathFunctions() {
		assertEquals(5 * rps, abs(5 * rps))
		assertEquals(5 * rps, abs(-5 * rps))
		assertEquals(0 * rps, abs(0 * rps))
		assertEquals(5 * dps, min(5 * dps, 10 * dps))
		assertEquals(-10 * dps, min(5 * dps, -10 * dps))
		assertEquals(10 * dps, max(5 * dps, 10 * dps))
		assertEquals(5 * dps, max(5 * dps, -10 * dps))
	}
}
