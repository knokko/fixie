package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixDecMicro32U {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixDecMicro32U.from(value).toInt())
		testValue(0)
		testValue(1)
		testValue(1042)
		testValue(4294)
	}

	@Test
	fun testLongConversion() {
		fun testValue(value: Long) = assertEquals(value, FixDecMicro32U.from(value).toLong())
		testValue(0)
		testValue(1)
		testValue(1042)
		testValue(4294)
	}

	@Test
	fun testFloatConversion() {
		val delta = 2.0E-6f
		assertEquals(1.0E-6f, FixDecMicro32U.from(1.0E-6f).toFloat(), delta)
		assertEquals(6.078089E-5f, FixDecMicro32U.from(6.078089E-5f).toFloat(), delta)
		assertEquals(0.0032344286f, FixDecMicro32U.from(0.0032344286f).toFloat(), delta)
		assertEquals(0.009767175f, FixDecMicro32U.from(0.009767175f).toFloat(), delta)
		assertEquals(0.5335246f, FixDecMicro32U.from(0.5335246f).toFloat(), delta)
		assertEquals(14.77877f, FixDecMicro32U.from(14.77877f).toFloat(), delta)
		assertEquals(57.959488f, FixDecMicro32U.from(57.959488f).toFloat(), delta)
		assertEquals(4294.9673f, FixDecMicro32U.from(4294.9673f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 2.0E-6
		assertEquals(1.0E-6, FixDecMicro32U.from(1.0E-6).toDouble(), delta)
		assertEquals(6.0780890065293825E-5, FixDecMicro32U.from(6.0780890065293825E-5).toDouble(), delta)
		assertEquals(0.0032344286451819098, FixDecMicro32U.from(0.0032344286451819098).toDouble(), delta)
		assertEquals(0.009767174703735716, FixDecMicro32U.from(0.009767174703735716).toDouble(), delta)
		assertEquals(0.5335245655773619, FixDecMicro32U.from(0.5335245655773619).toDouble(), delta)
		assertEquals(14.778770326257877, FixDecMicro32U.from(14.778770326257877).toDouble(), delta)
		assertEquals(57.95948821553071, FixDecMicro32U.from(57.95948821553071).toDouble(), delta)
		assertEquals(4294.9672949999995, FixDecMicro32U.from(4294.9672949999995).toDouble(), delta)
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
		testValues(2147, 1634, 3781)
		testValues(FixDecMicro32U.raw(UInt.MAX_VALUE - 1000000u), FixDecMicro32U.ONE, FixDecMicro32U.raw(UInt.MAX_VALUE))
	}

	@Test
	fun testMultiplication() {
		assertEquals(FixDecMicro32U.raw(UInt.MAX_VALUE), 1 * FixDecMicro32U.raw(UInt.MAX_VALUE))
		assertThrows(FixedPointException::class.java) { -1 * FixDecMicro32U.ONE }
		assertThrows(FixedPointException::class.java) { -1 * FixDecMicro32U.raw(UInt.MAX_VALUE)}

		fun testValues(a: Long, b: Long) {
			assertEquals(FixDecMicro32U.from(a * b), FixDecMicro32U.from(a) * FixDecMicro32U.from(b))
			assertEquals(FixDecMicro32U.from(a * b), FixDecMicro32U.from(a) * b)
			assertEquals(FixDecMicro32U.from(a * b), b * FixDecMicro32U.from(a))
		}
		testValues(0, 1493)
		testValues(1, 1493)
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(42) * 1493 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(134) * 1493 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(165) * 1493 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(231) * 1493 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(1493) * 1493 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(1881) * 1493 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32U.from(4294) * 1493 }
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixDecMicro32U.ZERO < FixDecMicro32U.ONE)
		assertFalse(FixDecMicro32U.ZERO > FixDecMicro32U.ONE)
		assertFalse(FixDecMicro32U.ONE < FixDecMicro32U.ONE)
		assertFalse(FixDecMicro32U.ONE > FixDecMicro32U.ONE)
		assertTrue(FixDecMicro32U.ONE <= FixDecMicro32U.ONE)
		assertTrue(FixDecMicro32U.ONE >= FixDecMicro32U.ONE)
		assertTrue(FixDecMicro32U.raw(UInt.MIN_VALUE) < FixDecMicro32U.raw(UInt.MAX_VALUE))

		val minDelta = FixDecMicro32U.raw(1u)
		assertEquals(FixDecMicro32U.from(12), FixDecMicro32U.from(12))
		assertNotEquals(FixDecMicro32U.from(12), FixDecMicro32U.from(12) - minDelta)
		assertTrue(FixDecMicro32U.from(1.0E-6) < FixDecMicro32U.from(1.0E-6) + minDelta)
		assertFalse(FixDecMicro32U.from(4.101245270674489E-4) < FixDecMicro32U.from(4.101245270674489E-4) - minDelta)
		assertTrue(FixDecMicro32U.from(4.101245270674489E-4) < FixDecMicro32U.from(4.101245270674489E-4) + minDelta)
		assertFalse(FixDecMicro32U.from(0.0014602674387652094) < FixDecMicro32U.from(0.0014602674387652094) - minDelta)
		assertTrue(FixDecMicro32U.from(0.0014602674387652094) < FixDecMicro32U.from(0.0014602674387652094) + minDelta)
		assertFalse(FixDecMicro32U.from(0.008314038763645868) < FixDecMicro32U.from(0.008314038763645868) - minDelta)
		assertTrue(FixDecMicro32U.from(0.008314038763645868) < FixDecMicro32U.from(0.008314038763645868) + minDelta)
		assertFalse(FixDecMicro32U.from(0.06313960308834927) < FixDecMicro32U.from(0.06313960308834927) - minDelta)
		assertTrue(FixDecMicro32U.from(0.06313960308834927) < FixDecMicro32U.from(0.06313960308834927) + minDelta)
		assertFalse(FixDecMicro32U.from(0.18938331340622416) < FixDecMicro32U.from(0.18938331340622416) - minDelta)
		assertTrue(FixDecMicro32U.from(0.18938331340622416) < FixDecMicro32U.from(0.18938331340622416) + minDelta)
		assertFalse(FixDecMicro32U.from(2.75487661886147) < FixDecMicro32U.from(2.75487661886147) - minDelta)
		assertTrue(FixDecMicro32U.from(2.75487661886147) < FixDecMicro32U.from(2.75487661886147) + minDelta)
		assertFalse(FixDecMicro32U.from(10.653674356901822) < FixDecMicro32U.from(10.653674356901822) - minDelta)
		assertTrue(FixDecMicro32U.from(10.653674356901822) < FixDecMicro32U.from(10.653674356901822) + minDelta)
		assertFalse(FixDecMicro32U.from(150.2659312211488) < FixDecMicro32U.from(150.2659312211488) - minDelta)
		assertTrue(FixDecMicro32U.from(150.2659312211488) < FixDecMicro32U.from(150.2659312211488) + minDelta)
		assertFalse(FixDecMicro32U.from(4294.9672949999995) < FixDecMicro32U.from(4294.9672949999995) - minDelta)
	}
}
