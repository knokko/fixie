package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixMicro64 {

	fun assertEquals(a: FixMicro64, b: FixMicro64, maxDelta: FixMicro64) {
		val rawDifference = a.raw - b.raw
		if (rawDifference.absoluteValue > maxDelta.raw) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixMicro64.ZERO.toString())
		assertEquals("1", FixMicro64.ONE.toString())
		assertTrue((FixMicro64.ONE / 3).toString().startsWith("0.33"))
		assertEquals("-1", (-FixMicro64.ONE).toString())
		assertTrue((FixMicro64.ONE / -3).toString().startsWith("-0.33"))
		assertTrue((FixMicro64.from(8796093022206) + FixMicro64.ONE / 3).toString().endsWith((FixMicro64.ONE / 3).toString().substring(1)))
		assertEquals("0.0625", (FixMicro64.ONE / 16).toString())
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixMicro64.ONE, FixMicro64.from(one))

		fun testValue(value: Int) = assertEquals(value, FixMicro64.from(value).toInt())
		testValue(-2147483648)
		testValue(-28826398)
		testValue(-3572966)
		testValue(-3562759)
		testValue(-14817)
		testValue(-10323)
		testValue(-1595)
		testValue(0)
		testValue(1)
		testValue(315)
		testValue(2529)
		testValue(17304)
		testValue(326172)
		testValue(2197776)
		testValue(123704344)
		testValue(2147483647)
	}

	@Test
	fun testLongConversion() {
		val one = 1L
		assertEquals(FixMicro64.ONE, FixMicro64.from(one))

		fun testValue(value: Long) = assertEquals(value, FixMicro64.from(value).toLong())
		fun testOverflow(value: Long) = assertThrows(FixedPointException::class.java) { FixMicro64.from(value) }
		testValue(-8796093022208)
		testValue(-3563822055015)
		testValue(-297715097014)
		testValue(-26640435322)
		testValue(-598026500)
		testValue(-84235557)
		testValue(-3353629)
		testValue(-536269)
		testValue(-34303)
		testValue(-681)
		testValue(-106)
		testValue(0)
		testValue(1)
		testValue(369)
		testValue(892)
		testValue(16893)
		testValue(1023733)
		testValue(15032394)
		testValue(115403725)
		testValue(2742457511)
		testValue(10197323574)
		testValue(549272688909)
		testValue(8796093022207)

		testOverflow(Long.MIN_VALUE)
		testOverflow(-589153389781491222)
		testOverflow(-43117419028065805)
		testOverflow(-785861820617605)
		testOverflow(-8796093022209)

		testOverflow(8796093022208)
		testOverflow(8558650835934428)
		testOverflow(394467231681481571)
		testOverflow(9223372036854775807)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixMicro64.ONE, FixMicro64.from(1f))
		val delta = 1.9073486E-6f
		assertEquals(9.536743E-7f, FixMicro64.from(9.536743E-7f).toFloat(), delta)
		assertEquals(-9.536743E-7f, FixMicro64.from(-9.536743E-7f).toFloat(), delta)
		assertEquals(5.7965175E-5f, FixMicro64.from(5.7965175E-5f).toFloat(), delta)
		assertEquals(-5.7965175E-5f, FixMicro64.from(-5.7965175E-5f).toFloat(), delta)
		assertEquals(0.0030845916f, FixMicro64.from(0.0030845916f).toFloat(), delta)
		assertEquals(-0.0030845916f, FixMicro64.from(-0.0030845916f).toFloat(), delta)
		assertEquals(0.009314704f, FixMicro64.from(0.009314704f).toFloat(), delta)
		assertEquals(-0.009314704f, FixMicro64.from(-0.009314704f).toFloat(), delta)
		assertEquals(0.5088087f, FixMicro64.from(0.5088087f).toFloat(), delta)
		assertEquals(-0.5088087f, FixMicro64.from(-0.5088087f).toFloat(), delta)
		assertEquals(14.094133f, FixMicro64.from(14.094133f).toFloat(), delta)
		assertEquals(-14.094133f, FixMicro64.from(-14.094133f).toFloat(), delta)
		assertEquals(55.274475f, FixMicro64.from(55.274475f).toFloat(), delta)
		assertEquals(-55.274475f, FixMicro64.from(-55.274475f).toFloat(), delta)
		assertEquals(488.1811f, FixMicro64.from(488.1811f).toFloat(), delta)
		assertEquals(-488.1811f, FixMicro64.from(-488.1811f).toFloat(), delta)
		assertEquals(19484.523f, FixMicro64.from(19484.523f).toFloat(), delta)
		assertEquals(-19484.523f, FixMicro64.from(-19484.523f).toFloat(), delta)
		assertEquals(978553.25f, FixMicro64.from(978553.25f).toFloat(), delta)
		assertEquals(-978553.25f, FixMicro64.from(-978553.25f).toFloat(), delta)
		assertEquals(1.8665852E7f, FixMicro64.from(1.8665852E7f).toFloat(), delta)
		assertEquals(-1.8665852E7f, FixMicro64.from(-1.8665852E7f).toFloat(), delta)
		assertEquals(2.77667648E8f, FixMicro64.from(2.77667648E8f).toFloat(), delta)
		assertEquals(-2.77667648E8f, FixMicro64.from(-2.77667648E8f).toFloat(), delta)
		assertEquals(4.4010537E9f, FixMicro64.from(4.4010537E9f).toFloat(), delta)
		assertEquals(-4.4010537E9f, FixMicro64.from(-4.4010537E9f).toFloat(), delta)
		assertEquals(7.1891963E9f, FixMicro64.from(7.1891963E9f).toFloat(), delta)
		assertEquals(-7.1891963E9f, FixMicro64.from(-7.1891963E9f).toFloat(), delta)
		assertEquals(3.67286419E11f, FixMicro64.from(3.67286419E11f).toFloat(), delta)
		assertEquals(-3.67286419E11f, FixMicro64.from(-3.67286419E11f).toFloat(), delta)
		assertEquals(8.796093E12f, FixMicro64.from(8.796093E12f).toFloat(), delta)
		assertEquals(-8.796093E12f, FixMicro64.from(-8.796093E12f).toFloat(), delta)

		assertThrows(FixedPointException::class.java) { FixMicro64.from(8.7969728E12f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-8.7969728E12f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(8.9122236E14f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-8.9122236E14f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(7.3642102E16f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-7.3642102E16f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(3.98255701E18f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-3.98255701E18f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(8.430682E19f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-8.430682E19f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(4.7019753E21f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-4.7019753E21f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(2.1746187E22f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-2.1746187E22f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(1.3515745E23f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-1.3515745E23f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(7.7371252E25f) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-7.7371252E25f) }
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixMicro64.ONE, FixMicro64.from(1.0))
		val delta = 1.9073486328125E-6
		assertEquals(9.5367431640625E-7, FixMicro64.from(9.5367431640625E-7).toDouble(), delta)
		assertEquals(-9.5367431640625E-7, FixMicro64.from(-9.5367431640625E-7).toDouble(), delta)
		assertEquals(5.7965173783582514E-5, FixMicro64.from(5.7965173783582514E-5).toDouble(), delta)
		assertEquals(-5.7965173783582514E-5, FixMicro64.from(-5.7965173783582514E-5).toDouble(), delta)
		assertEquals(0.003084591527158651, FixMicro64.from(0.003084591527158651).toDouble(), delta)
		assertEquals(-0.003084591527158651, FixMicro64.from(-0.003084591527158651).toDouble(), delta)
		assertEquals(0.009314703658805575, FixMicro64.from(0.009314703658805575).toDouble(), delta)
		assertEquals(-0.009314703658805575, FixMicro64.from(-0.009314703658805575).toDouble(), delta)
		assertEquals(0.5088086753629321, FixMicro64.from(0.5088086753629321).toDouble(), delta)
		assertEquals(-0.5088086753629321, FixMicro64.from(-0.5088086753629321).toDouble(), delta)
		assertEquals(14.094133688218953, FixMicro64.from(14.094133688218953).toDouble(), delta)
		assertEquals(-14.094133688218953, FixMicro64.from(-14.094133688218953).toDouble(), delta)
		assertEquals(55.274475303202365, FixMicro64.from(55.274475303202365).toDouble(), delta)
		assertEquals(-55.274475303202365, FixMicro64.from(-55.274475303202365).toDouble(), delta)
		assertEquals(488.18110381699654, FixMicro64.from(488.18110381699654).toDouble(), delta)
		assertEquals(-488.18110381699654, FixMicro64.from(-488.18110381699654).toDouble(), delta)
		assertEquals(19484.523405800377, FixMicro64.from(19484.523405800377).toDouble(), delta)
		assertEquals(-19484.523405800377, FixMicro64.from(-19484.523405800377).toDouble(), delta)
		assertEquals(978553.2634048663, FixMicro64.from(978553.2634048663).toDouble(), delta)
		assertEquals(-978553.2634048663, FixMicro64.from(-978553.2634048663).toDouble(), delta)
		assertEquals(1.8665851216571603E7, FixMicro64.from(1.8665851216571603E7).toDouble(), delta)
		assertEquals(-1.8665851216571603E7, FixMicro64.from(-1.8665851216571603E7).toDouble(), delta)
		assertEquals(2.776676497250265E8, FixMicro64.from(2.776676497250265E8).toDouble(), delta)
		assertEquals(-2.776676497250265E8, FixMicro64.from(-2.776676497250265E8).toDouble(), delta)
		assertEquals(4.401053941594629E9, FixMicro64.from(4.401053941594629E9).toDouble(), delta)
		assertEquals(-4.401053941594629E9, FixMicro64.from(-4.401053941594629E9).toDouble(), delta)
		assertEquals(7.18919617871774E9, FixMicro64.from(7.18919617871774E9).toDouble(), delta)
		assertEquals(-7.18919617871774E9, FixMicro64.from(-7.18919617871774E9).toDouble(), delta)
		assertEquals(3.672864068723384E11, FixMicro64.from(3.672864068723384E11).toDouble(), delta)
		assertEquals(-3.672864068723384E11, FixMicro64.from(-3.672864068723384E11).toDouble(), delta)
		assertEquals(8.796093022208E12, FixMicro64.from(8.796093022208E12).toDouble(), delta)
		assertEquals(-8.796093022208E12, FixMicro64.from(-8.796093022208E12).toDouble(), delta)

		assertThrows(FixedPointException::class.java) { FixMicro64.from(8.79697263151022E12) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-8.79697263151022E12) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(8.912223887060171E14) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-8.912223887060171E14) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(7.3642105648855824E16) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-7.3642105648855824E16) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(3.9825570628735816E18) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-3.9825570628735816E18) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(8.430682287138901E19) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-8.430682287138901E19) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(4.7019752045726883E21) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-4.7019752045726883E21) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(2.174618712907724E22) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-2.174618712907724E22) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(1.3515745041391995E23) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-1.3515745041391995E23) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(7.737125245533627E25) }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-7.737125245533627E25) }
	}

	@Test
	fun testUnaryMinus() {
		assertThrows(FixedPointException::class.java) { -FixMicro64.raw(Long.MIN_VALUE) }
		assertEquals(9223372036854775807, -FixMicro64.raw(-9223372036854775807).raw)
		assertEquals(386268115287331654, -FixMicro64.raw(-386268115287331654).raw)
		assertEquals(84771190261467287, -FixMicro64.raw(-84771190261467287).raw)
		assertEquals(4950739029511539, -FixMicro64.raw(-4950739029511539).raw)
		assertEquals(49399445901691, -FixMicro64.raw(-49399445901691).raw)
		assertEquals(4706486801583, -FixMicro64.raw(-4706486801583).raw)
		assertEquals(172834677536, -FixMicro64.raw(-172834677536).raw)
		assertEquals(6828972630, -FixMicro64.raw(-6828972630).raw)
		assertEquals(134392659, -FixMicro64.raw(-134392659).raw)
		assertEquals(4481280, -FixMicro64.raw(-4481280).raw)
		assertEquals(221202, -FixMicro64.raw(-221202).raw)
		assertEquals(1963, -FixMicro64.raw(-1963).raw)
		assertEquals(0, -FixMicro64.raw(0).raw)
		assertEquals(-1, -FixMicro64.raw(1).raw)
		assertEquals(-4732, -FixMicro64.raw(4732).raw)
		assertEquals(-11204, -FixMicro64.raw(11204).raw)
		assertEquals(-218793, -FixMicro64.raw(218793).raw)
		assertEquals(-8234988, -FixMicro64.raw(8234988).raw)
		assertEquals(-838900040, -FixMicro64.raw(838900040).raw)
		assertEquals(-11818534200, -FixMicro64.raw(11818534200).raw)
		assertEquals(-111327146492, -FixMicro64.raw(111327146492).raw)
		assertEquals(-20393277992782, -FixMicro64.raw(20393277992782).raw)
		assertEquals(-387856025849162, -FixMicro64.raw(387856025849162).raw)
		assertEquals(-3232553098941491, -FixMicro64.raw(3232553098941491).raw)
		assertEquals(-310510768390365198, -FixMicro64.raw(310510768390365198).raw)
		assertEquals(-9223372036854775807, -FixMicro64.raw(9223372036854775807).raw)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixMicro64, b: FixMicro64, c: FixMicro64) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}

		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixMicro64.from(a), FixMicro64.from(b), FixMicro64.from(c))
			assertEquals(FixMicro64.from(c), FixMicro64.from(a) + b)
			assertEquals(FixMicro64.from(c), b + FixMicro64.from(a))
			assertEquals(FixMicro64.from(a), FixMicro64.from(c) - b)
			assertEquals(FixMicro64.from(b), c - FixMicro64.from(a))
		}

		testValues(FixMicro64.raw(Long.MIN_VALUE), FixMicro64.ONE, FixMicro64.raw(Long.MIN_VALUE + 1048576))
		testValues(-8796093022208, 4250525201524, -4545567820684)
		testValues(-4700374746247, 1572318993297, -3128055752950)
		testValues(-642480652528, 7411942889936, 6769462237408)
		testValues(-239201181438, 3226197163462, 2986995982024)
		testValues(-127734379568, 2893313220199, 2765578840631)
		testValues(-10751753907, 6516544959997, 6505793206090)
		testValues(-1228753186, 7007231189724, 7006002436538)
		testValues(-159424173, 5660921831307, 5660762407134)
		testValues(-110026238, 6901093181789, 6900983155551)
		testValues(-18741456, 1048454354665, 1048435613209)
		testValues(-12348199, 3891241045956, 3891228697757)
		testValues(-721387, 4838156837950, 4838156116563)
		testValues(-707865, 8091245289848, 8091244581983)
		testValues(-19309, 732640427031, 732640407722)
		testValues(-1604, 1359795396451, 1359795394847)
		testValues(-983, 1417182164599, 1417182163616)
		testValues(-537, 169667085954, 169667085417)
		testValues(0, 5603484901904, 5603484901904)
		testValues(1, 2622382602628, 2622382602629)
		testValues(921, 4732696699198, 4732696700119)
		testValues(2131, 5809298801724, 5809298803855)
		testValues(7796, 5418873810800, 5418873818596)
		testValues(13105, 1765711013759, 1765711026864)
		testValues(116335, 2557583217723, 2557583334058)
		testValues(182653, 7041117777487, 7041117960140)
		testValues(4229071, 371820067376, 371824296447)
		testValues(9659905, 3959431040192, 3959440700097)
		testValues(86274790, 1743136461310, 1743222736100)
		testValues(96223217, 1512888231561, 1512984454778)
		testValues(3229576840, 7803455343896, 7806684920736)
		testValues(5705385067, 3682732859982, 3688438245049)
		testValues(33774587482, 3331710320785, 3365484908267)
		testValues(121193785341, 8605637005394, 8726830790735)
		testValues(984869232416, 6485611054730, 7470480287146)
		testValues(1712126437875, 1254812455709, 2966938893584)
		testValues(8796093022207, 0, 8796093022207)
		testValues(FixMicro64.raw(Long.MAX_VALUE - 1048576), FixMicro64.ONE, FixMicro64.raw(Long.MAX_VALUE))

		fun testOverflowPlus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) + FixMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro64.from(b) }
		}

		fun testOverflowMinus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) - FixMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro64.from(b) }
		}

		fun testOverflowPlus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) + FixMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro64.from(b) }
		}

		fun testOverflowMinus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) - FixMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro64.from(b) }
		}

		fun testOverflowPlus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) + FixMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro64.from(b) }
		}

		fun testOverflowMinus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) - FixMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro64.from(b) }
		}

		fun testOverflowPlus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) + FixMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixMicro64.from(b) }
		}

		fun testOverflowMinus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) - FixMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixMicro64.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixMicro64.from(b) }
		}

		testOverflowPlus(-8796093022208L, 17592186044416L)
		testOverflowPlus(-8.796093E12f, 1.7592186E13f)
		testOverflowPlus(-8.796093022208E12, 1.7592186044416E13)
		testOverflowPlus(-8796093022208L, 35184372088833L)
		testOverflowPlus(-8.796093E12f, 3.5184372E13f)
		testOverflowPlus(-8.796093022208E12, 3.5184372088833E13)
		testOverflowMinus(-8796093022208L, 1L)
		testOverflowMinus(-8796093022208L, 4L)
		testOverflowPlus(-1341525896690L, 10137618918898L)
		testOverflowPlus(-1.34152585E12f, 1.01376191E13f)
		testOverflowPlus(-1.34152589669E12, 1.0137618918898E13)
		testOverflowPlus(-1341525896690L, 5104629032356041897L)
		testOverflowMinus(-1341525896690L, 7454567125519L)
		testOverflowMinus(-1.34152589669E12, 7.454567125519E12)
		testOverflowPlus(-29137213347L, 8825230235555L)
		testOverflowPlus(-2.9137213347E10, 8.825230235555E12)
		testOverflowPlus(-29137213347L, 8027431986115305744L)
		testOverflowMinus(-29137213347L, 8766955808862L)
		testOverflowMinus(-2.9137213347E10, 8.766955808862E12)
		testOverflowPlus(-817832510L, 8796910854718L)
		testOverflowMinus(-817832510L, 8795275189699L)
		testOverflowMinus(-817832510L, 1667593597255155216L)
		testOverflowPlus(-11601756L, 8796104623964L)
		testOverflowPlus(-11601756L, 1186217376066058185L)
		testOverflowMinus(-11601756L, 8796081420453L)
		testOverflowPlus(-28432L, 8796093050640L)
		testOverflowPlus(-28432L, 500198626609315617L)
		testOverflowMinus(-28432L, 8796092993777L)
		testOverflowPlus(0L, 8796093022208L)
		testOverflowPlus(0L, 17592186044417L)
		testOverflowMinus(0L, 8796093022209L)
		testOverflowMinus(0L, 35184372088836L)
		testOverflowPlus(1L, 8796093022207L)
		testOverflowMinus(1L, 8796093022210L)
		testOverflowMinus(1L, 52776558133257L)
		testOverflowPlus(27304L, 8796092994904L)
		testOverflowMinus(27304L, 8796093049513L)
		testOverflowMinus(27304L, 480372232874440932L)
		testOverflowPlus(92225L, 8796092929983L)
		testOverflowMinus(92225L, 8796093114434L)
		testOverflowMinus(92225L, 1622474550824173961L)
		testOverflowPlus(5253803L, 8796087768405L)
		testOverflowMinus(5253803L, 8796098276012L)
		testOverflowMinus(5253803L, 192222235002222825L)
		testOverflowPlus(242063775L, 8795850958433L)
		testOverflowPlus(242063775L, 2825529076120960388L)
		testOverflowMinus(242063775L, 8796335085984L)
		testOverflowPlus(29255199622L, 8766837822586L)
		testOverflowPlus(2.9255199622E10, 8.766837822586E12)
		testOverflowPlus(29255199622L, 8561634809956889369L)
		testOverflowMinus(29255199622L, 8825348221831L)
		testOverflowMinus(2.9255199622E10, 8.825348221831E12)
		testOverflowMinus(29255199622L, 6071373089970780224L)
		testOverflowPlus(8796093022207L, 1L)
		testOverflowPlus(8796093022207L, 4L)
		testOverflowMinus(8796093022207L, 17592186044416L)
		testOverflowMinus(8.796093022207E12, 1.7592186044416E13)
		testOverflowMinus(8796093022207L, 35184372088833L)
		testOverflowMinus(8.796093E12f, 3.5184372E13f)
		testOverflowMinus(8.796093022207E12, 3.5184372088833E13)
		assertEquals(FixMicro64.raw(9223372036853240796), FixMicro64.raw(-486436) + 8796093022207)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixMicro64.raw(Long.MAX_VALUE), 1 * FixMicro64.raw(Long.MAX_VALUE))
		assertEquals(FixMicro64.raw(Long.MAX_VALUE), FixMicro64.raw(Long.MAX_VALUE) / 1)
		assertEquals(FixMicro64.raw(Long.MIN_VALUE), 1 * FixMicro64.raw(Long.MIN_VALUE))
		assertEquals(FixMicro64.raw(Long.MIN_VALUE), FixMicro64.raw(Long.MIN_VALUE) / 1)
		assertEquals(FixMicro64.raw(Long.MIN_VALUE + 1), -1 * FixMicro64.raw(Long.MAX_VALUE))
		assertEquals(FixMicro64.raw(Long.MIN_VALUE + 1), FixMicro64.raw(Long.MAX_VALUE) / -1)
		assertThrows(FixedPointException::class.java) { -1 * FixMicro64.raw(Long.MIN_VALUE) }
		assertThrows(FixedPointException::class.java) { FixMicro64.raw(Long.MIN_VALUE) / -1 }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixMicro64.from(a * b), FixMicro64.from(a) * FixMicro64.from(b))
			assertEquals(FixMicro64.from(a * b), FixMicro64.from(a) * b)
			assertEquals(FixMicro64.from(a * b), b * FixMicro64.from(a))
			if (b != 0L) assertEquals(FixMicro64.from(a), FixMicro64.from(a * b) / b)
			if (a != 0L) assertEquals(FixMicro64.from(b), FixMicro64.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixMicro64.from(b), FixMicro64.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixMicro64.from(a * b), FixMicro64.from(a) * b.toInt())
				assertEquals(FixMicro64.from(a * b), b.toInt() * FixMicro64.from(a))
			}
		}
		testValues(-8796093022208, 0)
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-5916980788781) * 25106 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-725700145590) * -101861414815 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-347279286395) * -3060630711 }
		testValues(-233656843420, 23)
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-145771851891) * -29270508365 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-101861414815) * 2791028 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-29270508365) * 4154 }
		testValues(-9119428782, -328)
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-3060630711) * 2315506768 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-1140761504) * -14533912 }
		testValues(-755474141, 4154)
		testValues(-521310339, 43)
		testValues(-128988175, 1)
		testValues(-69103277, 13474)
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-14533912) * 125464405 }
		testValues(-10399772, -20537)
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-3602089) * 7710104 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-2023396) * -347279286395 }
		testValues(-484049, -3602089)
		testValues(-309459, 1977)
		testValues(-37181, -801)
		assertThrows(FixedPointException::class.java) { FixMicro64.from(-20537) * 1097279137153 }
		testValues(-17257, -309459)
		testValues(-11455, -2023396)
		testValues(-2839, 64)
		testValues(-801, -309459)
		testValues(-328, 4154)
		testValues(-302, 64)
		testValues(-64, 23296)
		testValues(-57, -484049)
		testValues(0, -11455)
		testValues(1, -101861414815)
		testValues(23, 23)
		testValues(43, -309459)
		testValues(64, -2023396)
		assertThrows(FixedPointException::class.java) { FixMicro64.from(101) * 2602201682408 }
		testValues(381, -17257)
		assertThrows(FixedPointException::class.java) { FixMicro64.from(1297) * 141920075433 }
		testValues(1977, -128988175)
		testValues(4154, 1977)
		assertThrows(FixedPointException::class.java) { FixMicro64.from(13474) * -347279286395 }
		testValues(23296, 125464405)
		testValues(25106, -14533912)
		testValues(191087, 2791028)
		assertThrows(FixedPointException::class.java) { FixMicro64.from(1094702) * 8796093022207 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(1394035) * 2177141597 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(1782341) * -233656843420 }
		testValues(2791028, 1782341)
		assertThrows(FixedPointException::class.java) { FixMicro64.from(7710104) * 1097279137153 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(57392869) * -29270508365 }
		testValues(61720171, 101)
		assertThrows(FixedPointException::class.java) { FixMicro64.from(125464405) * 1031359677 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(1031359677) * 2791028 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(2177141597) * -347279286395 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(2315506768) * -8796093022208 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(7178700588) * 1394035 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(49826605938) * -5916980788781 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(51907992994) * -755474141 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(141920075433) * -725700145590 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(417145258360) * -521310339 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(1097279137153) * 1097279137153 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(2602201682408) * 1977 }
		assertThrows(FixedPointException::class.java) { FixMicro64.from(8796093022207) * 1394035 }
		assertEquals(FixMicro64.raw(-688617), (FixMicro64.raw(-688617) * FixMicro64.raw(888629)) / FixMicro64.raw(888629), FixMicro64.raw(10485))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixMicro64.ZERO < FixMicro64.ONE)
		assertTrue(0 < FixMicro64.ONE)
		assertFalse(FixMicro64.ZERO > FixMicro64.ONE)
		assertFalse(0 > FixMicro64.ONE)
		assertFalse(FixMicro64.ONE < FixMicro64.ONE)
		assertFalse(FixMicro64.ONE < 1)
		assertFalse(FixMicro64.ONE > FixMicro64.ONE)
		assertTrue(FixMicro64.ONE <= FixMicro64.ONE)
		assertTrue(FixMicro64.ONE >= FixMicro64.ONE)
		assertTrue(FixMicro64.raw(Long.MIN_VALUE) < FixMicro64.raw(Long.MAX_VALUE))

		val minDelta = FixMicro64.raw(1)
		assertEquals(FixMicro64.from(12), FixMicro64.from(12))
		assertNotEquals(FixMicro64.from(12), FixMicro64.from(12) - minDelta)
		assertTrue(FixMicro64.from(9.5367431640625E-7) < FixMicro64.from(9.5367431640625E-7) + minDelta)
		assertTrue(9.5367431640625E-7 < FixMicro64.from(9.5367431640625E-7) + minDelta)
		assertFalse(FixMicro64.from(3.911252279924859E-4) < FixMicro64.from(3.911252279924859E-4) - minDelta)
		assertFalse(3.911252279924859E-4 < FixMicro64.from(3.911252279924859E-4) - minDelta)
		assertTrue(FixMicro64.from(3.911252279924859E-4) < FixMicro64.from(3.911252279924859E-4) + minDelta)
		assertTrue(3.911252279924859E-4 < FixMicro64.from(3.911252279924859E-4) + minDelta)
		assertFalse(FixMicro64.from(0.0013926195514347168) < FixMicro64.from(0.0013926195514347168) - minDelta)
		assertFalse(0.0013926195514347168 < FixMicro64.from(0.0013926195514347168) - minDelta)
		assertTrue(FixMicro64.from(0.0013926195514347168) < FixMicro64.from(0.0013926195514347168) + minDelta)
		assertTrue(0.0013926195514347168 < FixMicro64.from(0.0013926195514347168) + minDelta)
		assertFalse(FixMicro64.from(0.007928885234495038) < FixMicro64.from(0.007928885234495038) - minDelta)
		assertFalse(0.007928885234495038 < FixMicro64.from(0.007928885234495038) - minDelta)
		assertTrue(FixMicro64.from(0.007928885234495038) < FixMicro64.from(0.007928885234495038) + minDelta)
		assertTrue(0.007928885234495038 < FixMicro64.from(0.007928885234495038) + minDelta)
		assertFalse(FixMicro64.from(0.060214617813443456) < FixMicro64.from(0.060214617813443456) - minDelta)
		assertFalse(0.060214617813443456 < FixMicro64.from(0.060214617813443456) - minDelta)
		assertTrue(FixMicro64.from(0.060214617813443456) < FixMicro64.from(0.060214617813443456) + minDelta)
		assertTrue(0.060214617813443456 < FixMicro64.from(0.060214617813443456) + minDelta)
		assertFalse(FixMicro64.from(0.18061000195143148) < FixMicro64.from(0.18061000195143148) - minDelta)
		assertFalse(0.18061000195143148 < FixMicro64.from(0.18061000195143148) - minDelta)
		assertTrue(FixMicro64.from(0.18061000195143148) < FixMicro64.from(0.18061000195143148) + minDelta)
		assertTrue(0.18061000195143148 < FixMicro64.from(0.18061000195143148) + minDelta)
		assertFalse(FixMicro64.from(2.6272550762762745) < FixMicro64.from(2.6272550762762745) - minDelta)
		assertFalse(2.6272550762762745 < FixMicro64.from(2.6272550762762745) - minDelta)
		assertTrue(FixMicro64.from(2.6272550762762745) < FixMicro64.from(2.6272550762762745) + minDelta)
		assertTrue(2.6272550762762745 < FixMicro64.from(2.6272550762762745) + minDelta)
		assertFalse(FixMicro64.from(10.160135609533143) < FixMicro64.from(10.160135609533143) - minDelta)
		assertFalse(10.160135609533143 < FixMicro64.from(10.160135609533143) - minDelta)
		assertTrue(FixMicro64.from(10.160135609533143) < FixMicro64.from(10.160135609533143) + minDelta)
		assertTrue(10.160135609533143 < FixMicro64.from(10.160135609533143) + minDelta)
		assertFalse(FixMicro64.from(143.3047592364777) < FixMicro64.from(143.3047592364777) - minDelta)
		assertFalse(143.3047592364777 < FixMicro64.from(143.3047592364777) - minDelta)
		assertTrue(FixMicro64.from(143.3047592364777) < FixMicro64.from(143.3047592364777) + minDelta)
		assertTrue(143.3047592364777 < FixMicro64.from(143.3047592364777) + minDelta)
		assertFalse(FixMicro64.from(3374.7500266128254) < FixMicro64.from(3374.7500266128254) - minDelta)
		assertFalse(3374.7500266128254 < FixMicro64.from(3374.7500266128254) - minDelta)
		assertTrue(FixMicro64.from(3374.7500266128254) < FixMicro64.from(3374.7500266128254) + minDelta)
		assertTrue(3374.7500266128254 < FixMicro64.from(3374.7500266128254) + minDelta)
		assertFalse(FixMicro64.from(12777.6029546566) < FixMicro64.from(12777.6029546566) - minDelta)
		assertFalse(12777.6029546566 < FixMicro64.from(12777.6029546566) - minDelta)
		assertTrue(FixMicro64.from(12777.6029546566) < FixMicro64.from(12777.6029546566) + minDelta)
		assertTrue(12777.6029546566 < FixMicro64.from(12777.6029546566) + minDelta)
		assertFalse(FixMicro64.from(110822.35838203174) < FixMicro64.from(110822.35838203174) - minDelta)
		assertFalse(110822.35838203174 < FixMicro64.from(110822.35838203174) - minDelta)
		assertTrue(FixMicro64.from(110822.35838203174) < FixMicro64.from(110822.35838203174) + minDelta)
		assertTrue(110822.35838203174 < FixMicro64.from(110822.35838203174) + minDelta)
		assertFalse(FixMicro64.from(604124.6329044729) < FixMicro64.from(604124.6329044729) - minDelta)
		assertFalse(604124.6329044729 < FixMicro64.from(604124.6329044729) - minDelta)
		assertTrue(FixMicro64.from(604124.6329044729) < FixMicro64.from(604124.6329044729) + minDelta)
		assertTrue(604124.6329044729 < FixMicro64.from(604124.6329044729) + minDelta)
		assertFalse(FixMicro64.from(1.6000793194106514E7) < FixMicro64.from(1.6000793194106514E7) - minDelta)
		assertFalse(1.6000793194106514E7 < FixMicro64.from(1.6000793194106514E7) - minDelta)
		assertTrue(FixMicro64.from(1.6000793194106514E7) < FixMicro64.from(1.6000793194106514E7) + minDelta)
		assertTrue(1.6000793194106514E7 < FixMicro64.from(1.6000793194106514E7) + minDelta)
		assertFalse(FixMicro64.from(2.3977921959639926E7) < FixMicro64.from(2.3977921959639926E7) - minDelta)
		assertFalse(2.3977921959639926E7 < FixMicro64.from(2.3977921959639926E7) - minDelta)
		assertTrue(FixMicro64.from(2.3977921959639926E7) < FixMicro64.from(2.3977921959639926E7) + minDelta)
		assertTrue(2.3977921959639926E7 < FixMicro64.from(2.3977921959639926E7) + minDelta)
		assertFalse(FixMicro64.from(9.521487864611689E8) < FixMicro64.from(9.521487864611689E8) - minDelta)
		assertFalse(9.521487864611689E8 < FixMicro64.from(9.521487864611689E8) - minDelta)
		assertTrue(FixMicro64.from(9.521487864611689E8) < FixMicro64.from(9.521487864611689E8) + minDelta)
		assertTrue(9.521487864611689E8 < FixMicro64.from(9.521487864611689E8) + minDelta)
		assertFalse(FixMicro64.from(1.5050441079000726E9) < FixMicro64.from(1.5050441079000726E9) - minDelta)
		assertFalse(1.5050441079000726E9 < FixMicro64.from(1.5050441079000726E9) - minDelta)
		assertTrue(FixMicro64.from(1.5050441079000726E9) < FixMicro64.from(1.5050441079000726E9) + minDelta)
		assertTrue(1.5050441079000726E9 < FixMicro64.from(1.5050441079000726E9) + minDelta)
		assertFalse(FixMicro64.from(5.3789076402765045E10) < FixMicro64.from(5.3789076402765045E10) - minDelta)
		assertFalse(5.3789076402765045E10 < FixMicro64.from(5.3789076402765045E10) - minDelta)
		assertTrue(FixMicro64.from(5.3789076402765045E10) < FixMicro64.from(5.3789076402765045E10) + minDelta)
		assertTrue(5.3789076402765045E10 < FixMicro64.from(5.3789076402765045E10) + minDelta)
		assertFalse(FixMicro64.from(6.900496279981952E10) < FixMicro64.from(6.900496279981952E10) - minDelta)
		assertFalse(6.900496279981952E10 < FixMicro64.from(6.900496279981952E10) - minDelta)
		assertTrue(FixMicro64.from(6.900496279981952E10) < FixMicro64.from(6.900496279981952E10) + minDelta)
		assertTrue(6.900496279981952E10 < FixMicro64.from(6.900496279981952E10) + minDelta)
		assertFalse(FixMicro64.from(1.415336468990056E12) < FixMicro64.from(1.415336468990056E12) - minDelta)
		assertFalse(1.415336468990056E12 < FixMicro64.from(1.415336468990056E12) - minDelta)
		assertTrue(FixMicro64.from(1.415336468990056E12) < FixMicro64.from(1.415336468990056E12) + minDelta)
		assertTrue(1.415336468990056E12 < FixMicro64.from(1.415336468990056E12) + minDelta)
		assertFalse(FixMicro64.from(3.5566628266165547E12) < FixMicro64.from(3.5566628266165547E12) - minDelta)
		assertFalse(3.5566628266165547E12 < FixMicro64.from(3.5566628266165547E12) - minDelta)
		assertTrue(FixMicro64.from(3.5566628266165547E12) < FixMicro64.from(3.5566628266165547E12) + minDelta)
		assertTrue(3.5566628266165547E12 < FixMicro64.from(3.5566628266165547E12) + minDelta)
		assertFalse(FixMicro64.from(8.796093022208E12) < FixMicro64.from(8.796093022208E12) - minDelta)
		assertFalse(8.796093022208E12 < FixMicro64.from(8.796093022208E12) - minDelta)
		assertTrue(FixMicro64.raw(Long.MAX_VALUE) >= 8796093022207)
		assertTrue(FixMicro64.raw(Long.MAX_VALUE) > 8796093022207)
		assertTrue(FixMicro64.raw(Long.MAX_VALUE) < 8796093022208)
		assertTrue(FixMicro64.raw(Long.MAX_VALUE) < 8.804889115230207E12)
		assertTrue(FixMicro64.raw(Long.MAX_VALUE) < Long.MAX_VALUE)
		assertTrue(FixMicro64.raw(Long.MAX_VALUE) < Long.MAX_VALUE.toFloat())
		assertTrue(FixMicro64.raw(Long.MAX_VALUE) < Long.MAX_VALUE.toDouble())
		assertTrue(FixMicro64.raw(Long.MIN_VALUE) <= -8796093022208)
		assertTrue(FixMicro64.raw(Long.MIN_VALUE) > -8796093022209)
		assertTrue(FixMicro64.raw(Long.MIN_VALUE) > -8.804889115231209E12)
		assertTrue(FixMicro64.raw(Long.MIN_VALUE) > Long.MIN_VALUE)
		assertTrue(FixMicro64.raw(Long.MIN_VALUE) > Long.MIN_VALUE.toFloat())
		assertTrue(FixMicro64.raw(Long.MIN_VALUE) > Long.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixMicro64.Array(2) { FixMicro64.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixMicro64.ONE, testArray[0])
		assertEquals(FixMicro64.ONE, testArray[1])
		testArray[1] = FixMicro64.ZERO
		assertEquals(FixMicro64.ONE, testArray[0])
		assertEquals(FixMicro64.ZERO, testArray[1])
		testArray.fill(FixMicro64.ZERO)
		assertEquals(FixMicro64.ZERO, testArray[0])
		assertEquals(FixMicro64.ZERO, testArray[1])
	}

	@Test
	fun testAbs() {
		assertEquals(FixMicro64.ZERO, abs(FixMicro64.ZERO))
		assertEquals(FixMicro64.ONE, abs(FixMicro64.ONE))
		assertEquals(FixMicro64.ONE, abs(-FixMicro64.ONE))
		assertEquals(FixMicro64.raw(Long.MAX_VALUE), abs(FixMicro64.raw(Long.MAX_VALUE)))
		assertEquals(FixMicro64.raw(Long.MAX_VALUE), abs(-FixMicro64.raw(Long.MAX_VALUE)))
		assertThrows(FixedPointException::class.java) { abs(FixMicro64.raw(Long.MIN_VALUE)) }
	}

	@Test
	fun testMinMax() {
		assertEquals(FixMicro64.ZERO, min(FixMicro64.ZERO, FixMicro64.ZERO))
		assertEquals(FixMicro64.ZERO, max(FixMicro64.ZERO, FixMicro64.ZERO))
		assertEquals(FixMicro64.ZERO, min(FixMicro64.ONE, FixMicro64.ZERO))
		assertEquals(FixMicro64.ONE, max(FixMicro64.ONE, FixMicro64.ZERO))
		assertEquals(FixMicro64.ZERO, min(FixMicro64.ZERO, FixMicro64.ONE))
		assertEquals(FixMicro64.ONE, max(FixMicro64.ZERO, FixMicro64.ONE))
		assertEquals(FixMicro64.ZERO, min(FixMicro64.ZERO, FixMicro64.raw(Long.MAX_VALUE)))
		assertEquals(FixMicro64.raw(Long.MAX_VALUE), max(FixMicro64.ZERO, FixMicro64.raw(Long.MAX_VALUE)))
		assertEquals(-FixMicro64.ONE, min(-FixMicro64.ONE, FixMicro64.ZERO))
		assertEquals(FixMicro64.ZERO, max(-FixMicro64.ONE, FixMicro64.ZERO))
		assertEquals(FixMicro64.raw(Long.MIN_VALUE), min(FixMicro64.ZERO, FixMicro64.raw(Long.MIN_VALUE)))
		assertEquals(FixMicro64.ZERO, max(FixMicro64.ZERO, FixMicro64.raw(Long.MIN_VALUE)))
	}
}
