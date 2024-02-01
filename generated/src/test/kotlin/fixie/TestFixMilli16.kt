package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixMilli16 {

	fun assertEquals(a: FixMilli16, b: FixMilli16, maxDelta: FixMilli16) {
		val rawDifference = a.raw - b.raw
		if (rawDifference.absoluteValue > maxDelta.raw) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixMilli16.ZERO.toString())
		assertEquals("1", FixMilli16.ONE.toString())
		assertTrue((FixMilli16.ONE / 3).toString().startsWith("0.3"))
		assertEquals("-1", (-FixMilli16.ONE).toString())
		assertTrue((FixMilli16.ONE / -3).toString().startsWith("-0.3"))
		assertTrue((FixMilli16.from(31) + FixMilli16.ONE / 3).toString().endsWith((FixMilli16.ONE / 3).toString().substring(1)))
		assertEquals("0.01", (FixMilli16.ONE / 100).toString())
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixMilli16.ONE, FixMilli16.from(one))

		fun testValue(value: Int) = assertEquals(value, FixMilli16.from(value).toInt())
		fun testOverflow(value: Int) = assertThrows(FixedPointException::class.java) { FixMilli16.from(value) }
		testValue(-32)
		testValue(0)
		testValue(1)
		testValue(32)

		testOverflow(-2147483648)
		testOverflow(-978689185)
		testOverflow(-974146)
		testOverflow(-740009)
		testOverflow(-16299)
		testOverflow(-33)

		testOverflow(33)
		testOverflow(37992)
		testOverflow(1025975)
		testOverflow(97658386)
		testOverflow(2147483647)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixMilli16.ONE, FixMilli16.from(one))

		fun testValue(value: Long) = assertEquals(value, FixMilli16.from(value).toLong())
		fun testOverflow(value: Long) = assertThrows(FixedPointException::class.java) { FixMilli16.from(value) }
		testValue(-32)
		testValue(0)
		testValue(1)
		testValue(32)

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
		testOverflow(-33)

		testOverflow(33)
		testOverflow(37992)
		testOverflow(1025975)
		testOverflow(97658386)
		testOverflow(4653877631)
		testOverflow(140731211783)
		testOverflow(555937046336)
		testOverflow(424337435280393)
		testOverflow(3594921538286243)
		testOverflow(976380258828249770)
		testOverflow(9223372036854775807)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixMilli16.ONE, FixMilli16.from(1f))
		val delta = 0.002f
		assertEquals(0.001f, FixMilli16.from(0.001f).toFloat(), delta)
		assertEquals(-0.001f, FixMilli16.from(-0.001f).toFloat(), delta)
		assertEquals(0.06078089f, FixMilli16.from(0.06078089f).toFloat(), delta)
		assertEquals(-0.06078089f, FixMilli16.from(-0.06078089f).toFloat(), delta)
		assertEquals(3.2344286f, FixMilli16.from(3.2344286f).toFloat(), delta)
		assertEquals(-3.2344286f, FixMilli16.from(-3.2344286f).toFloat(), delta)
		assertEquals(32.767f, FixMilli16.from(32.767f).toFloat(), delta)
		assertEquals(-32.767f, FixMilli16.from(-32.767f).toFloat(), delta)

		assertThrows(FixedPointException::class.java) { FixMilli16.from(33.767f) }
		assertThrows(FixedPointException::class.java) { FixMilli16.from(-33.767f) }
		assertThrows(FixedPointException::class.java) { FixMilli16.from(1073.6763f) }
		assertThrows(FixedPointException::class.java) { FixMilli16.from(-1073.6763f) }
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixMilli16.ONE, FixMilli16.from(1.0))
		val delta = 0.002
		assertEquals(0.001, FixMilli16.from(0.001).toDouble(), delta)
		assertEquals(-0.001, FixMilli16.from(-0.001).toDouble(), delta)
		assertEquals(0.06078089006529382, FixMilli16.from(0.06078089006529382).toDouble(), delta)
		assertEquals(-0.06078089006529382, FixMilli16.from(-0.06078089006529382).toDouble(), delta)
		assertEquals(3.2344286451819104, FixMilli16.from(3.2344286451819104).toDouble(), delta)
		assertEquals(-3.2344286451819104, FixMilli16.from(-3.2344286451819104).toDouble(), delta)
		assertEquals(32.766999999999996, FixMilli16.from(32.766999999999996).toDouble(), delta)
		assertEquals(-32.766999999999996, FixMilli16.from(-32.766999999999996).toDouble(), delta)

		assertThrows(FixedPointException::class.java) { FixMilli16.from(33.767) }
		assertThrows(FixedPointException::class.java) { FixMilli16.from(-33.767) }
		assertThrows(FixedPointException::class.java) { FixMilli16.from(1073.6762890000002) }
		assertThrows(FixedPointException::class.java) { FixMilli16.from(-1073.6762890000002) }
	}

	@Test
	fun testUnaryMinus() {
		assertThrows(FixedPointException::class.java) { -FixMilli16.raw(Short.MIN_VALUE) }
		assertEquals(32767, -FixMilli16.raw(-32767).raw)
		assertEquals(4180, -FixMilli16.raw(-4180).raw)
		assertEquals(233, -FixMilli16.raw(-233).raw)
		assertEquals(0, -FixMilli16.raw(0).raw)
		assertEquals(-1, -FixMilli16.raw(1).raw)
		assertEquals(-10469, -FixMilli16.raw(10469).raw)
		assertEquals(-32767, -FixMilli16.raw(32767).raw)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixMilli16, b: FixMilli16, c: FixMilli16) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}

		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixMilli16.from(a), FixMilli16.from(b), FixMilli16.from(c))
			assertEquals(FixMilli16.from(c), FixMilli16.from(a) + b)
			assertEquals(FixMilli16.from(c), b + FixMilli16.from(a))
			assertEquals(FixMilli16.from(a), FixMilli16.from(c) - b)
			assertEquals(FixMilli16.from(b), c - FixMilli16.from(a))
		}

		testValues(FixMilli16.raw(Short.MIN_VALUE), FixMilli16.ONE, FixMilli16.raw((Short.MIN_VALUE + 1000).toShort()))
		testValues(-32, 8, -24)
		testValues(0, 9, 9)
		testValues(1, 0, 1)
		testValues(6, 14, 20)
		testValues(13, 11, 24)
		testValues(32, 0, 32)
		testValues(FixMilli16.raw((Short.MAX_VALUE - 1000).toShort()), FixMilli16.ONE, FixMilli16.raw(Short.MAX_VALUE))

		fun testOverflowPlus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) + FixMilli16.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMilli16.from(b) }
		}

		fun testOverflowMinus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) - FixMilli16.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMilli16.from(b) }
		}

		fun testOverflowPlus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) + FixMilli16.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMilli16.from(b) }
		}

		fun testOverflowMinus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) - FixMilli16.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMilli16.from(b) }
		}

		fun testOverflowPlus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) + FixMilli16.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMilli16.from(b) }
		}

		fun testOverflowMinus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) - FixMilli16.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMilli16.from(b) }
		}

		fun testOverflowPlus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) + FixMilli16.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMilli16.from(b) }
		}

		fun testOverflowMinus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) - FixMilli16.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMilli16.from(b) }
		}

		testOverflowPlus(-32, 65)
		testOverflowPlus(-32L, 65L)
		testOverflowPlus(-32.0f, 65.0f)
		testOverflowPlus(-32.0, 65.0)
		testOverflowPlus(-32, 4356)
		testOverflowPlus(-32L, 4356L)
		testOverflowPlus(-32.0, 4356.0)
		testOverflowMinus(-32, 1)
		testOverflowMinus(-32L, 1L)
		testOverflowMinus(-32.0f, 1.0f)
		testOverflowMinus(-32.0, 1.0)
		testOverflowMinus(-32, 4)
		testOverflowMinus(-32L, 4L)
		testOverflowMinus(-32.0f, 4.0f)
		testOverflowMinus(-32.0, 4.0)
		testOverflowPlus(0, 33)
		testOverflowPlus(0L, 33L)
		testOverflowPlus(0, 1156)
		testOverflowPlus(0L, 1156L)
		testOverflowMinus(0, 33)
		testOverflowMinus(0L, 33L)
		testOverflowMinus(0, 1156)
		testOverflowMinus(0L, 1156L)
		testOverflowPlus(1, 32)
		testOverflowPlus(1L, 32L)
		testOverflowPlus(1.0f, 32.0f)
		testOverflowPlus(1.0, 32.0)
		testOverflowPlus(1, 1089)
		testOverflowPlus(1L, 1089L)
		testOverflowPlus(1.0, 1089.0)
		testOverflowMinus(1, 34)
		testOverflowMinus(1L, 34L)
		testOverflowMinus(1.0f, 34.0f)
		testOverflowMinus(1.0, 34.0)
		testOverflowMinus(1, 1225)
		testOverflowMinus(1L, 1225L)
		testOverflowMinus(1.0, 1225.0)
		testOverflowPlus(32, 1)
		testOverflowPlus(32L, 1L)
		testOverflowPlus(32.0f, 1.0f)
		testOverflowPlus(32.0, 1.0)
		testOverflowPlus(32, 4)
		testOverflowPlus(32L, 4L)
		testOverflowPlus(32.0f, 4.0f)
		testOverflowPlus(32.0, 4.0)
		testOverflowMinus(32, 65)
		testOverflowMinus(32L, 65L)
		testOverflowMinus(32.0f, 65.0f)
		testOverflowMinus(32.0, 65.0)
		testOverflowMinus(32, 4356)
		testOverflowMinus(32L, 4356L)
		testOverflowMinus(32.0, 4356.0)
		assertEquals(FixMilli16.raw(31019), FixMilli16.raw(-981) + 32)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixMilli16.raw(Short.MAX_VALUE), 1 * FixMilli16.raw(Short.MAX_VALUE))
		assertEquals(FixMilli16.raw(Short.MAX_VALUE), FixMilli16.raw(Short.MAX_VALUE) / 1)
		assertEquals(FixMilli16.raw(Short.MIN_VALUE), 1 * FixMilli16.raw(Short.MIN_VALUE))
		assertEquals(FixMilli16.raw(Short.MIN_VALUE), FixMilli16.raw(Short.MIN_VALUE) / 1)
		assertEquals(FixMilli16.raw((Short.MIN_VALUE + 1).toShort()), -1 * FixMilli16.raw(Short.MAX_VALUE))
		assertEquals(FixMilli16.raw((Short.MIN_VALUE + 1).toShort()), FixMilli16.raw(Short.MAX_VALUE) / -1)
		assertThrows(FixedPointException::class.java) { -1 * FixMilli16.raw(Short.MIN_VALUE) }
		assertThrows(FixedPointException::class.java) { FixMilli16.raw(Short.MIN_VALUE) / -1 }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixMilli16.from(a * b), FixMilli16.from(a) * FixMilli16.from(b))
			assertEquals(FixMilli16.from(a * b), FixMilli16.from(a) * b)
			assertEquals(FixMilli16.from(a * b), b * FixMilli16.from(a))
			if (b != 0L) assertEquals(FixMilli16.from(a), FixMilli16.from(a * b) / b)
			if (a != 0L) assertEquals(FixMilli16.from(b), FixMilli16.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixMilli16.from(b), FixMilli16.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixMilli16.from(a * b), FixMilli16.from(a) * b.toInt())
				assertEquals(FixMilli16.from(a * b), b.toInt() * FixMilli16.from(a))
			}
		}
		assertThrows(FixedPointException::class.java) { FixMilli16.from(-32) * 4 }
		testValues(0, 1)
		testValues(1, 4)
		assertThrows(FixedPointException::class.java) { FixMilli16.from(4) * 32 }
		assertThrows(FixedPointException::class.java) { FixMilli16.from(7) * -32 }
		assertThrows(FixedPointException::class.java) { FixMilli16.from(32) * 32 }
		assertEquals(FixMilli16.raw(-958), (FixMilli16.raw(-958) * FixMilli16.raw(670)) / FixMilli16.raw(670), FixMilli16.raw(10))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixMilli16.ZERO < FixMilli16.ONE)
		assertTrue(0 < FixMilli16.ONE)
		assertFalse(FixMilli16.ZERO > FixMilli16.ONE)
		assertFalse(0 > FixMilli16.ONE)
		assertFalse(FixMilli16.ONE < FixMilli16.ONE)
		assertFalse(FixMilli16.ONE < 1)
		assertFalse(FixMilli16.ONE > FixMilli16.ONE)
		assertTrue(FixMilli16.ONE <= FixMilli16.ONE)
		assertTrue(FixMilli16.ONE >= FixMilli16.ONE)
		assertTrue(FixMilli16.raw(Short.MIN_VALUE) < FixMilli16.raw(Short.MAX_VALUE))

		val minDelta = FixMilli16.raw(1)
		assertEquals(FixMilli16.from(12), FixMilli16.from(12))
		assertNotEquals(FixMilli16.from(12), FixMilli16.from(12) - minDelta)
		assertTrue(FixMilli16.from(0.001) < FixMilli16.from(0.001) + minDelta)
		assertTrue(0.001 < FixMilli16.from(0.001) + minDelta)
		assertFalse(FixMilli16.from(0.41012452706744895) < FixMilli16.from(0.41012452706744895) - minDelta)
		assertFalse(0.41012452706744895 < FixMilli16.from(0.41012452706744895) - minDelta)
		assertTrue(FixMilli16.from(0.41012452706744895) < FixMilli16.from(0.41012452706744895) + minDelta)
		assertTrue(0.41012452706744895 < FixMilli16.from(0.41012452706744895) + minDelta)
		assertFalse(FixMilli16.from(1.4602674387652097) < FixMilli16.from(1.4602674387652097) - minDelta)
		assertFalse(1.4602674387652097 < FixMilli16.from(1.4602674387652097) - minDelta)
		assertTrue(FixMilli16.from(1.4602674387652097) < FixMilli16.from(1.4602674387652097) + minDelta)
		assertTrue(1.4602674387652097 < FixMilli16.from(1.4602674387652097) + minDelta)
		assertFalse(FixMilli16.from(32.766999999999996) < FixMilli16.from(32.766999999999996) - minDelta)
		assertFalse(32.766999999999996 < FixMilli16.from(32.766999999999996) - minDelta)
		assertTrue(FixMilli16.raw(Short.MAX_VALUE) >= 32)
		assertTrue(FixMilli16.raw(Short.MAX_VALUE) > 32)
		assertTrue(FixMilli16.raw(Short.MAX_VALUE) < 33)
		assertTrue(FixMilli16.raw(Short.MAX_VALUE) < 33.032999999999994)
		assertTrue(FixMilli16.raw(Short.MAX_VALUE) < Short.MAX_VALUE)
		assertTrue(FixMilli16.raw(Short.MAX_VALUE) < Short.MAX_VALUE.toFloat())
		assertTrue(FixMilli16.raw(Short.MAX_VALUE) < Short.MAX_VALUE.toDouble())
		assertTrue(FixMilli16.raw(Short.MIN_VALUE) <= -32)
		assertTrue(FixMilli16.raw(Short.MIN_VALUE) < -32)
		assertTrue(FixMilli16.raw(Short.MIN_VALUE) > -33)
		assertTrue(FixMilli16.raw(Short.MIN_VALUE) > -33.032999999999994)
		assertTrue(FixMilli16.raw(Short.MIN_VALUE) > Short.MIN_VALUE)
		assertTrue(FixMilli16.raw(Short.MIN_VALUE) > Short.MIN_VALUE.toFloat())
		assertTrue(FixMilli16.raw(Short.MIN_VALUE) > Short.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixMilli16.Array(2) { FixMilli16.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixMilli16.ONE, testArray[0])
		assertEquals(FixMilli16.ONE, testArray[1])
		testArray[1] = FixMilli16.ZERO
		assertEquals(FixMilli16.ONE, testArray[0])
		assertEquals(FixMilli16.ZERO, testArray[1])
		testArray.fill(FixMilli16.ZERO)
		assertEquals(FixMilli16.ZERO, testArray[0])
		assertEquals(FixMilli16.ZERO, testArray[1])
	}

	@Test
	fun testAbs() {
		assertEquals(FixMilli16.ZERO, abs(FixMilli16.ZERO))
		assertEquals(FixMilli16.ONE, abs(FixMilli16.ONE))
		assertEquals(FixMilli16.ONE, abs(-FixMilli16.ONE))
		assertEquals(FixMilli16.raw(Short.MAX_VALUE), abs(FixMilli16.raw(Short.MAX_VALUE)))
		assertEquals(FixMilli16.raw(Short.MAX_VALUE), abs(-FixMilli16.raw(Short.MAX_VALUE)))
		assertThrows(FixedPointException::class.java) { abs(FixMilli16.raw(Short.MIN_VALUE)) }
	}

	@Test
	fun testMinMax() {
		assertEquals(FixMilli16.ZERO, min(FixMilli16.ZERO, FixMilli16.ZERO))
		assertEquals(FixMilli16.ZERO, max(FixMilli16.ZERO, FixMilli16.ZERO))
		assertEquals(FixMilli16.ZERO, min(FixMilli16.ONE, FixMilli16.ZERO))
		assertEquals(FixMilli16.ONE, max(FixMilli16.ONE, FixMilli16.ZERO))
		assertEquals(FixMilli16.ZERO, min(FixMilli16.ZERO, FixMilli16.ONE))
		assertEquals(FixMilli16.ONE, max(FixMilli16.ZERO, FixMilli16.ONE))
		assertEquals(FixMilli16.ZERO, min(FixMilli16.ZERO, FixMilli16.raw(Short.MAX_VALUE)))
		assertEquals(FixMilli16.raw(Short.MAX_VALUE), max(FixMilli16.ZERO, FixMilli16.raw(Short.MAX_VALUE)))
		assertEquals(-FixMilli16.ONE, min(-FixMilli16.ONE, FixMilli16.ZERO))
		assertEquals(FixMilli16.ZERO, max(-FixMilli16.ONE, FixMilli16.ZERO))
		assertEquals(FixMilli16.raw(Short.MIN_VALUE), min(FixMilli16.ZERO, FixMilli16.raw(Short.MIN_VALUE)))
		assertEquals(FixMilli16.ZERO, max(FixMilli16.ZERO, FixMilli16.raw(Short.MIN_VALUE)))
	}
}
