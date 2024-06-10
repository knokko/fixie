package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.time.Duration.Companion.seconds

class TestAcceleration {

	@Test
	fun testEquals() {
		assertEquals((Acceleration.MPS2 * 1.1).toDouble(), (Acceleration.MPS2 * 1.1).toDouble(), 0.001)
		assertEquals((Acceleration.MPS2 * 1.1).toDouble(), (Acceleration.MPS2 * 1.1000001).toDouble(), 0.001)
		assertNotEquals((Acceleration.MPS2 * 1.2).toDouble(), (Acceleration.MPS2 * 1.1).toDouble(), 0.001)
	}

	@Test
	fun testToDouble() {
		assertEquals(1.23, 1.23.mps2.toDouble(), 0.001)
	}

	@Test
	fun testToString() {
		assertEquals("2.34m/s^2", (Acceleration.MPS2 * 2.34).toString())
	}

	@Test
	fun testCompareTo() {
		assertFalse(Acceleration.MPS2 > Acceleration.MPS2)
		assertFalse(Acceleration.MPS2 < Acceleration.MPS2)
		assertFalse(Acceleration.MPS2 > Acceleration.MPS2 * 1.2)
		assertTrue(Acceleration.MPS2 < Acceleration.MPS2 * 1.2)
		assertTrue(Acceleration.MPS2 * 1.2 > Acceleration.MPS2)
		assertFalse(Acceleration.MPS2 * 1.2 < Acceleration.MPS2)
		assertFalse(Acceleration.MPS2 * -1.2 > Acceleration.MPS2 * -1)
		assertTrue(Acceleration.MPS2 * -1.2 < Acceleration.MPS2 * -1)
	}

	@Test
	fun testArithmetic() {
		val x = Acceleration.MPS2
		fun approximatelyEqual(a: Acceleration, b: Acceleration) {
			assertEquals(a.toDouble(), b.toDouble(), 0.001)
		}
		approximatelyEqual(x * 3L, x + x + x)
		approximatelyEqual(x * 0.3, x - x * 0.7f)
		approximatelyEqual(x / 0.4, x * 2.5)
		assertEquals(0.25f, x / (x * 4), 0.001f)
		assertEquals(0.8, ((x * 0.2) * 4.seconds).toDouble(SpeedUnit.METERS_PER_SECOND), 0.01)
	}

	@Test
	fun testExtensionFunctions() {
		assertEquals(2.mps2.toDouble(), (2 * Acceleration.MPS2).toDouble(), 0.001)
		assertNotEquals(2.1.mps2.toDouble(), (2 * Acceleration.MPS2).toDouble(), 0.001)
		assertEquals(4.0, (4 * Acceleration.MPS2).toDouble(), 0.001)
		assertEquals(4.0, (4L * Acceleration.MPS2).toDouble(), 0.001)
		assertEquals(4.0, (4f * Acceleration.MPS2).toDouble(), 0.001)
		assertEquals(4.0, (4.0 * Acceleration.MPS2).toDouble(), 0.001)
	}

	@Test
	fun testMathFunctions() {
		val x = Acceleration.MPS2
		assertEquals(x, abs(x))
		assertEquals(x, abs(-x))
		assertEquals(0 * x, abs(0 * x))
		assertEquals(2 * x, min(2 * x, 3 * x))
		assertEquals(3 * x, max(2 * x, 3 * x))
		assertEquals(-3 * x, min(2 * x, -3 * x))
		assertEquals(2 * x, max(2 * x, -3 * x))
	}
}
