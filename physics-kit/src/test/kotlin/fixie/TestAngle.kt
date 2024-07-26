package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.math.PI

class TestAngle {

	private fun assertEquals(expected: Angle, actual: Angle) {
		val difference = (expected - actual).toDouble(AngleUnit.DEGREES)
		if (difference > 0.1 || difference < -0.1) org.junit.jupiter.api.Assertions.assertEquals(expected, actual)
	}

	private fun assertNotEquals(expected: Angle, actual: Angle) {
		assertThrows<AssertionError> { assertEquals(expected, actual) }
	}

	@Test
	fun testAssertEquals() {
		assertEquals(Angle.degrees(123), Angle.degrees(123))
		assertThrows<AssertionError> { assertNotEquals(Angle.degrees(123), Angle.degrees(123)) }
		assertEquals(Angle.degrees(123), Angle.degrees(123.0001))
		assertThrows<AssertionError> { assertEquals(Angle.degrees(0), Angle.degrees(5)) }
		assertNotEquals(Angle.degrees(0), Angle.degrees(5))
		assertThrows<AssertionError> { assertEquals(Angle.degrees(0), Angle.degrees(-5)) }
		assertThrows<AssertionError> { assertEquals(Angle.degrees(355), Angle.degrees(359)) }
	}

	@Test
	fun testToDouble() {
		assertEquals(12.0, Angle.degrees(12).toDouble(AngleUnit.DEGREES), 0.1)
		assertEquals(90.0, Angle.radians(0.5 * PI).toDouble(AngleUnit.DEGREES), 0.1)
		assertEquals(2.0, Angle.radians(2).toDouble(AngleUnit.RADIANS), 0.001)
		assertEquals(0.5 * PI, Angle.degrees(90).toDouble(AngleUnit.RADIANS), 0.001)
	}

	@Test
	fun testToString() {
		assertEquals("100°", Angle.degrees(100).toString(AngleUnit.DEGREES, 1))
		assertEquals("1.23rad", Angle.radians(1.23).toString(AngleUnit.RADIANS, 2))
		assertEquals("0°", Angle.degrees(0).toString(AngleUnit.DEGREES, 1))
		assertEquals("0rad", Angle.degrees(0).toString(AngleUnit.RADIANS, 2))
		assertEquals("0°", Angle.radians(0L).toString(AngleUnit.DEGREES, 1))
		assertEquals("0rad", Angle.radians(0).toString(AngleUnit.RADIANS, 2))
		assertEquals("90°", Angle.radians(0.5 * PI).toString(AngleUnit.DEGREES, 0))
	}

	@Test
	fun testCompareTo() {
		assertNotEquals(Angle.degrees(100), Angle.degrees(101))
		assertNotEquals(Angle.radians(1.2), Angle.radians(1.21))
		assertEquals(Angle.degrees(100), Angle.degrees(820))
		assertEquals(Angle.degrees(60f), Angle.degrees(-300f))
		assertEquals(Angle.degrees(260.0), Angle.degrees(-100))
		assertEquals(Angle.degrees(0), Angle.degrees(360))
		assertEquals(123.0, Angle.degrees(123).toDouble(AngleUnit.DEGREES), 0.01)
		assertEquals(45.0, Angle.degrees(45f).toDouble(AngleUnit.DEGREES), 0.01)
		assertEquals(-60.0, Angle.degrees(-60.0).toDouble(AngleUnit.DEGREES), 0.01)
		assertEquals(1.23, Angle.radians(1.23).toDouble(AngleUnit.RADIANS), 0.001)
		val rawValue = 123
		assertEquals(rawValue, Angle.raw(rawValue).raw)
	}

	@Test
	fun testArithmetic() {
		assertEquals(Angle.degrees(50), Angle.degrees(30) + Angle.degrees(20))
		assertEquals(Angle.radians(0f), Angle.degrees(180) + Angle.degrees(180))
		assertEquals(Angle.radians(0f), Angle.radians(PI) + Angle.degrees(180))
		assertEquals(Angle.radians(0.25f * PI.toFloat()), Angle.radians(0.75 * PI) - Angle.degrees(90))
		assertEquals(Angle.degrees(300), Angle.degrees(320) + Angle.degrees(340))
		assertEquals(Angle.degrees(30f), Angle.degrees(50) - Angle.degrees(20))
		assertEquals(Angle.degrees(180), Angle.degrees(180L) - Angle.degrees(0))
		assertEquals(Angle.degrees(320L), Angle.degrees(300) - Angle.degrees(340))
		assertEquals(Angle.degrees(100L), 2 * Angle.degrees(50))
		assertEquals(Angle.degrees(100), 2L * Angle.degrees(50))
	}

	@Test
	fun testExtensionFunctions() {
		assertEquals(Angle.degrees(123), 123.degrees)
		assertEquals(Angle.degrees(123), 123L.degrees)
		assertEquals(Angle.degrees(-45), -45f.degrees)
		assertEquals(Angle.radians(1.5), 1.5.radians)
	}

	@Test
	fun testMathFunctions() {
		assertEquals(Angle.degrees(0), abs(Angle.degrees(0)))
		assertEquals(Angle.degrees(40), abs(Angle.degrees(40)))
		assertEquals(Angle.degrees(40), abs(Angle.degrees(-40)))
		assertEquals(0.5, sin(Angle.degrees(30)), 0.01)
		assertEquals(-1.0, cos(Angle.radians(PI)), 0.01)
		assertEquals(1.0, tan(Angle.degrees(45)), 0.01)
	}
}
