package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixAngle {

	fun assertEquals(a: FixAngle, b: FixAngle, maxDelta: FixAngle) {
		val rawDifference = a.raw.toInt() - b.raw.toInt()
		if (rawDifference.absoluteValue > maxDelta.raw.toInt()) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixAngle.ZERO.toString())
		assertEquals("1", FixAngle.ONE.toString())
		assertTrue((FixAngle.ONE / 3).toString().startsWith("0.33"))
		assertTrue((FixAngle.from(254) + FixAngle.ONE / 3).toString().endsWith((FixAngle.ONE / 3).toString().substring(1)))
		assertEquals("0.0625", (FixAngle.ONE / 16).toString())
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixAngle.ONE, FixAngle.from(one))

		fun testValue(value: Int) = assertEquals(value, FixAngle.from(value).toInt())
		testValue(0)
		testValue(1)
		testValue(255)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixAngle.ONE, FixAngle.from(one))

		fun testValue(value: Long) = assertEquals(value, FixAngle.from(value).toLong())
		testValue(0)
		testValue(1)
		testValue(255)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixAngle.ONE, FixAngle.from(1f))
		val delta = 1.1920929E-7f
		assertEquals(5.9604645E-8f, FixAngle.from(5.9604645E-8f).toFloat(), delta)
		assertEquals(3.6228234E-6f, FixAngle.from(3.6228234E-6f).toFloat(), delta)
		assertEquals(1.9278697E-4f, FixAngle.from(1.9278697E-4f).toFloat(), delta)
		assertEquals(5.82169E-4f, FixAngle.from(5.82169E-4f).toFloat(), delta)
		assertEquals(0.031800542f, FixAngle.from(0.031800542f).toFloat(), delta)
		assertEquals(0.88088334f, FixAngle.from(0.88088334f).toFloat(), delta)
		assertEquals(3.4546547f, FixAngle.from(3.4546547f).toFloat(), delta)
		assertEquals(255.99998f, FixAngle.from(255.99998f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixAngle.ONE, FixAngle.from(1.0))
		val delta = 1.1920928955078125E-7
		assertEquals(5.9604644775390625E-8, FixAngle.from(5.9604644775390625E-8).toDouble(), delta)
		assertEquals(3.622823361473907E-6, FixAngle.from(3.622823361473907E-6).toDouble(), delta)
		assertEquals(1.927869704474157E-4, FixAngle.from(1.927869704474157E-4).toDouble(), delta)
		assertEquals(5.821689786753484E-4, FixAngle.from(5.821689786753484E-4).toDouble(), delta)
		assertEquals(0.031800542210183254, FixAngle.from(0.031800542210183254).toDouble(), delta)
		assertEquals(0.8808833555136846, FixAngle.from(0.8808833555136846).toDouble(), delta)
		assertEquals(3.454654706450148, FixAngle.from(3.454654706450148).toDouble(), delta)
		assertEquals(255.99999994039536, FixAngle.from(255.99999994039536).toDouble(), delta)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixAngle, b: FixAngle, c: FixAngle) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}

		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixAngle.from(a), FixAngle.from(b), FixAngle.from(c))
			assertEquals(FixAngle.from(c), FixAngle.from(a) + b)
			assertEquals(FixAngle.from(c), b + FixAngle.from(a))
			assertEquals(FixAngle.from(a), FixAngle.from(c) - b)
			assertEquals(FixAngle.from(b), c - FixAngle.from(a))
		}

		testValues(FixAngle.raw(UInt.MIN_VALUE), FixAngle.ONE, FixAngle.raw(UInt.MIN_VALUE + 16777216u))
		testValues(0, 136, 136)
		testValues(1, 144, 145)
		testValues(255, 0, 255)
		testValues(FixAngle.raw(UInt.MAX_VALUE - 16777216u), FixAngle.ONE, FixAngle.raw(UInt.MAX_VALUE))
		assertEquals(FixAngle.raw(4263727996u), FixAngle.raw(2315132u) + 254)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixAngle.raw(UInt.MAX_VALUE), 1 * FixAngle.raw(UInt.MAX_VALUE))
		assertEquals(FixAngle.raw(UInt.MAX_VALUE), FixAngle.raw(UInt.MAX_VALUE) / 1)

		fun testValues(a: Long, b: Long) {
			assertEquals(FixAngle.from(a * b), FixAngle.from(a) * FixAngle.from(b))
			assertEquals(FixAngle.from(a * b), FixAngle.from(a) * b)
			assertEquals(FixAngle.from(a * b), b * FixAngle.from(a))
			if (b != 0L) assertEquals(FixAngle.from(a), FixAngle.from(a * b) / b)
			if (a != 0L) assertEquals(FixAngle.from(b), FixAngle.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixAngle.from(b), FixAngle.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixAngle.from(a * b), FixAngle.from(a) * b.toInt())
				assertEquals(FixAngle.from(a * b), b.toInt() * FixAngle.from(a))
			}
		}
		testValues(0, 134)
		testValues(1, 42)
		testValues(255, 0)
		assertEquals(FixAngle.raw(9406511u), (FixAngle.raw(9406511u) * FixAngle.raw(14701165u)) / FixAngle.raw(14701165u), FixAngle.raw(167772u))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixAngle.ZERO < FixAngle.ONE)
		assertTrue(0 < FixAngle.ONE)
		assertFalse(FixAngle.ZERO > FixAngle.ONE)
		assertFalse(0 > FixAngle.ONE)
		assertFalse(FixAngle.ONE < FixAngle.ONE)
		assertFalse(FixAngle.ONE < 1)
		assertFalse(FixAngle.ONE > FixAngle.ONE)
		assertTrue(FixAngle.ONE <= FixAngle.ONE)
		assertTrue(FixAngle.ONE >= FixAngle.ONE)
		assertTrue(FixAngle.raw(UInt.MIN_VALUE) < FixAngle.raw(UInt.MAX_VALUE))

		val minDelta = FixAngle.raw(1u)
		assertEquals(FixAngle.from(12), FixAngle.from(12))
		assertNotEquals(FixAngle.from(12), FixAngle.from(12) - minDelta)
		assertTrue(FixAngle.from(5.9604644775390625E-8) < FixAngle.from(5.9604644775390625E-8) + minDelta)
		assertTrue(5.9604644775390625E-8 < FixAngle.from(5.9604644775390625E-8) + minDelta)
		assertFalse(FixAngle.from(2.444532674953037E-5) < FixAngle.from(2.444532674953037E-5) - minDelta)
		assertFalse(2.444532674953037E-5 < FixAngle.from(2.444532674953037E-5) - minDelta)
		assertTrue(FixAngle.from(2.444532674953037E-5) < FixAngle.from(2.444532674953037E-5) + minDelta)
		assertTrue(2.444532674953037E-5 < FixAngle.from(2.444532674953037E-5) + minDelta)
		assertFalse(FixAngle.from(8.70387219646698E-5) < FixAngle.from(8.70387219646698E-5) - minDelta)
		assertFalse(8.70387219646698E-5 < FixAngle.from(8.70387219646698E-5) - minDelta)
		assertTrue(FixAngle.from(8.70387219646698E-5) < FixAngle.from(8.70387219646698E-5) + minDelta)
		assertTrue(8.70387219646698E-5 < FixAngle.from(8.70387219646698E-5) + minDelta)
		assertFalse(FixAngle.from(4.955553271559399E-4) < FixAngle.from(4.955553271559399E-4) - minDelta)
		assertFalse(4.955553271559399E-4 < FixAngle.from(4.955553271559399E-4) - minDelta)
		assertTrue(FixAngle.from(4.955553271559399E-4) < FixAngle.from(4.955553271559399E-4) + minDelta)
		assertTrue(4.955553271559399E-4 < FixAngle.from(4.955553271559399E-4) + minDelta)
		assertFalse(FixAngle.from(0.003763413613340216) < FixAngle.from(0.003763413613340216) - minDelta)
		assertFalse(0.003763413613340216 < FixAngle.from(0.003763413613340216) - minDelta)
		assertTrue(FixAngle.from(0.003763413613340216) < FixAngle.from(0.003763413613340216) + minDelta)
		assertTrue(0.003763413613340216 < FixAngle.from(0.003763413613340216) + minDelta)
		assertFalse(FixAngle.from(0.011288125121964468) < FixAngle.from(0.011288125121964468) - minDelta)
		assertFalse(0.011288125121964468 < FixAngle.from(0.011288125121964468) - minDelta)
		assertTrue(FixAngle.from(0.011288125121964468) < FixAngle.from(0.011288125121964468) + minDelta)
		assertTrue(0.011288125121964468 < FixAngle.from(0.011288125121964468) + minDelta)
		assertFalse(FixAngle.from(0.16420344226726716) < FixAngle.from(0.16420344226726716) - minDelta)
		assertFalse(0.16420344226726716 < FixAngle.from(0.16420344226726716) - minDelta)
		assertTrue(FixAngle.from(0.16420344226726716) < FixAngle.from(0.16420344226726716) + minDelta)
		assertTrue(0.16420344226726716 < FixAngle.from(0.16420344226726716) + minDelta)
		assertFalse(FixAngle.from(0.6350084755958214) < FixAngle.from(0.6350084755958214) - minDelta)
		assertFalse(0.6350084755958214 < FixAngle.from(0.6350084755958214) - minDelta)
		assertTrue(FixAngle.from(0.6350084755958214) < FixAngle.from(0.6350084755958214) + minDelta)
		assertTrue(0.6350084755958214 < FixAngle.from(0.6350084755958214) + minDelta)
		assertFalse(FixAngle.from(8.956547452279857) < FixAngle.from(8.956547452279857) - minDelta)
		assertFalse(8.956547452279857 < FixAngle.from(8.956547452279857) - minDelta)
		assertTrue(FixAngle.from(8.956547452279857) < FixAngle.from(8.956547452279857) + minDelta)
		assertTrue(8.956547452279857 < FixAngle.from(8.956547452279857) + minDelta)
		assertFalse(FixAngle.from(255.99999994039536) < FixAngle.from(255.99999994039536) - minDelta)
		assertFalse(255.99999994039536 < FixAngle.from(255.99999994039536) - minDelta)
		assertTrue(FixAngle.raw(UInt.MAX_VALUE) >= 255)
		assertTrue(FixAngle.raw(UInt.MAX_VALUE) > 255)
		assertTrue(FixAngle.raw(UInt.MAX_VALUE) < 256)
		assertTrue(FixAngle.raw(UInt.MAX_VALUE) < 256.256)
		assertTrue(FixAngle.raw(UInt.MAX_VALUE) < UInt.MAX_VALUE)
		assertTrue(FixAngle.raw(UInt.MAX_VALUE) < UInt.MAX_VALUE.toFloat())
		assertTrue(FixAngle.raw(UInt.MAX_VALUE) < UInt.MAX_VALUE.toDouble())
		assertTrue(FixAngle.ZERO > -1)
		assertTrue(FixAngle.ZERO > -0.001f)
		assertTrue(FixAngle.ZERO > -0.001)
		assertTrue(FixAngle.ZERO > Long.MIN_VALUE)
		assertTrue(FixAngle.ZERO > Long.MIN_VALUE.toFloat())
		assertTrue(FixAngle.ZERO > Long.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixAngle.Array(2) { FixAngle.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixAngle.ONE, testArray[0])
		assertEquals(FixAngle.ONE, testArray[1])
		testArray[1] = FixAngle.ZERO
		assertEquals(FixAngle.ONE, testArray[0])
		assertEquals(FixAngle.ZERO, testArray[1])
		testArray.fill(FixAngle.ZERO)
		assertEquals(FixAngle.ZERO, testArray[0])
		assertEquals(FixAngle.ZERO, testArray[1])
	}

	@Test
	fun testMinMax() {
		assertEquals(FixAngle.ZERO, min(FixAngle.ZERO, FixAngle.ZERO))
		assertEquals(FixAngle.ZERO, max(FixAngle.ZERO, FixAngle.ZERO))
		assertEquals(FixAngle.ZERO, min(FixAngle.ONE, FixAngle.ZERO))
		assertEquals(FixAngle.ONE, max(FixAngle.ONE, FixAngle.ZERO))
		assertEquals(FixAngle.ZERO, min(FixAngle.ZERO, FixAngle.ONE))
		assertEquals(FixAngle.ONE, max(FixAngle.ZERO, FixAngle.ONE))
		assertEquals(FixAngle.ZERO, min(FixAngle.ZERO, FixAngle.raw(UInt.MAX_VALUE)))
		assertEquals(FixAngle.raw(UInt.MAX_VALUE), max(FixAngle.ZERO, FixAngle.raw(UInt.MAX_VALUE)))
	}
}
