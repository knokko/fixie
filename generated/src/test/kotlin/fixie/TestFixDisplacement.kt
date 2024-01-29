package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixDisplacement {

	fun assertEquals(a: FixDisplacement, b: FixDisplacement, maxDelta: FixDisplacement) {
		val rawDifference = a.raw - b.raw
		if (rawDifference.absoluteValue > maxDelta.raw) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixDisplacement.ZERO.toString())
		assertEquals("1", FixDisplacement.ONE.toString())
		assertTrue((FixDisplacement.ONE / 3).toString().startsWith("0.33"))
		assertEquals("-1", (-FixDisplacement.ONE).toString())
		assertTrue((FixDisplacement.ONE / -3).toString().startsWith("-0.33"))
		assertTrue((FixDisplacement.from(30) + FixDisplacement.ONE / 3).toString().endsWith((FixDisplacement.ONE / 3).toString().substring(1)))
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixDisplacement.ONE, FixDisplacement.from(one))

		fun testValue(value: Int) = assertEquals(value, FixDisplacement.from(value).toInt())
		testValue(-32)
		testValue(0)
		testValue(1)
		testValue(31)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixDisplacement.ONE, FixDisplacement.from(one))

		fun testValue(value: Long) = assertEquals(value, FixDisplacement.from(value).toLong())
		testValue(-32)
		testValue(0)
		testValue(1)
		testValue(31)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixDisplacement.ONE, FixDisplacement.from(1f))
		val delta = 0.001953125f
		assertEquals(9.765625E-4f, FixDisplacement.from(9.765625E-4f).toFloat(), delta)
		assertEquals(-9.765625E-4f, FixDisplacement.from(-9.765625E-4f).toFloat(), delta)
		assertEquals(0.05935634f, FixDisplacement.from(0.05935634f).toFloat(), delta)
		assertEquals(-0.05935634f, FixDisplacement.from(-0.05935634f).toFloat(), delta)
		assertEquals(3.1586218f, FixDisplacement.from(3.1586218f).toFloat(), delta)
		assertEquals(-3.1586218f, FixDisplacement.from(-3.1586218f).toFloat(), delta)
		assertEquals(31.999023f, FixDisplacement.from(31.999023f).toFloat(), delta)
		assertEquals(-31.999023f, FixDisplacement.from(-31.999023f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixDisplacement.ONE, FixDisplacement.from(1.0))
		val delta = 0.001953125
		assertEquals(9.765625E-4, FixDisplacement.from(9.765625E-4).toDouble(), delta)
		assertEquals(-9.765625E-4, FixDisplacement.from(-9.765625E-4).toDouble(), delta)
		assertEquals(0.059356337954388494, FixDisplacement.from(0.059356337954388494).toDouble(), delta)
		assertEquals(-0.059356337954388494, FixDisplacement.from(-0.059356337954388494).toDouble(), delta)
		assertEquals(3.158621723810459, FixDisplacement.from(3.158621723810459).toDouble(), delta)
		assertEquals(-3.158621723810459, FixDisplacement.from(-3.158621723810459).toDouble(), delta)
		assertEquals(31.9990234375, FixDisplacement.from(31.9990234375).toDouble(), delta)
		assertEquals(-31.9990234375, FixDisplacement.from(-31.9990234375).toDouble(), delta)
	}

	@Test
	fun testUnaryMinus() {
		assertEquals(32767, -FixDisplacement.raw(-32767).raw)
		assertEquals(4180, -FixDisplacement.raw(-4180).raw)
		assertEquals(233, -FixDisplacement.raw(-233).raw)
		assertEquals(0, -FixDisplacement.raw(0).raw)
		assertEquals(-1, -FixDisplacement.raw(1).raw)
		assertEquals(-10469, -FixDisplacement.raw(10469).raw)
		assertEquals(-32767, -FixDisplacement.raw(32767).raw)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixDisplacement, b: FixDisplacement, c: FixDisplacement) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}

		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixDisplacement.from(a), FixDisplacement.from(b), FixDisplacement.from(c))
			assertEquals(FixDisplacement.from(c), FixDisplacement.from(a) + b)
			assertEquals(FixDisplacement.from(c), b + FixDisplacement.from(a))
			assertEquals(FixDisplacement.from(a), FixDisplacement.from(c) - b)
			assertEquals(FixDisplacement.from(b), c - FixDisplacement.from(a))
		}

		testValues(FixDisplacement.raw(Short.MIN_VALUE), FixDisplacement.ONE, FixDisplacement.raw((Short.MIN_VALUE + 1024).toShort()))
		testValues(-32, 4, -28)
		testValues(0, 30, 30)
		testValues(1, 13, 14)
		testValues(6, 14, 20)
		testValues(13, 12, 25)
		testValues(31, 0, 31)
		testValues(FixDisplacement.raw((Short.MAX_VALUE - 1024).toShort()), FixDisplacement.ONE, FixDisplacement.raw(Short.MAX_VALUE))
		assertEquals(FixDisplacement.raw(31673), FixDisplacement.raw(-71) + 31)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixDisplacement.raw(Short.MAX_VALUE), 1 * FixDisplacement.raw(Short.MAX_VALUE))
		assertEquals(FixDisplacement.raw(Short.MAX_VALUE), FixDisplacement.raw(Short.MAX_VALUE) / 1)
		assertEquals(FixDisplacement.raw(Short.MIN_VALUE), 1 * FixDisplacement.raw(Short.MIN_VALUE))
		assertEquals(FixDisplacement.raw(Short.MIN_VALUE), FixDisplacement.raw(Short.MIN_VALUE) / 1)
		assertEquals(FixDisplacement.raw((Short.MIN_VALUE + 1).toShort()), -1 * FixDisplacement.raw(Short.MAX_VALUE))
		assertEquals(FixDisplacement.raw((Short.MIN_VALUE + 1).toShort()), FixDisplacement.raw(Short.MAX_VALUE) / -1)

		fun testValues(a: Long, b: Long) {
			assertEquals(FixDisplacement.from(a * b), FixDisplacement.from(a) * FixDisplacement.from(b))
			assertEquals(FixDisplacement.from(a * b), FixDisplacement.from(a) * b)
			assertEquals(FixDisplacement.from(a * b), b * FixDisplacement.from(a))
			if (b != 0L) assertEquals(FixDisplacement.from(a), FixDisplacement.from(a * b) / b)
			if (a != 0L) assertEquals(FixDisplacement.from(b), FixDisplacement.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixDisplacement.from(b), FixDisplacement.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixDisplacement.from(a * b), FixDisplacement.from(a) * b.toInt())
				assertEquals(FixDisplacement.from(a * b), b.toInt() * FixDisplacement.from(a))
			}
		}
		testValues(0, 1)
		testValues(1, 4)
		assertEquals(FixDisplacement.raw(-897), (FixDisplacement.raw(-897) * FixDisplacement.raw(833)) / FixDisplacement.raw(833), FixDisplacement.raw(10))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixDisplacement.ZERO < FixDisplacement.ONE)
		assertTrue(0 < FixDisplacement.ONE)
		assertFalse(FixDisplacement.ZERO > FixDisplacement.ONE)
		assertFalse(0 > FixDisplacement.ONE)
		assertFalse(FixDisplacement.ONE < FixDisplacement.ONE)
		assertFalse(FixDisplacement.ONE < 1)
		assertFalse(FixDisplacement.ONE > FixDisplacement.ONE)
		assertTrue(FixDisplacement.ONE <= FixDisplacement.ONE)
		assertTrue(FixDisplacement.ONE >= FixDisplacement.ONE)
		assertTrue(FixDisplacement.raw(Short.MIN_VALUE) < FixDisplacement.raw(Short.MAX_VALUE))

		val minDelta = FixDisplacement.raw(1)
		assertEquals(FixDisplacement.from(12), FixDisplacement.from(12))
		assertNotEquals(FixDisplacement.from(12), FixDisplacement.from(12) - minDelta)
		assertTrue(FixDisplacement.from(9.765625E-4) < FixDisplacement.from(9.765625E-4) + minDelta)
		assertTrue(9.765625E-4 < FixDisplacement.from(9.765625E-4) + minDelta)
		assertFalse(FixDisplacement.from(0.4005122334643056) < FixDisplacement.from(0.4005122334643056) - minDelta)
		assertFalse(0.4005122334643056 < FixDisplacement.from(0.4005122334643056) - minDelta)
		assertTrue(FixDisplacement.from(0.4005122334643056) < FixDisplacement.from(0.4005122334643056) + minDelta)
		assertTrue(0.4005122334643056 < FixDisplacement.from(0.4005122334643056) + minDelta)
		assertFalse(FixDisplacement.from(1.42604242066915) < FixDisplacement.from(1.42604242066915) - minDelta)
		assertFalse(1.42604242066915 < FixDisplacement.from(1.42604242066915) - minDelta)
		assertTrue(FixDisplacement.from(1.42604242066915) < FixDisplacement.from(1.42604242066915) + minDelta)
		assertTrue(1.42604242066915 < FixDisplacement.from(1.42604242066915) + minDelta)
		assertFalse(FixDisplacement.from(31.9990234375) < FixDisplacement.from(31.9990234375) - minDelta)
		assertFalse(31.9990234375 < FixDisplacement.from(31.9990234375) - minDelta)
		assertTrue(FixDisplacement.raw(Short.MAX_VALUE) >= 31)
		assertTrue(FixDisplacement.raw(Short.MAX_VALUE) > 31)
		assertTrue(FixDisplacement.raw(Short.MAX_VALUE) < 32)
		assertTrue(FixDisplacement.raw(Short.MAX_VALUE) < 32.032)
		assertTrue(FixDisplacement.raw(Short.MAX_VALUE) < Short.MAX_VALUE)
		assertTrue(FixDisplacement.raw(Short.MAX_VALUE) < Short.MAX_VALUE.toFloat())
		assertTrue(FixDisplacement.raw(Short.MAX_VALUE) < Short.MAX_VALUE.toDouble())
		assertTrue(FixDisplacement.raw(Short.MIN_VALUE) <= -32)
		assertTrue(FixDisplacement.raw(Short.MIN_VALUE) > -33)
		assertTrue(FixDisplacement.raw(Short.MIN_VALUE) > -33.032999999999994)
		assertTrue(FixDisplacement.raw(Short.MIN_VALUE) > Short.MIN_VALUE)
		assertTrue(FixDisplacement.raw(Short.MIN_VALUE) > Short.MIN_VALUE.toFloat())
		assertTrue(FixDisplacement.raw(Short.MIN_VALUE) > Short.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixDisplacement.Array(2) { FixDisplacement.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixDisplacement.ONE, testArray[0])
		assertEquals(FixDisplacement.ONE, testArray[1])
		testArray[1] = FixDisplacement.ZERO
		assertEquals(FixDisplacement.ONE, testArray[0])
		assertEquals(FixDisplacement.ZERO, testArray[1])
		testArray.fill(FixDisplacement.ZERO)
		assertEquals(FixDisplacement.ZERO, testArray[0])
		assertEquals(FixDisplacement.ZERO, testArray[1])
	}

	@Test
	fun testAbs() {
		assertEquals(FixDisplacement.ZERO, abs(FixDisplacement.ZERO))
		assertEquals(FixDisplacement.ONE, abs(FixDisplacement.ONE))
		assertEquals(FixDisplacement.ONE, abs(-FixDisplacement.ONE))
		assertEquals(FixDisplacement.raw(Short.MAX_VALUE), abs(FixDisplacement.raw(Short.MAX_VALUE)))
		assertEquals(FixDisplacement.raw(Short.MAX_VALUE), abs(-FixDisplacement.raw(Short.MAX_VALUE)))
	}

	@Test
	fun testMinMax() {
		assertEquals(FixDisplacement.ZERO, min(FixDisplacement.ZERO, FixDisplacement.ZERO))
		assertEquals(FixDisplacement.ZERO, max(FixDisplacement.ZERO, FixDisplacement.ZERO))
		assertEquals(FixDisplacement.ZERO, min(FixDisplacement.ONE, FixDisplacement.ZERO))
		assertEquals(FixDisplacement.ONE, max(FixDisplacement.ONE, FixDisplacement.ZERO))
		assertEquals(FixDisplacement.ZERO, min(FixDisplacement.ZERO, FixDisplacement.ONE))
		assertEquals(FixDisplacement.ONE, max(FixDisplacement.ZERO, FixDisplacement.ONE))
		assertEquals(FixDisplacement.ZERO, min(FixDisplacement.ZERO, FixDisplacement.raw(Short.MAX_VALUE)))
		assertEquals(FixDisplacement.raw(Short.MAX_VALUE), max(FixDisplacement.ZERO, FixDisplacement.raw(Short.MAX_VALUE)))
		assertEquals(-FixDisplacement.ONE, min(-FixDisplacement.ONE, FixDisplacement.ZERO))
		assertEquals(FixDisplacement.ZERO, max(-FixDisplacement.ONE, FixDisplacement.ZERO))
		assertEquals(FixDisplacement.raw(Short.MIN_VALUE), min(FixDisplacement.ZERO, FixDisplacement.raw(Short.MIN_VALUE)))
		assertEquals(FixDisplacement.ZERO, max(FixDisplacement.ZERO, FixDisplacement.raw(Short.MIN_VALUE)))
	}
}
