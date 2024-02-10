package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

class TestSpeed {

	@Test
	fun testEquals() {
		assertEquals(Speed.raw(10), Speed.raw(10))
		assertNotEquals(Speed.raw(10), Speed.raw(11))
		assertNotEquals(Speed.METERS_PER_SECOND, Speed.KILOMETERS_PER_HOUR)
		assertNotEquals(Speed.METERS_PER_SECOND, Speed.MILES_PER_HOUR)
	}

	@Test
	fun testToDouble() {
		assertEquals(0.7, (0.7 * Speed.METERS_PER_SECOND).toDouble(SpeedUnit.METERS_PER_SECOND), 0.001)
		assertEquals(1.4400000000000002, (0.4 * Speed.METERS_PER_SECOND).toDouble(SpeedUnit.KILOMETERS_PER_HOUR), 0.001)
	}

	@Test
	fun testToString() {
		assertEquals("0.5m/s", (0.5 * Speed.METERS_PER_SECOND).toString(SpeedUnit.METERS_PER_SECOND))
		assertEquals("0.5km/h", (0.5 * Speed.KILOMETERS_PER_HOUR).toString(SpeedUnit.KILOMETERS_PER_HOUR))
		assertEquals("0.5km/s", (0.5 * Speed.KILOMETERS_PER_SECOND).toString(SpeedUnit.KILOMETERS_PER_SECOND))
	}

	@Test
	fun testCompanionConstructors() {
		assertEquals(Speed.METERS_PER_SECOND, Speed.METERS_PER_SECOND)
		assertNotEquals(Speed.METERS_PER_SECOND * 0.9, Speed.METERS_PER_SECOND)
		assertTrue(Speed.METERS_PER_SECOND > Speed.METERS_PER_SECOND * 0.9)
		assertTrue(Speed.METERS_PER_SECOND > Speed.MILES_PER_HOUR)
		assertFalse(Speed.KILOMETERS_PER_HOUR > Speed.MILES_PER_HOUR)
	}

	@Test
	fun testCompareTo() {
		assertTrue(Speed.raw(40) < Speed.raw(41))
		assertTrue(Speed.raw(40) <= Speed.raw(41))
		assertTrue(Speed.raw(40) <= Speed.raw(40))
		assertTrue(Speed.raw(40) >= Speed.raw(40))
		assertFalse(Speed.raw(40) >= Speed.raw(41))
		assertFalse(Speed.raw(40) > Speed.raw(41))

		assertFalse(Speed.raw(40) < Speed.raw(39))
		assertFalse(Speed.raw(40) <= Speed.raw(39))
		assertTrue(Speed.raw(40) >= Speed.raw(39))
		assertTrue(Speed.raw(40) > Speed.raw(39))
	}

	@Test
	fun testArithmetic() {
		assertEquals(Speed.raw(73), Speed.raw(70) + Speed.raw(3))
		assertEquals(Speed.raw(20), Speed.raw(61) - Speed.raw(41))
		assertEquals(Speed.raw(63), Speed.raw(3) * 21)
		assertEquals(Speed.raw(63), Speed.raw(3) * FixDisplacement.from(21))
		assertEquals(Speed.raw(63), Speed.raw(3) * 21L)
		assertEquals(Speed.raw(63), Speed.raw(3) * 21f)
		assertEquals(Speed.raw(63), Speed.raw(3) * 21.0)
		assertEquals(Speed.raw(20), Speed.raw(40) / 2)
		assertEquals(Speed.raw(20), Speed.raw(40) / FixDisplacement.from(2))
		assertEquals(FixDisplacement.from(20), Speed.raw(40) / Speed.raw(2))
		assertEquals(Speed.raw(20), Speed.raw(40) / 2L)
		assertEquals(Speed.raw(20), Speed.raw(50) / 2.5f)
		assertEquals(Speed.raw(20), Speed.raw(50) / 2.5)
		assertEquals(Speed.raw(-43), -Speed.raw(43))
		assertEquals(20.0, (10 * Speed.METERS_PER_SECOND * 2.seconds).toDouble(DistanceUnit.METER), 0.001)
		assertEquals(20.0, (10 * Speed.KILOMETERS_PER_HOUR * 2.hours).toDouble(DistanceUnit.KILOMETER), 0.001)
	}

	@Test
	fun testExtensionFunctions() {
		assertEquals(Speed.raw(63), 21 * Speed.raw(3))
		assertEquals(Speed.raw(63), 21L * Speed.raw(3))
		assertEquals(Speed.raw(63), 21f * Speed.raw(3))
		assertEquals(Speed.raw(63), 21.0 * Speed.raw(3))
		assertEquals(Speed.METERS_PER_SECOND, 1.mps)
		assertEquals(Speed.METERS_PER_SECOND, 1L.mps)
		assertEquals(0.5 * Speed.METERS_PER_SECOND, 0.5f.mps)
		assertEquals(0.5 * Speed.METERS_PER_SECOND, 0.5.mps)
		assertTrue(1.miph > 1.kmph)
		assertTrue(1.miph < 1.mps)
	}

	@Test
	fun testMathFunctions() {
		assertEquals(Speed.raw(0), abs(Speed.raw(0)))
		assertEquals(Speed.raw(5), abs(Speed.raw(-5)))
		assertEquals(Speed.raw(5), abs(Speed.raw(5)))
		assertEquals(Speed.raw(4), min(Speed.raw(6), Speed.raw(4)))
		assertEquals(Speed.raw(6), max(Speed.raw(6), Speed.raw(4)))
	}
}
