package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixCenti8U {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixCenti8U.from(value).toInt())
		testValue(0)
		testValue(1)
		testValue(8)
	}

	@Test
	fun testLongConversion() {
		fun testValue(value: Long) = assertEquals(value, FixCenti8U.from(value).toLong())
		testValue(0)
		testValue(1)
		testValue(8)
	}

	@Test
	fun testFloatConversion() {
		val delta = 0.06666667f
		assertEquals(0.033333335f, FixCenti8U.from(0.033333335f).toFloat(), delta)
		assertEquals(8.5f, FixCenti8U.from(8.5f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 0.06666666666666667
		assertEquals(0.03333333333333333, FixCenti8U.from(0.03333333333333333).toDouble(), delta)
		assertEquals(8.5, FixCenti8U.from(8.5).toDouble(), delta)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixCenti8U, b: FixCenti8U, c: FixCenti8U) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}
		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixCenti8U.from(a), FixCenti8U.from(b), FixCenti8U.from(c))
			assertEquals(FixCenti8U.from(c), FixCenti8U.from(a) + b)
			assertEquals(FixCenti8U.from(c), b + FixCenti8U.from(a))
			assertEquals(FixCenti8U.from(a), FixCenti8U.from(c) - b)
			assertEquals(FixCenti8U.from(b), c - FixCenti8U.from(a))
		}
		testValues(FixCenti8U.raw(UByte.MIN_VALUE), FixCenti8U.ONE, FixCenti8U.raw((UByte.MIN_VALUE + 30u).toUByte()))
		testValues(0, 1, 1)
		testValues(1, 1, 2)
		testValues(4, 0, 4)
		testValues(FixCenti8U.raw((UByte.MAX_VALUE - 30u).toUByte()), FixCenti8U.ONE, FixCenti8U.raw(UByte.MAX_VALUE))
	}

	@Test
	fun testMultiplication() {
		assertEquals(FixCenti8U.raw(UByte.MAX_VALUE), 1 * FixCenti8U.raw(UByte.MAX_VALUE))
		assertThrows(FixedPointException::class.java) { -1 * FixCenti8U.ONE }
		assertThrows(FixedPointException::class.java) { -1 * FixCenti8U.raw(UByte.MAX_VALUE)}

		fun testValues(a: Long, b: Long) {
			assertEquals(FixCenti8U.from(a * b), FixCenti8U.from(a) * FixCenti8U.from(b))
			assertEquals(FixCenti8U.from(a * b), FixCenti8U.from(a) * b)
			assertEquals(FixCenti8U.from(a * b), b * FixCenti8U.from(a))
		}
		testValues(0, 1)
		testValues(1, 8)
		testValues(8, 0)
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixCenti8U.ZERO < FixCenti8U.ONE)
		assertFalse(FixCenti8U.ZERO > FixCenti8U.ONE)
		assertFalse(FixCenti8U.ONE < FixCenti8U.ONE)
		assertFalse(FixCenti8U.ONE > FixCenti8U.ONE)
		assertTrue(FixCenti8U.ONE <= FixCenti8U.ONE)
		assertTrue(FixCenti8U.ONE >= FixCenti8U.ONE)
		assertTrue(FixCenti8U.raw(UByte.MIN_VALUE) < FixCenti8U.raw(UByte.MAX_VALUE))

		val minDelta = FixCenti8U.raw(1u)
		assertEquals(FixCenti8U.from(6), FixCenti8U.from(6))
		assertNotEquals(FixCenti8U.from(6), FixCenti8U.from(6) - minDelta)
		assertTrue(FixCenti8U.from(0.03333333333333333) < FixCenti8U.from(0.03333333333333333) + minDelta)
		assertFalse(FixCenti8U.from(8.5) < FixCenti8U.from(8.5) - minDelta)
	}
}
