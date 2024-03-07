package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.math.PI

class TestAngle {

	private fun assertNearlyEquals(expected: Angle, actual: Angle) {
		val difference = (expected - actual).toDouble(AngleUnit.DEGREES)
		if (difference > 0.1 && difference < 359.9) assertEquals(expected, actual)
	}

	@Test
	fun testAssertNearlyEquals() {
		assertNearlyEquals(Angle.degrees(123), Angle.degrees(123))
		assertNearlyEquals(Angle.degrees(123), Angle.degrees(123.0001))
		assertThrows<AssertionError> { assertNearlyEquals(Angle.degrees(0), Angle.degrees(5)) }
		assertThrows<AssertionError> { assertNearlyEquals(Angle.degrees(0), Angle.degrees(-5)) }
		assertThrows<AssertionError> { assertNearlyEquals(Angle.degrees(355), Angle.degrees(359)) }
	}

	@Test
	fun testToString() {
		assertEquals("100°", Angle.degrees(100).toString(AngleUnit.DEGREES, 1))
		assertEquals("1.23rad", Angle.radians(1.23).toString(AngleUnit.RADIANS, 2))
		assertEquals("0°", Angle.degrees(0).toString(AngleUnit.DEGREES, 1))
		assertEquals("0rad", Angle.degrees(0).toString(AngleUnit.RADIANS, 2))
		assertEquals("0°", Angle.radians(0).toString(AngleUnit.DEGREES, 1))
		assertEquals("0rad", Angle.radians(0).toString(AngleUnit.RADIANS, 2))
	}

	@Test
	fun testCompanionConstructors() {
		assertNotEquals(Angle.degrees(100), Angle.degrees(101))
		assertNotEquals(Angle.radians(1.2), Angle.radians(1.21))
		assertNearlyEquals(Angle.degrees(100), Angle.degrees(820))
		assertNearlyEquals(Angle.degrees(60f), Angle.degrees(-300f))
		assertNearlyEquals(Angle.degrees(260.0), Angle.degrees(-100))
		assertNearlyEquals(Angle.degrees(0), Angle.degrees(360))
		assertEquals(123.0, Angle.degrees(123).toDouble(AngleUnit.DEGREES), 0.01)
		assertEquals(45.0, Angle.degrees(45f).toDouble(AngleUnit.DEGREES), 0.01)
		assertEquals(300.0, Angle.degrees(-60.0).toDouble(AngleUnit.DEGREES), 0.01)
		assertEquals(1.23, Angle.radians(1.23).toDouble(AngleUnit.RADIANS), 0.001)
		val rawValue = 123u
		assertEquals(rawValue, Angle.raw(rawValue).value.raw)
	}

	@Test
	fun testArithmetic() {
		assertNearlyEquals(Angle.degrees(50), Angle.degrees(30) + Angle.degrees(20))
		assertNearlyEquals(Angle.degrees(0), Angle.degrees(180) + Angle.degrees(180))
		assertNearlyEquals(Angle.degrees(300), Angle.degrees(320) + Angle.degrees(340))
		assertNearlyEquals(Angle.degrees(30), Angle.degrees(50) - Angle.degrees(20))
		assertNearlyEquals(Angle.degrees(180), Angle.degrees(180) - Angle.degrees(0))
		assertNearlyEquals(Angle.degrees(320), Angle.degrees(300) - Angle.degrees(340))
	}

	@Test
	fun testExtensionFunctions() {
		assertNearlyEquals(Angle.degrees(123), 123.degrees)
		assertNearlyEquals(Angle.degrees(-45), -45f.degrees)
		assertNearlyEquals(Angle.radians(1.5), 1.5.radians)
	}

	@Test
	fun testMathFunctions() {
		assertEquals(0.5, sin(Angle.degrees(30)), 0.01)
		assertEquals(-1.0, cos(Angle.radians(PI)), 0.01)
		assertEquals(1.0, tan(Angle.degrees(45)), 0.01)
	}
}
