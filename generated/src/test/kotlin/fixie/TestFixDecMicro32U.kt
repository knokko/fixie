package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixDecMicro32U {

	fun assertEquals(a: FixDecMicro32U, b: FixDecMicro32U, maxDelta: FixDecMicro32U) {
		val rawDifference = a.raw.toInt() - b.raw.toInt()
		if (rawDifference.absoluteValue > maxDelta.raw.toInt()) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixDecMicro32U.ZERO.toString())
		assertEquals("1", FixDecMicro32U.ONE.toString())
		assertTrue((FixDecMicro32U.ONE / 3).toString().startsWith("0.33"))
		assertTrue((FixDecMicro32U.from(4293) + FixDecMicro32U.ONE / 3).toString().endsWith((FixDecMicro32U.ONE / 3).toString().substring(1)))
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixDecMicro32U.ONE, FixDecMicro32U.from(one))

		fun testValue(value: Int) = assertEquals(value, FixDecMicro32U.from(value).toInt())
		fun testOverflow(value: Int) = assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(value) }
		testValue(0)
		testValue(1)
		testValue(1042)
		testValue(4294)

		testOverflow(-2147483648)
		testOverflow(-978689185)
		testOverflow(-974146)
		testOverflow(-740009)
		testOverflow(-16299)
		testOverflow(-1)

		testOverflow(4295)
		testOverflow(5608621)
		testOverflow(109678691)
		testOverflow(2147483647)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixDecMicro32U.ONE, FixDecMicro32U.from(one))

		fun testValue(value: Long) = assertEquals(value, FixDecMicro32U.from(value).toLong())
		fun testOverflow(value: Long) = assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(value) }
		testValue(0)
		testValue(1)
		testValue(1042)
		testValue(4294)

		testOverflow(Long.MIN_VALUE)
		testOverflow(-589153389781491222)
		testOverflow(-43117419028065805)
		testOverflow(-785861820617605)
		testOverflow(-19482330338386)
		testOverflow(-34771906616)
		testOverflow(-6550113726)
		testOverflow(-448440224)
		testOverflow(-2065398)
		testOverflow(-11740)
		testOverflow(-7239)
		testOverflow(-1)

		testOverflow(4295)
		testOverflow(5608621)
		testOverflow(109678691)
		testOverflow(6447414458)
		testOverflow(72358219803)
		testOverflow(16517773559003)
		testOverflow(816996720371753)
		testOverflow(35212590478468515)
		testOverflow(280879600729486179)
		testOverflow(9223372036854775807)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixDecMicro32U.ONE, FixDecMicro32U.from(1f))
		val delta = 2.0E-6f
		assertEquals(1.0E-6f, FixDecMicro32U.from(1.0E-6f).toFloat(), delta)
		assertEquals(6.078089E-5f, FixDecMicro32U.from(6.078089E-5f).toFloat(), delta)
		assertEquals(0.0032344286f, FixDecMicro32U.from(0.0032344286f).toFloat(), delta)
		assertEquals(0.009767175f, FixDecMicro32U.from(0.009767175f).toFloat(), delta)
		assertEquals(0.5335246f, FixDecMicro32U.from(0.5335246f).toFloat(), delta)
		assertEquals(14.77877f, FixDecMicro32U.from(14.77877f).toFloat(), delta)
		assertEquals(57.959488f, FixDecMicro32U.from(57.959488f).toFloat(), delta)
		assertEquals(4294.9673f, FixDecMicro32U.from(4294.9673f).toFloat(), delta)

		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(4295.9673f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(-4295.9673f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(435224.97f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(-435224.97f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(1.8446744E7f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(-1.8446744E7f) }
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixDecMicro32U.ONE, FixDecMicro32U.from(1.0))
		val delta = 2.0E-6
		assertEquals(1.0E-6, FixDecMicro32U.from(1.0E-6).toDouble(), delta)
		assertEquals(6.0780890065293825E-5, FixDecMicro32U.from(6.0780890065293825E-5).toDouble(), delta)
		assertEquals(0.0032344286451819098, FixDecMicro32U.from(0.0032344286451819098).toDouble(), delta)
		assertEquals(0.009767174703735716, FixDecMicro32U.from(0.009767174703735716).toDouble(), delta)
		assertEquals(0.5335245655773619, FixDecMicro32U.from(0.5335245655773619).toDouble(), delta)
		assertEquals(14.778770326257877, FixDecMicro32U.from(14.778770326257877).toDouble(), delta)
		assertEquals(57.95948821553071, FixDecMicro32U.from(57.95948821553071).toDouble(), delta)
		assertEquals(4294.9672949999995, FixDecMicro32U.from(4294.9672949999995).toDouble(), delta)

		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(4295.967295) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(-4295.967295) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(435224.9796411543) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(-435224.9796411543) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(1.844674406511962E7) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(-1.844674406511962E7) }
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixDecMicro32U, b: FixDecMicro32U, c: FixDecMicro32U) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}

		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixDecMicro32U.from(a), FixDecMicro32U.from(b), FixDecMicro32U.from(c))
			assertEquals(FixDecMicro32U.from(c), FixDecMicro32U.from(a) + b)
			assertEquals(FixDecMicro32U.from(c), b + FixDecMicro32U.from(a))
			assertEquals(FixDecMicro32U.from(a), FixDecMicro32U.from(c) - b)
			assertEquals(FixDecMicro32U.from(b), c - FixDecMicro32U.from(a))
		}

		testValues(FixDecMicro32U.raw(UInt.MIN_VALUE), FixDecMicro32U.ONE, FixDecMicro32U.raw(UInt.MIN_VALUE + 1000000u))
		testValues(0, 1680, 1680)
		testValues(1, 672, 673)
		testValues(86, 603, 689)
		testValues(706, 1331, 2037)
		testValues(1177, 977, 2154)
		testValues(2168, 62, 2230)
		testValues(4294, 0, 4294)
		testValues(FixDecMicro32U.raw(UInt.MAX_VALUE - 1000000u), FixDecMicro32U.ONE, FixDecMicro32U.raw(UInt.MAX_VALUE))

		fun testOverflowPlus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) + FixDecMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro32U.from(b) }
		}

		fun testOverflowMinus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) - FixDecMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro32U.from(b) }
		}

		fun testOverflowPlus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) + FixDecMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro32U.from(b) }
		}

		fun testOverflowMinus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) - FixDecMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro32U.from(b) }
		}

		fun testOverflowPlus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) + FixDecMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro32U.from(b) }
		}

		fun testOverflowMinus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) - FixDecMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro32U.from(b) }
		}

		fun testOverflowPlus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) + FixDecMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro32U.from(b) }
		}

		fun testOverflowMinus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) - FixDecMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro32U.from(b) }
		}

		testOverflowPlus(0, 4295)
		testOverflowPlus(0L, 4295L)
		testOverflowPlus(0, 18455616)
		testOverflowPlus(0L, 18455616L)
		testOverflowMinus(0, 1)
		testOverflowMinus(0L, 1L)
		testOverflowMinus(0, 4)
		testOverflowMinus(0L, 4L)
		testOverflowPlus(1, 4294)
		testOverflowPlus(1L, 4294L)
		testOverflowPlus(1.0, 4294.0)
		testOverflowPlus(1, 18447025)
		testOverflowPlus(1L, 18447025L)
		testOverflowMinus(1, 2)
		testOverflowMinus(1L, 2L)
		testOverflowMinus(1.0f, 2.0f)
		testOverflowMinus(1.0, 2.0)
		testOverflowMinus(1, 9)
		testOverflowMinus(1L, 9L)
		testOverflowMinus(1.0f, 9.0f)
		testOverflowMinus(1.0, 9.0)
		testOverflowPlus(4294, 1)
		testOverflowPlus(4294L, 1L)
		testOverflowPlus(4294.0, 1.0)
		testOverflowPlus(4294, 4)
		testOverflowPlus(4294L, 4L)
		testOverflowPlus(4294.0, 4.0)
		testOverflowMinus(4294, 4295)
		testOverflowMinus(4294L, 4295L)
		testOverflowMinus(4294.0f, 4295.0f)
		testOverflowMinus(4294.0, 4295.0)
		testOverflowMinus(4294, 18455616)
		testOverflowMinus(4294L, 18455616L)
		testOverflowMinus(4294.0, 1.8455616E7)
		assertEquals(FixDecMicro32U.raw(4293970780u), FixDecMicro32U.raw(970780u) + 4293)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixDecMicro32U.raw(UInt.MAX_VALUE), 1 * FixDecMicro32U.raw(UInt.MAX_VALUE))
		assertEquals(FixDecMicro32U.raw(UInt.MAX_VALUE), FixDecMicro32U.raw(UInt.MAX_VALUE) / 1)
		assertThrows(FixedPointException::class.java) { -1 * FixDecMicro32U.ONE }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.ONE / -1 }
		assertThrows(FixedPointException::class.java) { -1 * FixDecMicro32U.raw(UInt.MAX_VALUE)}
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.raw(UInt.MAX_VALUE) / -1 }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixDecMicro32U.from(a * b), FixDecMicro32U.from(a) * FixDecMicro32U.from(b))
			assertEquals(FixDecMicro32U.from(a * b), FixDecMicro32U.from(a) * b)
			assertEquals(FixDecMicro32U.from(a * b), b * FixDecMicro32U.from(a))
			if (b != 0L) assertEquals(FixDecMicro32U.from(a), FixDecMicro32U.from(a * b) / b)
			if (a != 0L) assertEquals(FixDecMicro32U.from(b), FixDecMicro32U.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixDecMicro32U.from(b), FixDecMicro32U.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixDecMicro32U.from(a * b), FixDecMicro32U.from(a) * b.toInt())
				assertEquals(FixDecMicro32U.from(a * b), b.toInt() * FixDecMicro32U.from(a))
			}
		}
		testValues(0, 1493)
		testValues(1, 231)
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(42) * 1493 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(134) * 4294 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(165) * 1493 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(231) * 231 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(1493) * 1881 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(1881) * 1493 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(4294) * 1881 }
		assertEquals(FixDecMicro32U.raw(842554u), (FixDecMicro32U.raw(842554u) * FixDecMicro32U.raw(503127u)) / FixDecMicro32U.raw(503127u), FixDecMicro32U.raw(10000u))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixDecMicro32U.ZERO < FixDecMicro32U.ONE)
		assertTrue(0 < FixDecMicro32U.ONE)
		assertFalse(FixDecMicro32U.ZERO > FixDecMicro32U.ONE)
		assertFalse(0 > FixDecMicro32U.ONE)
		assertFalse(FixDecMicro32U.ONE < FixDecMicro32U.ONE)
		assertFalse(FixDecMicro32U.ONE < 1)
		assertFalse(FixDecMicro32U.ONE > FixDecMicro32U.ONE)
		assertTrue(FixDecMicro32U.ONE <= FixDecMicro32U.ONE)
		assertTrue(FixDecMicro32U.ONE >= FixDecMicro32U.ONE)
		assertTrue(FixDecMicro32U.raw(UInt.MIN_VALUE) < FixDecMicro32U.raw(UInt.MAX_VALUE))

		val minDelta = FixDecMicro32U.raw(1u)
		assertEquals(FixDecMicro32U.from(12), FixDecMicro32U.from(12))
		assertNotEquals(FixDecMicro32U.from(12), FixDecMicro32U.from(12) - minDelta)
		assertTrue(FixDecMicro32U.from(1.0E-6) < FixDecMicro32U.from(1.0E-6) + minDelta)
		assertTrue(1.0E-6 < FixDecMicro32U.from(1.0E-6) + minDelta)
		assertFalse(FixDecMicro32U.from(4.101245270674489E-4) < FixDecMicro32U.from(4.101245270674489E-4) - minDelta)
		assertFalse(4.101245270674489E-4 < FixDecMicro32U.from(4.101245270674489E-4) - minDelta)
		assertTrue(FixDecMicro32U.from(4.101245270674489E-4) < FixDecMicro32U.from(4.101245270674489E-4) + minDelta)
		assertTrue(4.101245270674489E-4 < FixDecMicro32U.from(4.101245270674489E-4) + minDelta)
		assertFalse(FixDecMicro32U.from(0.0014602674387652094) < FixDecMicro32U.from(0.0014602674387652094) - minDelta)
		assertFalse(0.0014602674387652094 < FixDecMicro32U.from(0.0014602674387652094) - minDelta)
		assertTrue(FixDecMicro32U.from(0.0014602674387652094) < FixDecMicro32U.from(0.0014602674387652094) + minDelta)
		assertTrue(0.0014602674387652094 < FixDecMicro32U.from(0.0014602674387652094) + minDelta)
		assertFalse(FixDecMicro32U.from(0.008314038763645868) < FixDecMicro32U.from(0.008314038763645868) - minDelta)
		assertFalse(0.008314038763645868 < FixDecMicro32U.from(0.008314038763645868) - minDelta)
		assertTrue(FixDecMicro32U.from(0.008314038763645868) < FixDecMicro32U.from(0.008314038763645868) + minDelta)
		assertTrue(0.008314038763645868 < FixDecMicro32U.from(0.008314038763645868) + minDelta)
		assertFalse(FixDecMicro32U.from(0.06313960308834927) < FixDecMicro32U.from(0.06313960308834927) - minDelta)
		assertFalse(0.06313960308834927 < FixDecMicro32U.from(0.06313960308834927) - minDelta)
		assertTrue(FixDecMicro32U.from(0.06313960308834927) < FixDecMicro32U.from(0.06313960308834927) + minDelta)
		assertTrue(0.06313960308834927 < FixDecMicro32U.from(0.06313960308834927) + minDelta)
		assertFalse(FixDecMicro32U.from(0.18938331340622416) < FixDecMicro32U.from(0.18938331340622416) - minDelta)
		assertFalse(0.18938331340622416 < FixDecMicro32U.from(0.18938331340622416) - minDelta)
		assertTrue(FixDecMicro32U.from(0.18938331340622416) < FixDecMicro32U.from(0.18938331340622416) + minDelta)
		assertTrue(0.18938331340622416 < FixDecMicro32U.from(0.18938331340622416) + minDelta)
		assertFalse(FixDecMicro32U.from(2.75487661886147) < FixDecMicro32U.from(2.75487661886147) - minDelta)
		assertFalse(2.75487661886147 < FixDecMicro32U.from(2.75487661886147) - minDelta)
		assertTrue(FixDecMicro32U.from(2.75487661886147) < FixDecMicro32U.from(2.75487661886147) + minDelta)
		assertTrue(2.75487661886147 < FixDecMicro32U.from(2.75487661886147) + minDelta)
		assertFalse(FixDecMicro32U.from(10.653674356901822) < FixDecMicro32U.from(10.653674356901822) - minDelta)
		assertFalse(10.653674356901822 < FixDecMicro32U.from(10.653674356901822) - minDelta)
		assertTrue(FixDecMicro32U.from(10.653674356901822) < FixDecMicro32U.from(10.653674356901822) + minDelta)
		assertTrue(10.653674356901822 < FixDecMicro32U.from(10.653674356901822) + minDelta)
		assertFalse(FixDecMicro32U.from(150.2659312211488) < FixDecMicro32U.from(150.2659312211488) - minDelta)
		assertFalse(150.2659312211488 < FixDecMicro32U.from(150.2659312211488) - minDelta)
		assertTrue(FixDecMicro32U.from(150.2659312211488) < FixDecMicro32U.from(150.2659312211488) + minDelta)
		assertTrue(150.2659312211488 < FixDecMicro32U.from(150.2659312211488) + minDelta)
		assertFalse(FixDecMicro32U.from(4294.9672949999995) < FixDecMicro32U.from(4294.9672949999995) - minDelta)
		assertFalse(4294.9672949999995 < FixDecMicro32U.from(4294.9672949999995) - minDelta)
		assertTrue(FixDecMicro32U.raw(UInt.MAX_VALUE) >= 4294)
		assertTrue(FixDecMicro32U.raw(UInt.MAX_VALUE) > 4294)
		assertTrue(FixDecMicro32U.raw(UInt.MAX_VALUE) < 4295)
		assertTrue(FixDecMicro32U.raw(UInt.MAX_VALUE) < 4299.294999999999)
		assertTrue(FixDecMicro32U.raw(UInt.MAX_VALUE) < UInt.MAX_VALUE)
		assertTrue(FixDecMicro32U.raw(UInt.MAX_VALUE) < UInt.MAX_VALUE.toFloat())
		assertTrue(FixDecMicro32U.raw(UInt.MAX_VALUE) < UInt.MAX_VALUE.toDouble())
		assertTrue(FixDecMicro32U.ZERO > -1)
		assertTrue(FixDecMicro32U.ZERO > -0.001f)
		assertTrue(FixDecMicro32U.ZERO > -0.001)
		assertTrue(FixDecMicro32U.ZERO > Long.MIN_VALUE)
		assertTrue(FixDecMicro32U.ZERO > Long.MIN_VALUE.toFloat())
		assertTrue(FixDecMicro32U.ZERO > Long.MIN_VALUE.toDouble())
	}
}
