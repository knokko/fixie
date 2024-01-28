package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixUncheckedCenti8U {

	fun assertEquals(a: FixUncheckedCenti8U, b: FixUncheckedCenti8U, maxDelta: FixUncheckedCenti8U) {
		val rawDifference = a.raw.toByte() - b.raw.toByte()
		if (rawDifference.absoluteValue > maxDelta.raw.toByte()) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixUncheckedCenti8U.ZERO.toString())
		assertEquals("1", FixUncheckedCenti8U.ONE.toString())
		assertTrue((FixUncheckedCenti8U.ONE / 3).toString().startsWith("0.3"))
		assertTrue((FixUncheckedCenti8U.from(7) + FixUncheckedCenti8U.ONE / 3).toString().endsWith((FixUncheckedCenti8U.ONE / 3).toString().substring(1)))
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixUncheckedCenti8U.ONE, FixUncheckedCenti8U.from(one))

		fun testValue(value: Int) = assertEquals(value, FixUncheckedCenti8U.from(value).toInt())
		testValue(0)
		testValue(1)
		testValue(8)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixUncheckedCenti8U.ONE, FixUncheckedCenti8U.from(one))

		fun testValue(value: Long) = assertEquals(value, FixUncheckedCenti8U.from(value).toLong())
		testValue(0)
		testValue(1)
		testValue(8)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixUncheckedCenti8U.ONE, FixUncheckedCenti8U.from(1f))
		val delta = 0.06666667f
		assertEquals(0.033333335f, FixUncheckedCenti8U.from(0.033333335f).toFloat(), delta)
		assertEquals(8.5f, FixUncheckedCenti8U.from(8.5f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixUncheckedCenti8U.ONE, FixUncheckedCenti8U.from(1.0))
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
		testValues(0, 2, 2)
		testValues(1, 2, 3)
		testValues(8, 0, 8)
		testValues(FixUncheckedCenti8U.raw((UByte.MAX_VALUE - 30u).toUByte()), FixUncheckedCenti8U.ONE, FixUncheckedCenti8U.raw(UByte.MAX_VALUE))
		assertEquals(FixUncheckedCenti8U.raw(211u), FixUncheckedCenti8U.raw(1u) + 7)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixUncheckedCenti8U.raw(UByte.MAX_VALUE), 1 * FixUncheckedCenti8U.raw(UByte.MAX_VALUE))
		assertEquals(FixUncheckedCenti8U.raw(UByte.MAX_VALUE), FixUncheckedCenti8U.raw(UByte.MAX_VALUE) / 1)

		fun testValues(a: Long, b: Long) {
			assertEquals(FixUncheckedCenti8U.from(a * b), FixUncheckedCenti8U.from(a) * FixUncheckedCenti8U.from(b))
			assertEquals(FixUncheckedCenti8U.from(a * b), FixUncheckedCenti8U.from(a) * b)
			assertEquals(FixUncheckedCenti8U.from(a * b), b * FixUncheckedCenti8U.from(a))
			if (b != 0L) assertEquals(FixUncheckedCenti8U.from(a), FixUncheckedCenti8U.from(a * b) / b)
			if (a != 0L) assertEquals(FixUncheckedCenti8U.from(b), FixUncheckedCenti8U.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixUncheckedCenti8U.from(b), FixUncheckedCenti8U.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixUncheckedCenti8U.from(a * b), FixUncheckedCenti8U.from(a) * b.toInt())
				assertEquals(FixUncheckedCenti8U.from(a * b), b.toInt() * FixUncheckedCenti8U.from(a))
			}
		}
		testValues(0, 0)
		testValues(1, 8)
		testValues(8, 0)
		assertEquals(FixUncheckedCenti8U.raw(29u), (FixUncheckedCenti8U.raw(29u) * FixUncheckedCenti8U.raw(21u)) / FixUncheckedCenti8U.raw(21u), FixUncheckedCenti8U.raw(1u))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixUncheckedCenti8U.ZERO < FixUncheckedCenti8U.ONE)
		assertTrue(0 < FixUncheckedCenti8U.ONE)
		assertFalse(FixUncheckedCenti8U.ZERO > FixUncheckedCenti8U.ONE)
		assertFalse(0 > FixUncheckedCenti8U.ONE)
		assertFalse(FixUncheckedCenti8U.ONE < FixUncheckedCenti8U.ONE)
		assertFalse(FixUncheckedCenti8U.ONE < 1)
		assertFalse(FixUncheckedCenti8U.ONE > FixUncheckedCenti8U.ONE)
		assertTrue(FixUncheckedCenti8U.ONE <= FixUncheckedCenti8U.ONE)
		assertTrue(FixUncheckedCenti8U.ONE >= FixUncheckedCenti8U.ONE)
		assertTrue(FixUncheckedCenti8U.raw(UByte.MIN_VALUE) < FixUncheckedCenti8U.raw(UByte.MAX_VALUE))

		val minDelta = FixUncheckedCenti8U.raw(1u)
		assertEquals(FixUncheckedCenti8U.from(6), FixUncheckedCenti8U.from(6))
		assertNotEquals(FixUncheckedCenti8U.from(6), FixUncheckedCenti8U.from(6) - minDelta)
		assertTrue(FixUncheckedCenti8U.from(0.03333333333333333) < FixUncheckedCenti8U.from(0.03333333333333333) + minDelta)
		assertTrue(0.03333333333333333 < FixUncheckedCenti8U.from(0.03333333333333333) + minDelta)
		assertFalse(FixUncheckedCenti8U.from(8.5) < FixUncheckedCenti8U.from(8.5) - minDelta)
		assertFalse(8.5 < FixUncheckedCenti8U.from(8.5) - minDelta)
		assertTrue(FixUncheckedCenti8U.raw(UByte.MAX_VALUE) >= 8)
		assertTrue(FixUncheckedCenti8U.raw(UByte.MAX_VALUE) > 8)
		assertTrue(FixUncheckedCenti8U.raw(UByte.MAX_VALUE) < 9)
		assertTrue(FixUncheckedCenti8U.raw(UByte.MAX_VALUE) < 9.008999999999999)
		assertTrue(FixUncheckedCenti8U.raw(UByte.MAX_VALUE) < UByte.MAX_VALUE)
		assertTrue(FixUncheckedCenti8U.raw(UByte.MAX_VALUE) < UByte.MAX_VALUE.toFloat())
		assertTrue(FixUncheckedCenti8U.raw(UByte.MAX_VALUE) < UByte.MAX_VALUE.toDouble())
		assertTrue(FixUncheckedCenti8U.ZERO > -1)
		assertTrue(FixUncheckedCenti8U.ZERO > -0.001f)
		assertTrue(FixUncheckedCenti8U.ZERO > -0.001)
		assertTrue(FixUncheckedCenti8U.ZERO > Long.MIN_VALUE)
		assertTrue(FixUncheckedCenti8U.ZERO > Long.MIN_VALUE.toFloat())
		assertTrue(FixUncheckedCenti8U.ZERO > Long.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixUncheckedCenti8U.Array(2) { FixUncheckedCenti8U.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixUncheckedCenti8U.ONE, testArray[0])
		assertEquals(FixUncheckedCenti8U.ONE, testArray[1])
		testArray[1] = FixUncheckedCenti8U.ZERO
		assertEquals(FixUncheckedCenti8U.ONE, testArray[0])
		assertEquals(FixUncheckedCenti8U.ZERO, testArray[1])
		testArray.fill(FixUncheckedCenti8U.ZERO)
		assertEquals(FixUncheckedCenti8U.ZERO, testArray[0])
		assertEquals(FixUncheckedCenti8U.ZERO, testArray[1])
	}

	@Test
	fun testMinMax() {
		assertEquals(FixUncheckedCenti8U.ZERO, min(FixUncheckedCenti8U.ZERO, FixUncheckedCenti8U.ZERO))
		assertEquals(FixUncheckedCenti8U.ZERO, max(FixUncheckedCenti8U.ZERO, FixUncheckedCenti8U.ZERO))
		assertEquals(FixUncheckedCenti8U.ZERO, min(FixUncheckedCenti8U.ONE, FixUncheckedCenti8U.ZERO))
		assertEquals(FixUncheckedCenti8U.ONE, max(FixUncheckedCenti8U.ONE, FixUncheckedCenti8U.ZERO))
		assertEquals(FixUncheckedCenti8U.ZERO, min(FixUncheckedCenti8U.ZERO, FixUncheckedCenti8U.ONE))
		assertEquals(FixUncheckedCenti8U.ONE, max(FixUncheckedCenti8U.ZERO, FixUncheckedCenti8U.ONE))
		assertEquals(FixUncheckedCenti8U.ZERO, min(FixUncheckedCenti8U.ZERO, FixUncheckedCenti8U.raw(UByte.MAX_VALUE)))
		assertEquals(FixUncheckedCenti8U.raw(UByte.MAX_VALUE), max(FixUncheckedCenti8U.ZERO, FixUncheckedCenti8U.raw(UByte.MAX_VALUE)))
	}
}
