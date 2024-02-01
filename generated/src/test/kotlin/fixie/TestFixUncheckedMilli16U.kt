package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixUncheckedMilli16U {

	fun assertEquals(a: FixUncheckedMilli16U, b: FixUncheckedMilli16U, maxDelta: FixUncheckedMilli16U) {
		val rawDifference = a.raw.toShort() - b.raw.toShort()
		if (rawDifference.absoluteValue > maxDelta.raw.toShort()) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixUncheckedMilli16U.ZERO.toString())
		assertEquals("1", FixUncheckedMilli16U.ONE.toString())
		assertTrue((FixUncheckedMilli16U.ONE / 3).toString().startsWith("0.3"))
		assertTrue((FixUncheckedMilli16U.from(64) + FixUncheckedMilli16U.ONE / 3).toString().endsWith((FixUncheckedMilli16U.ONE / 3).toString().substring(1)))
		assertEquals("0.01", (FixUncheckedMilli16U.ONE / 100).toString())
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixUncheckedMilli16U.ONE, FixUncheckedMilli16U.from(one))

		fun testValue(value: Int) = assertEquals(value, FixUncheckedMilli16U.from(value).toInt())
		testValue(0)
		testValue(1)
		testValue(65)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixUncheckedMilli16U.ONE, FixUncheckedMilli16U.from(one))

		fun testValue(value: Long) = assertEquals(value, FixUncheckedMilli16U.from(value).toLong())
		testValue(0)
		testValue(1)
		testValue(65)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixUncheckedMilli16U.ONE, FixUncheckedMilli16U.from(1f))
		val delta = 0.002f
		assertEquals(0.001f, FixUncheckedMilli16U.from(0.001f).toFloat(), delta)
		assertEquals(0.06078089f, FixUncheckedMilli16U.from(0.06078089f).toFloat(), delta)
		assertEquals(3.2344286f, FixUncheckedMilli16U.from(3.2344286f).toFloat(), delta)
		assertEquals(65.534996f, FixUncheckedMilli16U.from(65.534996f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixUncheckedMilli16U.ONE, FixUncheckedMilli16U.from(1.0))
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
		testValues(0, 26, 26)
		testValues(1, 54, 55)
		testValues(65, 0, 65)
		testValues(FixUncheckedMilli16U.raw((UShort.MAX_VALUE - 1000u).toUShort()), FixUncheckedMilli16U.ONE, FixUncheckedMilli16U.raw(UShort.MAX_VALUE))
		assertEquals(FixUncheckedMilli16U.raw(64440u), FixUncheckedMilli16U.raw(440u) + 64)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixUncheckedMilli16U.raw(UShort.MAX_VALUE), 1 * FixUncheckedMilli16U.raw(UShort.MAX_VALUE))
		assertEquals(FixUncheckedMilli16U.raw(UShort.MAX_VALUE), FixUncheckedMilli16U.raw(UShort.MAX_VALUE) / 1)

		fun testValues(a: Long, b: Long) {
			assertEquals(FixUncheckedMilli16U.from(a * b), FixUncheckedMilli16U.from(a) * FixUncheckedMilli16U.from(b))
			assertEquals(FixUncheckedMilli16U.from(a * b), FixUncheckedMilli16U.from(a) * b)
			assertEquals(FixUncheckedMilli16U.from(a * b), b * FixUncheckedMilli16U.from(a))
			if (b != 0L) assertEquals(FixUncheckedMilli16U.from(a), FixUncheckedMilli16U.from(a * b) / b)
			if (a != 0L) assertEquals(FixUncheckedMilli16U.from(b), FixUncheckedMilli16U.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixUncheckedMilli16U.from(b), FixUncheckedMilli16U.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixUncheckedMilli16U.from(a * b), FixUncheckedMilli16U.from(a) * b.toInt())
				assertEquals(FixUncheckedMilli16U.from(a * b), b.toInt() * FixUncheckedMilli16U.from(a))
			}
		}
		testValues(0, 0)
		testValues(1, 65)
		testValues(65, 0)
		assertEquals(FixUncheckedMilli16U.raw(634u), (FixUncheckedMilli16U.raw(634u) * FixUncheckedMilli16U.raw(701u)) / FixUncheckedMilli16U.raw(701u), FixUncheckedMilli16U.raw(10u))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixUncheckedMilli16U.ZERO < FixUncheckedMilli16U.ONE)
		assertTrue(0 < FixUncheckedMilli16U.ONE)
		assertFalse(FixUncheckedMilli16U.ZERO > FixUncheckedMilli16U.ONE)
		assertFalse(0 > FixUncheckedMilli16U.ONE)
		assertFalse(FixUncheckedMilli16U.ONE < FixUncheckedMilli16U.ONE)
		assertFalse(FixUncheckedMilli16U.ONE < 1)
		assertFalse(FixUncheckedMilli16U.ONE > FixUncheckedMilli16U.ONE)
		assertTrue(FixUncheckedMilli16U.ONE <= FixUncheckedMilli16U.ONE)
		assertTrue(FixUncheckedMilli16U.ONE >= FixUncheckedMilli16U.ONE)
		assertTrue(FixUncheckedMilli16U.raw(UShort.MIN_VALUE) < FixUncheckedMilli16U.raw(UShort.MAX_VALUE))

		val minDelta = FixUncheckedMilli16U.raw(1u)
		assertEquals(FixUncheckedMilli16U.from(12), FixUncheckedMilli16U.from(12))
		assertNotEquals(FixUncheckedMilli16U.from(12), FixUncheckedMilli16U.from(12) - minDelta)
		assertTrue(FixUncheckedMilli16U.from(0.001) < FixUncheckedMilli16U.from(0.001) + minDelta)
		assertTrue(0.001 < FixUncheckedMilli16U.from(0.001) + minDelta)
		assertFalse(FixUncheckedMilli16U.from(0.41012452706744895) < FixUncheckedMilli16U.from(0.41012452706744895) - minDelta)
		assertFalse(0.41012452706744895 < FixUncheckedMilli16U.from(0.41012452706744895) - minDelta)
		assertTrue(FixUncheckedMilli16U.from(0.41012452706744895) < FixUncheckedMilli16U.from(0.41012452706744895) + minDelta)
		assertTrue(0.41012452706744895 < FixUncheckedMilli16U.from(0.41012452706744895) + minDelta)
		assertFalse(FixUncheckedMilli16U.from(1.4602674387652097) < FixUncheckedMilli16U.from(1.4602674387652097) - minDelta)
		assertFalse(1.4602674387652097 < FixUncheckedMilli16U.from(1.4602674387652097) - minDelta)
		assertTrue(FixUncheckedMilli16U.from(1.4602674387652097) < FixUncheckedMilli16U.from(1.4602674387652097) + minDelta)
		assertTrue(1.4602674387652097 < FixUncheckedMilli16U.from(1.4602674387652097) + minDelta)
		assertFalse(FixUncheckedMilli16U.from(65.535) < FixUncheckedMilli16U.from(65.535) - minDelta)
		assertFalse(65.535 < FixUncheckedMilli16U.from(65.535) - minDelta)
		assertTrue(FixUncheckedMilli16U.raw(UShort.MAX_VALUE) >= 65)
		assertTrue(FixUncheckedMilli16U.raw(UShort.MAX_VALUE) > 65)
		assertTrue(FixUncheckedMilli16U.raw(UShort.MAX_VALUE) < 66)
		assertTrue(FixUncheckedMilli16U.raw(UShort.MAX_VALUE) < 66.06599999999999)
		assertTrue(FixUncheckedMilli16U.raw(UShort.MAX_VALUE) < UShort.MAX_VALUE)
		assertTrue(FixUncheckedMilli16U.raw(UShort.MAX_VALUE) < UShort.MAX_VALUE.toFloat())
		assertTrue(FixUncheckedMilli16U.raw(UShort.MAX_VALUE) < UShort.MAX_VALUE.toDouble())
		assertTrue(FixUncheckedMilli16U.ZERO > -1)
		assertTrue(FixUncheckedMilli16U.ZERO > -0.001f)
		assertTrue(FixUncheckedMilli16U.ZERO > -0.001)
		assertTrue(FixUncheckedMilli16U.ZERO > Long.MIN_VALUE)
		assertTrue(FixUncheckedMilli16U.ZERO > Long.MIN_VALUE.toFloat())
		assertTrue(FixUncheckedMilli16U.ZERO > Long.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixUncheckedMilli16U.Array(2) { FixUncheckedMilli16U.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixUncheckedMilli16U.ONE, testArray[0])
		assertEquals(FixUncheckedMilli16U.ONE, testArray[1])
		testArray[1] = FixUncheckedMilli16U.ZERO
		assertEquals(FixUncheckedMilli16U.ONE, testArray[0])
		assertEquals(FixUncheckedMilli16U.ZERO, testArray[1])
		testArray.fill(FixUncheckedMilli16U.ZERO)
		assertEquals(FixUncheckedMilli16U.ZERO, testArray[0])
		assertEquals(FixUncheckedMilli16U.ZERO, testArray[1])
	}

	@Test
	fun testMinMax() {
		assertEquals(FixUncheckedMilli16U.ZERO, min(FixUncheckedMilli16U.ZERO, FixUncheckedMilli16U.ZERO))
		assertEquals(FixUncheckedMilli16U.ZERO, max(FixUncheckedMilli16U.ZERO, FixUncheckedMilli16U.ZERO))
		assertEquals(FixUncheckedMilli16U.ZERO, min(FixUncheckedMilli16U.ONE, FixUncheckedMilli16U.ZERO))
		assertEquals(FixUncheckedMilli16U.ONE, max(FixUncheckedMilli16U.ONE, FixUncheckedMilli16U.ZERO))
		assertEquals(FixUncheckedMilli16U.ZERO, min(FixUncheckedMilli16U.ZERO, FixUncheckedMilli16U.ONE))
		assertEquals(FixUncheckedMilli16U.ONE, max(FixUncheckedMilli16U.ZERO, FixUncheckedMilli16U.ONE))
		assertEquals(FixUncheckedMilli16U.ZERO, min(FixUncheckedMilli16U.ZERO, FixUncheckedMilli16U.raw(UShort.MAX_VALUE)))
		assertEquals(FixUncheckedMilli16U.raw(UShort.MAX_VALUE), max(FixUncheckedMilli16U.ZERO, FixUncheckedMilli16U.raw(UShort.MAX_VALUE)))
	}
}
