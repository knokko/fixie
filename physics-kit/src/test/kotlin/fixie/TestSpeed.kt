package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

class TestSpeed {

	@Test
	fun testEquals() {
		assertEquals(Speed.raw(10f), Speed.raw(10f))
		assertNotEquals(Speed.raw(10f), Speed.raw(11f))
		assertNotEquals(Speed.METERS_PER_SECOND, Speed.MILES_PER_HOUR)
		assertNotEquals(Speed.METERS_PER_SECOND, Speed.KILOMETERS_PER_SECOND)
	}

	@Test
	fun testToDouble() {
		assertEquals(0.7, (0.7 * Speed.METERS_PER_SECOND).toDouble(SpeedUnit.METERS_PER_SECOND), 0.001)
		assertEquals(1.4400000000000002, (0.4 * Speed.METERS_PER_SECOND).toDouble(SpeedUnit.KILOMETERS_PER_HOUR), 0.001)
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
	fun testCompanionConstructors() {
		assertEquals(Speed.METERS_PER_SECOND, Speed.METERS_PER_SECOND)
		assertNotEquals(Speed.METERS_PER_SECOND * 0.9, Speed.METERS_PER_SECOND)
		assertTrue(Speed.METERS_PER_SECOND > Speed.METERS_PER_SECOND * 0.9)
		assertTrue(Speed.METERS_PER_SECOND > Speed.MILES_PER_HOUR)
		assertTrue(Speed.METERS_PER_SECOND < Speed.KILOMETERS_PER_SECOND)
	}

	@Test
	fun testCompareTo() {
		assertTrue(Speed.raw(40f) < Speed.raw(41f))
		assertTrue(Speed.raw(40f) <= Speed.raw(41f))
		assertTrue(Speed.raw(40f) <= Speed.raw(40f))
		assertTrue(Speed.raw(40f) >= Speed.raw(40f))
		assertFalse(Speed.raw(40f) >= Speed.raw(41f))
		assertFalse(Speed.raw(40f) > Speed.raw(41f))

		assertFalse(Speed.raw(40f) < Speed.raw(39f))
		assertFalse(Speed.raw(40f) <= Speed.raw(39f))
		assertTrue(Speed.raw(40f) >= Speed.raw(39f))
		assertTrue(Speed.raw(40f) > Speed.raw(39f))
	}

	@Test
	fun testArithmetic() {
		assertEquals(Speed.raw(73f), Speed.raw(70f) + Speed.raw(3f))
		assertEquals(Speed.raw(20f), Speed.raw(61f) - Speed.raw(41f))
		assertEquals(Speed.raw(63f), Speed.raw(3f) * 21)
		assertEquals(Speed.raw(63f), Speed.raw(3f) * 21f)
		assertEquals(Speed.raw(63f), Speed.raw(3f) * 21.0)
		assertEquals(Speed.raw(63f), Speed.raw(3f) * 21L)
		assertEquals(Speed.raw(20f), Speed.raw(40f) / 2)
		assertEquals(Speed.raw(20f), Speed.raw(40f) / 2L)
		assertEquals(Speed.raw(20f), Speed.raw(50f) / 2.5f)
		assertEquals(Speed.raw(20f), Speed.raw(50f) / 2.5)
		assertEquals(Speed.raw(-43f), -Speed.raw(43f))
		assertEquals(20.0, (10 * Speed.METERS_PER_SECOND * 2.seconds).toDouble(DistanceUnit.METER), 0.01)
		assertEquals(20.0, (10 * Speed.KILOMETERS_PER_HOUR * 2.hours).toDouble(DistanceUnit.KILOMETER), 0.01)
	}

	@Test
	fun testExtensionFunctions() {
		assertEquals(Speed.raw(63f), 21 * Speed.raw(3f))
		assertEquals(Speed.raw(63f), 21L * Speed.raw(3f))
		assertEquals(Speed.raw(63f), 21f * Speed.raw(3f))
		assertEquals(Speed.raw(63f), 21.0 * Speed.raw(3f))
		assertEquals(Speed.METERS_PER_SECOND, 1.mps)
		assertEquals(Speed.METERS_PER_SECOND, 1L.mps)
		assertEquals(0.5 * Speed.METERS_PER_SECOND, 0.5f.mps)
		assertEquals(0.5 * Speed.METERS_PER_SECOND, 0.5.mps)
		assertTrue(1.miph > 1.kmph)
		assertTrue(1.miph < 1.mps)
	}

	@Test
	fun testMathFunctions() {
		assertEquals(Speed.raw(0f), abs(Speed.raw(0f)))
		assertEquals(Speed.raw(5f), abs(Speed.raw(-5f)))
		assertEquals(Speed.raw(5f), abs(Speed.raw(5f)))
		assertEquals(Speed.raw(4f), min(Speed.raw(6f), Speed.raw(4f)))
		assertEquals(Speed.raw(6f), max(Speed.raw(6f), Speed.raw(4f)))
	}
}
