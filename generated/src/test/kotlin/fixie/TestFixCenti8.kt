package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixCenti8 {

	fun assertEquals(a: FixCenti8, b: FixCenti8, maxDelta: FixCenti8) {
		val rawDifference = a.raw - b.raw
		if (rawDifference.absoluteValue > maxDelta.raw) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixCenti8.ZERO.toString())
		assertEquals("1", FixCenti8.ONE.toString())
		assertTrue((FixCenti8.ONE / 3).toString().startsWith("0.3"))
		assertEquals("-1", (-FixCenti8.ONE).toString())
		assertTrue((FixCenti8.ONE / -3).toString().startsWith("-0.3"))
		assertTrue((FixCenti8.from(3) + FixCenti8.ONE / 3).toString().endsWith((FixCenti8.ONE / 3).toString().substring(1)))
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixCenti8.ONE, FixCenti8.from(one))

		fun testValue(value: Int) = assertEquals(value, FixCenti8.from(value).toInt())
		fun testOverflow(value: Int) = assertThrows(FixedPointException::class.java) { FixCenti8.from(value) }
		testValue(-4)
		testValue(0)
		testValue(1)
		testValue(4)

		testOverflow(-2147483648)
		testOverflow(-978689185)
		testOverflow(-974146)
		testOverflow(-740009)
		testOverflow(-16299)
		testOverflow(-5)

		testOverflow(5)
		testOverflow(26366)
		testOverflow(2597427)
		testOverflow(40787410)
		testOverflow(2147483647)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixCenti8.ONE, FixCenti8.from(one))

		fun testValue(value: Long) = assertEquals(value, FixCenti8.from(value).toLong())
		fun testOverflow(value: Long) = assertThrows(FixedPointException::class.java) { FixCenti8.from(value) }
		testValue(-4)
		testValue(0)
		testValue(1)
		testValue(4)

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
		testOverflow(-5)

		testOverflow(5)
		testOverflow(26366)
		testOverflow(2597427)
		testOverflow(40787410)
		testOverflow(2122113452)
		testOverflow(5298037815)
		testOverflow(2771683059598)
		testOverflow(147341570514374)
		testOverflow(4591751312959523)
		testOverflow(1021237598688550034)
		testOverflow(9223372036854775807)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixCenti8.ONE, FixCenti8.from(1f))
		val delta = 0.06666667f
		assertEquals(0.033333335f, FixCenti8.from(0.033333335f).toFloat(), delta)
		assertEquals(-0.033333335f, FixCenti8.from(-0.033333335f).toFloat(), delta)
		assertEquals(4.233333f, FixCenti8.from(4.233333f).toFloat(), delta)
		assertEquals(-4.233333f, FixCenti8.from(-4.233333f).toFloat(), delta)

		assertThrows(FixedPointException::class.java) { FixCenti8.from(5.233333f) }
		assertThrows(FixedPointException::class.java) { FixCenti8.from(-5.233333f) }
		assertThrows(FixedPointException::class.java) { FixCenti8.from(17.921112f) }
		assertThrows(FixedPointException::class.java) { FixCenti8.from(-17.921112f) }
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixCenti8.ONE, FixCenti8.from(1.0))
		val delta = 0.06666666666666667
		assertEquals(0.03333333333333333, FixCenti8.from(0.03333333333333333).toDouble(), delta)
		assertEquals(-0.03333333333333333, FixCenti8.from(-0.03333333333333333).toDouble(), delta)
		assertEquals(4.233333333333333, FixCenti8.from(4.233333333333333).toDouble(), delta)
		assertEquals(-4.233333333333333, FixCenti8.from(-4.233333333333333).toDouble(), delta)

		assertThrows(FixedPointException::class.java) { FixCenti8.from(5.233333333333333) }
		assertThrows(FixedPointException::class.java) { FixCenti8.from(-5.233333333333333) }
		assertThrows(FixedPointException::class.java) { FixCenti8.from(17.921111111111113) }
		assertThrows(FixedPointException::class.java) { FixCenti8.from(-17.921111111111113) }
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
		testValues(-4, 1, -3)
		testValues(0, 1, 1)
		testValues(1, 0, 1)
		testValues(4, 0, 4)
		testValues(FixCenti8.raw((Byte.MAX_VALUE - 30).toByte()), FixCenti8.ONE, FixCenti8.raw(Byte.MAX_VALUE))

		fun testOverflowPlus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) + FixCenti8.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixCenti8.from(b) }
		}

		fun testOverflowMinus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) - FixCenti8.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixCenti8.from(b) }
		}

		fun testOverflowPlus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) + FixCenti8.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixCenti8.from(b) }
		}

		fun testOverflowMinus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) - FixCenti8.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixCenti8.from(b) }
		}

		fun testOverflowPlus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) + FixCenti8.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixCenti8.from(b) }
		}

		fun testOverflowMinus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) - FixCenti8.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixCenti8.from(b) }
		}

		fun testOverflowPlus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) + FixCenti8.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixCenti8.from(b) }
		}

		fun testOverflowMinus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) - FixCenti8.from(b) }
			assertThrows(FixedPointException::class.java) { FixCenti8.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixCenti8.from(b) }
		}

		testOverflowPlus(-4, 9)
		testOverflowPlus(-4L, 9L)
		testOverflowPlus(-4.0f, 9.0f)
		testOverflowPlus(-4.0, 9.0)
		testOverflowPlus(-4, 100)
		testOverflowPlus(-4L, 100L)
		testOverflowPlus(-4.0f, 100.0f)
		testOverflowPlus(-4.0, 100.0)
		testOverflowMinus(-4, 1)
		testOverflowMinus(-4L, 1L)
		testOverflowMinus(-4.0f, 1.0f)
		testOverflowMinus(-4.0, 1.0)
		testOverflowMinus(-4, 4)
		testOverflowMinus(-4L, 4L)
		testOverflowMinus(-4.0f, 4.0f)
		testOverflowMinus(-4.0, 4.0)
		testOverflowPlus(0, 5)
		testOverflowPlus(0L, 5L)
		testOverflowPlus(0, 36)
		testOverflowPlus(0L, 36L)
		testOverflowMinus(0, 5)
		testOverflowMinus(0L, 5L)
		testOverflowMinus(0, 36)
		testOverflowMinus(0L, 36L)
		testOverflowPlus(1, 4)
		testOverflowPlus(1L, 4L)
		testOverflowPlus(1.0f, 4.0f)
		testOverflowPlus(1.0, 4.0)
		testOverflowPlus(1, 25)
		testOverflowPlus(1L, 25L)
		testOverflowPlus(1.0f, 25.0f)
		testOverflowPlus(1.0, 25.0)
		testOverflowMinus(1, 6)
		testOverflowMinus(1L, 6L)
		testOverflowMinus(1.0f, 6.0f)
		testOverflowMinus(1.0, 6.0)
		testOverflowMinus(1, 49)
		testOverflowMinus(1L, 49L)
		testOverflowMinus(1.0f, 49.0f)
		testOverflowMinus(1.0, 49.0)
		testOverflowPlus(4, 1)
		testOverflowPlus(4L, 1L)
		testOverflowPlus(4.0f, 1.0f)
		testOverflowPlus(4.0, 1.0)
		testOverflowPlus(4, 4)
		testOverflowPlus(4L, 4L)
		testOverflowPlus(4.0f, 4.0f)
		testOverflowPlus(4.0, 4.0)
		testOverflowMinus(4, 9)
		testOverflowMinus(4L, 9L)
		testOverflowMinus(4.0f, 9.0f)
		testOverflowMinus(4.0, 9.0)
		testOverflowMinus(4, 100)
		testOverflowMinus(4L, 100L)
		testOverflowMinus(4.0f, 100.0f)
		testOverflowMinus(4.0, 100.0)
		assertEquals(FixCenti8.raw(99), FixCenti8.raw(-21) + 4)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixCenti8.raw(Byte.MAX_VALUE), 1 * FixCenti8.raw(Byte.MAX_VALUE))
		assertEquals(FixCenti8.raw(Byte.MAX_VALUE), FixCenti8.raw(Byte.MAX_VALUE) / 1)
		assertEquals(FixCenti8.raw(Byte.MIN_VALUE), 1 * FixCenti8.raw(Byte.MIN_VALUE))
		assertEquals(FixCenti8.raw(Byte.MIN_VALUE), FixCenti8.raw(Byte.MIN_VALUE) / 1)
		assertEquals(FixCenti8.raw((Byte.MIN_VALUE + 1).toByte()), -1 * FixCenti8.raw(Byte.MAX_VALUE))
		assertEquals(FixCenti8.raw((Byte.MIN_VALUE + 1).toByte()), FixCenti8.raw(Byte.MAX_VALUE) / -1)
		assertThrows(FixedPointException::class.java) { -1 * FixCenti8.raw(Byte.MIN_VALUE) }
		assertThrows(FixedPointException::class.java) { FixCenti8.raw(Byte.MIN_VALUE) / -1 }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixCenti8.from(a * b), FixCenti8.from(a) * FixCenti8.from(b))
			assertEquals(FixCenti8.from(a * b), FixCenti8.from(a) * b)
			assertEquals(FixCenti8.from(a * b), b * FixCenti8.from(a))
			if (b != 0L) assertEquals(FixCenti8.from(a), FixCenti8.from(a * b) / b)
			if (a != 0L) assertEquals(FixCenti8.from(b), FixCenti8.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixCenti8.from(b), FixCenti8.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixCenti8.from(a * b), FixCenti8.from(a) * b.toInt())
				assertEquals(FixCenti8.from(a * b), b.toInt() * FixCenti8.from(a))
			}
		}
		testValues(-4, 0)
		testValues(0, 1)
		testValues(1, -4)
		assertThrows(FixedPointException::class.java) { FixCenti8.from(4) * -4 }
		assertEquals(FixCenti8.raw(-17), (FixCenti8.raw(-17) * FixCenti8.raw(28)) / FixCenti8.raw(28), FixCenti8.raw(1))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixCenti8.ZERO < FixCenti8.ONE)
		assertTrue(0 < FixCenti8.ONE)
		assertFalse(FixCenti8.ZERO > FixCenti8.ONE)
		assertFalse(0 > FixCenti8.ONE)
		assertFalse(FixCenti8.ONE < FixCenti8.ONE)
		assertFalse(FixCenti8.ONE < 1)
		assertFalse(FixCenti8.ONE > FixCenti8.ONE)
		assertTrue(FixCenti8.ONE <= FixCenti8.ONE)
		assertTrue(FixCenti8.ONE >= FixCenti8.ONE)
		assertTrue(FixCenti8.raw(Byte.MIN_VALUE) < FixCenti8.raw(Byte.MAX_VALUE))

		val minDelta = FixCenti8.raw(1)
		assertEquals(FixCenti8.from(3), FixCenti8.from(3))
		assertNotEquals(FixCenti8.from(3), FixCenti8.from(3) - minDelta)
		assertTrue(FixCenti8.from(0.03333333333333333) < FixCenti8.from(0.03333333333333333) + minDelta)
		assertTrue(0.03333333333333333 < FixCenti8.from(0.03333333333333333) + minDelta)
		assertFalse(FixCenti8.from(4.233333333333333) < FixCenti8.from(4.233333333333333) - minDelta)
		assertFalse(4.233333333333333 < FixCenti8.from(4.233333333333333) - minDelta)
		assertTrue(FixCenti8.raw(Byte.MAX_VALUE) >= 4)
		assertTrue(FixCenti8.raw(Byte.MAX_VALUE) > 4)
		assertTrue(FixCenti8.raw(Byte.MAX_VALUE) < 5)
		assertTrue(FixCenti8.raw(Byte.MAX_VALUE) < 5.004999999999999)
		assertTrue(FixCenti8.raw(Byte.MAX_VALUE) < Byte.MAX_VALUE)
		assertTrue(FixCenti8.raw(Byte.MAX_VALUE) < Byte.MAX_VALUE.toFloat())
		assertTrue(FixCenti8.raw(Byte.MAX_VALUE) < Byte.MAX_VALUE.toDouble())
		assertTrue(FixCenti8.raw(Byte.MIN_VALUE) <= -4)
		assertTrue(FixCenti8.raw(Byte.MIN_VALUE) < -4)
		assertTrue(FixCenti8.raw(Byte.MIN_VALUE) > -5)
		assertTrue(FixCenti8.raw(Byte.MIN_VALUE) > -5.004999999999999)
		assertTrue(FixCenti8.raw(Byte.MIN_VALUE) > Byte.MIN_VALUE)
		assertTrue(FixCenti8.raw(Byte.MIN_VALUE) > Byte.MIN_VALUE.toFloat())
		assertTrue(FixCenti8.raw(Byte.MIN_VALUE) > Byte.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixCenti8.Array(2) { FixCenti8.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixCenti8.ONE, testArray[0])
		assertEquals(FixCenti8.ONE, testArray[1])
		testArray[1] = FixCenti8.ZERO
		assertEquals(FixCenti8.ONE, testArray[0])
		assertEquals(FixCenti8.ZERO, testArray[1])
		testArray.fill(FixCenti8.ZERO)
		assertEquals(FixCenti8.ZERO, testArray[0])
		assertEquals(FixCenti8.ZERO, testArray[1])
	}

	@Test
	fun testAbs() {
		assertEquals(FixCenti8.ZERO, abs(FixCenti8.ZERO))
		assertEquals(FixCenti8.ONE, abs(FixCenti8.ONE))
		assertEquals(FixCenti8.ONE, abs(-FixCenti8.ONE))
		assertEquals(FixCenti8.raw(Byte.MAX_VALUE), abs(FixCenti8.raw(Byte.MAX_VALUE)))
		assertEquals(FixCenti8.raw(Byte.MAX_VALUE), abs(-FixCenti8.raw(Byte.MAX_VALUE)))
		assertThrows(FixedPointException::class.java) { abs(FixCenti8.raw(Byte.MIN_VALUE)) }
	}

	@Test
	fun testMinMax() {
		assertEquals(FixCenti8.ZERO, min(FixCenti8.ZERO, FixCenti8.ZERO))
		assertEquals(FixCenti8.ZERO, max(FixCenti8.ZERO, FixCenti8.ZERO))
		assertEquals(FixCenti8.ZERO, min(FixCenti8.ONE, FixCenti8.ZERO))
		assertEquals(FixCenti8.ONE, max(FixCenti8.ONE, FixCenti8.ZERO))
		assertEquals(FixCenti8.ZERO, min(FixCenti8.ZERO, FixCenti8.ONE))
		assertEquals(FixCenti8.ONE, max(FixCenti8.ZERO, FixCenti8.ONE))
		assertEquals(FixCenti8.ZERO, min(FixCenti8.ZERO, FixCenti8.raw(Byte.MAX_VALUE)))
		assertEquals(FixCenti8.raw(Byte.MAX_VALUE), max(FixCenti8.ZERO, FixCenti8.raw(Byte.MAX_VALUE)))
		assertEquals(-FixCenti8.ONE, min(-FixCenti8.ONE, FixCenti8.ZERO))
		assertEquals(FixCenti8.ZERO, max(-FixCenti8.ONE, FixCenti8.ZERO))
		assertEquals(FixCenti8.raw(Byte.MIN_VALUE), min(FixCenti8.ZERO, FixCenti8.raw(Byte.MIN_VALUE)))
		assertEquals(FixCenti8.ZERO, max(FixCenti8.ZERO, FixCenti8.raw(Byte.MIN_VALUE)))
	}
}
