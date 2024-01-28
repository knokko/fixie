package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixUncheckedMilli16 {

	fun assertEquals(a: FixUncheckedMilli16, b: FixUncheckedMilli16, maxDelta: FixUncheckedMilli16) {
		val rawDifference = a.raw - b.raw
		if (rawDifference.absoluteValue > maxDelta.raw) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixUncheckedMilli16.ZERO.toString())
		assertEquals("1", FixUncheckedMilli16.ONE.toString())
		assertTrue((FixUncheckedMilli16.ONE / 3).toString().startsWith("0.3"))
		assertEquals("-1", (-FixUncheckedMilli16.ONE).toString())
		assertTrue((FixUncheckedMilli16.ONE / -3).toString().startsWith("-0.3"))
		assertTrue((FixUncheckedMilli16.from(31) + FixUncheckedMilli16.ONE / 3).toString().endsWith((FixUncheckedMilli16.ONE / 3).toString().substring(1)))
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixUncheckedMilli16.ONE, FixUncheckedMilli16.from(one))

		fun testValue(value: Int) = assertEquals(value, FixUncheckedMilli16.from(value).toInt())
		testValue(-32)
		testValue(0)
		testValue(1)
		testValue(32)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixUncheckedMilli16.ONE, FixUncheckedMilli16.from(one))

		fun testValue(value: Long) = assertEquals(value, FixUncheckedMilli16.from(value).toLong())
		testValue(-32)
		testValue(0)
		testValue(1)
		testValue(32)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixUncheckedMilli16.ONE, FixUncheckedMilli16.from(1f))
		val delta = 0.002f
		assertEquals(0.001f, FixUncheckedMilli16.from(0.001f).toFloat(), delta)
		assertEquals(-0.001f, FixUncheckedMilli16.from(-0.001f).toFloat(), delta)
		assertEquals(0.06078089f, FixUncheckedMilli16.from(0.06078089f).toFloat(), delta)
		assertEquals(-0.06078089f, FixUncheckedMilli16.from(-0.06078089f).toFloat(), delta)
		assertEquals(3.2344286f, FixUncheckedMilli16.from(3.2344286f).toFloat(), delta)
		assertEquals(-3.2344286f, FixUncheckedMilli16.from(-3.2344286f).toFloat(), delta)
		assertEquals(32.767f, FixUncheckedMilli16.from(32.767f).toFloat(), delta)
		assertEquals(-32.767f, FixUncheckedMilli16.from(-32.767f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixUncheckedMilli16.ONE, FixUncheckedMilli16.from(1.0))
		val delta = 0.002
		assertEquals(0.001, FixUncheckedMilli16.from(0.001).toDouble(), delta)
		assertEquals(-0.001, FixUncheckedMilli16.from(-0.001).toDouble(), delta)
		assertEquals(0.06078089006529382, FixUncheckedMilli16.from(0.06078089006529382).toDouble(), delta)
		assertEquals(-0.06078089006529382, FixUncheckedMilli16.from(-0.06078089006529382).toDouble(), delta)
		assertEquals(3.2344286451819104, FixUncheckedMilli16.from(3.2344286451819104).toDouble(), delta)
		assertEquals(-3.2344286451819104, FixUncheckedMilli16.from(-3.2344286451819104).toDouble(), delta)
		assertEquals(32.766999999999996, FixUncheckedMilli16.from(32.766999999999996).toDouble(), delta)
		assertEquals(-32.766999999999996, FixUncheckedMilli16.from(-32.766999999999996).toDouble(), delta)
	}

	@Test
	fun testUnaryMinus() {
		assertEquals(32767, -FixUncheckedMilli16.raw(-32767).raw)
		assertEquals(4180, -FixUncheckedMilli16.raw(-4180).raw)
		assertEquals(233, -FixUncheckedMilli16.raw(-233).raw)
		assertEquals(0, -FixUncheckedMilli16.raw(0).raw)
		assertEquals(-1, -FixUncheckedMilli16.raw(1).raw)
		assertEquals(-10469, -FixUncheckedMilli16.raw(10469).raw)
		assertEquals(-32767, -FixUncheckedMilli16.raw(32767).raw)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixUncheckedMilli16, b: FixUncheckedMilli16, c: FixUncheckedMilli16) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}

		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixUncheckedMilli16.from(a), FixUncheckedMilli16.from(b), FixUncheckedMilli16.from(c))
			assertEquals(FixUncheckedMilli16.from(c), FixUncheckedMilli16.from(a) + b)
			assertEquals(FixUncheckedMilli16.from(c), b + FixUncheckedMilli16.from(a))
			assertEquals(FixUncheckedMilli16.from(a), FixUncheckedMilli16.from(c) - b)
			assertEquals(FixUncheckedMilli16.from(b), c - FixUncheckedMilli16.from(a))
		}

		testValues(FixUncheckedMilli16.raw(Short.MIN_VALUE), FixUncheckedMilli16.ONE, FixUncheckedMilli16.raw((Short.MIN_VALUE + 1000).toShort()))
		testValues(-32, 8, -24)
		testValues(0, 9, 9)
		testValues(1, 0, 1)
		testValues(6, 14, 20)
		testValues(13, 11, 24)
		testValues(32, 0, 32)
		testValues(FixUncheckedMilli16.raw((Short.MAX_VALUE - 1000).toShort()), FixUncheckedMilli16.ONE, FixUncheckedMilli16.raw(Short.MAX_VALUE))
		assertEquals(FixUncheckedMilli16.raw(31019), FixUncheckedMilli16.raw(-981) + 32)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixUncheckedMilli16.raw(Short.MAX_VALUE), 1 * FixUncheckedMilli16.raw(Short.MAX_VALUE))
		assertEquals(FixUncheckedMilli16.raw(Short.MAX_VALUE), FixUncheckedMilli16.raw(Short.MAX_VALUE) / 1)
		assertEquals(FixUncheckedMilli16.raw(Short.MIN_VALUE), 1 * FixUncheckedMilli16.raw(Short.MIN_VALUE))
		assertEquals(FixUncheckedMilli16.raw(Short.MIN_VALUE), FixUncheckedMilli16.raw(Short.MIN_VALUE) / 1)
		assertEquals(FixUncheckedMilli16.raw((Short.MIN_VALUE + 1).toShort()), -1 * FixUncheckedMilli16.raw(Short.MAX_VALUE))
		assertEquals(FixUncheckedMilli16.raw((Short.MIN_VALUE + 1).toShort()), FixUncheckedMilli16.raw(Short.MAX_VALUE) / -1)

		fun testValues(a: Long, b: Long) {
			assertEquals(FixUncheckedMilli16.from(a * b), FixUncheckedMilli16.from(a) * FixUncheckedMilli16.from(b))
			assertEquals(FixUncheckedMilli16.from(a * b), FixUncheckedMilli16.from(a) * b)
			assertEquals(FixUncheckedMilli16.from(a * b), b * FixUncheckedMilli16.from(a))
			if (b != 0L) assertEquals(FixUncheckedMilli16.from(a), FixUncheckedMilli16.from(a * b) / b)
			if (a != 0L) assertEquals(FixUncheckedMilli16.from(b), FixUncheckedMilli16.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixUncheckedMilli16.from(b), FixUncheckedMilli16.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixUncheckedMilli16.from(a * b), FixUncheckedMilli16.from(a) * b.toInt())
				assertEquals(FixUncheckedMilli16.from(a * b), b.toInt() * FixUncheckedMilli16.from(a))
			}
		}
		testValues(0, 1)
		testValues(1, 4)
		assertEquals(FixUncheckedMilli16.raw(-958), (FixUncheckedMilli16.raw(-958) * FixUncheckedMilli16.raw(670)) / FixUncheckedMilli16.raw(670), FixUncheckedMilli16.raw(10))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixUncheckedMilli16.ZERO < FixUncheckedMilli16.ONE)
		assertTrue(0 < FixUncheckedMilli16.ONE)
		assertFalse(FixUncheckedMilli16.ZERO > FixUncheckedMilli16.ONE)
		assertFalse(0 > FixUncheckedMilli16.ONE)
		assertFalse(FixUncheckedMilli16.ONE < FixUncheckedMilli16.ONE)
		assertFalse(FixUncheckedMilli16.ONE < 1)
		assertFalse(FixUncheckedMilli16.ONE > FixUncheckedMilli16.ONE)
		assertTrue(FixUncheckedMilli16.ONE <= FixUncheckedMilli16.ONE)
		assertTrue(FixUncheckedMilli16.ONE >= FixUncheckedMilli16.ONE)
		assertTrue(FixUncheckedMilli16.raw(Short.MIN_VALUE) < FixUncheckedMilli16.raw(Short.MAX_VALUE))

		val minDelta = FixUncheckedMilli16.raw(1)
		assertEquals(FixUncheckedMilli16.from(12), FixUncheckedMilli16.from(12))
		assertNotEquals(FixUncheckedMilli16.from(12), FixUncheckedMilli16.from(12) - minDelta)
		assertTrue(FixUncheckedMilli16.from(0.001) < FixUncheckedMilli16.from(0.001) + minDelta)
		assertTrue(0.001 < FixUncheckedMilli16.from(0.001) + minDelta)
		assertFalse(FixUncheckedMilli16.from(0.41012452706744895) < FixUncheckedMilli16.from(0.41012452706744895) - minDelta)
		assertFalse(0.41012452706744895 < FixUncheckedMilli16.from(0.41012452706744895) - minDelta)
		assertTrue(FixUncheckedMilli16.from(0.41012452706744895) < FixUncheckedMilli16.from(0.41012452706744895) + minDelta)
		assertTrue(0.41012452706744895 < FixUncheckedMilli16.from(0.41012452706744895) + minDelta)
		assertFalse(FixUncheckedMilli16.from(1.4602674387652097) < FixUncheckedMilli16.from(1.4602674387652097) - minDelta)
		assertFalse(1.4602674387652097 < FixUncheckedMilli16.from(1.4602674387652097) - minDelta)
		assertTrue(FixUncheckedMilli16.from(1.4602674387652097) < FixUncheckedMilli16.from(1.4602674387652097) + minDelta)
		assertTrue(1.4602674387652097 < FixUncheckedMilli16.from(1.4602674387652097) + minDelta)
		assertFalse(FixUncheckedMilli16.from(32.766999999999996) < FixUncheckedMilli16.from(32.766999999999996) - minDelta)
		assertFalse(32.766999999999996 < FixUncheckedMilli16.from(32.766999999999996) - minDelta)
		assertTrue(FixUncheckedMilli16.raw(Short.MAX_VALUE) >= 32)
		assertTrue(FixUncheckedMilli16.raw(Short.MAX_VALUE) > 32)
		assertTrue(FixUncheckedMilli16.raw(Short.MAX_VALUE) < 33)
		assertTrue(FixUncheckedMilli16.raw(Short.MAX_VALUE) < 33.032999999999994)
		assertTrue(FixUncheckedMilli16.raw(Short.MAX_VALUE) < Short.MAX_VALUE)
		assertTrue(FixUncheckedMilli16.raw(Short.MAX_VALUE) < Short.MAX_VALUE.toFloat())
		assertTrue(FixUncheckedMilli16.raw(Short.MAX_VALUE) < Short.MAX_VALUE.toDouble())
		assertTrue(FixUncheckedMilli16.raw(Short.MIN_VALUE) <= -32)
		assertTrue(FixUncheckedMilli16.raw(Short.MIN_VALUE) < -32)
		assertTrue(FixUncheckedMilli16.raw(Short.MIN_VALUE) > -33)
		assertTrue(FixUncheckedMilli16.raw(Short.MIN_VALUE) > -33.032999999999994)
		assertTrue(FixUncheckedMilli16.raw(Short.MIN_VALUE) > Short.MIN_VALUE)
		assertTrue(FixUncheckedMilli16.raw(Short.MIN_VALUE) > Short.MIN_VALUE.toFloat())
		assertTrue(FixUncheckedMilli16.raw(Short.MIN_VALUE) > Short.MIN_VALUE.toDouble())
	}
}
