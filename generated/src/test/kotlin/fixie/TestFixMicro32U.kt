package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixMicro32U {

	fun assertEquals(a: FixMicro32U, b: FixMicro32U, maxDelta: FixMicro32U) {
		val rawDifference = a.raw.toInt() - b.raw.toInt()
		if (rawDifference.absoluteValue > maxDelta.raw.toInt()) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixMicro32U.ZERO.toString())
		assertEquals("1", FixMicro32U.ONE.toString())
		assertTrue((FixMicro32U.ONE / 3).toString().startsWith("0.33"))
		assertTrue((FixMicro32U.from(4094) + FixMicro32U.ONE / 3).toString().endsWith((FixMicro32U.ONE / 3).toString().substring(1)))
		assertEquals("0.0625", (FixMicro32U.ONE / 16).toString())
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixMicro32U.ONE, FixMicro32U.from(one))

		fun testValue(value: Int) = assertEquals(value, FixMicro32U.from(value).toInt())
		fun testOverflow(value: Int) = assertThrows(FixedPointException::class.java) { FixMicro32U.from(value) }
		testValue(0)
		testValue(1)
		testValue(1042)
		testValue(4095)

		testOverflow(-2147483648)
		testOverflow(-978689185)
		testOverflow(-974146)
		testOverflow(-740009)
		testOverflow(-16299)
		testOverflow(-1)

		testOverflow(4096)
		testOverflow(5167678)
		testOverflow(165027427)
		testOverflow(2147483647)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixMicro32U.ONE, FixMicro32U.from(one))

		fun testValue(value: Long) = assertEquals(value, FixMicro32U.from(value).toLong())
		fun testOverflow(value: Long) = assertThrows(FixedPointException::class.java) { FixMicro32U.from(value) }
		testValue(0)
		testValue(1)
		testValue(1042)
		testValue(4095)

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

		testOverflow(4096)
		testOverflow(5167678)
		testOverflow(165027427)
		testOverflow(10373374268)
		testOverflow(206902383502)
		testOverflow(19506336530618)
		testOverflow(735958579550397)
		testOverflow(2933171691147347)
		testOverflow(599687123308051043)
		testOverflow(9223372036854775807)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixMicro32U.ONE, FixMicro32U.from(1f))
		val delta = 1.9073486E-6f
		assertEquals(9.536743E-7f, FixMicro32U.from(9.536743E-7f).toFloat(), delta)
		assertEquals(5.7965175E-5f, FixMicro32U.from(5.7965175E-5f).toFloat(), delta)
		assertEquals(0.0030845916f, FixMicro32U.from(0.0030845916f).toFloat(), delta)
		assertEquals(0.009314704f, FixMicro32U.from(0.009314704f).toFloat(), delta)
		assertEquals(0.5088087f, FixMicro32U.from(0.5088087f).toFloat(), delta)
		assertEquals(14.094133f, FixMicro32U.from(14.094133f).toFloat(), delta)
		assertEquals(55.274475f, FixMicro32U.from(55.274475f).toFloat(), delta)
		assertEquals(4095.9998f, FixMicro32U.from(4095.9998f).toFloat(), delta)

		assertThrows(FixedPointException::class.java) { FixMicro32U.from(4097.0f) }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(-4097.0f) }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(415067.6f) }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(-415067.6f) }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(1.6777216E7f) }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(-1.6777216E7f) }
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixMicro32U.ONE, FixMicro32U.from(1.0))
		val delta = 1.9073486328125E-6
		assertEquals(9.5367431640625E-7, FixMicro32U.from(9.5367431640625E-7).toDouble(), delta)
		assertEquals(5.7965173783582514E-5, FixMicro32U.from(5.7965173783582514E-5).toDouble(), delta)
		assertEquals(0.003084591527158651, FixMicro32U.from(0.003084591527158651).toDouble(), delta)
		assertEquals(0.009314703658805575, FixMicro32U.from(0.009314703658805575).toDouble(), delta)
		assertEquals(0.5088086753629321, FixMicro32U.from(0.5088086753629321).toDouble(), delta)
		assertEquals(14.094133688218953, FixMicro32U.from(14.094133688218953).toDouble(), delta)
		assertEquals(55.274475303202365, FixMicro32U.from(55.274475303202365).toDouble(), delta)
		assertEquals(4095.9999990463257, FixMicro32U.from(4095.9999990463257).toDouble(), delta)

		assertThrows(FixedPointException::class.java) { FixMicro32U.from(4096.999999046326) }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(-4096.999999046326) }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(415067.57820295414) }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(-415067.57820295414) }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(1.67772159921875E7) }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(-1.67772159921875E7) }
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
		testValues(0, 3601, 3601)
		testValues(1, 2004, 2005)
		testValues(86, 832, 918)
		testValues(706, 102, 808)
		testValues(1177, 1674, 2851)
		testValues(2168, 267, 2435)
		testValues(4095, 0, 4095)
		testValues(FixMicro32U.raw(UInt.MAX_VALUE - 1048576u), FixMicro32U.ONE, FixMicro32U.raw(UInt.MAX_VALUE))

		fun testOverflowPlus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) + FixMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro32U.from(b) }
		}

		fun testOverflowMinus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) - FixMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro32U.from(b) }
		}

		fun testOverflowPlus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) + FixMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro32U.from(b) }
		}

		fun testOverflowMinus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) - FixMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro32U.from(b) }
		}

		fun testOverflowPlus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) + FixMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro32U.from(b) }
		}

		fun testOverflowMinus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) - FixMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro32U.from(b) }
		}

		fun testOverflowPlus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) + FixMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro32U.from(b) }
		}

		fun testOverflowMinus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) - FixMicro32U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro32U.from(b) }
		}

		testOverflowPlus(0, 4096)
		testOverflowPlus(0L, 4096L)
		testOverflowPlus(0, 16785409)
		testOverflowPlus(0L, 16785409L)
		testOverflowMinus(0, 1)
		testOverflowMinus(0L, 1L)
		testOverflowMinus(0, 4)
		testOverflowMinus(0L, 4L)
		testOverflowPlus(1, 4095)
		testOverflowPlus(1L, 4095L)
		testOverflowPlus(1.0, 4095.0)
		testOverflowPlus(1, 16777216)
		testOverflowPlus(1L, 16777216L)
		testOverflowMinus(1, 2)
		testOverflowMinus(1L, 2L)
		testOverflowMinus(1.0f, 2.0f)
		testOverflowMinus(1.0, 2.0)
		testOverflowMinus(1, 9)
		testOverflowMinus(1L, 9L)
		testOverflowMinus(1.0f, 9.0f)
		testOverflowMinus(1.0, 9.0)
		testOverflowPlus(4095, 1)
		testOverflowPlus(4095L, 1L)
		testOverflowPlus(4095.0, 1.0)
		testOverflowPlus(4095, 4)
		testOverflowPlus(4095L, 4L)
		testOverflowPlus(4095.0, 4.0)
		testOverflowMinus(4095, 4096)
		testOverflowMinus(4095L, 4096L)
		testOverflowMinus(4095.0f, 4096.0f)
		testOverflowMinus(4095.0, 4096.0)
		testOverflowMinus(4095, 16785409)
		testOverflowMinus(4095L, 16785409L)
		testOverflowMinus(4095.0, 1.6785409E7)
		assertEquals(FixMicro32U.raw(4293823335u), FixMicro32U.raw(953191u) + 4094)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixMicro32U.raw(UInt.MAX_VALUE), 1 * FixMicro32U.raw(UInt.MAX_VALUE))
		assertEquals(FixMicro32U.raw(UInt.MAX_VALUE), FixMicro32U.raw(UInt.MAX_VALUE) / 1)
		assertThrows(FixedPointException::class.java) { -1 * FixMicro32U.ONE }
		assertThrows(FixedPointException::class.java) { FixMicro32U.ONE / -1 }
		assertThrows(FixedPointException::class.java) { -1 * FixMicro32U.raw(UInt.MAX_VALUE)}
		assertThrows(FixedPointException::class.java) { FixMicro32U.raw(UInt.MAX_VALUE) / -1 }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixMicro32U.from(a * b), FixMicro32U.from(a) * FixMicro32U.from(b))
			assertEquals(FixMicro32U.from(a * b), FixMicro32U.from(a) * b)
			assertEquals(FixMicro32U.from(a * b), b * FixMicro32U.from(a))
			if (b != 0L) assertEquals(FixMicro32U.from(a), FixMicro32U.from(a * b) / b)
			if (a != 0L) assertEquals(FixMicro32U.from(b), FixMicro32U.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixMicro32U.from(b), FixMicro32U.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixMicro32U.from(a * b), FixMicro32U.from(a) * b.toInt())
				assertEquals(FixMicro32U.from(a * b), b.toInt() * FixMicro32U.from(a))
			}
		}
		testValues(0, 1493)
		testValues(1, 231)
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(42) * 1493 }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(134) * 4095 }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(165) * 1493 }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(231) * 231 }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(1493) * 1881 }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(1881) * 1493 }
		assertThrows(FixedPointException::class.java) { FixMicro32U.from(4095) * 1881 }
		assertEquals(FixMicro32U.raw(977198u), (FixMicro32U.raw(977198u) * FixMicro32U.raw(654241u)) / FixMicro32U.raw(654241u), FixMicro32U.raw(10485u))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixMicro32U.ZERO < FixMicro32U.ONE)
		assertTrue(0 < FixMicro32U.ONE)
		assertFalse(FixMicro32U.ZERO > FixMicro32U.ONE)
		assertFalse(0 > FixMicro32U.ONE)
		assertFalse(FixMicro32U.ONE < FixMicro32U.ONE)
		assertFalse(FixMicro32U.ONE < 1)
		assertFalse(FixMicro32U.ONE > FixMicro32U.ONE)
		assertTrue(FixMicro32U.ONE <= FixMicro32U.ONE)
		assertTrue(FixMicro32U.ONE >= FixMicro32U.ONE)
		assertTrue(FixMicro32U.raw(UInt.MIN_VALUE) < FixMicro32U.raw(UInt.MAX_VALUE))

		val minDelta = FixMicro32U.raw(1u)
		assertEquals(FixMicro32U.from(12), FixMicro32U.from(12))
		assertNotEquals(FixMicro32U.from(12), FixMicro32U.from(12) - minDelta)
		assertTrue(FixMicro32U.from(9.5367431640625E-7) < FixMicro32U.from(9.5367431640625E-7) + minDelta)
		assertTrue(9.5367431640625E-7 < FixMicro32U.from(9.5367431640625E-7) + minDelta)
		assertFalse(FixMicro32U.from(3.911252279924859E-4) < FixMicro32U.from(3.911252279924859E-4) - minDelta)
		assertFalse(3.911252279924859E-4 < FixMicro32U.from(3.911252279924859E-4) - minDelta)
		assertTrue(FixMicro32U.from(3.911252279924859E-4) < FixMicro32U.from(3.911252279924859E-4) + minDelta)
		assertTrue(3.911252279924859E-4 < FixMicro32U.from(3.911252279924859E-4) + minDelta)
		assertFalse(FixMicro32U.from(0.0013926195514347168) < FixMicro32U.from(0.0013926195514347168) - minDelta)
		assertFalse(0.0013926195514347168 < FixMicro32U.from(0.0013926195514347168) - minDelta)
		assertTrue(FixMicro32U.from(0.0013926195514347168) < FixMicro32U.from(0.0013926195514347168) + minDelta)
		assertTrue(0.0013926195514347168 < FixMicro32U.from(0.0013926195514347168) + minDelta)
		assertFalse(FixMicro32U.from(0.007928885234495038) < FixMicro32U.from(0.007928885234495038) - minDelta)
		assertFalse(0.007928885234495038 < FixMicro32U.from(0.007928885234495038) - minDelta)
		assertTrue(FixMicro32U.from(0.007928885234495038) < FixMicro32U.from(0.007928885234495038) + minDelta)
		assertTrue(0.007928885234495038 < FixMicro32U.from(0.007928885234495038) + minDelta)
		assertFalse(FixMicro32U.from(0.060214617813443456) < FixMicro32U.from(0.060214617813443456) - minDelta)
		assertFalse(0.060214617813443456 < FixMicro32U.from(0.060214617813443456) - minDelta)
		assertTrue(FixMicro32U.from(0.060214617813443456) < FixMicro32U.from(0.060214617813443456) + minDelta)
		assertTrue(0.060214617813443456 < FixMicro32U.from(0.060214617813443456) + minDelta)
		assertFalse(FixMicro32U.from(0.18061000195143148) < FixMicro32U.from(0.18061000195143148) - minDelta)
		assertFalse(0.18061000195143148 < FixMicro32U.from(0.18061000195143148) - minDelta)
		assertTrue(FixMicro32U.from(0.18061000195143148) < FixMicro32U.from(0.18061000195143148) + minDelta)
		assertTrue(0.18061000195143148 < FixMicro32U.from(0.18061000195143148) + minDelta)
		assertFalse(FixMicro32U.from(2.6272550762762745) < FixMicro32U.from(2.6272550762762745) - minDelta)
		assertFalse(2.6272550762762745 < FixMicro32U.from(2.6272550762762745) - minDelta)
		assertTrue(FixMicro32U.from(2.6272550762762745) < FixMicro32U.from(2.6272550762762745) + minDelta)
		assertTrue(2.6272550762762745 < FixMicro32U.from(2.6272550762762745) + minDelta)
		assertFalse(FixMicro32U.from(10.160135609533143) < FixMicro32U.from(10.160135609533143) - minDelta)
		assertFalse(10.160135609533143 < FixMicro32U.from(10.160135609533143) - minDelta)
		assertTrue(FixMicro32U.from(10.160135609533143) < FixMicro32U.from(10.160135609533143) + minDelta)
		assertTrue(10.160135609533143 < FixMicro32U.from(10.160135609533143) + minDelta)
		assertFalse(FixMicro32U.from(143.3047592364777) < FixMicro32U.from(143.3047592364777) - minDelta)
		assertFalse(143.3047592364777 < FixMicro32U.from(143.3047592364777) - minDelta)
		assertTrue(FixMicro32U.from(143.3047592364777) < FixMicro32U.from(143.3047592364777) + minDelta)
		assertTrue(143.3047592364777 < FixMicro32U.from(143.3047592364777) + minDelta)
		assertFalse(FixMicro32U.from(4095.9999990463257) < FixMicro32U.from(4095.9999990463257) - minDelta)
		assertFalse(4095.9999990463257 < FixMicro32U.from(4095.9999990463257) - minDelta)
		assertTrue(FixMicro32U.raw(UInt.MAX_VALUE) >= 4095)
		assertTrue(FixMicro32U.raw(UInt.MAX_VALUE) > 4095)
		assertTrue(FixMicro32U.raw(UInt.MAX_VALUE) < 4096)
		assertTrue(FixMicro32U.raw(UInt.MAX_VALUE) < 4100.096)
		assertTrue(FixMicro32U.raw(UInt.MAX_VALUE) < UInt.MAX_VALUE)
		assertTrue(FixMicro32U.raw(UInt.MAX_VALUE) < UInt.MAX_VALUE.toFloat())
		assertTrue(FixMicro32U.raw(UInt.MAX_VALUE) < UInt.MAX_VALUE.toDouble())
		assertTrue(FixMicro32U.ZERO > -1)
		assertTrue(FixMicro32U.ZERO > -0.001f)
		assertTrue(FixMicro32U.ZERO > -0.001)
		assertTrue(FixMicro32U.ZERO > Long.MIN_VALUE)
		assertTrue(FixMicro32U.ZERO > Long.MIN_VALUE.toFloat())
		assertTrue(FixMicro32U.ZERO > Long.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixMicro32U.Array(2) { FixMicro32U.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixMicro32U.ONE, testArray[0])
		assertEquals(FixMicro32U.ONE, testArray[1])
		testArray[1] = FixMicro32U.ZERO
		assertEquals(FixMicro32U.ONE, testArray[0])
		assertEquals(FixMicro32U.ZERO, testArray[1])
		testArray.fill(FixMicro32U.ZERO)
		assertEquals(FixMicro32U.ZERO, testArray[0])
		assertEquals(FixMicro32U.ZERO, testArray[1])
	}

	@Test
	fun testMinMax() {
		assertEquals(FixMicro32U.ZERO, min(FixMicro32U.ZERO, FixMicro32U.ZERO))
		assertEquals(FixMicro32U.ZERO, max(FixMicro32U.ZERO, FixMicro32U.ZERO))
		assertEquals(FixMicro32U.ZERO, min(FixMicro32U.ONE, FixMicro32U.ZERO))
		assertEquals(FixMicro32U.ONE, max(FixMicro32U.ONE, FixMicro32U.ZERO))
		assertEquals(FixMicro32U.ZERO, min(FixMicro32U.ZERO, FixMicro32U.ONE))
		assertEquals(FixMicro32U.ONE, max(FixMicro32U.ZERO, FixMicro32U.ONE))
		assertEquals(FixMicro32U.ZERO, min(FixMicro32U.ZERO, FixMicro32U.raw(UInt.MAX_VALUE)))
		assertEquals(FixMicro32U.raw(UInt.MAX_VALUE), max(FixMicro32U.ZERO, FixMicro32U.raw(UInt.MAX_VALUE)))
	}
}
