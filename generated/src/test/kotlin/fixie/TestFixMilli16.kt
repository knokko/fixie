package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixMilli16 {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixMilli16.from(value).toInt())
		testValue(-32)
		testValue(0)
		testValue(1)
		testValue(32)
	}

	@Test
	fun testLongConversion() {
		fun testValue(value: Long) = assertEquals(value, FixMilli16.from(value).toLong())
		testValue(-32)
		testValue(0)
		testValue(1)
		testValue(32)
	}

	@Test
	fun testFloatConversion() {
		val delta = 0.002f
		assertEquals(0.001f, FixMilli16.from(0.001f).toFloat(), delta)
		assertEquals(-0.001f, FixMilli16.from(-0.001f).toFloat(), delta)
		assertEquals(0.06078089f, FixMilli16.from(0.06078089f).toFloat(), delta)
		assertEquals(-0.06078089f, FixMilli16.from(-0.06078089f).toFloat(), delta)
		assertEquals(3.2344286f, FixMilli16.from(3.2344286f).toFloat(), delta)
		assertEquals(-3.2344286f, FixMilli16.from(-3.2344286f).toFloat(), delta)
		assertEquals(32.767f, FixMilli16.from(32.767f).toFloat(), delta)
		assertEquals(-32.767f, FixMilli16.from(-32.767f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 0.002
		assertEquals(0.001, FixMilli16.from(0.001).toDouble(), delta)
		assertEquals(-0.001, FixMilli16.from(-0.001).toDouble(), delta)
		assertEquals(0.06078089006529382, FixMilli16.from(0.06078089006529382).toDouble(), delta)
		assertEquals(-0.06078089006529382, FixMilli16.from(-0.06078089006529382).toDouble(), delta)
		assertEquals(3.2344286451819104, FixMilli16.from(3.2344286451819104).toDouble(), delta)
		assertEquals(-3.2344286451819104, FixMilli16.from(-3.2344286451819104).toDouble(), delta)
		assertEquals(32.766999999999996, FixMilli16.from(32.766999999999996).toDouble(), delta)
		assertEquals(-32.766999999999996, FixMilli16.from(-32.766999999999996).toDouble(), delta)
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
		testValues(-32, 4, -28)
		testValues(0, 4, 4)
		testValues(1, 0, 1)
		testValues(16, 2, 18)
		testValues(FixMilli16.raw((Short.MAX_VALUE - 1000).toShort()), FixMilli16.ONE, FixMilli16.raw(Short.MAX_VALUE))
	}

	@Test
	fun testMultiplication() {
		assertEquals(FixMilli16.raw(Short.MAX_VALUE), 1 * FixMilli16.raw(Short.MAX_VALUE))
		assertEquals(FixMilli16.raw(Short.MIN_VALUE), 1 * FixMilli16.raw(Short.MIN_VALUE))
		assertEquals(FixMilli16.raw((Short.MIN_VALUE + 1).toShort()), -1 * FixMilli16.raw(Short.MAX_VALUE))
		assertThrows(FixedPointException::class.java) { -1 * FixMilli16.raw(Short.MIN_VALUE) }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixMilli16.from(a * b), FixMilli16.from(a) * FixMilli16.from(b))
			assertEquals(FixMilli16.from(a * b), FixMilli16.from(a) * b)
			assertEquals(FixMilli16.from(a * b), b * FixMilli16.from(a))
		}
		assertThrows(FixedPointException::class.java) { FixMilli16.from(-32) * 4 }
		testValues(0, 4)
		testValues(1, 4)
		testValues(4, 4)
		testValues(7, 4)
		assertThrows(FixedPointException::class.java) { FixMilli16.from(32) * 4 }
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixMilli16.ZERO < FixMilli16.ONE)
		assertFalse(FixMilli16.ZERO > FixMilli16.ONE)
		assertFalse(FixMilli16.ONE < FixMilli16.ONE)
		assertFalse(FixMilli16.ONE > FixMilli16.ONE)
		assertTrue(FixMilli16.ONE <= FixMilli16.ONE)
		assertTrue(FixMilli16.ONE >= FixMilli16.ONE)
		assertTrue(FixMilli16.raw(Short.MIN_VALUE) < FixMilli16.raw(Short.MAX_VALUE))

		val minDelta = FixMilli16.raw(1)
		assertEquals(FixMilli16.from(12), FixMilli16.from(12))
		assertNotEquals(FixMilli16.from(12), FixMilli16.from(12) - minDelta)
		assertTrue(FixMilli16.from(0.001) < FixMilli16.from(0.001) + minDelta)
		assertFalse(FixMilli16.from(0.41012452706744895) < FixMilli16.from(0.41012452706744895) - minDelta)
		assertTrue(FixMilli16.from(0.41012452706744895) < FixMilli16.from(0.41012452706744895) + minDelta)
		assertFalse(FixMilli16.from(1.4602674387652097) < FixMilli16.from(1.4602674387652097) - minDelta)
		assertTrue(FixMilli16.from(1.4602674387652097) < FixMilli16.from(1.4602674387652097) + minDelta)
		assertFalse(FixMilli16.from(32.766999999999996) < FixMilli16.from(32.766999999999996) - minDelta)
	}
}
