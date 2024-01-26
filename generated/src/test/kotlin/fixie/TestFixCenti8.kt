package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixCenti8 {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixCenti8.from(value).toInt())
		testValue(-4)
		testValue(0)
		testValue(1)
		testValue(4)
	}

	@Test
	fun testLongConversion() {
		fun testValue(value: Long) = assertEquals(value, FixCenti8.from(value).toLong())
		testValue(-4)
		testValue(0)
		testValue(1)
		testValue(4)
	}

	@Test
	fun testFloatConversion() {
		val delta = 0.06666667f
		assertEquals(0.033333335f, FixCenti8.from(0.033333335f).toFloat(), delta)
		assertEquals(-0.033333335f, FixCenti8.from(-0.033333335f).toFloat(), delta)
		assertEquals(4.233333f, FixCenti8.from(4.233333f).toFloat(), delta)
		assertEquals(-4.233333f, FixCenti8.from(-4.233333f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 0.06666666666666667
		assertEquals(0.03333333333333333, FixCenti8.from(0.03333333333333333).toDouble(), delta)
		assertEquals(-0.03333333333333333, FixCenti8.from(-0.03333333333333333).toDouble(), delta)
		assertEquals(4.233333333333333, FixCenti8.from(4.233333333333333).toDouble(), delta)
		assertEquals(-4.233333333333333, FixCenti8.from(-4.233333333333333).toDouble(), delta)
	}

	@Test
	fun testUnaryMinus() {
		assertThrows(FixedPointException::class.java) { -FixCenti8.raw(Byte.MIN_VALUE) }
		assertEquals(127, -FixCenti8.raw(-127).raw)
		assertEquals(0, -FixCenti8.raw(0).raw)
		assertEquals(-1, -FixCenti8.raw(1).raw)
		assertEquals(-127, -FixCenti8.raw(127).raw)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixCenti8, b: FixCenti8, c: FixCenti8) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}
		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixCenti8.from(a), FixCenti8.from(b), FixCenti8.from(c))
			assertEquals(FixCenti8.from(c), FixCenti8.from(a) + b)
			assertEquals(FixCenti8.from(c), b + FixCenti8.from(a))
			assertEquals(FixCenti8.from(a), FixCenti8.from(c) - b)
			assertEquals(FixCenti8.from(b), c - FixCenti8.from(a))
		}
		testValues(FixCenti8.raw(Byte.MIN_VALUE), FixCenti8.ONE, FixCenti8.raw((Byte.MIN_VALUE + 30).toByte()))
		testValues(-4, 0, -4)
		testValues(0, 0, 0)
		testValues(1, 0, 1)
		testValues(2, 0, 2)
		testValues(FixCenti8.raw((Byte.MAX_VALUE - 30).toByte()), FixCenti8.ONE, FixCenti8.raw(Byte.MAX_VALUE))
	}

	@Test
	fun testMultiplication() {
		assertEquals(FixCenti8.raw(Byte.MAX_VALUE), 1 * FixCenti8.raw(Byte.MAX_VALUE))
		assertEquals(FixCenti8.raw(Byte.MIN_VALUE), 1 * FixCenti8.raw(Byte.MIN_VALUE))
		assertEquals(FixCenti8.raw((Byte.MIN_VALUE + 1).toByte()), -1 * FixCenti8.raw(Byte.MAX_VALUE))
		assertThrows(FixedPointException::class.java) { -1 * FixCenti8.raw(Byte.MIN_VALUE) }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixCenti8.from(a * b), FixCenti8.from(a) * FixCenti8.from(b))
			assertEquals(FixCenti8.from(a * b), FixCenti8.from(a) * b)
			assertEquals(FixCenti8.from(a * b), b * FixCenti8.from(a))
		}
		testValues(-4, 0)
		testValues(0, 0)
		testValues(1, 0)
		testValues(4, 0)
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixCenti8.ZERO < FixCenti8.ONE)
		assertFalse(FixCenti8.ZERO > FixCenti8.ONE)
		assertFalse(FixCenti8.ONE < FixCenti8.ONE)
		assertFalse(FixCenti8.ONE > FixCenti8.ONE)
		assertTrue(FixCenti8.ONE <= FixCenti8.ONE)
		assertTrue(FixCenti8.ONE >= FixCenti8.ONE)
		assertTrue(FixCenti8.raw(Byte.MIN_VALUE) < FixCenti8.raw(Byte.MAX_VALUE))

		val minDelta = FixCenti8.raw(1)
		assertEquals(FixCenti8.from(3), FixCenti8.from(3))
		assertNotEquals(FixCenti8.from(3), FixCenti8.from(3) - minDelta)
		assertTrue(FixCenti8.from(0.03333333333333333) < FixCenti8.from(0.03333333333333333) + minDelta)
		assertFalse(FixCenti8.from(4.233333333333333) < FixCenti8.from(4.233333333333333) - minDelta)
	}
}
