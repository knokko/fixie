package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixUncheckedMicro32 {

	fun assertEquals(a: FixUncheckedMicro32, b: FixUncheckedMicro32, maxDelta: FixUncheckedMicro32) {
		val rawDifference = a.raw - b.raw
		if (rawDifference.absoluteValue > maxDelta.raw) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixUncheckedMicro32.ZERO.toString())
		assertEquals("1", FixUncheckedMicro32.ONE.toString())
		assertTrue((FixUncheckedMicro32.ONE / 3).toString().startsWith("0.33"))
		assertEquals("-1", (-FixUncheckedMicro32.ONE).toString())
		assertTrue((FixUncheckedMicro32.ONE / -3).toString().startsWith("-0.33"))
		assertTrue((FixUncheckedMicro32.from(2046) + FixUncheckedMicro32.ONE / 3).toString().endsWith((FixUncheckedMicro32.ONE / 3).toString().substring(1)))
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixUncheckedMicro32.ONE, FixUncheckedMicro32.from(one))

		fun testValue(value: Int) = assertEquals(value, FixUncheckedMicro32.from(value).toInt())
		testValue(-2048)
		testValue(-701)
		testValue(0)
		testValue(1)
		testValue(32)
		testValue(2047)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixUncheckedMicro32.ONE, FixUncheckedMicro32.from(one))

		fun testValue(value: Long) = assertEquals(value, FixUncheckedMicro32.from(value).toLong())
		testValue(-2048)
		testValue(-701)
		testValue(0)
		testValue(1)
		testValue(32)
		testValue(2047)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixUncheckedMicro32.ONE, FixUncheckedMicro32.from(1f))
		val delta = 1.9073486E-6f
		assertEquals(9.536743E-7f, FixUncheckedMicro32.from(9.536743E-7f).toFloat(), delta)
		assertEquals(-9.536743E-7f, FixUncheckedMicro32.from(-9.536743E-7f).toFloat(), delta)
		assertEquals(5.7965175E-5f, FixUncheckedMicro32.from(5.7965175E-5f).toFloat(), delta)
		assertEquals(-5.7965175E-5f, FixUncheckedMicro32.from(-5.7965175E-5f).toFloat(), delta)
		assertEquals(0.0030845916f, FixUncheckedMicro32.from(0.0030845916f).toFloat(), delta)
		assertEquals(-0.0030845916f, FixUncheckedMicro32.from(-0.0030845916f).toFloat(), delta)
		assertEquals(0.009314704f, FixUncheckedMicro32.from(0.009314704f).toFloat(), delta)
		assertEquals(-0.009314704f, FixUncheckedMicro32.from(-0.009314704f).toFloat(), delta)
		assertEquals(0.5088087f, FixUncheckedMicro32.from(0.5088087f).toFloat(), delta)
		assertEquals(-0.5088087f, FixUncheckedMicro32.from(-0.5088087f).toFloat(), delta)
		assertEquals(14.094133f, FixUncheckedMicro32.from(14.094133f).toFloat(), delta)
		assertEquals(-14.094133f, FixUncheckedMicro32.from(-14.094133f).toFloat(), delta)
		assertEquals(55.274475f, FixUncheckedMicro32.from(55.274475f).toFloat(), delta)
		assertEquals(-55.274475f, FixUncheckedMicro32.from(-55.274475f).toFloat(), delta)
		assertEquals(2047.9999f, FixUncheckedMicro32.from(2047.9999f).toFloat(), delta)
		assertEquals(-2047.9999f, FixUncheckedMicro32.from(-2047.9999f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixUncheckedMicro32.ONE, FixUncheckedMicro32.from(1.0))
		val delta = 1.9073486328125E-6
		assertEquals(9.5367431640625E-7, FixUncheckedMicro32.from(9.5367431640625E-7).toDouble(), delta)
		assertEquals(-9.5367431640625E-7, FixUncheckedMicro32.from(-9.5367431640625E-7).toDouble(), delta)
		assertEquals(5.7965173783582514E-5, FixUncheckedMicro32.from(5.7965173783582514E-5).toDouble(), delta)
		assertEquals(-5.7965173783582514E-5, FixUncheckedMicro32.from(-5.7965173783582514E-5).toDouble(), delta)
		assertEquals(0.003084591527158651, FixUncheckedMicro32.from(0.003084591527158651).toDouble(), delta)
		assertEquals(-0.003084591527158651, FixUncheckedMicro32.from(-0.003084591527158651).toDouble(), delta)
		assertEquals(0.009314703658805575, FixUncheckedMicro32.from(0.009314703658805575).toDouble(), delta)
		assertEquals(-0.009314703658805575, FixUncheckedMicro32.from(-0.009314703658805575).toDouble(), delta)
		assertEquals(0.5088086753629321, FixUncheckedMicro32.from(0.5088086753629321).toDouble(), delta)
		assertEquals(-0.5088086753629321, FixUncheckedMicro32.from(-0.5088086753629321).toDouble(), delta)
		assertEquals(14.094133688218953, FixUncheckedMicro32.from(14.094133688218953).toDouble(), delta)
		assertEquals(-14.094133688218953, FixUncheckedMicro32.from(-14.094133688218953).toDouble(), delta)
		assertEquals(55.274475303202365, FixUncheckedMicro32.from(55.274475303202365).toDouble(), delta)
		assertEquals(-55.274475303202365, FixUncheckedMicro32.from(-55.274475303202365).toDouble(), delta)
		assertEquals(2047.9999990463257, FixUncheckedMicro32.from(2047.9999990463257).toDouble(), delta)
		assertEquals(-2047.9999990463257, FixUncheckedMicro32.from(-2047.9999990463257).toDouble(), delta)
	}

	@Test
	fun testUnaryMinus() {
		assertEquals(2147483647, -FixUncheckedMicro32.raw(-2147483647).raw)
		assertEquals(352975531, -FixUncheckedMicro32.raw(-352975531).raw)
		assertEquals(30294627, -FixUncheckedMicro32.raw(-30294627).raw)
		assertEquals(1475174, -FixUncheckedMicro32.raw(-1475174).raw)
		assertEquals(25682, -FixUncheckedMicro32.raw(-25682).raw)
		assertEquals(386, -FixUncheckedMicro32.raw(-386).raw)
		assertEquals(0, -FixUncheckedMicro32.raw(0).raw)
		assertEquals(-1, -FixUncheckedMicro32.raw(1).raw)
		assertEquals(-16270, -FixUncheckedMicro32.raw(16270).raw)
		assertEquals(-17228, -FixUncheckedMicro32.raw(17228).raw)
		assertEquals(-778569, -FixUncheckedMicro32.raw(778569).raw)
		assertEquals(-7590381, -FixUncheckedMicro32.raw(7590381).raw)
		assertEquals(-793898393, -FixUncheckedMicro32.raw(793898393).raw)
		assertEquals(-2147483647, -FixUncheckedMicro32.raw(2147483647).raw)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixUncheckedMicro32, b: FixUncheckedMicro32, c: FixUncheckedMicro32) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}

		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixUncheckedMicro32.from(a), FixUncheckedMicro32.from(b), FixUncheckedMicro32.from(c))
			assertEquals(FixUncheckedMicro32.from(c), FixUncheckedMicro32.from(a) + b)
			assertEquals(FixUncheckedMicro32.from(c), b + FixUncheckedMicro32.from(a))
			assertEquals(FixUncheckedMicro32.from(a), FixUncheckedMicro32.from(c) - b)
			assertEquals(FixUncheckedMicro32.from(b), c - FixUncheckedMicro32.from(a))
		}

		testValues(FixUncheckedMicro32.raw(Int.MIN_VALUE), FixUncheckedMicro32.ONE, FixUncheckedMicro32.raw(Int.MIN_VALUE + 1048576))
		testValues(-2048, 422, -1626)
		testValues(-1351, 854, -497)
		testValues(-1138, 143, -995)
		testValues(-97, 1550, 1453)
		testValues(-47, 721, 674)
		testValues(0, 738, 738)
		testValues(1, 1616, 1617)
		testValues(366, 1193, 1559)
		testValues(407, 72, 479)
		testValues(2047, 0, 2047)
		testValues(FixUncheckedMicro32.raw(Int.MAX_VALUE - 1048576), FixUncheckedMicro32.ONE, FixUncheckedMicro32.raw(Int.MAX_VALUE))
		assertEquals(FixUncheckedMicro32.raw(2146371848), FixUncheckedMicro32.raw(-63224) + 2047)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixUncheckedMicro32.raw(Int.MAX_VALUE), 1 * FixUncheckedMicro32.raw(Int.MAX_VALUE))
		assertEquals(FixUncheckedMicro32.raw(Int.MAX_VALUE), FixUncheckedMicro32.raw(Int.MAX_VALUE) / 1)
		assertEquals(FixUncheckedMicro32.raw(Int.MIN_VALUE), 1 * FixUncheckedMicro32.raw(Int.MIN_VALUE))
		assertEquals(FixUncheckedMicro32.raw(Int.MIN_VALUE), FixUncheckedMicro32.raw(Int.MIN_VALUE) / 1)
		assertEquals(FixUncheckedMicro32.raw(Int.MIN_VALUE + 1), -1 * FixUncheckedMicro32.raw(Int.MAX_VALUE))
		assertEquals(FixUncheckedMicro32.raw(Int.MIN_VALUE + 1), FixUncheckedMicro32.raw(Int.MAX_VALUE) / -1)

		fun testValues(a: Long, b: Long) {
			assertEquals(FixUncheckedMicro32.from(a * b), FixUncheckedMicro32.from(a) * FixUncheckedMicro32.from(b))
			assertEquals(FixUncheckedMicro32.from(a * b), FixUncheckedMicro32.from(a) * b)
			assertEquals(FixUncheckedMicro32.from(a * b), b * FixUncheckedMicro32.from(a))
			if (b != 0L) assertEquals(FixUncheckedMicro32.from(a), FixUncheckedMicro32.from(a * b) / b)
			if (a != 0L) assertEquals(FixUncheckedMicro32.from(b), FixUncheckedMicro32.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixUncheckedMicro32.from(b), FixUncheckedMicro32.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixUncheckedMicro32.from(a * b), FixUncheckedMicro32.from(a) * b.toInt())
				assertEquals(FixUncheckedMicro32.from(a * b), b.toInt() * FixUncheckedMicro32.from(a))
			}
		}
		testValues(-2048, 0)
		testValues(-49, 1)
		testValues(-37, -37)
		testValues(0, -2048)
		testValues(1, -49)
		assertEquals(FixUncheckedMicro32.raw(-865232), (FixUncheckedMicro32.raw(-865232) * FixUncheckedMicro32.raw(953370)) / FixUncheckedMicro32.raw(953370), FixUncheckedMicro32.raw(10485))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixUncheckedMicro32.ZERO < FixUncheckedMicro32.ONE)
		assertTrue(0 < FixUncheckedMicro32.ONE)
		assertFalse(FixUncheckedMicro32.ZERO > FixUncheckedMicro32.ONE)
		assertFalse(0 > FixUncheckedMicro32.ONE)
		assertFalse(FixUncheckedMicro32.ONE < FixUncheckedMicro32.ONE)
		assertFalse(FixUncheckedMicro32.ONE < 1)
		assertFalse(FixUncheckedMicro32.ONE > FixUncheckedMicro32.ONE)
		assertTrue(FixUncheckedMicro32.ONE <= FixUncheckedMicro32.ONE)
		assertTrue(FixUncheckedMicro32.ONE >= FixUncheckedMicro32.ONE)
		assertTrue(FixUncheckedMicro32.raw(Int.MIN_VALUE) < FixUncheckedMicro32.raw(Int.MAX_VALUE))

		val minDelta = FixUncheckedMicro32.raw(1)
		assertEquals(FixUncheckedMicro32.from(12), FixUncheckedMicro32.from(12))
		assertNotEquals(FixUncheckedMicro32.from(12), FixUncheckedMicro32.from(12) - minDelta)
		assertTrue(FixUncheckedMicro32.from(9.5367431640625E-7) < FixUncheckedMicro32.from(9.5367431640625E-7) + minDelta)
		assertTrue(9.5367431640625E-7 < FixUncheckedMicro32.from(9.5367431640625E-7) + minDelta)
		assertFalse(FixUncheckedMicro32.from(3.911252279924859E-4) < FixUncheckedMicro32.from(3.911252279924859E-4) - minDelta)
		assertFalse(3.911252279924859E-4 < FixUncheckedMicro32.from(3.911252279924859E-4) - minDelta)
		assertTrue(FixUncheckedMicro32.from(3.911252279924859E-4) < FixUncheckedMicro32.from(3.911252279924859E-4) + minDelta)
		assertTrue(3.911252279924859E-4 < FixUncheckedMicro32.from(3.911252279924859E-4) + minDelta)
		assertFalse(FixUncheckedMicro32.from(0.0013926195514347168) < FixUncheckedMicro32.from(0.0013926195514347168) - minDelta)
		assertFalse(0.0013926195514347168 < FixUncheckedMicro32.from(0.0013926195514347168) - minDelta)
		assertTrue(FixUncheckedMicro32.from(0.0013926195514347168) < FixUncheckedMicro32.from(0.0013926195514347168) + minDelta)
		assertTrue(0.0013926195514347168 < FixUncheckedMicro32.from(0.0013926195514347168) + minDelta)
		assertFalse(FixUncheckedMicro32.from(0.007928885234495038) < FixUncheckedMicro32.from(0.007928885234495038) - minDelta)
		assertFalse(0.007928885234495038 < FixUncheckedMicro32.from(0.007928885234495038) - minDelta)
		assertTrue(FixUncheckedMicro32.from(0.007928885234495038) < FixUncheckedMicro32.from(0.007928885234495038) + minDelta)
		assertTrue(0.007928885234495038 < FixUncheckedMicro32.from(0.007928885234495038) + minDelta)
		assertFalse(FixUncheckedMicro32.from(0.060214617813443456) < FixUncheckedMicro32.from(0.060214617813443456) - minDelta)
		assertFalse(0.060214617813443456 < FixUncheckedMicro32.from(0.060214617813443456) - minDelta)
		assertTrue(FixUncheckedMicro32.from(0.060214617813443456) < FixUncheckedMicro32.from(0.060214617813443456) + minDelta)
		assertTrue(0.060214617813443456 < FixUncheckedMicro32.from(0.060214617813443456) + minDelta)
		assertFalse(FixUncheckedMicro32.from(0.18061000195143148) < FixUncheckedMicro32.from(0.18061000195143148) - minDelta)
		assertFalse(0.18061000195143148 < FixUncheckedMicro32.from(0.18061000195143148) - minDelta)
		assertTrue(FixUncheckedMicro32.from(0.18061000195143148) < FixUncheckedMicro32.from(0.18061000195143148) + minDelta)
		assertTrue(0.18061000195143148 < FixUncheckedMicro32.from(0.18061000195143148) + minDelta)
		assertFalse(FixUncheckedMicro32.from(2.6272550762762745) < FixUncheckedMicro32.from(2.6272550762762745) - minDelta)
		assertFalse(2.6272550762762745 < FixUncheckedMicro32.from(2.6272550762762745) - minDelta)
		assertTrue(FixUncheckedMicro32.from(2.6272550762762745) < FixUncheckedMicro32.from(2.6272550762762745) + minDelta)
		assertTrue(2.6272550762762745 < FixUncheckedMicro32.from(2.6272550762762745) + minDelta)
		assertFalse(FixUncheckedMicro32.from(10.160135609533143) < FixUncheckedMicro32.from(10.160135609533143) - minDelta)
		assertFalse(10.160135609533143 < FixUncheckedMicro32.from(10.160135609533143) - minDelta)
		assertTrue(FixUncheckedMicro32.from(10.160135609533143) < FixUncheckedMicro32.from(10.160135609533143) + minDelta)
		assertTrue(10.160135609533143 < FixUncheckedMicro32.from(10.160135609533143) + minDelta)
		assertFalse(FixUncheckedMicro32.from(143.3047592364777) < FixUncheckedMicro32.from(143.3047592364777) - minDelta)
		assertFalse(143.3047592364777 < FixUncheckedMicro32.from(143.3047592364777) - minDelta)
		assertTrue(FixUncheckedMicro32.from(143.3047592364777) < FixUncheckedMicro32.from(143.3047592364777) + minDelta)
		assertTrue(143.3047592364777 < FixUncheckedMicro32.from(143.3047592364777) + minDelta)
		assertFalse(FixUncheckedMicro32.from(2047.9999990463257) < FixUncheckedMicro32.from(2047.9999990463257) - minDelta)
		assertFalse(2047.9999990463257 < FixUncheckedMicro32.from(2047.9999990463257) - minDelta)
		assertTrue(FixUncheckedMicro32.raw(Int.MAX_VALUE) >= 2047)
		assertTrue(FixUncheckedMicro32.raw(Int.MAX_VALUE) > 2047)
		assertTrue(FixUncheckedMicro32.raw(Int.MAX_VALUE) < 2048)
		assertTrue(FixUncheckedMicro32.raw(Int.MAX_VALUE) < 2050.048)
		assertTrue(FixUncheckedMicro32.raw(Int.MAX_VALUE) < Int.MAX_VALUE)
		assertTrue(FixUncheckedMicro32.raw(Int.MAX_VALUE) < Int.MAX_VALUE.toFloat())
		assertTrue(FixUncheckedMicro32.raw(Int.MAX_VALUE) < Int.MAX_VALUE.toDouble())
		assertTrue(FixUncheckedMicro32.raw(Int.MIN_VALUE) <= -2048)
		assertTrue(FixUncheckedMicro32.raw(Int.MIN_VALUE) > -2049)
		assertTrue(FixUncheckedMicro32.raw(Int.MIN_VALUE) > -2051.049)
		assertTrue(FixUncheckedMicro32.raw(Int.MIN_VALUE) > Int.MIN_VALUE)
		assertTrue(FixUncheckedMicro32.raw(Int.MIN_VALUE) > Int.MIN_VALUE.toFloat())
		assertTrue(FixUncheckedMicro32.raw(Int.MIN_VALUE) > Int.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixUncheckedMicro32.Array(2) { FixUncheckedMicro32.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixUncheckedMicro32.ONE, testArray[0])
		assertEquals(FixUncheckedMicro32.ONE, testArray[1])
		testArray[1] = FixUncheckedMicro32.ZERO
		assertEquals(FixUncheckedMicro32.ONE, testArray[0])
		assertEquals(FixUncheckedMicro32.ZERO, testArray[1])
		testArray.fill(FixUncheckedMicro32.ZERO)
		assertEquals(FixUncheckedMicro32.ZERO, testArray[0])
		assertEquals(FixUncheckedMicro32.ZERO, testArray[1])
	}
}
