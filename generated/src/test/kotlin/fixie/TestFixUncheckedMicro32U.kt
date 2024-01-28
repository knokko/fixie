package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixUncheckedMicro32U {

	fun assertEquals(a: FixUncheckedMicro32U, b: FixUncheckedMicro32U, maxDelta: FixUncheckedMicro32U) {
		val rawDifference = a.raw.toInt() - b.raw.toInt()
		if (rawDifference.absoluteValue > maxDelta.raw.toInt()) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixUncheckedMicro32U.ZERO.toString())
		assertEquals("1", FixUncheckedMicro32U.ONE.toString())
		assertTrue((FixUncheckedMicro32U.ONE / 3).toString().startsWith("0.33"))
		assertTrue((FixUncheckedMicro32U.from(4094) + FixUncheckedMicro32U.ONE / 3).toString().endsWith((FixUncheckedMicro32U.ONE / 3).toString().substring(1)))
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixUncheckedMicro32U.ONE, FixUncheckedMicro32U.from(one))

		fun testValue(value: Int) = assertEquals(value, FixUncheckedMicro32U.from(value).toInt())
		testValue(0)
		testValue(1)
		testValue(1042)
		testValue(4095)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixUncheckedMicro32U.ONE, FixUncheckedMicro32U.from(one))

		fun testValue(value: Long) = assertEquals(value, FixUncheckedMicro32U.from(value).toLong())
		testValue(0)
		testValue(1)
		testValue(1042)
		testValue(4095)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixUncheckedMicro32U.ONE, FixUncheckedMicro32U.from(1f))
		val delta = 1.9073486E-6f
		assertEquals(9.536743E-7f, FixUncheckedMicro32U.from(9.536743E-7f).toFloat(), delta)
		assertEquals(5.7965175E-5f, FixUncheckedMicro32U.from(5.7965175E-5f).toFloat(), delta)
		assertEquals(0.0030845916f, FixUncheckedMicro32U.from(0.0030845916f).toFloat(), delta)
		assertEquals(0.009314704f, FixUncheckedMicro32U.from(0.009314704f).toFloat(), delta)
		assertEquals(0.5088087f, FixUncheckedMicro32U.from(0.5088087f).toFloat(), delta)
		assertEquals(14.094133f, FixUncheckedMicro32U.from(14.094133f).toFloat(), delta)
		assertEquals(55.274475f, FixUncheckedMicro32U.from(55.274475f).toFloat(), delta)
		assertEquals(4095.9998f, FixUncheckedMicro32U.from(4095.9998f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixUncheckedMicro32U.ONE, FixUncheckedMicro32U.from(1.0))
		val delta = 1.9073486328125E-6
		assertEquals(9.5367431640625E-7, FixUncheckedMicro32U.from(9.5367431640625E-7).toDouble(), delta)
		assertEquals(5.7965173783582514E-5, FixUncheckedMicro32U.from(5.7965173783582514E-5).toDouble(), delta)
		assertEquals(0.003084591527158651, FixUncheckedMicro32U.from(0.003084591527158651).toDouble(), delta)
		assertEquals(0.009314703658805575, FixUncheckedMicro32U.from(0.009314703658805575).toDouble(), delta)
		assertEquals(0.5088086753629321, FixUncheckedMicro32U.from(0.5088086753629321).toDouble(), delta)
		assertEquals(14.094133688218953, FixUncheckedMicro32U.from(14.094133688218953).toDouble(), delta)
		assertEquals(55.274475303202365, FixUncheckedMicro32U.from(55.274475303202365).toDouble(), delta)
		assertEquals(4095.9999990463257, FixUncheckedMicro32U.from(4095.9999990463257).toDouble(), delta)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixUncheckedMicro32U, b: FixUncheckedMicro32U, c: FixUncheckedMicro32U) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}

		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixUncheckedMicro32U.from(a), FixUncheckedMicro32U.from(b), FixUncheckedMicro32U.from(c))
			assertEquals(FixUncheckedMicro32U.from(c), FixUncheckedMicro32U.from(a) + b)
			assertEquals(FixUncheckedMicro32U.from(c), b + FixUncheckedMicro32U.from(a))
			assertEquals(FixUncheckedMicro32U.from(a), FixUncheckedMicro32U.from(c) - b)
			assertEquals(FixUncheckedMicro32U.from(b), c - FixUncheckedMicro32U.from(a))
		}

		testValues(FixUncheckedMicro32U.raw(UInt.MIN_VALUE), FixUncheckedMicro32U.ONE, FixUncheckedMicro32U.raw(UInt.MIN_VALUE + 1048576u))
		testValues(0, 3601, 3601)
		testValues(1, 2004, 2005)
		testValues(86, 832, 918)
		testValues(706, 102, 808)
		testValues(1177, 1674, 2851)
		testValues(2168, 267, 2435)
		testValues(4095, 0, 4095)
		testValues(FixUncheckedMicro32U.raw(UInt.MAX_VALUE - 1048576u), FixUncheckedMicro32U.ONE, FixUncheckedMicro32U.raw(UInt.MAX_VALUE))
		assertEquals(FixUncheckedMicro32U.raw(4293823335u), FixUncheckedMicro32U.raw(953191u) + 4094)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixUncheckedMicro32U.raw(UInt.MAX_VALUE), 1 * FixUncheckedMicro32U.raw(UInt.MAX_VALUE))
		assertEquals(FixUncheckedMicro32U.raw(UInt.MAX_VALUE), FixUncheckedMicro32U.raw(UInt.MAX_VALUE) / 1)

		fun testValues(a: Long, b: Long) {
			assertEquals(FixUncheckedMicro32U.from(a * b), FixUncheckedMicro32U.from(a) * FixUncheckedMicro32U.from(b))
			assertEquals(FixUncheckedMicro32U.from(a * b), FixUncheckedMicro32U.from(a) * b)
			assertEquals(FixUncheckedMicro32U.from(a * b), b * FixUncheckedMicro32U.from(a))
			if (b != 0L) assertEquals(FixUncheckedMicro32U.from(a), FixUncheckedMicro32U.from(a * b) / b)
			if (a != 0L) assertEquals(FixUncheckedMicro32U.from(b), FixUncheckedMicro32U.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixUncheckedMicro32U.from(b), FixUncheckedMicro32U.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixUncheckedMicro32U.from(a * b), FixUncheckedMicro32U.from(a) * b.toInt())
				assertEquals(FixUncheckedMicro32U.from(a * b), b.toInt() * FixUncheckedMicro32U.from(a))
			}
		}
		testValues(0, 1493)
		testValues(1, 231)
		assertEquals(FixUncheckedMicro32U.raw(977198u), (FixUncheckedMicro32U.raw(977198u) * FixUncheckedMicro32U.raw(654241u)) / FixUncheckedMicro32U.raw(654241u), FixUncheckedMicro32U.raw(10485u))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixUncheckedMicro32U.ZERO < FixUncheckedMicro32U.ONE)
		assertTrue(0 < FixUncheckedMicro32U.ONE)
		assertFalse(FixUncheckedMicro32U.ZERO > FixUncheckedMicro32U.ONE)
		assertFalse(0 > FixUncheckedMicro32U.ONE)
		assertFalse(FixUncheckedMicro32U.ONE < FixUncheckedMicro32U.ONE)
		assertFalse(FixUncheckedMicro32U.ONE < 1)
		assertFalse(FixUncheckedMicro32U.ONE > FixUncheckedMicro32U.ONE)
		assertTrue(FixUncheckedMicro32U.ONE <= FixUncheckedMicro32U.ONE)
		assertTrue(FixUncheckedMicro32U.ONE >= FixUncheckedMicro32U.ONE)
		assertTrue(FixUncheckedMicro32U.raw(UInt.MIN_VALUE) < FixUncheckedMicro32U.raw(UInt.MAX_VALUE))

		val minDelta = FixUncheckedMicro32U.raw(1u)
		assertEquals(FixUncheckedMicro32U.from(12), FixUncheckedMicro32U.from(12))
		assertNotEquals(FixUncheckedMicro32U.from(12), FixUncheckedMicro32U.from(12) - minDelta)
		assertTrue(FixUncheckedMicro32U.from(9.5367431640625E-7) < FixUncheckedMicro32U.from(9.5367431640625E-7) + minDelta)
		assertTrue(9.5367431640625E-7 < FixUncheckedMicro32U.from(9.5367431640625E-7) + minDelta)
		assertFalse(FixUncheckedMicro32U.from(3.911252279924859E-4) < FixUncheckedMicro32U.from(3.911252279924859E-4) - minDelta)
		assertFalse(3.911252279924859E-4 < FixUncheckedMicro32U.from(3.911252279924859E-4) - minDelta)
		assertTrue(FixUncheckedMicro32U.from(3.911252279924859E-4) < FixUncheckedMicro32U.from(3.911252279924859E-4) + minDelta)
		assertTrue(3.911252279924859E-4 < FixUncheckedMicro32U.from(3.911252279924859E-4) + minDelta)
		assertFalse(FixUncheckedMicro32U.from(0.0013926195514347168) < FixUncheckedMicro32U.from(0.0013926195514347168) - minDelta)
		assertFalse(0.0013926195514347168 < FixUncheckedMicro32U.from(0.0013926195514347168) - minDelta)
		assertTrue(FixUncheckedMicro32U.from(0.0013926195514347168) < FixUncheckedMicro32U.from(0.0013926195514347168) + minDelta)
		assertTrue(0.0013926195514347168 < FixUncheckedMicro32U.from(0.0013926195514347168) + minDelta)
		assertFalse(FixUncheckedMicro32U.from(0.007928885234495038) < FixUncheckedMicro32U.from(0.007928885234495038) - minDelta)
		assertFalse(0.007928885234495038 < FixUncheckedMicro32U.from(0.007928885234495038) - minDelta)
		assertTrue(FixUncheckedMicro32U.from(0.007928885234495038) < FixUncheckedMicro32U.from(0.007928885234495038) + minDelta)
		assertTrue(0.007928885234495038 < FixUncheckedMicro32U.from(0.007928885234495038) + minDelta)
		assertFalse(FixUncheckedMicro32U.from(0.060214617813443456) < FixUncheckedMicro32U.from(0.060214617813443456) - minDelta)
		assertFalse(0.060214617813443456 < FixUncheckedMicro32U.from(0.060214617813443456) - minDelta)
		assertTrue(FixUncheckedMicro32U.from(0.060214617813443456) < FixUncheckedMicro32U.from(0.060214617813443456) + minDelta)
		assertTrue(0.060214617813443456 < FixUncheckedMicro32U.from(0.060214617813443456) + minDelta)
		assertFalse(FixUncheckedMicro32U.from(0.18061000195143148) < FixUncheckedMicro32U.from(0.18061000195143148) - minDelta)
		assertFalse(0.18061000195143148 < FixUncheckedMicro32U.from(0.18061000195143148) - minDelta)
		assertTrue(FixUncheckedMicro32U.from(0.18061000195143148) < FixUncheckedMicro32U.from(0.18061000195143148) + minDelta)
		assertTrue(0.18061000195143148 < FixUncheckedMicro32U.from(0.18061000195143148) + minDelta)
		assertFalse(FixUncheckedMicro32U.from(2.6272550762762745) < FixUncheckedMicro32U.from(2.6272550762762745) - minDelta)
		assertFalse(2.6272550762762745 < FixUncheckedMicro32U.from(2.6272550762762745) - minDelta)
		assertTrue(FixUncheckedMicro32U.from(2.6272550762762745) < FixUncheckedMicro32U.from(2.6272550762762745) + minDelta)
		assertTrue(2.6272550762762745 < FixUncheckedMicro32U.from(2.6272550762762745) + minDelta)
		assertFalse(FixUncheckedMicro32U.from(10.160135609533143) < FixUncheckedMicro32U.from(10.160135609533143) - minDelta)
		assertFalse(10.160135609533143 < FixUncheckedMicro32U.from(10.160135609533143) - minDelta)
		assertTrue(FixUncheckedMicro32U.from(10.160135609533143) < FixUncheckedMicro32U.from(10.160135609533143) + minDelta)
		assertTrue(10.160135609533143 < FixUncheckedMicro32U.from(10.160135609533143) + minDelta)
		assertFalse(FixUncheckedMicro32U.from(143.3047592364777) < FixUncheckedMicro32U.from(143.3047592364777) - minDelta)
		assertFalse(143.3047592364777 < FixUncheckedMicro32U.from(143.3047592364777) - minDelta)
		assertTrue(FixUncheckedMicro32U.from(143.3047592364777) < FixUncheckedMicro32U.from(143.3047592364777) + minDelta)
		assertTrue(143.3047592364777 < FixUncheckedMicro32U.from(143.3047592364777) + minDelta)
		assertFalse(FixUncheckedMicro32U.from(4095.9999990463257) < FixUncheckedMicro32U.from(4095.9999990463257) - minDelta)
		assertFalse(4095.9999990463257 < FixUncheckedMicro32U.from(4095.9999990463257) - minDelta)
		assertTrue(FixUncheckedMicro32U.raw(UInt.MAX_VALUE) >= 4095)
		assertTrue(FixUncheckedMicro32U.raw(UInt.MAX_VALUE) > 4095)
		assertTrue(FixUncheckedMicro32U.raw(UInt.MAX_VALUE) < 4096)
		assertTrue(FixUncheckedMicro32U.raw(UInt.MAX_VALUE) < 4100.096)
		assertTrue(FixUncheckedMicro32U.raw(UInt.MAX_VALUE) < UInt.MAX_VALUE)
		assertTrue(FixUncheckedMicro32U.raw(UInt.MAX_VALUE) < UInt.MAX_VALUE.toFloat())
		assertTrue(FixUncheckedMicro32U.raw(UInt.MAX_VALUE) < UInt.MAX_VALUE.toDouble())
		assertTrue(FixUncheckedMicro32U.ZERO > -1)
		assertTrue(FixUncheckedMicro32U.ZERO > -0.001f)
		assertTrue(FixUncheckedMicro32U.ZERO > -0.001)
		assertTrue(FixUncheckedMicro32U.ZERO > Long.MIN_VALUE)
		assertTrue(FixUncheckedMicro32U.ZERO > Long.MIN_VALUE.toFloat())
		assertTrue(FixUncheckedMicro32U.ZERO > Long.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixUncheckedMicro32U.Array(2) { FixUncheckedMicro32U.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixUncheckedMicro32U.ONE, testArray[0])
		assertEquals(FixUncheckedMicro32U.ONE, testArray[1])
		testArray[1] = FixUncheckedMicro32U.ZERO
		assertEquals(FixUncheckedMicro32U.ONE, testArray[0])
		assertEquals(FixUncheckedMicro32U.ZERO, testArray[1])
		testArray.fill(FixUncheckedMicro32U.ZERO)
		assertEquals(FixUncheckedMicro32U.ZERO, testArray[0])
		assertEquals(FixUncheckedMicro32U.ZERO, testArray[1])
	}
}
