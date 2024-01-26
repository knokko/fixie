package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixDecMicro32 {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixDecMicro32.from(value).toInt())
		testValue(-2147)
		testValue(-883)
		testValue(0)
		testValue(1)
		testValue(375)
		testValue(2147)
	}

	@Test
	fun testLongConversion() {
		fun testValue(value: Long) = assertEquals(value, FixDecMicro32.from(value).toLong())
		testValue(-2147)
		testValue(-883)
		testValue(0)
		testValue(1)
		testValue(375)
		testValue(2147)
	}

	@Test
	fun testFloatConversion() {
		val delta = 2.0E-6f
		assertEquals(1.0E-6f, FixDecMicro32.from(1.0E-6f).toFloat(), delta)
		assertEquals(-1.0E-6f, FixDecMicro32.from(-1.0E-6f).toFloat(), delta)
		assertEquals(6.078089E-5f, FixDecMicro32.from(6.078089E-5f).toFloat(), delta)
		assertEquals(-6.078089E-5f, FixDecMicro32.from(-6.078089E-5f).toFloat(), delta)
		assertEquals(0.0032344286f, FixDecMicro32.from(0.0032344286f).toFloat(), delta)
		assertEquals(-0.0032344286f, FixDecMicro32.from(-0.0032344286f).toFloat(), delta)
		assertEquals(0.009767175f, FixDecMicro32.from(0.009767175f).toFloat(), delta)
		assertEquals(-0.009767175f, FixDecMicro32.from(-0.009767175f).toFloat(), delta)
		assertEquals(0.5335246f, FixDecMicro32.from(0.5335246f).toFloat(), delta)
		assertEquals(-0.5335246f, FixDecMicro32.from(-0.5335246f).toFloat(), delta)
		assertEquals(14.77877f, FixDecMicro32.from(14.77877f).toFloat(), delta)
		assertEquals(-14.77877f, FixDecMicro32.from(-14.77877f).toFloat(), delta)
		assertEquals(57.959488f, FixDecMicro32.from(57.959488f).toFloat(), delta)
		assertEquals(-57.959488f, FixDecMicro32.from(-57.959488f).toFloat(), delta)
		assertEquals(2147.4836f, FixDecMicro32.from(2147.4836f).toFloat(), delta)
		assertEquals(-2147.4836f, FixDecMicro32.from(-2147.4836f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 2.0E-6
		assertEquals(1.0E-6, FixDecMicro32.from(1.0E-6).toDouble(), delta)
		assertEquals(-1.0E-6, FixDecMicro32.from(-1.0E-6).toDouble(), delta)
		assertEquals(6.0780890065293825E-5, FixDecMicro32.from(6.0780890065293825E-5).toDouble(), delta)
		assertEquals(-6.0780890065293825E-5, FixDecMicro32.from(-6.0780890065293825E-5).toDouble(), delta)
		assertEquals(0.0032344286451819098, FixDecMicro32.from(0.0032344286451819098).toDouble(), delta)
		assertEquals(-0.0032344286451819098, FixDecMicro32.from(-0.0032344286451819098).toDouble(), delta)
		assertEquals(0.009767174703735716, FixDecMicro32.from(0.009767174703735716).toDouble(), delta)
		assertEquals(-0.009767174703735716, FixDecMicro32.from(-0.009767174703735716).toDouble(), delta)
		assertEquals(0.5335245655773619, FixDecMicro32.from(0.5335245655773619).toDouble(), delta)
		assertEquals(-0.5335245655773619, FixDecMicro32.from(-0.5335245655773619).toDouble(), delta)
		assertEquals(14.778770326257877, FixDecMicro32.from(14.778770326257877).toDouble(), delta)
		assertEquals(-14.778770326257877, FixDecMicro32.from(-14.778770326257877).toDouble(), delta)
		assertEquals(57.95948821553071, FixDecMicro32.from(57.95948821553071).toDouble(), delta)
		assertEquals(-57.95948821553071, FixDecMicro32.from(-57.95948821553071).toDouble(), delta)
		assertEquals(2147.483647, FixDecMicro32.from(2147.483647).toDouble(), delta)
		assertEquals(-2147.483647, FixDecMicro32.from(-2147.483647).toDouble(), delta)
	}

	@Test
	fun testUnaryMinus() {
		assertThrows(FixedPointException::class.java) { -FixDecMicro32.raw(Int.MIN_VALUE) }
		assertEquals(2147483647, -FixDecMicro32.raw(-2147483647).raw)
		assertEquals(352975531, -FixDecMicro32.raw(-352975531).raw)
		assertEquals(30294627, -FixDecMicro32.raw(-30294627).raw)
		assertEquals(1475174, -FixDecMicro32.raw(-1475174).raw)
		assertEquals(25682, -FixDecMicro32.raw(-25682).raw)
		assertEquals(386, -FixDecMicro32.raw(-386).raw)
		assertEquals(0, -FixDecMicro32.raw(0).raw)
		assertEquals(-1, -FixDecMicro32.raw(1).raw)
		assertEquals(-16270, -FixDecMicro32.raw(16270).raw)
		assertEquals(-17228, -FixDecMicro32.raw(17228).raw)
		assertEquals(-778569, -FixDecMicro32.raw(778569).raw)
		assertEquals(-7590381, -FixDecMicro32.raw(7590381).raw)
		assertEquals(-793898393, -FixDecMicro32.raw(793898393).raw)
		assertEquals(-2147483647, -FixDecMicro32.raw(2147483647).raw)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixDecMicro32, b: FixDecMicro32, c: FixDecMicro32) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}
		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixDecMicro32.from(a), FixDecMicro32.from(b), FixDecMicro32.from(c))
			assertEquals(FixDecMicro32.from(c), FixDecMicro32.from(a) + b)
			assertEquals(FixDecMicro32.from(c), b + FixDecMicro32.from(a))
			assertEquals(FixDecMicro32.from(a), FixDecMicro32.from(c) - b)
			assertEquals(FixDecMicro32.from(b), c - FixDecMicro32.from(a))
		}
		testValues(FixDecMicro32.raw(Int.MIN_VALUE), FixDecMicro32.ONE, FixDecMicro32.raw(Int.MIN_VALUE + 1000000))
		testValues(-2147, 698, -1449)
		testValues(0, 86, 86)
		testValues(1, 37, 38)
		testValues(1073, 110, 1183)
		testValues(FixDecMicro32.raw(Int.MAX_VALUE - 1000000), FixDecMicro32.ONE, FixDecMicro32.raw(Int.MAX_VALUE))
	}

	@Test
	fun testMultiplication() {
		assertEquals(FixDecMicro32.raw(Int.MAX_VALUE), 1 * FixDecMicro32.raw(Int.MAX_VALUE))
		assertEquals(FixDecMicro32.raw(Int.MIN_VALUE), 1 * FixDecMicro32.raw(Int.MIN_VALUE))
		assertEquals(FixDecMicro32.raw(Int.MIN_VALUE + 1), -1 * FixDecMicro32.raw(Int.MAX_VALUE))
		assertThrows(FixedPointException::class.java) { -1 * FixDecMicro32.raw(Int.MIN_VALUE) }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixDecMicro32.from(a * b), FixDecMicro32.from(a) * FixDecMicro32.from(b))
			assertEquals(FixDecMicro32.from(a * b), FixDecMicro32.from(a) * b)
			assertEquals(FixDecMicro32.from(a * b), b * FixDecMicro32.from(a))
		}
		testValues(-2147, 0)
		testValues(-1181, 0)
		testValues(-294, 0)
		testValues(-256, 0)
		testValues(-134, 0)
		testValues(-37, 0)
		testValues(-35, 0)
		testValues(0, 0)
		testValues(1, 0)
		testValues(51, 0)
		testValues(70, 0)
		testValues(158, 0)
		testValues(239, 0)
		testValues(2147, 0)
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixDecMicro32.ZERO < FixDecMicro32.ONE)
		assertFalse(FixDecMicro32.ZERO > FixDecMicro32.ONE)
		assertFalse(FixDecMicro32.ONE < FixDecMicro32.ONE)
		assertFalse(FixDecMicro32.ONE > FixDecMicro32.ONE)
		assertTrue(FixDecMicro32.ONE <= FixDecMicro32.ONE)
		assertTrue(FixDecMicro32.ONE >= FixDecMicro32.ONE)
		assertTrue(FixDecMicro32.raw(Int.MIN_VALUE) < FixDecMicro32.raw(Int.MAX_VALUE))

		val minDelta = FixDecMicro32.raw(1)
		assertEquals(FixDecMicro32.from(12), FixDecMicro32.from(12))
		assertNotEquals(FixDecMicro32.from(12), FixDecMicro32.from(12) - minDelta)
		assertTrue(FixDecMicro32.from(1.0E-6) < FixDecMicro32.from(1.0E-6) + minDelta)
		assertFalse(FixDecMicro32.from(4.101245270674489E-4) < FixDecMicro32.from(4.101245270674489E-4) - minDelta)
		assertTrue(FixDecMicro32.from(4.101245270674489E-4) < FixDecMicro32.from(4.101245270674489E-4) + minDelta)
		assertFalse(FixDecMicro32.from(0.0014602674387652094) < FixDecMicro32.from(0.0014602674387652094) - minDelta)
		assertTrue(FixDecMicro32.from(0.0014602674387652094) < FixDecMicro32.from(0.0014602674387652094) + minDelta)
		assertFalse(FixDecMicro32.from(0.008314038763645868) < FixDecMicro32.from(0.008314038763645868) - minDelta)
		assertTrue(FixDecMicro32.from(0.008314038763645868) < FixDecMicro32.from(0.008314038763645868) + minDelta)
		assertFalse(FixDecMicro32.from(0.06313960308834927) < FixDecMicro32.from(0.06313960308834927) - minDelta)
		assertTrue(FixDecMicro32.from(0.06313960308834927) < FixDecMicro32.from(0.06313960308834927) + minDelta)
		assertFalse(FixDecMicro32.from(0.18938331340622416) < FixDecMicro32.from(0.18938331340622416) - minDelta)
		assertTrue(FixDecMicro32.from(0.18938331340622416) < FixDecMicro32.from(0.18938331340622416) + minDelta)
		assertFalse(FixDecMicro32.from(2.75487661886147) < FixDecMicro32.from(2.75487661886147) - minDelta)
		assertTrue(FixDecMicro32.from(2.75487661886147) < FixDecMicro32.from(2.75487661886147) + minDelta)
		assertFalse(FixDecMicro32.from(10.653674356901822) < FixDecMicro32.from(10.653674356901822) - minDelta)
		assertTrue(FixDecMicro32.from(10.653674356901822) < FixDecMicro32.from(10.653674356901822) + minDelta)
		assertFalse(FixDecMicro32.from(150.2659312211488) < FixDecMicro32.from(150.2659312211488) - minDelta)
		assertTrue(FixDecMicro32.from(150.2659312211488) < FixDecMicro32.from(150.2659312211488) + minDelta)
		assertFalse(FixDecMicro32.from(2147.483647) < FixDecMicro32.from(2147.483647) - minDelta)
	}
}
