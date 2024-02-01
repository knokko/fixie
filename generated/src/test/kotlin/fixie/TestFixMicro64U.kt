package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixMicro64U {

	fun assertEquals(a: FixMicro64U, b: FixMicro64U, maxDelta: FixMicro64U) {
		val rawDifference = a.raw.toLong() - b.raw.toLong()
		if (rawDifference.absoluteValue > maxDelta.raw.toLong()) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixMicro64U.ZERO.toString())
		assertEquals("1", FixMicro64U.ONE.toString())
		assertTrue((FixMicro64U.ONE / 3).toString().startsWith("0.33"))
		assertTrue((FixMicro64U.from(17592186044414) + FixMicro64U.ONE / 3).toString().endsWith((FixMicro64U.ONE / 3).toString().substring(1)))
		assertEquals("0.0625", (FixMicro64U.ONE / 16).toString())
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixMicro64U.ONE, FixMicro64U.from(one))

		fun testValue(value: Int) = assertEquals(value, FixMicro64U.from(value).toInt())
		fun testOverflow(value: Int) = assertThrows(FixedPointException::class.java) { FixMicro64U.from(value) }
		testValue(0)
		testValue(1)
		testValue(1042)
		testValue(2299)
		testValue(27518)
		testValue(134221)
		testValue(9763729)
		testValue(55681182)
		testValue(2147483647)

		testOverflow(-2147483648)
		testOverflow(-978689185)
		testOverflow(-974146)
		testOverflow(-740009)
		testOverflow(-16299)
		testOverflow(-1)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixMicro64U.ONE, FixMicro64U.from(one))

		fun testValue(value: Long) = assertEquals(value, FixMicro64U.from(value).toLong())
		fun testOverflow(value: Long) = assertThrows(FixedPointException::class.java) { FixMicro64U.from(value) }
		testValue(0)
		testValue(1)
		testValue(1042)
		testValue(2299)
		testValue(27518)
		testValue(134221)
		testValue(9763729)
		testValue(55681182)
		testValue(1275859582)
		testValue(6792104696)
		testValue(274799541850)
		testValue(2548601958618)
		testValue(17592186044415)

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
		testOverflow(-1)

		testOverflow(17592186044416)
		testOverflow(21145859950714074)
		testOverflow(960891641846565667)
		testOverflow(9223372036854775807)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixMicro64U.ONE, FixMicro64U.from(1f))
		val delta = 1.9073486E-6f
		assertEquals(9.536743E-7f, FixMicro64U.from(9.536743E-7f).toFloat(), delta)
		assertEquals(5.7965175E-5f, FixMicro64U.from(5.7965175E-5f).toFloat(), delta)
		assertEquals(0.0030845916f, FixMicro64U.from(0.0030845916f).toFloat(), delta)
		assertEquals(0.009314704f, FixMicro64U.from(0.009314704f).toFloat(), delta)
		assertEquals(0.5088087f, FixMicro64U.from(0.5088087f).toFloat(), delta)
		assertEquals(14.094133f, FixMicro64U.from(14.094133f).toFloat(), delta)
		assertEquals(55.274475f, FixMicro64U.from(55.274475f).toFloat(), delta)
		assertEquals(488.1811f, FixMicro64U.from(488.1811f).toFloat(), delta)
		assertEquals(19484.523f, FixMicro64U.from(19484.523f).toFloat(), delta)
		assertEquals(978553.25f, FixMicro64U.from(978553.25f).toFloat(), delta)
		assertEquals(1.8665852E7f, FixMicro64U.from(1.8665852E7f).toFloat(), delta)
		assertEquals(2.77667648E8f, FixMicro64U.from(2.77667648E8f).toFloat(), delta)
		assertEquals(4.4010537E9f, FixMicro64U.from(4.4010537E9f).toFloat(), delta)
		assertEquals(7.1891963E9f, FixMicro64U.from(7.1891963E9f).toFloat(), delta)
		assertEquals(3.67286419E11f, FixMicro64U.from(3.67286419E11f).toFloat(), delta)
		assertEquals(1.7592186E13f, FixMicro64U.from(1.7592186E13f).toFloat(), delta)

		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1.75939456E13f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-1.75939456E13f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1.78244471E15f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-1.78244471E15f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1.47284204E17f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-1.47284204E17f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(7.965114E18f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-7.965114E18f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1.6861364E20f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-1.6861364E20f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(9.4039506E21f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-9.4039506E21f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(4.3492374E22f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-4.3492374E22f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(2.703149E23f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-2.703149E23f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(3.0948501E26f) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-3.0948501E26f) }
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixMicro64U.ONE, FixMicro64U.from(1.0))
		val delta = 1.9073486328125E-6
		assertEquals(9.5367431640625E-7, FixMicro64U.from(9.5367431640625E-7).toDouble(), delta)
		assertEquals(5.7965173783582514E-5, FixMicro64U.from(5.7965173783582514E-5).toDouble(), delta)
		assertEquals(0.003084591527158651, FixMicro64U.from(0.003084591527158651).toDouble(), delta)
		assertEquals(0.009314703658805575, FixMicro64U.from(0.009314703658805575).toDouble(), delta)
		assertEquals(0.5088086753629321, FixMicro64U.from(0.5088086753629321).toDouble(), delta)
		assertEquals(14.094133688218953, FixMicro64U.from(14.094133688218953).toDouble(), delta)
		assertEquals(55.274475303202365, FixMicro64U.from(55.274475303202365).toDouble(), delta)
		assertEquals(488.18110381699654, FixMicro64U.from(488.18110381699654).toDouble(), delta)
		assertEquals(19484.523405800377, FixMicro64U.from(19484.523405800377).toDouble(), delta)
		assertEquals(978553.2634048663, FixMicro64U.from(978553.2634048663).toDouble(), delta)
		assertEquals(1.8665851216571603E7, FixMicro64U.from(1.8665851216571603E7).toDouble(), delta)
		assertEquals(2.776676497250265E8, FixMicro64U.from(2.776676497250265E8).toDouble(), delta)
		assertEquals(4.401053941594629E9, FixMicro64U.from(4.401053941594629E9).toDouble(), delta)
		assertEquals(7.18919617871774E9, FixMicro64U.from(7.18919617871774E9).toDouble(), delta)
		assertEquals(3.672864068723384E11, FixMicro64U.from(3.672864068723384E11).toDouble(), delta)
		assertEquals(1.7592186044416E13, FixMicro64U.from(1.7592186044416E13).toDouble(), delta)

		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1.759394526302044E13) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-1.759394526302044E13) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1.7824447774120342E15) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-1.7824447774120342E15) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1.47284211297711648E17) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-1.47284211297711648E17) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(7.9651141257471631E18) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-7.9651141257471631E18) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1.6861364574277802E20) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-1.6861364574277802E20) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(9.403950409145377E21) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-9.403950409145377E21) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(4.349237425815448E22) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-4.349237425815448E22) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(2.703149008278399E23) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-2.703149008278399E23) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(3.0948500982134507E26) }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(-3.0948500982134507E26) }
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixMicro64U, b: FixMicro64U, c: FixMicro64U) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}

		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixMicro64U.from(a), FixMicro64U.from(b), FixMicro64U.from(c))
			assertEquals(FixMicro64U.from(c), FixMicro64U.from(a) + b)
			assertEquals(FixMicro64U.from(c), b + FixMicro64U.from(a))
			assertEquals(FixMicro64U.from(a), FixMicro64U.from(c) - b)
			assertEquals(FixMicro64U.from(b), c - FixMicro64U.from(a))
		}

		testValues(FixMicro64U.raw(ULong.MIN_VALUE), FixMicro64U.ONE, FixMicro64U.raw(ULong.MIN_VALUE + 1048576u))
		testValues(0, 4250525057011, 4250525057011)
		testValues(1, 10368412007889, 10368412007890)
		testValues(3567, 7413514302721, 7413514306288)
		testValues(3788, 3227693447452, 3227693451240)
		testValues(9103, 2897431613820, 2897431622923)
		testValues(9886, 6517855509209, 6517855519095)
		testValues(53441, 7019025497658, 7019025551099)
		testValues(469344, 5855368344539, 5855368813883)
		testValues(5279943, 153142072892, 153147352835)
		testValues(10532273, 5883999019372, 5884009551645)
		testValues(98281323, 9851092484661, 9851190765984)
		testValues(143170306, 489387658565, 489530828871)
		testValues(2251333916, 1513687685477, 1515939019393)
		testValues(3899907567, 1004726788763, 1008626696330)
		testValues(126403199838, 6263954489360, 6390357689198)
		testValues(143594785468, 7533489241863, 7677084027331)
		testValues(2185542315817, 12982086833824, 15167629149641)
		testValues(3480974189933, 5846705752447, 9327679942380)
		testValues(17592186044415, 0, 17592186044415)
		testValues(FixMicro64U.raw(ULong.MAX_VALUE - 1048576u), FixMicro64U.ONE, FixMicro64U.raw(ULong.MAX_VALUE))

		fun testOverflowPlus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) + FixMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro64U.from(b) }
		}

		fun testOverflowMinus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) - FixMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro64U.from(b) }
		}

		fun testOverflowPlus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) + FixMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro64U.from(b) }
		}

		fun testOverflowMinus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) - FixMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro64U.from(b) }
		}

		fun testOverflowPlus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) + FixMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro64U.from(b) }
		}

		fun testOverflowMinus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) - FixMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro64U.from(b) }
		}

		fun testOverflowPlus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) + FixMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro64U.from(b) }
		}

		fun testOverflowMinus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) - FixMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro64U.from(b) }
		}

		testOverflowPlus(0L, 17592186044416L)
		testOverflowPlus(0L, 35184372088833L)
		testOverflowMinus(0, 1)
		testOverflowMinus(0L, 1L)
		testOverflowMinus(0, 4)
		testOverflowMinus(0L, 4L)
		testOverflowPlus(1L, 17592186044415L)
		testOverflowMinus(1, 2)
		testOverflowMinus(1L, 2L)
		testOverflowMinus(1.0f, 2.0f)
		testOverflowMinus(1.0, 2.0)
		testOverflowMinus(1, 9)
		testOverflowMinus(1L, 9L)
		testOverflowMinus(1.0f, 9.0f)
		testOverflowMinus(1.0, 9.0)
		testOverflowPlus(171418L, 17592185872998L)
		testOverflowMinus(171418, 171419)
		testOverflowMinus(171418L, 171419L)
		testOverflowMinus(171418.0, 171419.0)
		testOverflowMinus(171418L, 29384816400L)
		testOverflowPlus(358643L, 17592185685773L)
		testOverflowPlus(358643L, 5828150627650749636L)
		testOverflowMinus(358643, 358644)
		testOverflowMinus(358643L, 358644L)
		testOverflowMinus(358643.0, 358644.0)
		testOverflowMinus(358643L, 128626236025L)
		testOverflowPlus(18806101L, 17592167238315L)
		testOverflowPlus(18806101L, 2402320383157592976L)
		testOverflowMinus(18806101, 18806102)
		testOverflowMinus(18806101L, 18806102L)
		testOverflowMinus(1.8806101E7, 1.8806102E7)
		testOverflowMinus(18806101L, 353669510046609L)
		testOverflowPlus(511305813L, 17591674738603L)
		testOverflowMinus(511305813, 511305814)
		testOverflowMinus(511305813L, 511305814L)
		testOverflowMinus(5.11305813E8, 5.11305814E8)
		testOverflowMinus(511305813L, 261433636452814225L)
		testOverflowPlus(114750512288L, 17477435532128L)
		testOverflowPlus(1.14750512288E11, 1.7477435532128E13)
		testOverflowMinus(114750512288L, 114750512289L)
		testOverflowMinus(1.14750512288E11, 1.14750512289E11)
		testOverflowPlus(17592186044415L, 1L)
		testOverflowPlus(17592186044415L, 4L)
		testOverflowMinus(17592186044415L, 17592186044416L)
		testOverflowMinus(1.7592186044415E13, 1.7592186044416E13)
		testOverflowMinus(17592186044415L, 35184372088833L)
		testOverflowMinus(1.7592186E13f, 3.5184372E13f)
		testOverflowMinus(1.7592186044415E13, 3.5184372088833E13)
		assertEquals(FixMicro64U.raw(18446744073708100036u), FixMicro64U.raw(645572u) + 17592186044414)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixMicro64U.raw(ULong.MAX_VALUE), 1 * FixMicro64U.raw(ULong.MAX_VALUE))
		assertEquals(FixMicro64U.raw(ULong.MAX_VALUE), FixMicro64U.raw(ULong.MAX_VALUE) / 1)
		assertThrows(FixedPointException::class.java) { -1 * FixMicro64U.ONE }
		assertThrows(FixedPointException::class.java) { FixMicro64U.ONE / -1 }
		assertThrows(FixedPointException::class.java) { -1 * FixMicro64U.raw(ULong.MAX_VALUE)}
		assertThrows(FixedPointException::class.java) { FixMicro64U.raw(ULong.MAX_VALUE) / -1 }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixMicro64U.from(a * b), FixMicro64U.from(a) * FixMicro64U.from(b))
			assertEquals(FixMicro64U.from(a * b), FixMicro64U.from(a) * b)
			assertEquals(FixMicro64U.from(a * b), b * FixMicro64U.from(a))
			if (b != 0L) assertEquals(FixMicro64U.from(a), FixMicro64U.from(a * b) / b)
			if (a != 0L) assertEquals(FixMicro64U.from(b), FixMicro64U.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixMicro64U.from(b), FixMicro64U.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixMicro64U.from(a * b), FixMicro64U.from(a) * b.toInt())
				assertEquals(FixMicro64U.from(a * b), b.toInt() * FixMicro64U.from(a))
			}
		}
		testValues(0, 71711606273)
		testValues(1, 231)
		testValues(42, 0)
		testValues(134, 21231)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(165) * 1222091827510 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(231) * 17592186044415 }
		testValues(1493, 1474163511)
		testValues(1881, 13620980)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(4505) * 12211872545 }
		testValues(11152, 329768868)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(11484) * 12211872545 }
		testValues(21231, 1970917)
		testValues(252026, 252026)
		testValues(301591, 134)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1389655) * 1222091827510 }
		testValues(1970917, 165)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(6075644) * 329768868 }
		testValues(13371719, 4505)
		testValues(13620980, 1493)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(32234314) * 44002482883 }
		testValues(39192496, 1881)
		testValues(329768868, 11152)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1474163511) * 301591 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(2854741395) * 1389655 }
		testValues(5116806831, 231)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(12211872545) * 5116806831 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(44002482883) * 2644929701422 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(71711606273) * 1881 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(364448964103) * 2854741395 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(500434653899) * 1970917 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1222091827510) * 252026 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(2644929701422) * 4505 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(17592186044415) * 364448964103 }
		assertEquals(FixMicro64U.raw(796336u), (FixMicro64U.raw(796336u) * FixMicro64U.raw(691445u)) / FixMicro64U.raw(691445u), FixMicro64U.raw(10485u))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixMicro64U.ZERO < FixMicro64U.ONE)
		assertTrue(0 < FixMicro64U.ONE)
		assertFalse(FixMicro64U.ZERO > FixMicro64U.ONE)
		assertFalse(0 > FixMicro64U.ONE)
		assertFalse(FixMicro64U.ONE < FixMicro64U.ONE)
		assertFalse(FixMicro64U.ONE < 1)
		assertFalse(FixMicro64U.ONE > FixMicro64U.ONE)
		assertTrue(FixMicro64U.ONE <= FixMicro64U.ONE)
		assertTrue(FixMicro64U.ONE >= FixMicro64U.ONE)
		assertTrue(FixMicro64U.raw(ULong.MIN_VALUE) < FixMicro64U.raw(ULong.MAX_VALUE))

		val minDelta = FixMicro64U.raw(1u)
		assertEquals(FixMicro64U.from(12), FixMicro64U.from(12))
		assertNotEquals(FixMicro64U.from(12), FixMicro64U.from(12) - minDelta)
		assertTrue(FixMicro64U.from(9.5367431640625E-7) < FixMicro64U.from(9.5367431640625E-7) + minDelta)
		assertTrue(9.5367431640625E-7 < FixMicro64U.from(9.5367431640625E-7) + minDelta)
		assertFalse(FixMicro64U.from(3.911252279924859E-4) < FixMicro64U.from(3.911252279924859E-4) - minDelta)
		assertFalse(3.911252279924859E-4 < FixMicro64U.from(3.911252279924859E-4) - minDelta)
		assertTrue(FixMicro64U.from(3.911252279924859E-4) < FixMicro64U.from(3.911252279924859E-4) + minDelta)
		assertTrue(3.911252279924859E-4 < FixMicro64U.from(3.911252279924859E-4) + minDelta)
		assertFalse(FixMicro64U.from(0.0013926195514347168) < FixMicro64U.from(0.0013926195514347168) - minDelta)
		assertFalse(0.0013926195514347168 < FixMicro64U.from(0.0013926195514347168) - minDelta)
		assertTrue(FixMicro64U.from(0.0013926195514347168) < FixMicro64U.from(0.0013926195514347168) + minDelta)
		assertTrue(0.0013926195514347168 < FixMicro64U.from(0.0013926195514347168) + minDelta)
		assertFalse(FixMicro64U.from(0.007928885234495038) < FixMicro64U.from(0.007928885234495038) - minDelta)
		assertFalse(0.007928885234495038 < FixMicro64U.from(0.007928885234495038) - minDelta)
		assertTrue(FixMicro64U.from(0.007928885234495038) < FixMicro64U.from(0.007928885234495038) + minDelta)
		assertTrue(0.007928885234495038 < FixMicro64U.from(0.007928885234495038) + minDelta)
		assertFalse(FixMicro64U.from(0.060214617813443456) < FixMicro64U.from(0.060214617813443456) - minDelta)
		assertFalse(0.060214617813443456 < FixMicro64U.from(0.060214617813443456) - minDelta)
		assertTrue(FixMicro64U.from(0.060214617813443456) < FixMicro64U.from(0.060214617813443456) + minDelta)
		assertTrue(0.060214617813443456 < FixMicro64U.from(0.060214617813443456) + minDelta)
		assertFalse(FixMicro64U.from(0.18061000195143148) < FixMicro64U.from(0.18061000195143148) - minDelta)
		assertFalse(0.18061000195143148 < FixMicro64U.from(0.18061000195143148) - minDelta)
		assertTrue(FixMicro64U.from(0.18061000195143148) < FixMicro64U.from(0.18061000195143148) + minDelta)
		assertTrue(0.18061000195143148 < FixMicro64U.from(0.18061000195143148) + minDelta)
		assertFalse(FixMicro64U.from(2.6272550762762745) < FixMicro64U.from(2.6272550762762745) - minDelta)
		assertFalse(2.6272550762762745 < FixMicro64U.from(2.6272550762762745) - minDelta)
		assertTrue(FixMicro64U.from(2.6272550762762745) < FixMicro64U.from(2.6272550762762745) + minDelta)
		assertTrue(2.6272550762762745 < FixMicro64U.from(2.6272550762762745) + minDelta)
		assertFalse(FixMicro64U.from(10.160135609533143) < FixMicro64U.from(10.160135609533143) - minDelta)
		assertFalse(10.160135609533143 < FixMicro64U.from(10.160135609533143) - minDelta)
		assertTrue(FixMicro64U.from(10.160135609533143) < FixMicro64U.from(10.160135609533143) + minDelta)
		assertTrue(10.160135609533143 < FixMicro64U.from(10.160135609533143) + minDelta)
		assertFalse(FixMicro64U.from(143.3047592364777) < FixMicro64U.from(143.3047592364777) - minDelta)
		assertFalse(143.3047592364777 < FixMicro64U.from(143.3047592364777) - minDelta)
		assertTrue(FixMicro64U.from(143.3047592364777) < FixMicro64U.from(143.3047592364777) + minDelta)
		assertTrue(143.3047592364777 < FixMicro64U.from(143.3047592364777) + minDelta)
		assertFalse(FixMicro64U.from(3374.7500266128254) < FixMicro64U.from(3374.7500266128254) - minDelta)
		assertFalse(3374.7500266128254 < FixMicro64U.from(3374.7500266128254) - minDelta)
		assertTrue(FixMicro64U.from(3374.7500266128254) < FixMicro64U.from(3374.7500266128254) + minDelta)
		assertTrue(3374.7500266128254 < FixMicro64U.from(3374.7500266128254) + minDelta)
		assertFalse(FixMicro64U.from(12777.6029546566) < FixMicro64U.from(12777.6029546566) - minDelta)
		assertFalse(12777.6029546566 < FixMicro64U.from(12777.6029546566) - minDelta)
		assertTrue(FixMicro64U.from(12777.6029546566) < FixMicro64U.from(12777.6029546566) + minDelta)
		assertTrue(12777.6029546566 < FixMicro64U.from(12777.6029546566) + minDelta)
		assertFalse(FixMicro64U.from(110822.35838203174) < FixMicro64U.from(110822.35838203174) - minDelta)
		assertFalse(110822.35838203174 < FixMicro64U.from(110822.35838203174) - minDelta)
		assertTrue(FixMicro64U.from(110822.35838203174) < FixMicro64U.from(110822.35838203174) + minDelta)
		assertTrue(110822.35838203174 < FixMicro64U.from(110822.35838203174) + minDelta)
		assertFalse(FixMicro64U.from(604124.6329044729) < FixMicro64U.from(604124.6329044729) - minDelta)
		assertFalse(604124.6329044729 < FixMicro64U.from(604124.6329044729) - minDelta)
		assertTrue(FixMicro64U.from(604124.6329044729) < FixMicro64U.from(604124.6329044729) + minDelta)
		assertTrue(604124.6329044729 < FixMicro64U.from(604124.6329044729) + minDelta)
		assertFalse(FixMicro64U.from(1.6000793194106514E7) < FixMicro64U.from(1.6000793194106514E7) - minDelta)
		assertFalse(1.6000793194106514E7 < FixMicro64U.from(1.6000793194106514E7) - minDelta)
		assertTrue(FixMicro64U.from(1.6000793194106514E7) < FixMicro64U.from(1.6000793194106514E7) + minDelta)
		assertTrue(1.6000793194106514E7 < FixMicro64U.from(1.6000793194106514E7) + minDelta)
		assertFalse(FixMicro64U.from(2.3977921959639926E7) < FixMicro64U.from(2.3977921959639926E7) - minDelta)
		assertFalse(2.3977921959639926E7 < FixMicro64U.from(2.3977921959639926E7) - minDelta)
		assertTrue(FixMicro64U.from(2.3977921959639926E7) < FixMicro64U.from(2.3977921959639926E7) + minDelta)
		assertTrue(2.3977921959639926E7 < FixMicro64U.from(2.3977921959639926E7) + minDelta)
		assertFalse(FixMicro64U.from(9.521487864611689E8) < FixMicro64U.from(9.521487864611689E8) - minDelta)
		assertFalse(9.521487864611689E8 < FixMicro64U.from(9.521487864611689E8) - minDelta)
		assertTrue(FixMicro64U.from(9.521487864611689E8) < FixMicro64U.from(9.521487864611689E8) + minDelta)
		assertTrue(9.521487864611689E8 < FixMicro64U.from(9.521487864611689E8) + minDelta)
		assertFalse(FixMicro64U.from(1.5050441079000726E9) < FixMicro64U.from(1.5050441079000726E9) - minDelta)
		assertFalse(1.5050441079000726E9 < FixMicro64U.from(1.5050441079000726E9) - minDelta)
		assertTrue(FixMicro64U.from(1.5050441079000726E9) < FixMicro64U.from(1.5050441079000726E9) + minDelta)
		assertTrue(1.5050441079000726E9 < FixMicro64U.from(1.5050441079000726E9) + minDelta)
		assertFalse(FixMicro64U.from(5.3789076402765045E10) < FixMicro64U.from(5.3789076402765045E10) - minDelta)
		assertFalse(5.3789076402765045E10 < FixMicro64U.from(5.3789076402765045E10) - minDelta)
		assertTrue(FixMicro64U.from(5.3789076402765045E10) < FixMicro64U.from(5.3789076402765045E10) + minDelta)
		assertTrue(5.3789076402765045E10 < FixMicro64U.from(5.3789076402765045E10) + minDelta)
		assertFalse(FixMicro64U.from(6.900496279981952E10) < FixMicro64U.from(6.900496279981952E10) - minDelta)
		assertFalse(6.900496279981952E10 < FixMicro64U.from(6.900496279981952E10) - minDelta)
		assertTrue(FixMicro64U.from(6.900496279981952E10) < FixMicro64U.from(6.900496279981952E10) + minDelta)
		assertTrue(6.900496279981952E10 < FixMicro64U.from(6.900496279981952E10) + minDelta)
		assertFalse(FixMicro64U.from(1.415336468990056E12) < FixMicro64U.from(1.415336468990056E12) - minDelta)
		assertFalse(1.415336468990056E12 < FixMicro64U.from(1.415336468990056E12) - minDelta)
		assertTrue(FixMicro64U.from(1.415336468990056E12) < FixMicro64U.from(1.415336468990056E12) + minDelta)
		assertTrue(1.415336468990056E12 < FixMicro64U.from(1.415336468990056E12) + minDelta)
		assertFalse(FixMicro64U.from(3.5566628266165547E12) < FixMicro64U.from(3.5566628266165547E12) - minDelta)
		assertFalse(3.5566628266165547E12 < FixMicro64U.from(3.5566628266165547E12) - minDelta)
		assertTrue(FixMicro64U.from(3.5566628266165547E12) < FixMicro64U.from(3.5566628266165547E12) + minDelta)
		assertTrue(3.5566628266165547E12 < FixMicro64U.from(3.5566628266165547E12) + minDelta)
		assertFalse(FixMicro64U.from(1.7592186044416E13) < FixMicro64U.from(1.7592186044416E13) - minDelta)
		assertFalse(1.7592186044416E13 < FixMicro64U.from(1.7592186044416E13) - minDelta)
		assertTrue(FixMicro64U.raw(ULong.MAX_VALUE) >= 17592186044415)
		assertTrue(FixMicro64U.raw(ULong.MAX_VALUE) > 17592186044415)
		assertTrue(FixMicro64U.raw(ULong.MAX_VALUE) < 17592186044416)
		assertTrue(FixMicro64U.raw(ULong.MAX_VALUE) < 1.7609778230460414E13)
		assertTrue(FixMicro64U.raw(ULong.MAX_VALUE) < ULong.MAX_VALUE)
		assertTrue(FixMicro64U.raw(ULong.MAX_VALUE) < ULong.MAX_VALUE.toFloat())
		assertTrue(FixMicro64U.raw(ULong.MAX_VALUE) < ULong.MAX_VALUE.toDouble())
		assertTrue(FixMicro64U.ZERO > -1)
		assertTrue(FixMicro64U.ZERO > -0.001f)
		assertTrue(FixMicro64U.ZERO > -0.001)
		assertTrue(FixMicro64U.ZERO > Long.MIN_VALUE)
		assertTrue(FixMicro64U.ZERO > Long.MIN_VALUE.toFloat())
		assertTrue(FixMicro64U.ZERO > Long.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixMicro64U.Array(2) { FixMicro64U.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixMicro64U.ONE, testArray[0])
		assertEquals(FixMicro64U.ONE, testArray[1])
		testArray[1] = FixMicro64U.ZERO
		assertEquals(FixMicro64U.ONE, testArray[0])
		assertEquals(FixMicro64U.ZERO, testArray[1])
		testArray.fill(FixMicro64U.ZERO)
		assertEquals(FixMicro64U.ZERO, testArray[0])
		assertEquals(FixMicro64U.ZERO, testArray[1])
	}

	@Test
	fun testMinMax() {
		assertEquals(FixMicro64U.ZERO, min(FixMicro64U.ZERO, FixMicro64U.ZERO))
		assertEquals(FixMicro64U.ZERO, max(FixMicro64U.ZERO, FixMicro64U.ZERO))
		assertEquals(FixMicro64U.ZERO, min(FixMicro64U.ONE, FixMicro64U.ZERO))
		assertEquals(FixMicro64U.ONE, max(FixMicro64U.ONE, FixMicro64U.ZERO))
		assertEquals(FixMicro64U.ZERO, min(FixMicro64U.ZERO, FixMicro64U.ONE))
		assertEquals(FixMicro64U.ONE, max(FixMicro64U.ZERO, FixMicro64U.ONE))
		assertEquals(FixMicro64U.ZERO, min(FixMicro64U.ZERO, FixMicro64U.raw(ULong.MAX_VALUE)))
		assertEquals(FixMicro64U.raw(ULong.MAX_VALUE), max(FixMicro64U.ZERO, FixMicro64U.raw(ULong.MAX_VALUE)))
	}
}
