package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixUncheckedCenti8 {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixUncheckedCenti8.from(value).toInt())
		testValue(-4)
		testValue(0)
		testValue(1)
		testValue(4)
	}

	@Test
	fun testLongConversion() {
		fun testValue(value: Long) = assertEquals(value, FixUncheckedCenti8.from(value).toLong())
		testValue(-4)
		testValue(0)
		testValue(1)
		testValue(4)
	}

	@Test
	fun testFloatConversion() {
		val delta = 0.06666667f
		assertEquals(0.033333335f, FixUncheckedCenti8.from(0.033333335f).toFloat(), delta)
		assertEquals(-0.033333335f, FixUncheckedCenti8.from(-0.033333335f).toFloat(), delta)
		assertEquals(4.233333f, FixUncheckedCenti8.from(4.233333f).toFloat(), delta)
		assertEquals(-4.233333f, FixUncheckedCenti8.from(-4.233333f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 0.06666666666666667
		assertEquals(0.03333333333333333, FixUncheckedCenti8.from(0.03333333333333333).toDouble(), delta)
		assertEquals(-0.03333333333333333, FixUncheckedCenti8.from(-0.03333333333333333).toDouble(), delta)
		assertEquals(4.233333333333333, FixUncheckedCenti8.from(4.233333333333333).toDouble(), delta)
		assertEquals(-4.233333333333333, FixUncheckedCenti8.from(-4.233333333333333).toDouble(), delta)
	}

	@Test
	fun testUnaryMinus() {
		assertEquals(127, -FixUncheckedCenti8.raw(-127).raw)
		assertEquals(0, -FixUncheckedCenti8.raw(0).raw)
		assertEquals(-1, -FixUncheckedCenti8.raw(1).raw)
		assertEquals(-127, -FixUncheckedCenti8.raw(127).raw)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixUncheckedCenti8, b: FixUncheckedCenti8, c: FixUncheckedCenti8) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}
		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixUncheckedCenti8.from(a), FixUncheckedCenti8.from(b), FixUncheckedCenti8.from(c))
			assertEquals(FixUncheckedCenti8.from(c), FixUncheckedCenti8.from(a) + b)
			assertEquals(FixUncheckedCenti8.from(c), b + FixUncheckedCenti8.from(a))
			assertEquals(FixUncheckedCenti8.from(a), FixUncheckedCenti8.from(c) - b)
			assertEquals(FixUncheckedCenti8.from(b), c - FixUncheckedCenti8.from(a))
		}
		testValues(FixUncheckedCenti8.raw(Byte.MIN_VALUE), FixUncheckedCenti8.ONE, FixUncheckedCenti8.raw((Byte.MIN_VALUE + 30).toByte()))
		testValues(-4, 0, -4)
		testValues(0, 0, 0)
		testValues(1, 0, 1)
		testValues(2, 0, 2)
		testValues(FixUncheckedCenti8.raw((Byte.MAX_VALUE - 30).toByte()), FixUncheckedCenti8.ONE, FixUncheckedCenti8.raw(Byte.MAX_VALUE))
	}

	@Test
	fun testMultiplication() {
		assertEquals(FixUncheckedCenti8.raw(Byte.MAX_VALUE), 1 * FixUncheckedCenti8.raw(Byte.MAX_VALUE))
		assertEquals(FixUncheckedCenti8.raw(Byte.MIN_VALUE), 1 * FixUncheckedCenti8.raw(Byte.MIN_VALUE))
		assertEquals(FixUncheckedCenti8.raw((Byte.MIN_VALUE + 1).toByte()), -1 * FixUncheckedCenti8.raw(Byte.MAX_VALUE))

		fun testValues(a: Long, b: Long) {
			assertEquals(FixUncheckedCenti8.from(a * b), FixUncheckedCenti8.from(a) * FixUncheckedCenti8.from(b))
			assertEquals(FixUncheckedCenti8.from(a * b), FixUncheckedCenti8.from(a) * b)
			assertEquals(FixUncheckedCenti8.from(a * b), b * FixUncheckedCenti8.from(a))
		}
		testValues(-4, 0)
		testValues(0, 0)
		testValues(1, 0)
		testValues(4, 0)
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixUncheckedCenti8.ZERO < FixUncheckedCenti8.ONE)
		assertFalse(FixUncheckedCenti8.ZERO > FixUncheckedCenti8.ONE)
		assertFalse(FixUncheckedCenti8.ONE < FixUncheckedCenti8.ONE)
		assertFalse(FixUncheckedCenti8.ONE > FixUncheckedCenti8.ONE)
		assertTrue(FixUncheckedCenti8.ONE <= FixUncheckedCenti8.ONE)
		assertTrue(FixUncheckedCenti8.ONE >= FixUncheckedCenti8.ONE)
		assertTrue(FixUncheckedCenti8.raw(Byte.MIN_VALUE) < FixUncheckedCenti8.raw(Byte.MAX_VALUE))

		val minDelta = FixUncheckedCenti8.raw(1)
		assertEquals(FixUncheckedCenti8.from(3), FixUncheckedCenti8.from(3))
		assertNotEquals(FixUncheckedCenti8.from(3), FixUncheckedCenti8.from(3) - minDelta)
		assertTrue(FixUncheckedCenti8.from(0.03333333333333333) < FixUncheckedCenti8.from(0.03333333333333333) + minDelta)
		assertFalse(FixUncheckedCenti8.from(4.233333333333333) < FixUncheckedCenti8.from(4.233333333333333) - minDelta)
	}
}
