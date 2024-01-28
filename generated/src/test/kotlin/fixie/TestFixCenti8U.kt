package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixCenti8U {

	fun assertEquals(a: FixCenti8U, b: FixCenti8U, maxDelta: FixCenti8U) {
		val rawDifference = a.raw.toByte() - b.raw.toByte()
		if (rawDifference.absoluteValue > maxDelta.raw.toByte()) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixCenti8U.ZERO.toString())
		assertEquals("1", FixCenti8U.ONE.toString())
		assertTrue((FixCenti8U.ONE / 3).toString().startsWith("0.3"))
		assertTrue((FixCenti8U.from(7) + FixCenti8U.ONE / 3).toString().endsWith((FixCenti8U.ONE / 3).toString().substring(1)))
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixCenti8U.ONE, FixCenti8U.from(one))

		fun testValue(value: Int) = assertEquals(value, FixCenti8U.from(value).toInt())
		fun testOverflow(value: Int) = assertThrows(FixedPointException::class.java) { FixCenti8U.from(value) }
		testValue(0)
		testValue(1)
		testValue(8)

		testOverflow(-2147483648)
		testOverflow(-978689185)
		testOverflow(-974146)
		testOverflow(-740009)
		testOverflow(-16299)
		testOverflow(-1)

		testOverflow(9)
		testOverflow(24215)
		testOverflow(722181)
		testOverflow(10754444)
		testOverflow(2147483647)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixCenti8U.ONE, FixCenti8U.from(one))

		fun testValue(value: Long) = assertEquals(value, FixCenti8U.from(value).toLong())
		fun testOverflow(value: Long) = assertThrows(FixedPointException::class.java) { FixCenti8U.from(value) }
		testValue(0)
		testValue(1)
		testValue(8)

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

		testOverflow(9)
		testOverflow(24215)
		testOverflow(722181)
		testOverflow(10754444)
		testOverflow(1897786115)
		testOverflow(13314266688)
		testOverflow(6711099046168)
		testOverflow(468649336819561)
		testOverflow(14634611861411183)
		testOverflow(1014829407279936262)
		testOverflow(9223372036854775807)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixCenti8U.ONE, FixCenti8U.from(1f))
		val delta = 0.06666667f
		assertEquals(0.033333335f, FixCenti8U.from(0.033333335f).toFloat(), delta)
		assertEquals(8.5f, FixCenti8U.from(8.5f).toFloat(), delta)

		assertThrows(FixedPointException::class.java) { FixCenti8U.from(9.5f) }
		assertThrows(FixedPointException::class.java) { FixCenti8U.from(-9.5f) }
		assertThrows(FixedPointException::class.java) { FixCenti8U.from(72.25f) }
		assertThrows(FixedPointException::class.java) { FixCenti8U.from(-72.25f) }
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixCenti8U.ONE, FixCenti8U.from(1.0))
		val delta = 0.06666666666666667
		assertEquals(0.03333333333333333, FixCenti8U.from(0.03333333333333333).toDouble(), delta)
		assertEquals(8.5, FixCenti8U.from(8.5).toDouble(), delta)

		assertThrows(FixedPointException::class.java) { FixCenti8U.from(9.5) }
		assertThrows(FixedPointException::class.java) { FixCenti8U.from(-9.5) }
		assertThrows(FixedPointException::class.java) { FixCenti8U.from(72.25) }
		assertThrows(FixedPointException::class.java) { FixCenti8U.from(-72.25) }
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
		testValues(0, 2, 2)
		testValues(1, 2, 3)
		testValues(8, 0, 8)
		testValues(FixCenti8U.raw((UByte.MAX_VALUE - 30u).toUByte()), FixCenti8U.ONE, FixCenti8U.raw(UByte.MAX_VALUE))

		fun testOverflowPlus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) + FixCenti8U.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixCenti8U.from(b) }
		}

		fun testOverflowMinus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) - FixCenti8U.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixCenti8U.from(b) }
		}

		fun testOverflowPlus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) + FixCenti8U.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixCenti8U.from(b) }
		}

		fun testOverflowMinus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) - FixCenti8U.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixCenti8U.from(b) }
		}

		fun testOverflowPlus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) + FixCenti8U.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixCenti8U.from(b) }
		}

		fun testOverflowMinus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) - FixCenti8U.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixCenti8U.from(b) }
		}

		fun testOverflowPlus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) + FixCenti8U.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixCenti8U.from(b) }
		}

		fun testOverflowMinus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) - FixCenti8U.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixCenti8U.from(b) }
		}

		testOverflowPlus(0, 9)
		testOverflowPlus(0L, 9L)
		testOverflowPlus(0, 100)
		testOverflowPlus(0L, 100L)
		testOverflowMinus(0, 1)
		testOverflowMinus(0L, 1L)
		testOverflowMinus(0, 4)
		testOverflowMinus(0L, 4L)
		testOverflowPlus(1, 8)
		testOverflowPlus(1L, 8L)
		testOverflowPlus(1.0f, 8.0f)
		testOverflowPlus(1.0, 8.0)
		testOverflowPlus(1, 81)
		testOverflowPlus(1L, 81L)
		testOverflowPlus(1.0f, 81.0f)
		testOverflowPlus(1.0, 81.0)
		testOverflowMinus(1, 2)
		testOverflowMinus(1L, 2L)
		testOverflowMinus(1.0f, 2.0f)
		testOverflowMinus(1.0, 2.0)
		testOverflowMinus(1, 9)
		testOverflowMinus(1L, 9L)
		testOverflowMinus(1.0f, 9.0f)
		testOverflowMinus(1.0, 9.0)
		testOverflowPlus(8, 1)
		testOverflowPlus(8L, 1L)
		testOverflowPlus(8.0f, 1.0f)
		testOverflowPlus(8.0, 1.0)
		testOverflowPlus(8, 4)
		testOverflowPlus(8L, 4L)
		testOverflowPlus(8.0f, 4.0f)
		testOverflowPlus(8.0, 4.0)
		testOverflowMinus(8, 9)
		testOverflowMinus(8L, 9L)
		testOverflowMinus(8.0f, 9.0f)
		testOverflowMinus(8.0, 9.0)
		testOverflowMinus(8, 100)
		testOverflowMinus(8L, 100L)
		testOverflowMinus(8.0f, 100.0f)
		testOverflowMinus(8.0, 100.0)
		assertEquals(FixCenti8U.raw(211u), FixCenti8U.raw(1u) + 7)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixCenti8U.raw(UByte.MAX_VALUE), 1 * FixCenti8U.raw(UByte.MAX_VALUE))
		assertEquals(FixCenti8U.raw(UByte.MAX_VALUE), FixCenti8U.raw(UByte.MAX_VALUE) / 1)
		assertThrows(FixedPointException::class.java) { -1 * FixCenti8U.ONE }
		assertThrows(FixedPointException::class.java) { FixCenti8U.ONE / -1 }
		assertThrows(FixedPointException::class.java) { -1 * FixCenti8U.raw(UByte.MAX_VALUE)}
		assertThrows(FixedPointException::class.java) { FixCenti8U.raw(UByte.MAX_VALUE) / -1 }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixCenti8U.from(a * b), FixCenti8U.from(a) * FixCenti8U.from(b))
			assertEquals(FixCenti8U.from(a * b), FixCenti8U.from(a) * b)
			assertEquals(FixCenti8U.from(a * b), b * FixCenti8U.from(a))
			if (b != 0L) assertEquals(FixCenti8U.from(a), FixCenti8U.from(a * b) / b)
			if (a != 0L) assertEquals(FixCenti8U.from(b), FixCenti8U.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixCenti8U.from(b), FixCenti8U.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixCenti8U.from(a * b), FixCenti8U.from(a) * b.toInt())
				assertEquals(FixCenti8U.from(a * b), b.toInt() * FixCenti8U.from(a))
			}
		}
		testValues(0, 0)
		testValues(1, 8)
		testValues(8, 0)
		assertEquals(FixCenti8U.raw(29u), (FixCenti8U.raw(29u) * FixCenti8U.raw(21u)) / FixCenti8U.raw(21u), FixCenti8U.raw(1u))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixCenti8U.ZERO < FixCenti8U.ONE)
		assertTrue(0 < FixCenti8U.ONE)
		assertFalse(FixCenti8U.ZERO > FixCenti8U.ONE)
		assertFalse(0 > FixCenti8U.ONE)
		assertFalse(FixCenti8U.ONE < FixCenti8U.ONE)
		assertFalse(FixCenti8U.ONE < 1)
		assertFalse(FixCenti8U.ONE > FixCenti8U.ONE)
		assertTrue(FixCenti8U.ONE <= FixCenti8U.ONE)
		assertTrue(FixCenti8U.ONE >= FixCenti8U.ONE)
		assertTrue(FixCenti8U.raw(UByte.MIN_VALUE) < FixCenti8U.raw(UByte.MAX_VALUE))

		val minDelta = FixCenti8U.raw(1u)
		assertEquals(FixCenti8U.from(6), FixCenti8U.from(6))
		assertNotEquals(FixCenti8U.from(6), FixCenti8U.from(6) - minDelta)
		assertTrue(FixCenti8U.from(0.03333333333333333) < FixCenti8U.from(0.03333333333333333) + minDelta)
		assertTrue(0.03333333333333333 < FixCenti8U.from(0.03333333333333333) + minDelta)
		assertFalse(FixCenti8U.from(8.5) < FixCenti8U.from(8.5) - minDelta)
		assertFalse(8.5 < FixCenti8U.from(8.5) - minDelta)
		assertTrue(FixCenti8U.raw(UByte.MAX_VALUE) >= 8)
		assertTrue(FixCenti8U.raw(UByte.MAX_VALUE) > 8)
		assertTrue(FixCenti8U.raw(UByte.MAX_VALUE) < 9)
		assertTrue(FixCenti8U.raw(UByte.MAX_VALUE) < 9.008999999999999)
		assertTrue(FixCenti8U.raw(UByte.MAX_VALUE) < UByte.MAX_VALUE)
		assertTrue(FixCenti8U.raw(UByte.MAX_VALUE) < UByte.MAX_VALUE.toFloat())
		assertTrue(FixCenti8U.raw(UByte.MAX_VALUE) < UByte.MAX_VALUE.toDouble())
		assertTrue(FixCenti8U.ZERO > -1)
		assertTrue(FixCenti8U.ZERO > -0.001f)
		assertTrue(FixCenti8U.ZERO > -0.001)
		assertTrue(FixCenti8U.ZERO > Long.MIN_VALUE)
		assertTrue(FixCenti8U.ZERO > Long.MIN_VALUE.toFloat())
		assertTrue(FixCenti8U.ZERO > Long.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixCenti8U.Array(2) { FixCenti8U.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixCenti8U.ONE, testArray[0])
		assertEquals(FixCenti8U.ONE, testArray[1])
		testArray[1] = FixCenti8U.ZERO
		assertEquals(FixCenti8U.ONE, testArray[0])
		assertEquals(FixCenti8U.ZERO, testArray[1])
		testArray.fill(FixCenti8U.ZERO)
		assertEquals(FixCenti8U.ZERO, testArray[0])
		assertEquals(FixCenti8U.ZERO, testArray[1])
	}

	@Test
	fun testMinMax() {
		assertEquals(FixCenti8U.ZERO, min(FixCenti8U.ZERO, FixCenti8U.ZERO))
		assertEquals(FixCenti8U.ZERO, max(FixCenti8U.ZERO, FixCenti8U.ZERO))
		assertEquals(FixCenti8U.ZERO, min(FixCenti8U.ONE, FixCenti8U.ZERO))
		assertEquals(FixCenti8U.ONE, max(FixCenti8U.ONE, FixCenti8U.ZERO))
		assertEquals(FixCenti8U.ZERO, min(FixCenti8U.ZERO, FixCenti8U.ONE))
		assertEquals(FixCenti8U.ONE, max(FixCenti8U.ZERO, FixCenti8U.ONE))
		assertEquals(FixCenti8U.ZERO, min(FixCenti8U.ZERO, FixCenti8U.raw(UByte.MAX_VALUE)))
		assertEquals(FixCenti8U.raw(UByte.MAX_VALUE), max(FixCenti8U.ZERO, FixCenti8U.raw(UByte.MAX_VALUE)))
	}
}
