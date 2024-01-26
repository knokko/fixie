package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixMicro32U {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixMicro32U.from(value).toInt())
		testValue(0)
		testValue(1)
		testValue(1042)
		testValue(4095)
	}

	@Test
	fun testLongConversion() {
		fun testValue(value: Long) = assertEquals(value, FixMicro32U.from(value).toLong())
		testValue(0)
		testValue(1)
		testValue(1042)
		testValue(4095)
	}

	@Test
	fun testFloatConversion() {
		val delta = 1.9073486E-6f
		assertEquals(9.536743E-7f, FixMicro32U.from(9.536743E-7f).toFloat(), delta)
		assertEquals(5.7965175E-5f, FixMicro32U.from(5.7965175E-5f).toFloat(), delta)
		assertEquals(0.0030845916f, FixMicro32U.from(0.0030845916f).toFloat(), delta)
		assertEquals(0.009314704f, FixMicro32U.from(0.009314704f).toFloat(), delta)
		assertEquals(0.5088087f, FixMicro32U.from(0.5088087f).toFloat(), delta)
		assertEquals(14.094133f, FixMicro32U.from(14.094133f).toFloat(), delta)
		assertEquals(55.274475f, FixMicro32U.from(55.274475f).toFloat(), delta)
		assertEquals(4095.9998f, FixMicro32U.from(4095.9998f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 1.9073486328125E-6
		assertEquals(9.5367431640625E-7, FixMicro32U.from(9.5367431640625E-7).toDouble(), delta)
		assertEquals(5.7965173783582514E-5, FixMicro32U.from(5.7965173783582514E-5).toDouble(), delta)
		assertEquals(0.003084591527158651, FixMicro32U.from(0.003084591527158651).toDouble(), delta)
		assertEquals(0.009314703658805575, FixMicro32U.from(0.009314703658805575).toDouble(), delta)
		assertEquals(0.5088086753629321, FixMicro32U.from(0.5088086753629321).toDouble(), delta)
		assertEquals(14.094133688218953, FixMicro32U.from(14.094133688218953).toDouble(), delta)
		assertEquals(55.274475303202365, FixMicro32U.from(55.274475303202365).toDouble(), delta)
		assertEquals(4095.9999990463257, FixMicro32U.from(4095.9999990463257).toDouble(), delta)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixMicro32U, b: FixMicro32U, c: FixMicro32U) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}
		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixMicro32U.from(a), FixMicro32U.from(b), FixMicro32U.from(c))
			assertEquals(FixMicro32U.from(c), FixMicro32U.from(a) + b)
			assertEquals(FixMicro32U.from(c), b + FixMicro32U.from(a))
			assertEquals(FixMicro32U.from(a), FixMicro32U.from(c) - b)
			assertEquals(FixMicro32U.from(b), c - FixMicro32U.from(a))
		}
		testValues(FixMicro32U.raw(UInt.MIN_VALUE), FixMicro32U.ONE, FixMicro32U.raw(UInt.MIN_VALUE + 1048576u))
		testValues(0, 422, 422)
		testValues(1, 854, 855)
		testValues(2047, 143, 2190)
		testValues(FixMicro32U.raw(UInt.MAX_VALUE - 1048576u), FixMicro32U.ONE, FixMicro32U.raw(UInt.MAX_VALUE))
	}

	@Test
	fun testMultiplication() {
		assertEquals(FixMicro32U.raw(UInt.MAX_VALUE), 1 * FixMicro32U.raw(UInt.MAX_VALUE))
		assertThrows(FixedPointException::class.java) { -1 * FixMicro32U.ONE }
		assertThrows(FixedPointException::class.java) { -1 * FixMicro32U.raw(UInt.MAX_VALUE)}

		fun testValues(a: Long, b: Long) {
			assertEquals(FixMicro32U.from(a * b), FixMicro32U.from(a) * FixMicro32U.from(b))
			assertEquals(FixMicro32U.from(a * b), FixMicro32U.from(a) * b)
			assertEquals(FixMicro32U.from(a * b), b * FixMicro32U.from(a))
		}
		testValues(0, 1493)
		testValues(1, 1493)
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(42) * 1493 }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(134) * 1493 }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(165) * 1493 }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(231) * 1493 }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(1493) * 1493 }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(1881) * 1493 }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(4095) * 1493 }
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixMicro32U.ZERO < FixMicro32U.ONE)
		assertFalse(FixMicro32U.ZERO > FixMicro32U.ONE)
		assertFalse(FixMicro32U.ONE < FixMicro32U.ONE)
		assertFalse(FixMicro32U.ONE > FixMicro32U.ONE)
		assertTrue(FixMicro32U.ONE <= FixMicro32U.ONE)
		assertTrue(FixMicro32U.ONE >= FixMicro32U.ONE)
		assertTrue(FixMicro32U.raw(UInt.MIN_VALUE) < FixMicro32U.raw(UInt.MAX_VALUE))

		val minDelta = FixMicro32U.raw(1u)
		assertEquals(FixMicro32U.from(12), FixMicro32U.from(12))
		assertNotEquals(FixMicro32U.from(12), FixMicro32U.from(12) - minDelta)
		assertTrue(FixMicro32U.from(9.5367431640625E-7) < FixMicro32U.from(9.5367431640625E-7) + minDelta)
		assertFalse(FixMicro32U.from(3.911252279924859E-4) < FixMicro32U.from(3.911252279924859E-4) - minDelta)
		assertTrue(FixMicro32U.from(3.911252279924859E-4) < FixMicro32U.from(3.911252279924859E-4) + minDelta)
		assertFalse(FixMicro32U.from(0.0013926195514347168) < FixMicro32U.from(0.0013926195514347168) - minDelta)
		assertTrue(FixMicro32U.from(0.0013926195514347168) < FixMicro32U.from(0.0013926195514347168) + minDelta)
		assertFalse(FixMicro32U.from(0.007928885234495038) < FixMicro32U.from(0.007928885234495038) - minDelta)
		assertTrue(FixMicro32U.from(0.007928885234495038) < FixMicro32U.from(0.007928885234495038) + minDelta)
		assertFalse(FixMicro32U.from(0.060214617813443456) < FixMicro32U.from(0.060214617813443456) - minDelta)
		assertTrue(FixMicro32U.from(0.060214617813443456) < FixMicro32U.from(0.060214617813443456) + minDelta)
		assertFalse(FixMicro32U.from(0.18061000195143148) < FixMicro32U.from(0.18061000195143148) - minDelta)
		assertTrue(FixMicro32U.from(0.18061000195143148) < FixMicro32U.from(0.18061000195143148) + minDelta)
		assertFalse(FixMicro32U.from(2.6272550762762745) < FixMicro32U.from(2.6272550762762745) - minDelta)
		assertTrue(FixMicro32U.from(2.6272550762762745) < FixMicro32U.from(2.6272550762762745) + minDelta)
		assertFalse(FixMicro32U.from(10.160135609533143) < FixMicro32U.from(10.160135609533143) - minDelta)
		assertTrue(FixMicro32U.from(10.160135609533143) < FixMicro32U.from(10.160135609533143) + minDelta)
		assertFalse(FixMicro32U.from(143.3047592364777) < FixMicro32U.from(143.3047592364777) - minDelta)
		assertTrue(FixMicro32U.from(143.3047592364777) < FixMicro32U.from(143.3047592364777) + minDelta)
		assertFalse(FixMicro32U.from(4095.9999990463257) < FixMicro32U.from(4095.9999990463257) - minDelta)
	}
}
