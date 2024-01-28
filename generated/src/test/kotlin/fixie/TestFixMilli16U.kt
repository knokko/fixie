package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixMilli16U {

	fun assertEquals(a: FixMilli16U, b: FixMilli16U, maxDelta: FixMilli16U) {
		val rawDifference = a.raw.toShort() - b.raw.toShort()
		if (rawDifference.absoluteValue > maxDelta.raw.toShort()) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixMilli16U.ZERO.toString())
		assertEquals("1", FixMilli16U.ONE.toString())
		assertTrue((FixMilli16U.ONE / 3).toString().startsWith("0.3"))
		assertTrue((FixMilli16U.from(64) + FixMilli16U.ONE / 3).toString().endsWith((FixMilli16U.ONE / 3).toString().substring(1)))
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixMilli16U.ONE, FixMilli16U.from(one))

		fun testValue(value: Int) = assertEquals(value, FixMilli16U.from(value).toInt())
		fun testOverflow(value: Int) = assertThrows(FixedPointException::class.java) { FixMilli16U.from(value) }
		testValue(0)
		testValue(1)
		testValue(65)

		testOverflow(-2147483648)
		testOverflow(-978689185)
		testOverflow(-974146)
		testOverflow(-740009)
		testOverflow(-16299)
		testOverflow(-1)

		testOverflow(66)
		testOverflow(11356)
		testOverflow(2716636)
		testOverflow(132636241)
		testOverflow(2147483647)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixMilli16U.ONE, FixMilli16U.from(one))

		fun testValue(value: Long) = assertEquals(value, FixMilli16U.from(value).toLong())
		fun testOverflow(value: Long) = assertThrows(FixedPointException::class.java) { FixMilli16U.from(value) }
		testValue(0)
		testValue(1)
		testValue(65)

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

		testOverflow(66)
		testOverflow(11356)
		testOverflow(2716636)
		testOverflow(132636241)
		testOverflow(3837660691)
		testOverflow(102037718619)
		testOverflow(1259591443114)
		testOverflow(527227515052805)
		testOverflow(12925866574619320)
		testOverflow(923512679707180754)
		testOverflow(9223372036854775807)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixMilli16U.ONE, FixMilli16U.from(1f))
		val delta = 0.002f
		assertEquals(0.001f, FixMilli16U.from(0.001f).toFloat(), delta)
		assertEquals(0.06078089f, FixMilli16U.from(0.06078089f).toFloat(), delta)
		assertEquals(3.2344286f, FixMilli16U.from(3.2344286f).toFloat(), delta)
		assertEquals(65.534996f, FixMilli16U.from(65.534996f).toFloat(), delta)

		assertThrows(FixedPointException::class.java) { FixMilli16U.from(66.535f) }
		assertThrows(FixedPointException::class.java) { FixMilli16U.from(-66.535f) }
		assertThrows(FixedPointException::class.java) { FixMilli16U.from(4294.8364f) }
		assertThrows(FixedPointException::class.java) { FixMilli16U.from(-4294.8364f) }
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixMilli16U.ONE, FixMilli16U.from(1.0))
		val delta = 0.002
		assertEquals(0.001, FixMilli16U.from(0.001).toDouble(), delta)
		assertEquals(0.06078089006529382, FixMilli16U.from(0.06078089006529382).toDouble(), delta)
		assertEquals(3.2344286451819104, FixMilli16U.from(3.2344286451819104).toDouble(), delta)
		assertEquals(65.535, FixMilli16U.from(65.535).toDouble(), delta)

		assertThrows(FixedPointException::class.java) { FixMilli16U.from(66.535) }
		assertThrows(FixedPointException::class.java) { FixMilli16U.from(-66.535) }
		assertThrows(FixedPointException::class.java) { FixMilli16U.from(4294.836224999999) }
		assertThrows(FixedPointException::class.java) { FixMilli16U.from(-4294.836224999999) }
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixMilli16U, b: FixMilli16U, c: FixMilli16U) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}

		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixMilli16U.from(a), FixMilli16U.from(b), FixMilli16U.from(c))
			assertEquals(FixMilli16U.from(c), FixMilli16U.from(a) + b)
			assertEquals(FixMilli16U.from(c), b + FixMilli16U.from(a))
			assertEquals(FixMilli16U.from(a), FixMilli16U.from(c) - b)
			assertEquals(FixMilli16U.from(b), c - FixMilli16U.from(a))
		}

		testValues(FixMilli16U.raw(UShort.MIN_VALUE), FixMilli16U.ONE, FixMilli16U.raw((UShort.MIN_VALUE + 1000u).toUShort()))
		testValues(0, 26, 26)
		testValues(1, 54, 55)
		testValues(65, 0, 65)
		testValues(FixMilli16U.raw((UShort.MAX_VALUE - 1000u).toUShort()), FixMilli16U.ONE, FixMilli16U.raw(UShort.MAX_VALUE))

		fun testOverflowPlus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) + FixMilli16U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMilli16U.from(b) }
		}

		fun testOverflowMinus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) - FixMilli16U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMilli16U.from(b) }
		}

		fun testOverflowPlus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) + FixMilli16U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMilli16U.from(b) }
		}

		fun testOverflowMinus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) - FixMilli16U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMilli16U.from(b) }
		}

		fun testOverflowPlus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) + FixMilli16U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMilli16U.from(b) }
		}

		fun testOverflowMinus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) - FixMilli16U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMilli16U.from(b) }
		}

		fun testOverflowPlus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) + FixMilli16U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMilli16U.from(b) }
		}

		fun testOverflowMinus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) - FixMilli16U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMilli16U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMilli16U.from(b) }
		}

		testOverflowPlus(0, 66)
		testOverflowPlus(0L, 66L)
		testOverflowPlus(0, 4489)
		testOverflowPlus(0L, 4489L)
		testOverflowMinus(0, 1)
		testOverflowMinus(0L, 1L)
		testOverflowMinus(0, 4)
		testOverflowMinus(0L, 4L)
		testOverflowPlus(1, 65)
		testOverflowPlus(1L, 65L)
		testOverflowPlus(1.0f, 65.0f)
		testOverflowPlus(1.0, 65.0)
		testOverflowPlus(1, 4356)
		testOverflowPlus(1L, 4356L)
		testOverflowPlus(1.0, 4356.0)
		testOverflowMinus(1, 2)
		testOverflowMinus(1L, 2L)
		testOverflowMinus(1.0f, 2.0f)
		testOverflowMinus(1.0, 2.0)
		testOverflowMinus(1, 9)
		testOverflowMinus(1L, 9L)
		testOverflowMinus(1.0f, 9.0f)
		testOverflowMinus(1.0, 9.0)
		testOverflowPlus(65, 1)
		testOverflowPlus(65L, 1L)
		testOverflowPlus(65.0f, 1.0f)
		testOverflowPlus(65.0, 1.0)
		testOverflowPlus(65, 4)
		testOverflowPlus(65L, 4L)
		testOverflowPlus(65.0f, 4.0f)
		testOverflowPlus(65.0, 4.0)
		testOverflowMinus(65, 66)
		testOverflowMinus(65L, 66L)
		testOverflowMinus(65.0f, 66.0f)
		testOverflowMinus(65.0, 66.0)
		testOverflowMinus(65, 4489)
		testOverflowMinus(65L, 4489L)
		testOverflowMinus(65.0f, 4489.0f)
		testOverflowMinus(65.0, 4489.0)
		assertEquals(FixMilli16U.raw(64440u), FixMilli16U.raw(440u) + 64)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixMilli16U.raw(UShort.MAX_VALUE), 1 * FixMilli16U.raw(UShort.MAX_VALUE))
		assertEquals(FixMilli16U.raw(UShort.MAX_VALUE), FixMilli16U.raw(UShort.MAX_VALUE) / 1)
		assertThrows(FixedPointException::class.java) { -1 * FixMilli16U.ONE }
		assertThrows(FixedPointException::class.java) { FixMilli16U.ONE / -1 }
		assertThrows(FixedPointException::class.java) { -1 * FixMilli16U.raw(UShort.MAX_VALUE)}
		assertThrows(FixedPointException::class.java) { FixMilli16U.raw(UShort.MAX_VALUE) / -1 }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixMilli16U.from(a * b), FixMilli16U.from(a) * FixMilli16U.from(b))
			assertEquals(FixMilli16U.from(a * b), FixMilli16U.from(a) * b)
			assertEquals(FixMilli16U.from(a * b), b * FixMilli16U.from(a))
			if (b != 0L) assertEquals(FixMilli16U.from(a), FixMilli16U.from(a * b) / b)
			if (a != 0L) assertEquals(FixMilli16U.from(b), FixMilli16U.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixMilli16U.from(b), FixMilli16U.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixMilli16U.from(a * b), FixMilli16U.from(a) * b.toInt())
				assertEquals(FixMilli16U.from(a * b), b.toInt() * FixMilli16U.from(a))
			}
		}
		testValues(0, 0)
		testValues(1, 65)
		testValues(65, 0)
		assertEquals(FixMilli16U.raw(634u), (FixMilli16U.raw(634u) * FixMilli16U.raw(701u)) / FixMilli16U.raw(701u), FixMilli16U.raw(10u))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixMilli16U.ZERO < FixMilli16U.ONE)
		assertTrue(0 < FixMilli16U.ONE)
		assertFalse(FixMilli16U.ZERO > FixMilli16U.ONE)
		assertFalse(0 > FixMilli16U.ONE)
		assertFalse(FixMilli16U.ONE < FixMilli16U.ONE)
		assertFalse(FixMilli16U.ONE < 1)
		assertFalse(FixMilli16U.ONE > FixMilli16U.ONE)
		assertTrue(FixMilli16U.ONE <= FixMilli16U.ONE)
		assertTrue(FixMilli16U.ONE >= FixMilli16U.ONE)
		assertTrue(FixMilli16U.raw(UShort.MIN_VALUE) < FixMilli16U.raw(UShort.MAX_VALUE))

		val minDelta = FixMilli16U.raw(1u)
		assertEquals(FixMilli16U.from(12), FixMilli16U.from(12))
		assertNotEquals(FixMilli16U.from(12), FixMilli16U.from(12) - minDelta)
		assertTrue(FixMilli16U.from(0.001) < FixMilli16U.from(0.001) + minDelta)
		assertTrue(0.001 < FixMilli16U.from(0.001) + minDelta)
		assertFalse(FixMilli16U.from(0.41012452706744895) < FixMilli16U.from(0.41012452706744895) - minDelta)
		assertFalse(0.41012452706744895 < FixMilli16U.from(0.41012452706744895) - minDelta)
		assertTrue(FixMilli16U.from(0.41012452706744895) < FixMilli16U.from(0.41012452706744895) + minDelta)
		assertTrue(0.41012452706744895 < FixMilli16U.from(0.41012452706744895) + minDelta)
		assertFalse(FixMilli16U.from(1.4602674387652097) < FixMilli16U.from(1.4602674387652097) - minDelta)
		assertFalse(1.4602674387652097 < FixMilli16U.from(1.4602674387652097) - minDelta)
		assertTrue(FixMilli16U.from(1.4602674387652097) < FixMilli16U.from(1.4602674387652097) + minDelta)
		assertTrue(1.4602674387652097 < FixMilli16U.from(1.4602674387652097) + minDelta)
		assertFalse(FixMilli16U.from(65.535) < FixMilli16U.from(65.535) - minDelta)
		assertFalse(65.535 < FixMilli16U.from(65.535) - minDelta)
		assertTrue(FixMilli16U.raw(UShort.MAX_VALUE) >= 65)
		assertTrue(FixMilli16U.raw(UShort.MAX_VALUE) > 65)
		assertTrue(FixMilli16U.raw(UShort.MAX_VALUE) < 66)
		assertTrue(FixMilli16U.raw(UShort.MAX_VALUE) < 66.06599999999999)
		assertTrue(FixMilli16U.raw(UShort.MAX_VALUE) < UShort.MAX_VALUE)
		assertTrue(FixMilli16U.raw(UShort.MAX_VALUE) < UShort.MAX_VALUE.toFloat())
		assertTrue(FixMilli16U.raw(UShort.MAX_VALUE) < UShort.MAX_VALUE.toDouble())
		assertTrue(FixMilli16U.ZERO > -1)
		assertTrue(FixMilli16U.ZERO > -0.001f)
		assertTrue(FixMilli16U.ZERO > -0.001)
		assertTrue(FixMilli16U.ZERO > Long.MIN_VALUE)
		assertTrue(FixMilli16U.ZERO > Long.MIN_VALUE.toFloat())
		assertTrue(FixMilli16U.ZERO > Long.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixMilli16U.Array(2) { FixMilli16U.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixMilli16U.ONE, testArray[0])
		assertEquals(FixMilli16U.ONE, testArray[1])
		testArray[1] = FixMilli16U.ZERO
		assertEquals(FixMilli16U.ONE, testArray[0])
		assertEquals(FixMilli16U.ZERO, testArray[1])
		testArray.fill(FixMilli16U.ZERO)
		assertEquals(FixMilli16U.ZERO, testArray[0])
		assertEquals(FixMilli16U.ZERO, testArray[1])
	}
}
