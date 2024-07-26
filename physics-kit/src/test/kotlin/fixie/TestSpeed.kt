package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.hours

class TestSpeed {

	private fun assertEquals(a: Speed, b: Speed, margin: Double = 0.001) {
		assertEquals(a.value, b.value, margin.toFloat())
	}

	private fun assertNotEquals(a: Speed, b: Speed, margin: Double = 0.001) {
		assertNotEquals(a.value, b.value, margin.toFloat())
	}

	@Test
	fun testAssertEquals() {
		assertEquals(Speed(1.23f), Speed(1.23f))
		assertNotEquals(Speed(1.23f), Speed(1.24f))
		assertEquals(Speed(1.23f), Speed(1.23001f))
		assertEquals(Speed(1.5f), Speed(1.6f), 0.2)
		assertNotEquals(Speed(1.5f), Speed(1.8f), 0.2)
		assertThrows<AssertionError> { assertNotEquals(Speed(1.23f), Speed(1.23f)) }
		assertThrows<AssertionError> { assertEquals(Speed(1.23f), Speed(1.24f)) }
		assertThrows<AssertionError> { assertNotEquals(Speed(1.23f), Speed(1.23001f)) }
		assertThrows<AssertionError> { assertNotEquals(Speed(1.5f), Speed(1.6f), 0.2) }
		assertThrows<AssertionError> { assertEquals(Speed(1.5f), Speed(1.8f), 0.2) }
	}

	@Test
	fun testToDouble() {
		assertEquals(1.0, Speed.KILOMETERS_PER_HOUR.toDouble(SpeedUnit.KILOMETERS_PER_HOUR), 0.002)
		assertEquals(0.234, (0.234 * Speed.KILOMETERS_PER_HOUR).toDouble(SpeedUnit.KILOMETERS_PER_HOUR), 0.002)
		assertFalse(Speed.KILOMETERS_PER_HOUR == Speed.MILES_PER_HOUR)
		assertEquals(1.0, Speed.MILES_PER_HOUR.toDouble(SpeedUnit.MILES_PER_HOUR), 0.002)
		assertEquals(0.234, (0.234 * Speed.MILES_PER_HOUR).toDouble(SpeedUnit.MILES_PER_HOUR), 0.002)
		assertFalse(Speed.MILES_PER_HOUR == Speed.METERS_PER_SECOND)
		assertEquals(1.0, Speed.METERS_PER_SECOND.toDouble(SpeedUnit.METERS_PER_SECOND), 0.002)
		assertEquals(0.234, (0.234 * Speed.METERS_PER_SECOND).toDouble(SpeedUnit.METERS_PER_SECOND), 0.002)
		assertFalse(Speed.METERS_PER_SECOND == Speed.KILOMETERS_PER_SECOND)
		assertEquals(1.0, Speed.KILOMETERS_PER_SECOND.toDouble(SpeedUnit.KILOMETERS_PER_SECOND), 0.002)
		assertEquals(0.234, (0.234 * Speed.KILOMETERS_PER_SECOND).toDouble(SpeedUnit.KILOMETERS_PER_SECOND), 0.002)
	}

	@Test
	fun testToString() {
		assertTrue((0.5 * Speed.METERS_PER_SECOND).toString(SpeedUnit.METERS_PER_SECOND).startsWith("0.5"))
		assertTrue((0.5 * Speed.METERS_PER_SECOND).toString(SpeedUnit.METERS_PER_SECOND).endsWith("m/s"))
		assertTrue((0.5 * Speed.KILOMETERS_PER_HOUR).toString(SpeedUnit.KILOMETERS_PER_HOUR).startsWith("0.5"))
		assertTrue((0.5 * Speed.KILOMETERS_PER_HOUR).toString(SpeedUnit.KILOMETERS_PER_HOUR).endsWith("km/h"))
		assertTrue((0.5 * Speed.KILOMETERS_PER_SECOND).toString(SpeedUnit.KILOMETERS_PER_SECOND).startsWith("0.5"))
		assertTrue((0.5 * Speed.KILOMETERS_PER_SECOND).toString(SpeedUnit.KILOMETERS_PER_SECOND).endsWith("km/s"))
	}

	@Test
	fun testCompareTo() {
		assertTrue(Speed.KILOMETERS_PER_HOUR >= Speed.KILOMETERS_PER_HOUR)
		assertTrue(Speed.KILOMETERS_PER_HOUR <= Speed.KILOMETERS_PER_HOUR)
		assertFalse(Speed.KILOMETERS_PER_HOUR > Speed.KILOMETERS_PER_HOUR)
		assertFalse(Speed.KILOMETERS_PER_HOUR < Speed.KILOMETERS_PER_HOUR)
		assertTrue(Speed.KILOMETERS_PER_HOUR > Speed.KILOMETERS_PER_HOUR * 0.8f)
		assertFalse(Speed.KILOMETERS_PER_HOUR < Speed.KILOMETERS_PER_HOUR * 0.8)
		assertTrue(Speed.KILOMETERS_PER_HOUR / 2 > -Speed.KILOMETERS_PER_HOUR)
		assertTrue(-Speed.KILOMETERS_PER_HOUR / 2L > -Speed.KILOMETERS_PER_HOUR)
		assertTrue(Speed.MILES_PER_HOUR >= Speed.MILES_PER_HOUR)
		assertTrue(Speed.MILES_PER_HOUR <= Speed.MILES_PER_HOUR)
		assertFalse(Speed.MILES_PER_HOUR > Speed.MILES_PER_HOUR)
		assertFalse(Speed.MILES_PER_HOUR < Speed.MILES_PER_HOUR)
		assertTrue(Speed.MILES_PER_HOUR > Speed.MILES_PER_HOUR * 0.8f)
		assertFalse(Speed.MILES_PER_HOUR < Speed.MILES_PER_HOUR * 0.8)
		assertTrue(Speed.MILES_PER_HOUR / 2 > -Speed.MILES_PER_HOUR)
		assertTrue(-Speed.MILES_PER_HOUR / 2L > -Speed.MILES_PER_HOUR)
		assertTrue(Speed.METERS_PER_SECOND >= Speed.METERS_PER_SECOND)
		assertTrue(Speed.METERS_PER_SECOND <= Speed.METERS_PER_SECOND)
		assertFalse(Speed.METERS_PER_SECOND > Speed.METERS_PER_SECOND)
		assertFalse(Speed.METERS_PER_SECOND < Speed.METERS_PER_SECOND)
		assertTrue(Speed.METERS_PER_SECOND > Speed.METERS_PER_SECOND * 0.8f)
		assertFalse(Speed.METERS_PER_SECOND < Speed.METERS_PER_SECOND * 0.8)
		assertTrue(Speed.METERS_PER_SECOND / 2 > -Speed.METERS_PER_SECOND)
		assertTrue(-Speed.METERS_PER_SECOND / 2L > -Speed.METERS_PER_SECOND)
		assertTrue(Speed.KILOMETERS_PER_SECOND >= Speed.KILOMETERS_PER_SECOND)
		assertTrue(Speed.KILOMETERS_PER_SECOND <= Speed.KILOMETERS_PER_SECOND)
		assertFalse(Speed.KILOMETERS_PER_SECOND > Speed.KILOMETERS_PER_SECOND)
		assertFalse(Speed.KILOMETERS_PER_SECOND < Speed.KILOMETERS_PER_SECOND)
		assertTrue(Speed.KILOMETERS_PER_SECOND > Speed.KILOMETERS_PER_SECOND * 0.8f)
		assertFalse(Speed.KILOMETERS_PER_SECOND < Speed.KILOMETERS_PER_SECOND * 0.8)
		assertTrue(Speed.KILOMETERS_PER_SECOND / 2 > -Speed.KILOMETERS_PER_SECOND)
		assertTrue(-Speed.KILOMETERS_PER_SECOND / 2L > -Speed.KILOMETERS_PER_SECOND)
		assertTrue(Speed.MILES_PER_HOUR > Speed.KILOMETERS_PER_HOUR)
		assertTrue(Speed.METERS_PER_SECOND > Speed.MILES_PER_HOUR)
		assertTrue(Speed.KILOMETERS_PER_SECOND > Speed.METERS_PER_SECOND)
	}

	@Test
	fun testArithmetic() {
		assertEquals(Speed.KILOMETERS_PER_HOUR / 2, Speed.KILOMETERS_PER_HOUR - 0.5f * Speed.KILOMETERS_PER_HOUR, 0.005)
		assertEquals(Speed.KILOMETERS_PER_HOUR, Speed.KILOMETERS_PER_HOUR / 4L + 0.75 * Speed.KILOMETERS_PER_HOUR, 0.005)
		assertEquals(0.25, Speed.KILOMETERS_PER_HOUR / (4 * Speed.KILOMETERS_PER_HOUR), 0.005)
		assertEquals(-Speed.KILOMETERS_PER_HOUR / 2, Speed.KILOMETERS_PER_HOUR / 2 - Speed.KILOMETERS_PER_HOUR, 0.005)
		assertEquals(Speed.MILES_PER_HOUR / 2, Speed.MILES_PER_HOUR - 0.5f * Speed.MILES_PER_HOUR, 0.005)
		assertEquals(Speed.MILES_PER_HOUR, Speed.MILES_PER_HOUR / 4L + 0.75 * Speed.MILES_PER_HOUR, 0.005)
		assertEquals(0.25, Speed.MILES_PER_HOUR / (4 * Speed.MILES_PER_HOUR), 0.005)
		assertEquals(-Speed.MILES_PER_HOUR / 2, Speed.MILES_PER_HOUR / 2 - Speed.MILES_PER_HOUR, 0.005)
		assertEquals(Speed.METERS_PER_SECOND / 2, Speed.METERS_PER_SECOND - 0.5f * Speed.METERS_PER_SECOND, 0.005)
		assertEquals(Speed.METERS_PER_SECOND, Speed.METERS_PER_SECOND / 4L + 0.75 * Speed.METERS_PER_SECOND, 0.005)
		assertEquals(0.25, Speed.METERS_PER_SECOND / (4 * Speed.METERS_PER_SECOND), 0.005)
		assertEquals(-Speed.METERS_PER_SECOND / 2, Speed.METERS_PER_SECOND / 2 - Speed.METERS_PER_SECOND, 0.005)
		assertEquals(Speed.KILOMETERS_PER_SECOND / 2, Speed.KILOMETERS_PER_SECOND - 0.5f * Speed.KILOMETERS_PER_SECOND, 0.005)
		assertEquals(Speed.KILOMETERS_PER_SECOND, Speed.KILOMETERS_PER_SECOND / 4L + 0.75 * Speed.KILOMETERS_PER_SECOND, 0.005)
		assertEquals(0.25, Speed.KILOMETERS_PER_SECOND / (4 * Speed.KILOMETERS_PER_SECOND), 0.005)
		assertEquals(-Speed.KILOMETERS_PER_SECOND / 2, Speed.KILOMETERS_PER_SECOND / 2 - Speed.KILOMETERS_PER_SECOND, 0.005)
		assertEquals(20.0, (2.seconds * (10 * Speed.METERS_PER_SECOND)).toDouble(DistanceUnit.METER), 0.01)
		assertEquals(20.0, (10 * Speed.KILOMETERS_PER_HOUR * 2.hours).toDouble(DistanceUnit.KILOMETER), 0.01)
		assertEquals(0.5, (Speed.METERS_PER_SECOND / 2.seconds).toDouble(), 2.5E-4)
	}

	@Test
	fun testExtensionFunctions() {
		assertEquals(0.8 * Speed.KILOMETERS_PER_HOUR, 0.8.kmph)
		assertEquals(0.6f * Speed.KILOMETERS_PER_HOUR, 0.6f.kmph)
		assertEquals(Speed.KILOMETERS_PER_HOUR, 1.kmph)
		assertEquals(Speed.KILOMETERS_PER_HOUR, 1L.kmph)
		assertEquals(0.8 * Speed.MILES_PER_HOUR, 0.8.miph)
		assertEquals(0.6f * Speed.MILES_PER_HOUR, 0.6f.miph)
		assertEquals(Speed.MILES_PER_HOUR, 1.miph)
		assertEquals(Speed.MILES_PER_HOUR, 1L.miph)
		assertEquals(0.8 * Speed.METERS_PER_SECOND, 0.8.mps)
		assertEquals(0.6f * Speed.METERS_PER_SECOND, 0.6f.mps)
		assertEquals(Speed.METERS_PER_SECOND, 1.mps)
		assertEquals(Speed.METERS_PER_SECOND, 1L.mps)
		assertEquals(0.8 * Speed.KILOMETERS_PER_SECOND, 0.8.kmps)
		assertEquals(0.6f * Speed.KILOMETERS_PER_SECOND, 0.6f.kmps)
		assertEquals(Speed.KILOMETERS_PER_SECOND, 1.kmps)
		assertEquals(Speed.KILOMETERS_PER_SECOND, 1L.kmps)
		assertEquals(0.8 * Speed.KILOMETERS_PER_HOUR, Speed.KILOMETERS_PER_HOUR * 0.8)
		assertEquals(0.3f * Speed.KILOMETERS_PER_HOUR, Speed.KILOMETERS_PER_HOUR * 0.3f)
		assertEquals(1 * Speed.KILOMETERS_PER_HOUR, Speed.KILOMETERS_PER_HOUR * 1)
		assertEquals(2L * Speed.KILOMETERS_PER_HOUR, Speed.KILOMETERS_PER_HOUR * 2L)
		assertEquals(0.8 * Speed.MILES_PER_HOUR, Speed.MILES_PER_HOUR * 0.8)
		assertEquals(0.3f * Speed.MILES_PER_HOUR, Speed.MILES_PER_HOUR * 0.3f)
		assertEquals(1 * Speed.MILES_PER_HOUR, Speed.MILES_PER_HOUR * 1)
		assertEquals(2L * Speed.MILES_PER_HOUR, Speed.MILES_PER_HOUR * 2L)
		assertEquals(0.8 * Speed.METERS_PER_SECOND, Speed.METERS_PER_SECOND * 0.8)
		assertEquals(0.3f * Speed.METERS_PER_SECOND, Speed.METERS_PER_SECOND * 0.3f)
		assertEquals(1 * Speed.METERS_PER_SECOND, Speed.METERS_PER_SECOND * 1)
		assertEquals(2L * Speed.METERS_PER_SECOND, Speed.METERS_PER_SECOND * 2L)
		assertEquals(0.8 * Speed.KILOMETERS_PER_SECOND, Speed.KILOMETERS_PER_SECOND * 0.8)
		assertEquals(0.3f * Speed.KILOMETERS_PER_SECOND, Speed.KILOMETERS_PER_SECOND * 0.3f)
		assertEquals(1 * Speed.KILOMETERS_PER_SECOND, Speed.KILOMETERS_PER_SECOND * 1)
		assertEquals(2L * Speed.KILOMETERS_PER_SECOND, Speed.KILOMETERS_PER_SECOND * 2L)
	}

	@Test
	fun testMathFunctions() {
		assertEquals(Speed.METERS_PER_SECOND, abs(Speed.METERS_PER_SECOND))
		assertEquals(Speed.METERS_PER_SECOND / 2, abs(Speed.METERS_PER_SECOND / 2))
		assertEquals(0 * Speed.METERS_PER_SECOND, abs(0 * Speed.METERS_PER_SECOND))
		assertEquals(Speed.METERS_PER_SECOND, abs(-Speed.METERS_PER_SECOND))
		assertEquals(Speed.METERS_PER_SECOND / 2, min(Speed.METERS_PER_SECOND, Speed.METERS_PER_SECOND / 2))
		assertEquals(Speed.METERS_PER_SECOND, max(Speed.METERS_PER_SECOND, Speed.METERS_PER_SECOND / 2))
		assertEquals(-Speed.METERS_PER_SECOND, min(-Speed.METERS_PER_SECOND, -Speed.METERS_PER_SECOND / 2))
		assertEquals(-Speed.METERS_PER_SECOND / 2, max(-Speed.METERS_PER_SECOND, -Speed.METERS_PER_SECOND / 2))
		assertEquals(Speed.METERS_PER_SECOND * 0, min(Speed.METERS_PER_SECOND, Speed.METERS_PER_SECOND * 0))
		assertEquals(Speed.METERS_PER_SECOND, max(Speed.METERS_PER_SECOND, Speed.METERS_PER_SECOND * 0))
		assertEquals(-Speed.METERS_PER_SECOND, min(-Speed.METERS_PER_SECOND, -Speed.METERS_PER_SECOND * 0))
		assertEquals(-Speed.METERS_PER_SECOND * 0, max(-Speed.METERS_PER_SECOND, -Speed.METERS_PER_SECOND * 0))
		assertEquals(-Speed.METERS_PER_SECOND, min(Speed.METERS_PER_SECOND / 2, -Speed.METERS_PER_SECOND))
		assertEquals(Speed.METERS_PER_SECOND / 2, max(Speed.METERS_PER_SECOND / 2, -Speed.METERS_PER_SECOND))
	}
}
