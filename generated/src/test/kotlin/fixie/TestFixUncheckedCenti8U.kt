package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixUncheckedCenti8U {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixUncheckedCenti8U.from(value).toInt())
		testValue(0)
		testValue(1)
		testValue(8)
	}

	@Test
	fun testLongConversion() {
		fun testValue(value: Long) = assertEquals(value, FixUncheckedCenti8U.from(value).toLong())
		testValue(0)
		testValue(1)
		testValue(8)
	}

	@Test
	fun testFloatConversion() {
		val delta = 0.06666667f
		assertEquals(0.033333335f, FixUncheckedCenti8U.from(0.033333335f).toFloat(), delta)
		assertEquals(8.5f, FixUncheckedCenti8U.from(8.5f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 0.06666666666666667
		assertEquals(0.03333333333333333, FixUncheckedCenti8U.from(0.03333333333333333).toDouble(), delta)
		assertEquals(8.5, FixUncheckedCenti8U.from(8.5).toDouble(), delta)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixUncheckedCenti8U, b: FixUncheckedCenti8U, c: FixUncheckedCenti8U) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}
		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixUncheckedCenti8U.from(a), FixUncheckedCenti8U.from(b), FixUncheckedCenti8U.from(c))
			assertEquals(FixUncheckedCenti8U.from(c), FixUncheckedCenti8U.from(a) + b)
			assertEquals(FixUncheckedCenti8U.from(c), b + FixUncheckedCenti8U.from(a))
			assertEquals(FixUncheckedCenti8U.from(a), FixUncheckedCenti8U.from(c) - b)
			assertEquals(FixUncheckedCenti8U.from(b), c - FixUncheckedCenti8U.from(a))
		}
		testValues(FixUncheckedCenti8U.raw(UByte.MIN_VALUE), FixUncheckedCenti8U.ONE, FixUncheckedCenti8U.raw((UByte.MIN_VALUE + 30u).toUByte()))
		testValues(0, 1, 1)
		testValues(1, 1, 2)
		testValues(4, 0, 4)
		testValues(FixUncheckedCenti8U.raw((UByte.MAX_VALUE - 30u).toUByte()), FixUncheckedCenti8U.ONE, FixUncheckedCenti8U.raw(UByte.MAX_VALUE))
	}

	@Test
	fun testMultiplication() {
		assertEquals(FixUncheckedCenti8U.raw(UByte.MAX_VALUE), 1 * FixUncheckedCenti8U.raw(UByte.MAX_VALUE))

		fun testValues(a: Long, b: Long) {
			assertEquals(FixUncheckedCenti8U.from(a * b), FixUncheckedCenti8U.from(a) * FixUncheckedCenti8U.from(b))
			assertEquals(FixUncheckedCenti8U.from(a * b), FixUncheckedCenti8U.from(a) * b)
			assertEquals(FixUncheckedCenti8U.from(a * b), b * FixUncheckedCenti8U.from(a))
		}
		testValues(0, 0)
		testValues(1, 0)
		testValues(8, 0)
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixUncheckedCenti8U.ZERO < FixUncheckedCenti8U.ONE)
		assertFalse(FixUncheckedCenti8U.ZERO > FixUncheckedCenti8U.ONE)
		assertFalse(FixUncheckedCenti8U.ONE < FixUncheckedCenti8U.ONE)
		assertFalse(FixUncheckedCenti8U.ONE > FixUncheckedCenti8U.ONE)
		assertTrue(FixUncheckedCenti8U.ONE <= FixUncheckedCenti8U.ONE)
		assertTrue(FixUncheckedCenti8U.ONE >= FixUncheckedCenti8U.ONE)
		assertTrue(FixUncheckedCenti8U.raw(UByte.MIN_VALUE) < FixUncheckedCenti8U.raw(UByte.MAX_VALUE))

		val minDelta = FixUncheckedCenti8U.raw(1u)
		assertEquals(FixUncheckedCenti8U.from(6), FixUncheckedCenti8U.from(6))
		assertNotEquals(FixUncheckedCenti8U.from(6), FixUncheckedCenti8U.from(6) - minDelta)
		assertTrue(FixUncheckedCenti8U.from(0.03333333333333333) < FixUncheckedCenti8U.from(0.03333333333333333) + minDelta)
		assertFalse(FixUncheckedCenti8U.from(8.5) < FixUncheckedCenti8U.from(8.5) - minDelta)
	}
}
