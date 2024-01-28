package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixMicro32 {

	fun assertEquals(a: FixMicro32, b: FixMicro32, maxDelta: FixMicro32) {
		val rawDifference = a.raw - b.raw
		if (rawDifference.absoluteValue > maxDelta.raw) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixMicro32.ZERO.toString())
		assertEquals("1", FixMicro32.ONE.toString())
		assertTrue((FixMicro32.ONE / 3).toString().startsWith("0.33"))
		assertEquals("-1", (-FixMicro32.ONE).toString())
		assertTrue((FixMicro32.ONE / -3).toString().startsWith("-0.33"))
		assertTrue((FixMicro32.from(2046) + FixMicro32.ONE / 3).toString().endsWith((FixMicro32.ONE / 3).toString().substring(1)))
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixMicro32.ONE, FixMicro32.from(one))

		fun testValue(value: Int) = assertEquals(value, FixMicro32.from(value).toInt())
		fun testOverflow(value: Int) = assertThrows(FixedPointException::class.java) { FixMicro32.from(value) }
		testValue(-2048)
		testValue(-701)
		testValue(0)
		testValue(1)
		testValue(32)
		testValue(2047)

		testOverflow(-2147483648)
		testOverflow(-978689185)
		testOverflow(-974146)
		testOverflow(-740009)
		testOverflow(-16299)
		testOverflow(-2049)

		testOverflow(2048)
		testOverflow(2236990)
		testOverflow(33146467)
		testOverflow(2147483647)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixMicro32.ONE, FixMicro32.from(one))

		fun testValue(value: Long) = assertEquals(value, FixMicro32.from(value).toLong())
		fun testOverflow(value: Long) = assertThrows(FixedPointException::class.java) { FixMicro32.from(value) }
		testValue(-2048)
		testValue(-701)
		testValue(0)
		testValue(1)
		testValue(32)
		testValue(2047)

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
		testOverflow(-2049)

		testOverflow(2048)
		testOverflow(2236990)
		testOverflow(33146467)
		testOverflow(4438731068)
		testOverflow(206638292936)
		testOverflow(7488684195594)
		testOverflow(195164217948304)
		testOverflow(2909106438317318)
		testOverflow(598604186930692556)
		testOverflow(9223372036854775807)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixMicro32.ONE, FixMicro32.from(1f))
		val delta = 1.9073486E-6f
		assertEquals(9.536743E-7f, FixMicro32.from(9.536743E-7f).toFloat(), delta)
		assertEquals(-9.536743E-7f, FixMicro32.from(-9.536743E-7f).toFloat(), delta)
		assertEquals(5.7965175E-5f, FixMicro32.from(5.7965175E-5f).toFloat(), delta)
		assertEquals(-5.7965175E-5f, FixMicro32.from(-5.7965175E-5f).toFloat(), delta)
		assertEquals(0.0030845916f, FixMicro32.from(0.0030845916f).toFloat(), delta)
		assertEquals(-0.0030845916f, FixMicro32.from(-0.0030845916f).toFloat(), delta)
		assertEquals(0.009314704f, FixMicro32.from(0.009314704f).toFloat(), delta)
		assertEquals(-0.009314704f, FixMicro32.from(-0.009314704f).toFloat(), delta)
		assertEquals(0.5088087f, FixMicro32.from(0.5088087f).toFloat(), delta)
		assertEquals(-0.5088087f, FixMicro32.from(-0.5088087f).toFloat(), delta)
		assertEquals(14.094133f, FixMicro32.from(14.094133f).toFloat(), delta)
		assertEquals(-14.094133f, FixMicro32.from(-14.094133f).toFloat(), delta)
		assertEquals(55.274475f, FixMicro32.from(55.274475f).toFloat(), delta)
		assertEquals(-55.274475f, FixMicro32.from(-55.274475f).toFloat(), delta)
		assertEquals(2047.9999f, FixMicro32.from(2047.9999f).toFloat(), delta)
		assertEquals(-2047.9999f, FixMicro32.from(-2047.9999f).toFloat(), delta)

		assertThrows(FixedPointException::class.java) { FixMicro32.from(2049.0f) }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(-2049.0f) }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(207584.44f) }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(-207584.44f) }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(4194304.0f) }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(-4194304.0f) }
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixMicro32.ONE, FixMicro32.from(1.0))
		val delta = 1.9073486328125E-6
		assertEquals(9.5367431640625E-7, FixMicro32.from(9.5367431640625E-7).toDouble(), delta)
		assertEquals(-9.5367431640625E-7, FixMicro32.from(-9.5367431640625E-7).toDouble(), delta)
		assertEquals(5.7965173783582514E-5, FixMicro32.from(5.7965173783582514E-5).toDouble(), delta)
		assertEquals(-5.7965173783582514E-5, FixMicro32.from(-5.7965173783582514E-5).toDouble(), delta)
		assertEquals(0.003084591527158651, FixMicro32.from(0.003084591527158651).toDouble(), delta)
		assertEquals(-0.003084591527158651, FixMicro32.from(-0.003084591527158651).toDouble(), delta)
		assertEquals(0.009314703658805575, FixMicro32.from(0.009314703658805575).toDouble(), delta)
		assertEquals(-0.009314703658805575, FixMicro32.from(-0.009314703658805575).toDouble(), delta)
		assertEquals(0.5088086753629321, FixMicro32.from(0.5088086753629321).toDouble(), delta)
		assertEquals(-0.5088086753629321, FixMicro32.from(-0.5088086753629321).toDouble(), delta)
		assertEquals(14.094133688218953, FixMicro32.from(14.094133688218953).toDouble(), delta)
		assertEquals(-14.094133688218953, FixMicro32.from(-14.094133688218953).toDouble(), delta)
		assertEquals(55.274475303202365, FixMicro32.from(55.274475303202365).toDouble(), delta)
		assertEquals(-55.274475303202365, FixMicro32.from(-55.274475303202365).toDouble(), delta)
		assertEquals(2047.9999990463257, FixMicro32.from(2047.9999990463257).toDouble(), delta)
		assertEquals(-2047.9999990463257, FixMicro32.from(-2047.9999990463257).toDouble(), delta)

		assertThrows(FixedPointException::class.java) { FixMicro32.from(2048.9999990463257) }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(-2048.9999990463257) }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(207584.4441152018) }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(-207584.4441152018) }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(4194303.99609375) }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(-4194303.99609375) }
	}

	@Test
	fun testUnaryMinus() {
		assertThrows(FixedPointException::class.java) { -FixMicro32.raw(Int.MIN_VALUE) }
		assertEquals(2147483647, -FixMicro32.raw(-2147483647).raw)
		assertEquals(352975531, -FixMicro32.raw(-352975531).raw)
		assertEquals(30294627, -FixMicro32.raw(-30294627).raw)
		assertEquals(1475174, -FixMicro32.raw(-1475174).raw)
		assertEquals(25682, -FixMicro32.raw(-25682).raw)
		assertEquals(386, -FixMicro32.raw(-386).raw)
		assertEquals(0, -FixMicro32.raw(0).raw)
		assertEquals(-1, -FixMicro32.raw(1).raw)
		assertEquals(-16270, -FixMicro32.raw(16270).raw)
		assertEquals(-17228, -FixMicro32.raw(17228).raw)
		assertEquals(-778569, -FixMicro32.raw(778569).raw)
		assertEquals(-7590381, -FixMicro32.raw(7590381).raw)
		assertEquals(-793898393, -FixMicro32.raw(793898393).raw)
		assertEquals(-2147483647, -FixMicro32.raw(2147483647).raw)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixMicro32, b: FixMicro32, c: FixMicro32) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}

		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixMicro32.from(a), FixMicro32.from(b), FixMicro32.from(c))
			assertEquals(FixMicro32.from(c), FixMicro32.from(a) + b)
			assertEquals(FixMicro32.from(c), b + FixMicro32.from(a))
			assertEquals(FixMicro32.from(a), FixMicro32.from(c) - b)
			assertEquals(FixMicro32.from(b), c - FixMicro32.from(a))
		}

		testValues(FixMicro32.raw(Int.MIN_VALUE), FixMicro32.ONE, FixMicro32.raw(Int.MIN_VALUE + 1048576))
		testValues(-2048, 422, -1626)
		testValues(-1351, 854, -497)
		testValues(-1138, 143, -995)
		testValues(-97, 1550, 1453)
		testValues(-47, 721, 674)
		testValues(0, 738, 738)
		testValues(1, 1616, 1617)
		testValues(366, 1193, 1559)
		testValues(407, 72, 479)
		testValues(2047, 0, 2047)
		testValues(FixMicro32.raw(Int.MAX_VALUE - 1048576), FixMicro32.ONE, FixMicro32.raw(Int.MAX_VALUE))

		fun testOverflowPlus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) + FixMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro32.from(b) }
		}

		fun testOverflowMinus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) - FixMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro32.from(b) }
		}

		fun testOverflowPlus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) + FixMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro32.from(b) }
		}

		fun testOverflowMinus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) - FixMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro32.from(b) }
		}

		fun testOverflowPlus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) + FixMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro32.from(b) }
		}

		fun testOverflowMinus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) - FixMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro32.from(b) }
		}

		fun testOverflowPlus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) + FixMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro32.from(b) }
		}

		fun testOverflowMinus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) - FixMicro32.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro32.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro32.from(b) }
		}

		testOverflowPlus(-2048, 4096)
		testOverflowPlus(-2048L, 4096L)
		testOverflowPlus(-2048.0f, 4096.0f)
		testOverflowPlus(-2048.0, 4096.0)
		testOverflowPlus(-2048, 16785409)
		testOverflowPlus(-2048L, 16785409L)
		testOverflowPlus(-2048.0, 1.6785409E7)
		testOverflowMinus(-2048, 1)
		testOverflowMinus(-2048L, 1L)
		testOverflowMinus(-2048.0, 1.0)
		testOverflowMinus(-2048, 4)
		testOverflowMinus(-2048L, 4L)
		testOverflowMinus(-2048.0, 4.0)
		testOverflowPlus(-665, 2713)
		testOverflowPlus(-665L, 2713L)
		testOverflowPlus(-665.0f, 2713.0f)
		testOverflowPlus(-665.0, 2713.0)
		testOverflowPlus(-665, 7365796)
		testOverflowPlus(-665L, 7365796L)
		testOverflowMinus(-665, 1384)
		testOverflowMinus(-665L, 1384L)
		testOverflowMinus(-665.0f, 1384.0f)
		testOverflowMinus(-665.0, 1384.0)
		testOverflowMinus(-665, 1918225)
		testOverflowMinus(-665L, 1918225L)
		testOverflowMinus(-665.0, 1918225.0)
		testOverflowPlus(0, 2048)
		testOverflowPlus(0L, 2048L)
		testOverflowPlus(0, 4198401)
		testOverflowPlus(0L, 4198401L)
		testOverflowMinus(0, 2049)
		testOverflowMinus(0L, 2049L)
		testOverflowMinus(0, 4202500)
		testOverflowMinus(0L, 4202500L)
		testOverflowPlus(1, 2047)
		testOverflowPlus(1L, 2047L)
		testOverflowPlus(1.0, 2047.0)
		testOverflowPlus(1, 4194304)
		testOverflowPlus(1L, 4194304L)
		testOverflowMinus(1, 2050)
		testOverflowMinus(1L, 2050L)
		testOverflowMinus(1.0, 2050.0)
		testOverflowMinus(1, 4206601)
		testOverflowMinus(1L, 4206601L)
		testOverflowPlus(2047, 1)
		testOverflowPlus(2047L, 1L)
		testOverflowPlus(2047.0, 1.0)
		testOverflowPlus(2047, 4)
		testOverflowPlus(2047L, 4L)
		testOverflowPlus(2047.0, 4.0)
		testOverflowMinus(2047, 4096)
		testOverflowMinus(2047L, 4096L)
		testOverflowMinus(2047.0f, 4096.0f)
		testOverflowMinus(2047.0, 4096.0)
		testOverflowMinus(2047, 16785409)
		testOverflowMinus(2047L, 16785409L)
		testOverflowMinus(2047.0, 1.6785409E7)
		assertEquals(FixMicro32.raw(2146371848), FixMicro32.raw(-63224) + 2047)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixMicro32.raw(Int.MAX_VALUE), 1 * FixMicro32.raw(Int.MAX_VALUE))
		assertEquals(FixMicro32.raw(Int.MAX_VALUE), FixMicro32.raw(Int.MAX_VALUE) / 1)
		assertEquals(FixMicro32.raw(Int.MIN_VALUE), 1 * FixMicro32.raw(Int.MIN_VALUE))
		assertEquals(FixMicro32.raw(Int.MIN_VALUE), FixMicro32.raw(Int.MIN_VALUE) / 1)
		assertEquals(FixMicro32.raw(Int.MIN_VALUE + 1), -1 * FixMicro32.raw(Int.MAX_VALUE))
		assertEquals(FixMicro32.raw(Int.MIN_VALUE + 1), FixMicro32.raw(Int.MAX_VALUE) / -1)
		assertThrows(FixedPointException::class.java) { -1 * FixMicro32.raw(Int.MIN_VALUE) }
		assertThrows(FixedPointException::class.java) { FixMicro32.raw(Int.MIN_VALUE) / -1 }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixMicro32.from(a * b), FixMicro32.from(a) * FixMicro32.from(b))
			assertEquals(FixMicro32.from(a * b), FixMicro32.from(a) * b)
			assertEquals(FixMicro32.from(a * b), b * FixMicro32.from(a))
			if (b != 0L) assertEquals(FixMicro32.from(a), FixMicro32.from(a * b) / b)
			if (a != 0L) assertEquals(FixMicro32.from(b), FixMicro32.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixMicro32.from(b), FixMicro32.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixMicro32.from(a * b), FixMicro32.from(a) * b.toInt())
				assertEquals(FixMicro32.from(a * b), b.toInt() * FixMicro32.from(a))
			}
		}
		testValues(-2048, 0)
		assertThrows(FixedPointException::class.java) { FixMicro32.from(-1212) * 70 }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(-656) * -37 }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(-195) * -1212 }
		testValues(-49, 1)
		testValues(-37, -37)
		assertThrows(FixedPointException::class.java) { FixMicro32.from(-35) * 2047 }
		testValues(0, -2048)
		testValues(1, -49)
		assertThrows(FixedPointException::class.java) { FixMicro32.from(51) * -656 }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(70) * 70 }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(158) * -37 }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(239) * 158 }
		assertThrows(FixedPointException::class.java) { FixMicro32.from(2047) * -37 }
		assertEquals(FixMicro32.raw(-865232), (FixMicro32.raw(-865232) * FixMicro32.raw(953370)) / FixMicro32.raw(953370), FixMicro32.raw(10485))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixMicro32.ZERO < FixMicro32.ONE)
		assertTrue(0 < FixMicro32.ONE)
		assertFalse(FixMicro32.ZERO > FixMicro32.ONE)
		assertFalse(0 > FixMicro32.ONE)
		assertFalse(FixMicro32.ONE < FixMicro32.ONE)
		assertFalse(FixMicro32.ONE < 1)
		assertFalse(FixMicro32.ONE > FixMicro32.ONE)
		assertTrue(FixMicro32.ONE <= FixMicro32.ONE)
		assertTrue(FixMicro32.ONE >= FixMicro32.ONE)
		assertTrue(FixMicro32.raw(Int.MIN_VALUE) < FixMicro32.raw(Int.MAX_VALUE))

		val minDelta = FixMicro32.raw(1)
		assertEquals(FixMicro32.from(12), FixMicro32.from(12))
		assertNotEquals(FixMicro32.from(12), FixMicro32.from(12) - minDelta)
		assertTrue(FixMicro32.from(9.5367431640625E-7) < FixMicro32.from(9.5367431640625E-7) + minDelta)
		assertTrue(9.5367431640625E-7 < FixMicro32.from(9.5367431640625E-7) + minDelta)
		assertFalse(FixMicro32.from(3.911252279924859E-4) < FixMicro32.from(3.911252279924859E-4) - minDelta)
		assertFalse(3.911252279924859E-4 < FixMicro32.from(3.911252279924859E-4) - minDelta)
		assertTrue(FixMicro32.from(3.911252279924859E-4) < FixMicro32.from(3.911252279924859E-4) + minDelta)
		assertTrue(3.911252279924859E-4 < FixMicro32.from(3.911252279924859E-4) + minDelta)
		assertFalse(FixMicro32.from(0.0013926195514347168) < FixMicro32.from(0.0013926195514347168) - minDelta)
		assertFalse(0.0013926195514347168 < FixMicro32.from(0.0013926195514347168) - minDelta)
		assertTrue(FixMicro32.from(0.0013926195514347168) < FixMicro32.from(0.0013926195514347168) + minDelta)
		assertTrue(0.0013926195514347168 < FixMicro32.from(0.0013926195514347168) + minDelta)
		assertFalse(FixMicro32.from(0.007928885234495038) < FixMicro32.from(0.007928885234495038) - minDelta)
		assertFalse(0.007928885234495038 < FixMicro32.from(0.007928885234495038) - minDelta)
		assertTrue(FixMicro32.from(0.007928885234495038) < FixMicro32.from(0.007928885234495038) + minDelta)
		assertTrue(0.007928885234495038 < FixMicro32.from(0.007928885234495038) + minDelta)
		assertFalse(FixMicro32.from(0.060214617813443456) < FixMicro32.from(0.060214617813443456) - minDelta)
		assertFalse(0.060214617813443456 < FixMicro32.from(0.060214617813443456) - minDelta)
		assertTrue(FixMicro32.from(0.060214617813443456) < FixMicro32.from(0.060214617813443456) + minDelta)
		assertTrue(0.060214617813443456 < FixMicro32.from(0.060214617813443456) + minDelta)
		assertFalse(FixMicro32.from(0.18061000195143148) < FixMicro32.from(0.18061000195143148) - minDelta)
		assertFalse(0.18061000195143148 < FixMicro32.from(0.18061000195143148) - minDelta)
		assertTrue(FixMicro32.from(0.18061000195143148) < FixMicro32.from(0.18061000195143148) + minDelta)
		assertTrue(0.18061000195143148 < FixMicro32.from(0.18061000195143148) + minDelta)
		assertFalse(FixMicro32.from(2.6272550762762745) < FixMicro32.from(2.6272550762762745) - minDelta)
		assertFalse(2.6272550762762745 < FixMicro32.from(2.6272550762762745) - minDelta)
		assertTrue(FixMicro32.from(2.6272550762762745) < FixMicro32.from(2.6272550762762745) + minDelta)
		assertTrue(2.6272550762762745 < FixMicro32.from(2.6272550762762745) + minDelta)
		assertFalse(FixMicro32.from(10.160135609533143) < FixMicro32.from(10.160135609533143) - minDelta)
		assertFalse(10.160135609533143 < FixMicro32.from(10.160135609533143) - minDelta)
		assertTrue(FixMicro32.from(10.160135609533143) < FixMicro32.from(10.160135609533143) + minDelta)
		assertTrue(10.160135609533143 < FixMicro32.from(10.160135609533143) + minDelta)
		assertFalse(FixMicro32.from(143.3047592364777) < FixMicro32.from(143.3047592364777) - minDelta)
		assertFalse(143.3047592364777 < FixMicro32.from(143.3047592364777) - minDelta)
		assertTrue(FixMicro32.from(143.3047592364777) < FixMicro32.from(143.3047592364777) + minDelta)
		assertTrue(143.3047592364777 < FixMicro32.from(143.3047592364777) + minDelta)
		assertFalse(FixMicro32.from(2047.9999990463257) < FixMicro32.from(2047.9999990463257) - minDelta)
		assertFalse(2047.9999990463257 < FixMicro32.from(2047.9999990463257) - minDelta)
		assertTrue(FixMicro32.raw(Int.MAX_VALUE) >= 2047)
		assertTrue(FixMicro32.raw(Int.MAX_VALUE) > 2047)
		assertTrue(FixMicro32.raw(Int.MAX_VALUE) < 2048)
		assertTrue(FixMicro32.raw(Int.MAX_VALUE) < 2050.048)
		assertTrue(FixMicro32.raw(Int.MAX_VALUE) < Int.MAX_VALUE)
		assertTrue(FixMicro32.raw(Int.MAX_VALUE) < Int.MAX_VALUE.toFloat())
		assertTrue(FixMicro32.raw(Int.MAX_VALUE) < Int.MAX_VALUE.toDouble())
		assertTrue(FixMicro32.raw(Int.MIN_VALUE) <= -2048)
		assertTrue(FixMicro32.raw(Int.MIN_VALUE) > -2049)
		assertTrue(FixMicro32.raw(Int.MIN_VALUE) > -2051.049)
		assertTrue(FixMicro32.raw(Int.MIN_VALUE) > Int.MIN_VALUE)
		assertTrue(FixMicro32.raw(Int.MIN_VALUE) > Int.MIN_VALUE.toFloat())
		assertTrue(FixMicro32.raw(Int.MIN_VALUE) > Int.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixMicro32.Array(2) { FixMicro32.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixMicro32.ONE, testArray[0])
		assertEquals(FixMicro32.ONE, testArray[1])
		testArray[1] = FixMicro32.ZERO
		assertEquals(FixMicro32.ONE, testArray[0])
		assertEquals(FixMicro32.ZERO, testArray[1])
		testArray.fill(FixMicro32.ZERO)
		assertEquals(FixMicro32.ZERO, testArray[0])
		assertEquals(FixMicro32.ZERO, testArray[1])
	}
}
