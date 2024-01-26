package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixMilli16U {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixMilli16U.from(value).toInt())
		testValue(0)
		testValue(1)
		testValue(65)
	}

	@Test
	fun testLongConversion() {
		fun testValue(value: Long) = assertEquals(value, FixMilli16U.from(value).toLong())
		testValue(0)
		testValue(1)
		testValue(65)
	}

	@Test
	fun testFloatConversion() {
		val delta = 0.002f
		assertEquals(0.001f, FixMilli16U.from(0.001f).toFloat(), delta)
		assertEquals(0.06078089f, FixMilli16U.from(0.06078089f).toFloat(), delta)
		assertEquals(3.2344286f, FixMilli16U.from(3.2344286f).toFloat(), delta)
		assertEquals(65.534996f, FixMilli16U.from(65.534996f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 0.002
		assertEquals(0.001, FixMilli16U.from(0.001).toDouble(), delta)
		assertEquals(0.06078089006529382, FixMilli16U.from(0.06078089006529382).toDouble(), delta)
		assertEquals(3.2344286451819104, FixMilli16U.from(3.2344286451819104).toDouble(), delta)
		assertEquals(65.535, FixMilli16U.from(65.535).toDouble(), delta)
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
		testValues(0, 8, 8)
		testValues(1, 9, 10)
		testValues(32, 0, 32)
		testValues(FixMilli16U.raw((UShort.MAX_VALUE - 1000u).toUShort()), FixMilli16U.ONE, FixMilli16U.raw(UShort.MAX_VALUE))
	}

	@Test
	fun testMultiplication() {
		assertEquals(FixMilli16U.raw(UShort.MAX_VALUE), 1 * FixMilli16U.raw(UShort.MAX_VALUE))
		assertThrows(FixedPointException::class.java) { -1 * FixMilli16U.ONE }
		assertThrows(FixedPointException::class.java) { -1 * FixMilli16U.raw(UShort.MAX_VALUE)}

		fun testValues(a: Long, b: Long) {
			assertEquals(FixMilli16U.from(a * b), FixMilli16U.from(a) * FixMilli16U.from(b))
			assertEquals(FixMilli16U.from(a * b), FixMilli16U.from(a) * b)
			assertEquals(FixMilli16U.from(a * b), b * FixMilli16U.from(a))
		}
		testValues(0, 0)
		testValues(1, 0)
		testValues(65, 0)
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixMilli16U.ZERO < FixMilli16U.ONE)
		assertFalse(FixMilli16U.ZERO > FixMilli16U.ONE)
		assertFalse(FixMilli16U.ONE < FixMilli16U.ONE)
		assertFalse(FixMilli16U.ONE > FixMilli16U.ONE)
		assertTrue(FixMilli16U.ONE <= FixMilli16U.ONE)
		assertTrue(FixMilli16U.ONE >= FixMilli16U.ONE)
		assertTrue(FixMilli16U.raw(UShort.MIN_VALUE) < FixMilli16U.raw(UShort.MAX_VALUE))

		val minDelta = FixMilli16U.raw(1u)
		assertEquals(FixMilli16U.from(12), FixMilli16U.from(12))
		assertNotEquals(FixMilli16U.from(12), FixMilli16U.from(12) - minDelta)
		assertTrue(FixMilli16U.from(0.001) < FixMilli16U.from(0.001) + minDelta)
		assertFalse(FixMilli16U.from(0.41012452706744895) < FixMilli16U.from(0.41012452706744895) - minDelta)
		assertTrue(FixMilli16U.from(0.41012452706744895) < FixMilli16U.from(0.41012452706744895) + minDelta)
		assertFalse(FixMilli16U.from(1.4602674387652097) < FixMilli16U.from(1.4602674387652097) - minDelta)
		assertTrue(FixMilli16U.from(1.4602674387652097) < FixMilli16U.from(1.4602674387652097) + minDelta)
		assertFalse(FixMilli16U.from(65.535) < FixMilli16U.from(65.535) - minDelta)
	}
}
