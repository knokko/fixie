package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixUncheckedMilli16U {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixUncheckedMilli16U.from(value).toInt())
		testValue(0)
		testValue(1)
		testValue(65)
	}

	@Test
	fun testLongConversion() {
		fun testValue(value: Long) = assertEquals(value, FixUncheckedMilli16U.from(value).toLong())
		testValue(0)
		testValue(1)
		testValue(65)
	}

	@Test
	fun testFloatConversion() {
		val delta = 0.002f
		assertEquals(0.001f, FixUncheckedMilli16U.from(0.001f).toFloat(), delta)
		assertEquals(0.06078089f, FixUncheckedMilli16U.from(0.06078089f).toFloat(), delta)
		assertEquals(3.2344286f, FixUncheckedMilli16U.from(3.2344286f).toFloat(), delta)
		assertEquals(65.534996f, FixUncheckedMilli16U.from(65.534996f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 0.002
		assertEquals(0.001, FixUncheckedMilli16U.from(0.001).toDouble(), delta)
		assertEquals(0.06078089006529382, FixUncheckedMilli16U.from(0.06078089006529382).toDouble(), delta)
		assertEquals(3.2344286451819104, FixUncheckedMilli16U.from(3.2344286451819104).toDouble(), delta)
		assertEquals(65.535, FixUncheckedMilli16U.from(65.535).toDouble(), delta)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixUncheckedMilli16U, b: FixUncheckedMilli16U, c: FixUncheckedMilli16U) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}
		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixUncheckedMilli16U.from(a), FixUncheckedMilli16U.from(b), FixUncheckedMilli16U.from(c))
			assertEquals(FixUncheckedMilli16U.from(c), FixUncheckedMilli16U.from(a) + b)
			assertEquals(FixUncheckedMilli16U.from(c), b + FixUncheckedMilli16U.from(a))
			assertEquals(FixUncheckedMilli16U.from(a), FixUncheckedMilli16U.from(c) - b)
			assertEquals(FixUncheckedMilli16U.from(b), c - FixUncheckedMilli16U.from(a))
		}
		testValues(FixUncheckedMilli16U.raw(UShort.MIN_VALUE), FixUncheckedMilli16U.ONE, FixUncheckedMilli16U.raw((UShort.MIN_VALUE + 1000u).toUShort()))
		testValues(0, 8, 8)
		testValues(1, 9, 10)
		testValues(32, 0, 32)
		testValues(FixUncheckedMilli16U.raw((UShort.MAX_VALUE - 1000u).toUShort()), FixUncheckedMilli16U.ONE, FixUncheckedMilli16U.raw(UShort.MAX_VALUE))
	}

	@Test
	fun testMultiplication() {
		assertEquals(FixUncheckedMilli16U.raw(UShort.MAX_VALUE), 1 * FixUncheckedMilli16U.raw(UShort.MAX_VALUE))

		fun testValues(a: Long, b: Long) {
			assertEquals(FixUncheckedMilli16U.from(a * b), FixUncheckedMilli16U.from(a) * FixUncheckedMilli16U.from(b))
			assertEquals(FixUncheckedMilli16U.from(a * b), FixUncheckedMilli16U.from(a) * b)
			assertEquals(FixUncheckedMilli16U.from(a * b), b * FixUncheckedMilli16U.from(a))
		}
		testValues(0, 1)
		testValues(1, 65)
		testValues(65, 0)
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixUncheckedMilli16U.ZERO < FixUncheckedMilli16U.ONE)
		assertFalse(FixUncheckedMilli16U.ZERO > FixUncheckedMilli16U.ONE)
		assertFalse(FixUncheckedMilli16U.ONE < FixUncheckedMilli16U.ONE)
		assertFalse(FixUncheckedMilli16U.ONE > FixUncheckedMilli16U.ONE)
		assertTrue(FixUncheckedMilli16U.ONE <= FixUncheckedMilli16U.ONE)
		assertTrue(FixUncheckedMilli16U.ONE >= FixUncheckedMilli16U.ONE)
		assertTrue(FixUncheckedMilli16U.raw(UShort.MIN_VALUE) < FixUncheckedMilli16U.raw(UShort.MAX_VALUE))

		val minDelta = FixUncheckedMilli16U.raw(1u)
		assertEquals(FixUncheckedMilli16U.from(12), FixUncheckedMilli16U.from(12))
		assertNotEquals(FixUncheckedMilli16U.from(12), FixUncheckedMilli16U.from(12) - minDelta)
		assertTrue(FixUncheckedMilli16U.from(0.001) < FixUncheckedMilli16U.from(0.001) + minDelta)
		assertFalse(FixUncheckedMilli16U.from(0.41012452706744895) < FixUncheckedMilli16U.from(0.41012452706744895) - minDelta)
		assertTrue(FixUncheckedMilli16U.from(0.41012452706744895) < FixUncheckedMilli16U.from(0.41012452706744895) + minDelta)
		assertFalse(FixUncheckedMilli16U.from(1.4602674387652097) < FixUncheckedMilli16U.from(1.4602674387652097) - minDelta)
		assertTrue(FixUncheckedMilli16U.from(1.4602674387652097) < FixUncheckedMilli16U.from(1.4602674387652097) + minDelta)
		assertFalse(FixUncheckedMilli16U.from(65.535) < FixUncheckedMilli16U.from(65.535) - minDelta)
	}
}
