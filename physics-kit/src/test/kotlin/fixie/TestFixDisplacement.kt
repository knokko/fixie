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
		assertTrue((FixDisplacement.from(21473) + FixDisplacement.ONE / 3).toString().endsWith((FixDisplacement.ONE / 3).toString().substring(1)))
		assertEquals("0.0625", (FixDisplacement.ONE / 16).toString())
		assertEquals("0.01", (FixDisplacement.ONE / 100).toString())
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixDisplacement.ONE, FixDisplacement.from(one))

		fun testValue(value: Int) = assertEquals(value, FixDisplacement.from(value).toInt())
		fun testOverflow(value: Int) = assertThrows(FixedPointException::class.java) { FixDisplacement.from(value) }
		testValue(-21474)
		testValue(-1457)
		testValue(-599)
		testValue(0)
		testValue(1)
		testValue(99)
		testValue(1630)
		testValue(21474)

		testOverflow(-2147483648)
		testOverflow(-978689185)
		testOverflow(-974146)
		testOverflow(-740009)
		testOverflow(-21475)

		testOverflow(21475)
		testOverflow(17006839)
		testOverflow(908752963)
		testOverflow(2147483647)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixDisplacement.ONE, FixDisplacement.from(one))

		fun testValue(value: Long) = assertEquals(value, FixDisplacement.from(value).toLong())
		fun testOverflow(value: Long) = assertThrows(FixedPointException::class.java) { FixDisplacement.from(value) }
		testValue(-21474)
		testValue(-1457)
		testValue(-599)
		testValue(0)
		testValue(1)
		testValue(99)
		testValue(1630)
		testValue(21474)

		testOverflow(Long.MIN_VALUE)
		testOverflow(-589153389781490615)
		testOverflow(-43117419028065905)
		testOverflow(-785861820617605)
		testOverflow(-19482330338386)
		testOverflow(-34771906616)
		testOverflow(-6550113726)
		testOverflow(-448440224)
		testOverflow(-2065398)
		testOverflow(-21475)

		testOverflow(21475)
		testOverflow(17006839)
		testOverflow(908752963)
		testOverflow(6508219567)
		testOverflow(634087209756)
		testOverflow(92151804038599)
		testOverflow(821482842808092)
		testOverflow(86400144687920735)
		testOverflow(9223372036854775807)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixDisplacement.ONE, FixDisplacement.from(1f))
		val delta = 2.0E-5f
		assertEquals(1.0E-5f, FixDisplacement.from(1.0E-5f).toFloat(), delta)
		assertEquals(-1.0E-5f, FixDisplacement.from(-1.0E-5f).toFloat(), delta)
		assertEquals(6.078089E-4f, FixDisplacement.from(6.078089E-4f).toFloat(), delta)
		assertEquals(-6.078089E-4f, FixDisplacement.from(-6.078089E-4f).toFloat(), delta)
		assertEquals(0.032344285f, FixDisplacement.from(0.032344285f).toFloat(), delta)
		assertEquals(-0.032344285f, FixDisplacement.from(-0.032344285f).toFloat(), delta)
		assertEquals(0.09767175f, FixDisplacement.from(0.09767175f).toFloat(), delta)
		assertEquals(-0.09767175f, FixDisplacement.from(-0.09767175f).toFloat(), delta)
		assertEquals(5.3352456f, FixDisplacement.from(5.3352456f).toFloat(), delta)
		assertEquals(-5.3352456f, FixDisplacement.from(-5.3352456f).toFloat(), delta)
		assertEquals(147.7877f, FixDisplacement.from(147.7877f).toFloat(), delta)
		assertEquals(-147.7877f, FixDisplacement.from(-147.7877f).toFloat(), delta)
		assertEquals(579.5949f, FixDisplacement.from(579.5949f).toFloat(), delta)
		assertEquals(-579.5949f, FixDisplacement.from(-579.5949f).toFloat(), delta)
		assertEquals(21474.836f, FixDisplacement.from(21474.836f).toFloat(), delta)
		assertEquals(-21474.836f, FixDisplacement.from(-21474.836f).toFloat(), delta)

		assertThrows(FixedPointException::class.java) { FixDisplacement.from(21476.984f) }
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(-21476.984f) }
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(2175836.0f) }
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(-2175836.0f) }
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(4.61260832E8f) }
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(-4.61260832E8f) }
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixDisplacement.ONE, FixDisplacement.from(1.0))
		val delta = 2.0E-5
		assertEquals(1.0E-5, FixDisplacement.from(1.0E-5).toDouble(), delta)
		assertEquals(-1.0E-5, FixDisplacement.from(-1.0E-5).toDouble(), delta)
		assertEquals(6.078089006529383E-4, FixDisplacement.from(6.078089006529383E-4).toDouble(), delta)
		assertEquals(-6.078089006529383E-4, FixDisplacement.from(-6.078089006529383E-4).toDouble(), delta)
		assertEquals(0.032344286451819096, FixDisplacement.from(0.032344286451819096).toDouble(), delta)
		assertEquals(-0.032344286451819096, FixDisplacement.from(-0.032344286451819096).toDouble(), delta)
		assertEquals(0.09767174703735716, FixDisplacement.from(0.09767174703735716).toDouble(), delta)
		assertEquals(-0.09767174703735716, FixDisplacement.from(-0.09767174703735716).toDouble(), delta)
		assertEquals(5.33524565577362, FixDisplacement.from(5.33524565577362).toDouble(), delta)
		assertEquals(-5.33524565577362, FixDisplacement.from(-5.33524565577362).toDouble(), delta)
		assertEquals(147.7877032625788, FixDisplacement.from(147.7877032625788).toDouble(), delta)
		assertEquals(-147.7877032625788, FixDisplacement.from(-147.7877032625788).toDouble(), delta)
		assertEquals(579.5948821553071, FixDisplacement.from(579.5948821553071).toDouble(), delta)
		assertEquals(-579.5948821553071, FixDisplacement.from(-579.5948821553071).toDouble(), delta)
		assertEquals(21474.83647, FixDisplacement.from(21474.83647).toDouble(), delta)
		assertEquals(-21474.83647, FixDisplacement.from(-21474.83647).toDouble(), delta)

		assertThrows(FixedPointException::class.java) { FixDisplacement.from(21476.983953647) }
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(-21476.983953647) }
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(2175835.9089135965) }
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(-2175835.9089135965) }
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(4.612608397452107E8) }
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(-4.612608397452107E8) }
	}

	@Test
	fun testUnaryMinus() {
		assertThrows(FixedPointException::class.java) { -FixDisplacement.raw(Int.MIN_VALUE) }
		assertEquals(2147483647, -FixDisplacement.raw(-2147483647).raw)
		assertEquals(352975531, -FixDisplacement.raw(-352975531).raw)
		assertEquals(30294627, -FixDisplacement.raw(-30294627).raw)
		assertEquals(1475174, -FixDisplacement.raw(-1475174).raw)
		assertEquals(25682, -FixDisplacement.raw(-25682).raw)
		assertEquals(386, -FixDisplacement.raw(-386).raw)
		assertEquals(0, -FixDisplacement.raw(0).raw)
		assertEquals(-1, -FixDisplacement.raw(1).raw)
		assertEquals(-16270, -FixDisplacement.raw(16270).raw)
		assertEquals(-17228, -FixDisplacement.raw(17228).raw)
		assertEquals(-778569, -FixDisplacement.raw(778569).raw)
		assertEquals(-7590381, -FixDisplacement.raw(7590381).raw)
		assertEquals(-793898393, -FixDisplacement.raw(793898393).raw)
		assertEquals(-2147483647, -FixDisplacement.raw(2147483647).raw)
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

		testValues(FixDisplacement.raw(Int.MIN_VALUE), FixDisplacement.ONE, FixDisplacement.raw(Int.MIN_VALUE + 100000))
		testValues(-21474, 16714, -4760)
		testValues(-15319, 14172, -1147)
		testValues(-12887, 20736, 7849)
		testValues(-1070, 10362, 9292)
		testValues(-870, 4113, 3243)
		testValues(-86, 21473, 21387)
		testValues(-70, 94, 24)
		testValues(0, 20407, 20407)
		testValues(1, 9342, 9343)
		testValues(429, 8991, 9420)
		testValues(607, 20432, 21039)
		testValues(888, 18569, 19457)
		testValues(1519, 15389, 16908)
		testValues(21474, 0, 21474)
		testValues(FixDisplacement.raw(Int.MAX_VALUE - 100000), FixDisplacement.ONE, FixDisplacement.raw(Int.MAX_VALUE))

		fun testOverflowPlus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) + FixDisplacement.from(b) }
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDisplacement.from(b) }
		}

		fun testOverflowMinus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) - FixDisplacement.from(b) }
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDisplacement.from(b) }
		}

		fun testOverflowPlus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) + FixDisplacement.from(b) }
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDisplacement.from(b) }
		}

		fun testOverflowMinus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) - FixDisplacement.from(b) }
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDisplacement.from(b) }
		}

		fun testOverflowPlus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) + FixDisplacement.from(b) }
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDisplacement.from(b) }
		}

		fun testOverflowMinus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) - FixDisplacement.from(b) }
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDisplacement.from(b) }
		}

		fun testOverflowPlus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) + FixDisplacement.from(b) }
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDisplacement.from(b) }
		}

		fun testOverflowMinus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) - FixDisplacement.from(b) }
			assertThrows(FixedPointException::class.java) { FixDisplacement.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDisplacement.from(b) }
		}

		testOverflowPlus(-21474, 42949)
		testOverflowPlus(-21474L, 42949L)
		testOverflowPlus(-21474.0f, 42949.0f)
		testOverflowPlus(-21474.0, 42949.0)
		testOverflowPlus(-21474, 1844702500)
		testOverflowPlus(-21474L, 1844702500L)
		testOverflowMinus(-21474, 1)
		testOverflowMinus(-21474L, 1L)
		testOverflowMinus(-21474, 4)
		testOverflowMinus(-21474L, 4L)
		testOverflowMinus(-21474.0, 4.0)
		testOverflowPlus(-8417, 29892)
		testOverflowPlus(-8417L, 29892L)
		testOverflowPlus(-8417.0f, 29892.0f)
		testOverflowPlus(-8417.0, 29892.0)
		testOverflowPlus(-8417, 893591449)
		testOverflowPlus(-8417L, 893591449L)
		testOverflowMinus(-8417, 13058)
		testOverflowMinus(-8417L, 13058L)
		testOverflowMinus(-8417.0f, 13058.0f)
		testOverflowMinus(-8417.0, 13058.0)
		testOverflowMinus(-8417, 170537481)
		testOverflowMinus(-8417L, 170537481L)
		testOverflowPlus(0, 21475)
		testOverflowPlus(0L, 21475L)
		testOverflowPlus(0, 461218576)
		testOverflowPlus(0L, 461218576L)
		testOverflowMinus(0, 21475)
		testOverflowMinus(0L, 21475L)
		testOverflowMinus(0, 461218576)
		testOverflowMinus(0L, 461218576L)
		testOverflowPlus(1, 21474)
		testOverflowPlus(1L, 21474L)
		testOverflowPlus(1, 461175625)
		testOverflowPlus(1L, 461175625L)
		testOverflowMinus(1, 21476)
		testOverflowMinus(1L, 21476L)
		testOverflowMinus(1, 461261529)
		testOverflowMinus(1L, 461261529L)
		testOverflowPlus(21474, 1)
		testOverflowPlus(21474L, 1L)
		testOverflowPlus(21474, 4)
		testOverflowPlus(21474L, 4L)
		testOverflowPlus(21474.0, 4.0)
		testOverflowMinus(21474, 42949)
		testOverflowMinus(21474L, 42949L)
		testOverflowMinus(21474.0, 42949.0)
		testOverflowMinus(21474, 1844702500)
		testOverflowMinus(21474L, 1844702500L)
		assertEquals(FixDisplacement.raw(2147318865), FixDisplacement.raw(-81135) + 21474)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixDisplacement.raw(Int.MAX_VALUE), 1 * FixDisplacement.raw(Int.MAX_VALUE))
		assertEquals(FixDisplacement.raw(Int.MAX_VALUE), FixDisplacement.raw(Int.MAX_VALUE) / 1)
		assertEquals(FixDisplacement.raw(Int.MIN_VALUE), 1 * FixDisplacement.raw(Int.MIN_VALUE))
		assertEquals(FixDisplacement.raw(Int.MIN_VALUE), FixDisplacement.raw(Int.MIN_VALUE) / 1)
		assertEquals(FixDisplacement.raw(Int.MIN_VALUE + 1), -1 * FixDisplacement.raw(Int.MAX_VALUE))
		assertEquals(FixDisplacement.raw(Int.MIN_VALUE + 1), FixDisplacement.raw(Int.MAX_VALUE) / -1)
		assertThrows(FixedPointException::class.java) { -1 * FixDisplacement.raw(Int.MIN_VALUE) }
		assertThrows(FixedPointException::class.java) { FixDisplacement.raw(Int.MIN_VALUE) / -1 }

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
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(-21474) * -2445 }
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(-11952) * -5684 }
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(-5684) * 1367 }
		testValues(-2445, 0)
		testValues(-469, 1)
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(-397) * 1367 }
		testValues(-385, 0)
		testValues(-67, 182)
		testValues(-8, -469)
		testValues(0, 182)
		testValues(1, 1)
		testValues(24, 163)
		testValues(65, -67)
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(163) * -2445 }
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(182) * -397 }
		testValues(775, 1)
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(994) * 182 }
		testValues(1367, 0)
		assertThrows(FixedPointException::class.java) { FixDisplacement.from(2428) * 2428 }
		testValues(21474, 1)
		assertEquals(FixDisplacement.raw(-82055), (FixDisplacement.raw(-82055) * FixDisplacement.raw(94770)) / FixDisplacement.raw(94770), FixDisplacement.raw(1000))
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
		assertTrue(FixDisplacement.raw(Int.MIN_VALUE) < FixDisplacement.raw(Int.MAX_VALUE))

		val minDelta = FixDisplacement.raw(1)
		assertEquals(FixDisplacement.from(12), FixDisplacement.from(12))
		assertNotEquals(FixDisplacement.from(12), FixDisplacement.from(12) - minDelta)
		assertTrue(FixDisplacement.from(1.0E-5) < FixDisplacement.from(1.0E-5) + minDelta)
		assertTrue(1.0E-5 < FixDisplacement.from(1.0E-5) + minDelta)
		assertFalse(FixDisplacement.from(0.0041012452706744905) < FixDisplacement.from(0.0041012452706744905) - minDelta)
		assertFalse(0.0041012452706744905 < FixDisplacement.from(0.0041012452706744905) - minDelta)
		assertTrue(FixDisplacement.from(0.0041012452706744905) < FixDisplacement.from(0.0041012452706744905) + minDelta)
		assertTrue(0.0041012452706744905 < FixDisplacement.from(0.0041012452706744905) + minDelta)
		assertFalse(FixDisplacement.from(0.0146026743876521) < FixDisplacement.from(0.0146026743876521) - minDelta)
		assertFalse(0.0146026743876521 < FixDisplacement.from(0.0146026743876521) - minDelta)
		assertTrue(FixDisplacement.from(0.0146026743876521) < FixDisplacement.from(0.0146026743876521) + minDelta)
		assertTrue(0.0146026743876521 < FixDisplacement.from(0.0146026743876521) + minDelta)
		assertFalse(FixDisplacement.from(0.0831403876364587) < FixDisplacement.from(0.0831403876364587) - minDelta)
		assertFalse(0.0831403876364587 < FixDisplacement.from(0.0831403876364587) - minDelta)
		assertTrue(FixDisplacement.from(0.0831403876364587) < FixDisplacement.from(0.0831403876364587) + minDelta)
		assertTrue(0.0831403876364587 < FixDisplacement.from(0.0831403876364587) + minDelta)
		assertFalse(FixDisplacement.from(0.6313960308834929) < FixDisplacement.from(0.6313960308834929) - minDelta)
		assertFalse(0.6313960308834929 < FixDisplacement.from(0.6313960308834929) - minDelta)
		assertTrue(FixDisplacement.from(0.6313960308834929) < FixDisplacement.from(0.6313960308834929) + minDelta)
		assertTrue(0.6313960308834929 < FixDisplacement.from(0.6313960308834929) + minDelta)
		assertFalse(FixDisplacement.from(1.893833134062242) < FixDisplacement.from(1.893833134062242) - minDelta)
		assertFalse(1.893833134062242 < FixDisplacement.from(1.893833134062242) - minDelta)
		assertTrue(FixDisplacement.from(1.893833134062242) < FixDisplacement.from(1.893833134062242) + minDelta)
		assertTrue(1.893833134062242 < FixDisplacement.from(1.893833134062242) + minDelta)
		assertFalse(FixDisplacement.from(27.548766188614707) < FixDisplacement.from(27.548766188614707) - minDelta)
		assertFalse(27.548766188614707 < FixDisplacement.from(27.548766188614707) - minDelta)
		assertTrue(FixDisplacement.from(27.548766188614707) < FixDisplacement.from(27.548766188614707) + minDelta)
		assertTrue(27.548766188614707 < FixDisplacement.from(27.548766188614707) + minDelta)
		assertFalse(FixDisplacement.from(106.53674356901827) < FixDisplacement.from(106.53674356901827) - minDelta)
		assertFalse(106.53674356901827 < FixDisplacement.from(106.53674356901827) - minDelta)
		assertTrue(FixDisplacement.from(106.53674356901827) < FixDisplacement.from(106.53674356901827) + minDelta)
		assertTrue(106.53674356901827 < FixDisplacement.from(106.53674356901827) + minDelta)
		assertFalse(FixDisplacement.from(1502.6593122114884) < FixDisplacement.from(1502.6593122114884) - minDelta)
		assertFalse(1502.6593122114884 < FixDisplacement.from(1502.6593122114884) - minDelta)
		assertTrue(FixDisplacement.from(1502.6593122114884) < FixDisplacement.from(1502.6593122114884) + minDelta)
		assertTrue(1502.6593122114884 < FixDisplacement.from(1502.6593122114884) + minDelta)
		assertFalse(FixDisplacement.from(21474.83647) < FixDisplacement.from(21474.83647) - minDelta)
		assertFalse(21474.83647 < FixDisplacement.from(21474.83647) - minDelta)
		assertTrue(FixDisplacement.raw(Int.MAX_VALUE) >= 21474)
		assertTrue(FixDisplacement.raw(Int.MAX_VALUE) > 21474)
		assertTrue(FixDisplacement.raw(Int.MAX_VALUE) < 21475)
		assertTrue(FixDisplacement.raw(Int.MAX_VALUE) < 21496.475)
		assertTrue(FixDisplacement.raw(Int.MAX_VALUE) < Int.MAX_VALUE)
		assertTrue(FixDisplacement.raw(Int.MAX_VALUE) < Int.MAX_VALUE.toFloat())
		assertTrue(FixDisplacement.raw(Int.MAX_VALUE) < Int.MAX_VALUE.toDouble())
		assertTrue(FixDisplacement.raw(Int.MIN_VALUE) <= -21474)
		assertTrue(FixDisplacement.raw(Int.MIN_VALUE) < -21474)
		assertTrue(FixDisplacement.raw(Int.MIN_VALUE) > -21475)
		assertTrue(FixDisplacement.raw(Int.MIN_VALUE) > -21496.475)
		assertTrue(FixDisplacement.raw(Int.MIN_VALUE) > Int.MIN_VALUE)
		assertTrue(FixDisplacement.raw(Int.MIN_VALUE) > Int.MIN_VALUE.toFloat())
		assertTrue(FixDisplacement.raw(Int.MIN_VALUE) > Int.MIN_VALUE.toDouble())
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
		assertEquals(FixDisplacement.raw(Int.MAX_VALUE), abs(FixDisplacement.raw(Int.MAX_VALUE)))
		assertEquals(FixDisplacement.raw(Int.MAX_VALUE), abs(-FixDisplacement.raw(Int.MAX_VALUE)))
		assertThrows(FixedPointException::class.java) { abs(FixDisplacement.raw(Int.MIN_VALUE)) }
	}

	@Test
	fun testMinMax() {
		assertEquals(FixDisplacement.ZERO, min(FixDisplacement.ZERO, FixDisplacement.ZERO))
		assertEquals(FixDisplacement.ZERO, max(FixDisplacement.ZERO, FixDisplacement.ZERO))
		assertEquals(FixDisplacement.ZERO, min(FixDisplacement.ONE, FixDisplacement.ZERO))
		assertEquals(FixDisplacement.ONE, max(FixDisplacement.ONE, FixDisplacement.ZERO))
		assertEquals(FixDisplacement.ZERO, min(FixDisplacement.ZERO, FixDisplacement.ONE))
		assertEquals(FixDisplacement.ONE, max(FixDisplacement.ZERO, FixDisplacement.ONE))
		assertEquals(FixDisplacement.ZERO, min(FixDisplacement.ZERO, FixDisplacement.raw(Int.MAX_VALUE)))
		assertEquals(FixDisplacement.raw(Int.MAX_VALUE), max(FixDisplacement.ZERO, FixDisplacement.raw(Int.MAX_VALUE)))
		assertEquals(-FixDisplacement.ONE, min(-FixDisplacement.ONE, FixDisplacement.ZERO))
		assertEquals(FixDisplacement.ZERO, max(-FixDisplacement.ONE, FixDisplacement.ZERO))
		assertEquals(FixDisplacement.raw(Int.MIN_VALUE), min(FixDisplacement.ZERO, FixDisplacement.raw(Int.MIN_VALUE)))
		assertEquals(FixDisplacement.ZERO, max(FixDisplacement.ZERO, FixDisplacement.raw(Int.MIN_VALUE)))
	}
}
