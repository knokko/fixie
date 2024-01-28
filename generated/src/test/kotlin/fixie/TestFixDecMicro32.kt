package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixDecMicro32 {

	fun assertEquals(a: FixDecMicro32, b: FixDecMicro32, maxDelta: FixDecMicro32) {
		val rawDifference = a.raw - b.raw
		if (rawDifference.absoluteValue > maxDelta.raw) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixDecMicro32.ZERO.toString())
		assertEquals("1", FixDecMicro32.ONE.toString())
		assertTrue((FixDecMicro32.ONE / 3).toString().startsWith("0.33"))
		assertEquals("-1", (-FixDecMicro32.ONE).toString())
		assertTrue((FixDecMicro32.ONE / -3).toString().startsWith("-0.33"))
		assertTrue((FixDecMicro32.from(2146) + FixDecMicro32.ONE / 3).toString().endsWith((FixDecMicro32.ONE / 3).toString().substring(1)))
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixDecMicro32.ONE, FixDecMicro32.from(one))

		fun testValue(value: Int) = assertEquals(value, FixDecMicro32.from(value).toInt())
		fun testOverflow(value: Int) = assertThrows(FixedPointException::class.java) { FixDecMicro32.from(value) }
		testValue(-2147)
		testValue(-883)
		testValue(0)
		testValue(1)
		testValue(375)
		testValue(2147)

		testOverflow(-2147483648)
		testOverflow(-978689185)
		testOverflow(-974146)
		testOverflow(-740009)
		testOverflow(-16299)
		testOverflow(-2148)

		testOverflow(2148)
		testOverflow(1805550)
		testOverflow(92804951)
		testOverflow(2147483647)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixDecMicro32.ONE, FixDecMicro32.from(one))

		fun testValue(value: Long) = assertEquals(value, FixDecMicro32.from(value).toLong())
		fun testOverflow(value: Long) = assertThrows(FixedPointException::class.java) { FixDecMicro32.from(value) }
		testValue(-2147)
		testValue(-883)
		testValue(0)
		testValue(1)
		testValue(375)
		testValue(2147)

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
		testOverflow(-2148)

		testOverflow(2148)
		testOverflow(1805550)
		testOverflow(92804951)
		testOverflow(664974396)
		testOverflow(112856022871)
		testOverflow(11897632377332)
		testOverflow(290157834400563)
		testOverflow(7896075458500019)
		testOverflow(278141738747173153)
		testOverflow(9223372036854775807)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixDecMicro32.ONE, FixDecMicro32.from(1f))
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

		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(2148.4836f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(-2148.4836f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(217663.14f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(-217663.14f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(4611686.0f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(-4611686.0f) }
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixDecMicro32.ONE, FixDecMicro32.from(1.0))
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

		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(2148.483647) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(-2148.483647) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(217663.1448319552) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(-217663.1448319552) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(4611686.0141324205) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(-4611686.0141324205) }
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
		testValues(-2147, 1680, -467)
		testValues(-1424, 672, -752)
		testValues(-97, 1634, 1537)
		testValues(-47, 780, 733)
		testValues(-30, 905, 875)
		testValues(0, 1657, 1657)
		testValues(1, 269, 270)
		testValues(366, 895, 1261)
		testValues(407, 385, 792)
		testValues(2147, 0, 2147)
		testValues(FixDecMicro32.raw(Int.MAX_VALUE - 1000000), FixDecMicro32.ONE, FixDecMicro32.raw(Int.MAX_VALUE))

		fun testOverflowPlus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) + FixDecMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro32.from(b) }
		}

		fun testOverflowMinus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) - FixDecMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro32.from(b) }
		}

		fun testOverflowPlus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) + FixDecMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro32.from(b) }
		}

		fun testOverflowMinus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) - FixDecMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro32.from(b) }
		}

		fun testOverflowPlus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) + FixDecMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro32.from(b) }
		}

		fun testOverflowMinus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) - FixDecMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro32.from(b) }
		}

		fun testOverflowPlus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) + FixDecMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro32.from(b) }
		}

		fun testOverflowMinus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) - FixDecMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro32.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro32.from(b) }
		}

		testOverflowPlus(-2147, 4295)
		testOverflowPlus(-2147L, 4295L)
		testOverflowPlus(-2147.0f, 4295.0f)
		testOverflowPlus(-2147.0, 4295.0)
		testOverflowPlus(-2147, 18455616)
		testOverflowPlus(-2147L, 18455616L)
		testOverflowPlus(-2147.0, 1.8455616E7)
		testOverflowMinus(-2147, 1)
		testOverflowMinus(-2147L, 1L)
		testOverflowMinus(-2147.0, 1.0)
		testOverflowMinus(-2147, 4)
		testOverflowMinus(-2147L, 4L)
		testOverflowMinus(-2147.0, 4.0)
		testOverflowPlus(-1155, 3303)
		testOverflowPlus(-1155L, 3303L)
		testOverflowPlus(-1155.0f, 3303.0f)
		testOverflowPlus(-1155.0, 3303.0)
		testOverflowPlus(-1155, 10916416)
		testOverflowPlus(-1155L, 10916416L)
		testOverflowPlus(-1155.0, 1.0916416E7)
		testOverflowMinus(-1155, 993)
		testOverflowMinus(-1155L, 993L)
		testOverflowMinus(-1155.0f, 993.0f)
		testOverflowMinus(-1155.0, 993.0)
		testOverflowMinus(-1155, 988036)
		testOverflowMinus(-1155L, 988036L)
		testOverflowMinus(-1155.0, 988036.0)
		testOverflowPlus(0, 2148)
		testOverflowPlus(0L, 2148L)
		testOverflowPlus(0, 4618201)
		testOverflowPlus(0L, 4618201L)
		testOverflowMinus(0, 2148)
		testOverflowMinus(0L, 2148L)
		testOverflowMinus(0, 4618201)
		testOverflowMinus(0L, 4618201L)
		testOverflowPlus(1, 2147)
		testOverflowPlus(1L, 2147L)
		testOverflowPlus(1.0, 2147.0)
		testOverflowPlus(1, 4613904)
		testOverflowPlus(1L, 4613904L)
		testOverflowMinus(1, 2149)
		testOverflowMinus(1L, 2149L)
		testOverflowMinus(1.0, 2149.0)
		testOverflowMinus(1, 4622500)
		testOverflowMinus(1L, 4622500L)
		testOverflowPlus(2147, 1)
		testOverflowPlus(2147L, 1L)
		testOverflowPlus(2147.0, 1.0)
		testOverflowPlus(2147, 4)
		testOverflowPlus(2147L, 4L)
		testOverflowPlus(2147.0, 4.0)
		testOverflowMinus(2147, 4295)
		testOverflowMinus(2147L, 4295L)
		testOverflowMinus(2147.0f, 4295.0f)
		testOverflowMinus(2147.0, 4295.0)
		testOverflowMinus(2147, 18455616)
		testOverflowMinus(2147L, 18455616L)
		testOverflowMinus(2147.0, 1.8455616E7)
		assertEquals(FixDecMicro32.raw(2146857906), FixDecMicro32.raw(-142094) + 2147)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixDecMicro32.raw(Int.MAX_VALUE), 1 * FixDecMicro32.raw(Int.MAX_VALUE))
		assertEquals(FixDecMicro32.raw(Int.MAX_VALUE), FixDecMicro32.raw(Int.MAX_VALUE) / 1)
		assertEquals(FixDecMicro32.raw(Int.MIN_VALUE), 1 * FixDecMicro32.raw(Int.MIN_VALUE))
		assertEquals(FixDecMicro32.raw(Int.MIN_VALUE), FixDecMicro32.raw(Int.MIN_VALUE) / 1)
		assertEquals(FixDecMicro32.raw(Int.MIN_VALUE + 1), -1 * FixDecMicro32.raw(Int.MAX_VALUE))
		assertEquals(FixDecMicro32.raw(Int.MIN_VALUE + 1), FixDecMicro32.raw(Int.MAX_VALUE) / -1)
		assertThrows(FixedPointException::class.java) { -1 * FixDecMicro32.raw(Int.MIN_VALUE) }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.raw(Int.MIN_VALUE) / -1 }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixDecMicro32.from(a * b), FixDecMicro32.from(a) * FixDecMicro32.from(b))
			assertEquals(FixDecMicro32.from(a * b), FixDecMicro32.from(a) * b)
			assertEquals(FixDecMicro32.from(a * b), b * FixDecMicro32.from(a))
			if (b != 0L) assertEquals(FixDecMicro32.from(a), FixDecMicro32.from(a * b) / b)
			if (a != 0L) assertEquals(FixDecMicro32.from(b), FixDecMicro32.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixDecMicro32.from(b), FixDecMicro32.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixDecMicro32.from(a * b), FixDecMicro32.from(a) * b.toInt())
				assertEquals(FixDecMicro32.from(a * b), b.toInt() * FixDecMicro32.from(a))
			}
		}
		testValues(-2147, 0)
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(-1181) * 70 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(-294) * -37 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(-256) * -1181 }
		testValues(-134, 1)
		testValues(-37, -37)
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(-35) * 2147 }
		testValues(0, -2147)
		testValues(1, -134)
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(51) * -294 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(70) * 70 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(158) * -37 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(239) * 158 }
		assertThrows(FixedPointException::class.java) { FixDecMicro32.from(2147) * -37 }
		assertEquals(FixDecMicro32.raw(-898622), (FixDecMicro32.raw(-898622) * FixDecMicro32.raw(933565)) / FixDecMicro32.raw(933565), FixDecMicro32.raw(10000))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixDecMicro32.ZERO < FixDecMicro32.ONE)
		assertTrue(0 < FixDecMicro32.ONE)
		assertFalse(FixDecMicro32.ZERO > FixDecMicro32.ONE)
		assertFalse(0 > FixDecMicro32.ONE)
		assertFalse(FixDecMicro32.ONE < FixDecMicro32.ONE)
		assertFalse(FixDecMicro32.ONE < 1)
		assertFalse(FixDecMicro32.ONE > FixDecMicro32.ONE)
		assertTrue(FixDecMicro32.ONE <= FixDecMicro32.ONE)
		assertTrue(FixDecMicro32.ONE >= FixDecMicro32.ONE)
		assertTrue(FixDecMicro32.raw(Int.MIN_VALUE) < FixDecMicro32.raw(Int.MAX_VALUE))

		val minDelta = FixDecMicro32.raw(1)
		assertEquals(FixDecMicro32.from(12), FixDecMicro32.from(12))
		assertNotEquals(FixDecMicro32.from(12), FixDecMicro32.from(12) - minDelta)
		assertTrue(FixDecMicro32.from(1.0E-6) < FixDecMicro32.from(1.0E-6) + minDelta)
		assertTrue(1.0E-6 < FixDecMicro32.from(1.0E-6) + minDelta)
		assertFalse(FixDecMicro32.from(4.101245270674489E-4) < FixDecMicro32.from(4.101245270674489E-4) - minDelta)
		assertFalse(4.101245270674489E-4 < FixDecMicro32.from(4.101245270674489E-4) - minDelta)
		assertTrue(FixDecMicro32.from(4.101245270674489E-4) < FixDecMicro32.from(4.101245270674489E-4) + minDelta)
		assertTrue(4.101245270674489E-4 < FixDecMicro32.from(4.101245270674489E-4) + minDelta)
		assertFalse(FixDecMicro32.from(0.0014602674387652094) < FixDecMicro32.from(0.0014602674387652094) - minDelta)
		assertFalse(0.0014602674387652094 < FixDecMicro32.from(0.0014602674387652094) - minDelta)
		assertTrue(FixDecMicro32.from(0.0014602674387652094) < FixDecMicro32.from(0.0014602674387652094) + minDelta)
		assertTrue(0.0014602674387652094 < FixDecMicro32.from(0.0014602674387652094) + minDelta)
		assertFalse(FixDecMicro32.from(0.008314038763645868) < FixDecMicro32.from(0.008314038763645868) - minDelta)
		assertFalse(0.008314038763645868 < FixDecMicro32.from(0.008314038763645868) - minDelta)
		assertTrue(FixDecMicro32.from(0.008314038763645868) < FixDecMicro32.from(0.008314038763645868) + minDelta)
		assertTrue(0.008314038763645868 < FixDecMicro32.from(0.008314038763645868) + minDelta)
		assertFalse(FixDecMicro32.from(0.06313960308834927) < FixDecMicro32.from(0.06313960308834927) - minDelta)
		assertFalse(0.06313960308834927 < FixDecMicro32.from(0.06313960308834927) - minDelta)
		assertTrue(FixDecMicro32.from(0.06313960308834927) < FixDecMicro32.from(0.06313960308834927) + minDelta)
		assertTrue(0.06313960308834927 < FixDecMicro32.from(0.06313960308834927) + minDelta)
		assertFalse(FixDecMicro32.from(0.18938331340622416) < FixDecMicro32.from(0.18938331340622416) - minDelta)
		assertFalse(0.18938331340622416 < FixDecMicro32.from(0.18938331340622416) - minDelta)
		assertTrue(FixDecMicro32.from(0.18938331340622416) < FixDecMicro32.from(0.18938331340622416) + minDelta)
		assertTrue(0.18938331340622416 < FixDecMicro32.from(0.18938331340622416) + minDelta)
		assertFalse(FixDecMicro32.from(2.75487661886147) < FixDecMicro32.from(2.75487661886147) - minDelta)
		assertFalse(2.75487661886147 < FixDecMicro32.from(2.75487661886147) - minDelta)
		assertTrue(FixDecMicro32.from(2.75487661886147) < FixDecMicro32.from(2.75487661886147) + minDelta)
		assertTrue(2.75487661886147 < FixDecMicro32.from(2.75487661886147) + minDelta)
		assertFalse(FixDecMicro32.from(10.653674356901822) < FixDecMicro32.from(10.653674356901822) - minDelta)
		assertFalse(10.653674356901822 < FixDecMicro32.from(10.653674356901822) - minDelta)
		assertTrue(FixDecMicro32.from(10.653674356901822) < FixDecMicro32.from(10.653674356901822) + minDelta)
		assertTrue(10.653674356901822 < FixDecMicro32.from(10.653674356901822) + minDelta)
		assertFalse(FixDecMicro32.from(150.2659312211488) < FixDecMicro32.from(150.2659312211488) - minDelta)
		assertFalse(150.2659312211488 < FixDecMicro32.from(150.2659312211488) - minDelta)
		assertTrue(FixDecMicro32.from(150.2659312211488) < FixDecMicro32.from(150.2659312211488) + minDelta)
		assertTrue(150.2659312211488 < FixDecMicro32.from(150.2659312211488) + minDelta)
		assertFalse(FixDecMicro32.from(2147.483647) < FixDecMicro32.from(2147.483647) - minDelta)
		assertFalse(2147.483647 < FixDecMicro32.from(2147.483647) - minDelta)
		assertTrue(FixDecMicro32.raw(Int.MAX_VALUE) >= 2147)
		assertTrue(FixDecMicro32.raw(Int.MAX_VALUE) > 2147)
		assertTrue(FixDecMicro32.raw(Int.MAX_VALUE) < 2148)
		assertTrue(FixDecMicro32.raw(Int.MAX_VALUE) < 2150.1479999999997)
		assertTrue(FixDecMicro32.raw(Int.MAX_VALUE) < Int.MAX_VALUE)
		assertTrue(FixDecMicro32.raw(Int.MAX_VALUE) < Int.MAX_VALUE.toFloat())
		assertTrue(FixDecMicro32.raw(Int.MAX_VALUE) < Int.MAX_VALUE.toDouble())
		assertTrue(FixDecMicro32.raw(Int.MIN_VALUE) <= -2147)
		assertTrue(FixDecMicro32.raw(Int.MIN_VALUE) < -2147)
		assertTrue(FixDecMicro32.raw(Int.MIN_VALUE) > -2148)
		assertTrue(FixDecMicro32.raw(Int.MIN_VALUE) > -2150.1479999999997)
		assertTrue(FixDecMicro32.raw(Int.MIN_VALUE) > Int.MIN_VALUE)
		assertTrue(FixDecMicro32.raw(Int.MIN_VALUE) > Int.MIN_VALUE.toFloat())
		assertTrue(FixDecMicro32.raw(Int.MIN_VALUE) > Int.MIN_VALUE.toDouble())
	}
}
