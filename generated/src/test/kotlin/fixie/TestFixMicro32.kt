package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixMicro32 {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixMicro32.from(value).toInt())
		testValue(-2048)
		testValue(-701)
		testValue(0)
		testValue(1)
		testValue(32)
		testValue(2047)
	}

	@Test
	fun testLongConversion() {
		fun testValue(value: Long) = assertEquals(value, FixMicro32.from(value).toLong())
		testValue(-2048)
		testValue(-701)
		testValue(0)
		testValue(1)
		testValue(32)
		testValue(2047)
	}

	@Test
	fun testFloatConversion() {
		val delta = 1.9073486E-6f
		assertEquals(9.536743E-7f, FixMicro32.from(9.536743E-7f).toFloat(), delta)
		assertEquals(-9.536743E-7f, FixMicro32.from(-9.536743E-7f).toFloat(), delta)
		assertEquals(5.7965175E-5f, FixMicro32.from(5.7965175E-5f).toFloat(), delta)
		assertEquals(-5.7965175E-5f, FixMicro32.from(-5.7965175E-5f).toFloat(), delta)
		assertEquals(0.0030845916f, FixMicro32.from(0.0030845916f).toFloat(), delta)
		assertEquals(-0.0030845916f, FixMicro32.from(-0.0030845916f).toFloat(), delta)
		assertEquals(0.009314704f, FixMicro32.from(0.009314704f).toFloat(), delta)
		assertEquals(-0.009314704f, FixMicro32.from(-0.009314704f).toFloat(), delta)
		assertEquals(0.5088087f, FixMicro32.from(0.5088087f).toFloat(), delta)
		assertEquals(-0.5088087f, FixMicro32.from(-0.5088087f).toFloat(), delta)
		assertEquals(14.094133f, FixMicro32.from(14.094133f).toFloat(), delta)
		assertEquals(-14.094133f, FixMicro32.from(-14.094133f).toFloat(), delta)
		assertEquals(55.274475f, FixMicro32.from(55.274475f).toFloat(), delta)
		assertEquals(-55.274475f, FixMicro32.from(-55.274475f).toFloat(), delta)
		assertEquals(2047.9999f, FixMicro32.from(2047.9999f).toFloat(), delta)
		assertEquals(-2047.9999f, FixMicro32.from(-2047.9999f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 1.9073486328125E-6
		assertEquals(9.5367431640625E-7, FixMicro32.from(9.5367431640625E-7).toDouble(), delta)
		assertEquals(-9.5367431640625E-7, FixMicro32.from(-9.5367431640625E-7).toDouble(), delta)
		assertEquals(5.7965173783582514E-5, FixMicro32.from(5.7965173783582514E-5).toDouble(), delta)
		assertEquals(-5.7965173783582514E-5, FixMicro32.from(-5.7965173783582514E-5).toDouble(), delta)
		assertEquals(0.003084591527158651, FixMicro32.from(0.003084591527158651).toDouble(), delta)
		assertEquals(-0.003084591527158651, FixMicro32.from(-0.003084591527158651).toDouble(), delta)
		assertEquals(0.009314703658805575, FixMicro32.from(0.009314703658805575).toDouble(), delta)
		assertEquals(-0.009314703658805575, FixMicro32.from(-0.009314703658805575).toDouble(), delta)
		assertEquals(0.5088086753629321, FixMicro32.from(0.5088086753629321).toDouble(), delta)
		assertEquals(-0.5088086753629321, FixMicro32.from(-0.5088086753629321).toDouble(), delta)
		assertEquals(14.094133688218953, FixMicro32.from(14.094133688218953).toDouble(), delta)
		assertEquals(-14.094133688218953, FixMicro32.from(-14.094133688218953).toDouble(), delta)
		assertEquals(55.274475303202365, FixMicro32.from(55.274475303202365).toDouble(), delta)
		assertEquals(-55.274475303202365, FixMicro32.from(-55.274475303202365).toDouble(), delta)
		assertEquals(2047.9999990463257, FixMicro32.from(2047.9999990463257).toDouble(), delta)
		assertEquals(-2047.9999990463257, FixMicro32.from(-2047.9999990463257).toDouble(), delta)
	}

	@Test
	fun testUnaryMinus() {
		assertThrows(FixedPointException::class.java) { -FixMicro32.raw(Int.MIN_VALUE) }
		assertEquals(2147483647, -FixMicro32.raw(-2147483647).raw)
		assertEquals(352975531, -FixMicro32.raw(-352975531).raw)
		assertEquals(30294627, -FixMicro32.raw(-30294627).raw)
		assertEquals(1475174, -FixMicro32.raw(-1475174).raw)
		assertEquals(25682, -FixMicro32.raw(-25682).raw)
		assertEquals(386, -FixMicro32.raw(-386).raw)
		assertEquals(0, -FixMicro32.raw(0).raw)
		assertEquals(-1, -FixMicro32.raw(1).raw)
		assertEquals(-16270, -FixMicro32.raw(16270).raw)
		assertEquals(-17228, -FixMicro32.raw(17228).raw)
		assertEquals(-778569, -FixMicro32.raw(778569).raw)
		assertEquals(-7590381, -FixMicro32.raw(7590381).raw)
		assertEquals(-793898393, -FixMicro32.raw(793898393).raw)
		assertEquals(-2147483647, -FixMicro32.raw(2147483647).raw)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixMicro32, b: FixMicro32, c: FixMicro32) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}
		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixMicro32.from(a), FixMicro32.from(b), FixMicro32.from(c))
			assertEquals(FixMicro32.from(c), FixMicro32.from(a) + b)
			assertEquals(FixMicro32.from(c), b + FixMicro32.from(a))
			assertEquals(FixMicro32.from(a), FixMicro32.from(c) - b)
			assertEquals(FixMicro32.from(b), c - FixMicro32.from(a))
		}
		testValues(FixMicro32.raw(Int.MIN_VALUE), FixMicro32.ONE, FixMicro32.raw(Int.MIN_VALUE + 1048576))
		testValues(-2048, 4, -2044)
		testValues(0, 123, 123)
		testValues(1, 1005, 1006)
		testValues(1023, 1020, 2043)
		testValues(FixMicro32.raw(Int.MAX_VALUE - 1048576), FixMicro32.ONE, FixMicro32.raw(Int.MAX_VALUE))
	}

	@Test
	fun testMultiplication() {
		assertEquals(FixMicro32.raw(Int.MAX_VALUE), 1 * FixMicro32.raw(Int.MAX_VALUE))
		assertEquals(FixMicro32.raw(Int.MIN_VALUE), 1 * FixMicro32.raw(Int.MIN_VALUE))
		assertEquals(FixMicro32.raw(Int.MIN_VALUE + 1), -1 * FixMicro32.raw(Int.MAX_VALUE))
		assertThrows(FixedPointException::class.java) { -1 * FixMicro32.raw(Int.MIN_VALUE) }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixMicro32.from(a * b), FixMicro32.from(a) * FixMicro32.from(b))
			assertEquals(FixMicro32.from(a * b), FixMicro32.from(a) * b)
			assertEquals(FixMicro32.from(a * b), b * FixMicro32.from(a))
		}
		testValues(-2048, 0)
		testValues(-1212, 0)
		testValues(-656, 0)
		testValues(-195, 0)
		testValues(-49, 0)
		testValues(-37, 0)
		testValues(-35, 0)
		testValues(0, 0)
		testValues(1, 0)
		testValues(51, 0)
		testValues(70, 0)
		testValues(158, 0)
		testValues(239, 0)
		testValues(2047, 0)
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixMicro32.ZERO < FixMicro32.ONE)
		assertFalse(FixMicro32.ZERO > FixMicro32.ONE)
		assertFalse(FixMicro32.ONE < FixMicro32.ONE)
		assertFalse(FixMicro32.ONE > FixMicro32.ONE)
		assertTrue(FixMicro32.ONE <= FixMicro32.ONE)
		assertTrue(FixMicro32.ONE >= FixMicro32.ONE)
		assertTrue(FixMicro32.raw(Int.MIN_VALUE) < FixMicro32.raw(Int.MAX_VALUE))

		val minDelta = FixMicro32.raw(1)
		assertEquals(FixMicro32.from(12), FixMicro32.from(12))
		assertNotEquals(FixMicro32.from(12), FixMicro32.from(12) - minDelta)
		assertTrue(FixMicro32.from(9.5367431640625E-7) < FixMicro32.from(9.5367431640625E-7) + minDelta)
		assertFalse(FixMicro32.from(3.911252279924859E-4) < FixMicro32.from(3.911252279924859E-4) - minDelta)
		assertTrue(FixMicro32.from(3.911252279924859E-4) < FixMicro32.from(3.911252279924859E-4) + minDelta)
		assertFalse(FixMicro32.from(0.0013926195514347168) < FixMicro32.from(0.0013926195514347168) - minDelta)
		assertTrue(FixMicro32.from(0.0013926195514347168) < FixMicro32.from(0.0013926195514347168) + minDelta)
		assertFalse(FixMicro32.from(0.007928885234495038) < FixMicro32.from(0.007928885234495038) - minDelta)
		assertTrue(FixMicro32.from(0.007928885234495038) < FixMicro32.from(0.007928885234495038) + minDelta)
		assertFalse(FixMicro32.from(0.060214617813443456) < FixMicro32.from(0.060214617813443456) - minDelta)
		assertTrue(FixMicro32.from(0.060214617813443456) < FixMicro32.from(0.060214617813443456) + minDelta)
		assertFalse(FixMicro32.from(0.18061000195143148) < FixMicro32.from(0.18061000195143148) - minDelta)
		assertTrue(FixMicro32.from(0.18061000195143148) < FixMicro32.from(0.18061000195143148) + minDelta)
		assertFalse(FixMicro32.from(2.6272550762762745) < FixMicro32.from(2.6272550762762745) - minDelta)
		assertTrue(FixMicro32.from(2.6272550762762745) < FixMicro32.from(2.6272550762762745) + minDelta)
		assertFalse(FixMicro32.from(10.160135609533143) < FixMicro32.from(10.160135609533143) - minDelta)
		assertTrue(FixMicro32.from(10.160135609533143) < FixMicro32.from(10.160135609533143) + minDelta)
		assertFalse(FixMicro32.from(143.3047592364777) < FixMicro32.from(143.3047592364777) - minDelta)
		assertTrue(FixMicro32.from(143.3047592364777) < FixMicro32.from(143.3047592364777) + minDelta)
		assertFalse(FixMicro32.from(2047.9999990463257) < FixMicro32.from(2047.9999990463257) - minDelta)
	}
}
