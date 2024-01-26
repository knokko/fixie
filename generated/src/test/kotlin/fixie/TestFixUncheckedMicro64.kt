package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixUncheckedMicro64 {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixUncheckedMicro64.from(value).toInt())
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
		fun testValue(value: Long) = assertEquals(value, FixUncheckedMicro64.from(value).toLong())
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
	}

	@Test
	fun testFloatConversion() {
		val delta = 1.9073486E-6f
		assertEquals(9.536743E-7f, FixUncheckedMicro64.from(9.536743E-7f).toFloat(), delta)
		assertEquals(-9.536743E-7f, FixUncheckedMicro64.from(-9.536743E-7f).toFloat(), delta)
		assertEquals(5.7965175E-5f, FixUncheckedMicro64.from(5.7965175E-5f).toFloat(), delta)
		assertEquals(-5.7965175E-5f, FixUncheckedMicro64.from(-5.7965175E-5f).toFloat(), delta)
		assertEquals(0.0030845916f, FixUncheckedMicro64.from(0.0030845916f).toFloat(), delta)
		assertEquals(-0.0030845916f, FixUncheckedMicro64.from(-0.0030845916f).toFloat(), delta)
		assertEquals(0.009314704f, FixUncheckedMicro64.from(0.009314704f).toFloat(), delta)
		assertEquals(-0.009314704f, FixUncheckedMicro64.from(-0.009314704f).toFloat(), delta)
		assertEquals(0.5088087f, FixUncheckedMicro64.from(0.5088087f).toFloat(), delta)
		assertEquals(-0.5088087f, FixUncheckedMicro64.from(-0.5088087f).toFloat(), delta)
		assertEquals(14.094133f, FixUncheckedMicro64.from(14.094133f).toFloat(), delta)
		assertEquals(-14.094133f, FixUncheckedMicro64.from(-14.094133f).toFloat(), delta)
		assertEquals(55.274475f, FixUncheckedMicro64.from(55.274475f).toFloat(), delta)
		assertEquals(-55.274475f, FixUncheckedMicro64.from(-55.274475f).toFloat(), delta)
		assertEquals(488.1811f, FixUncheckedMicro64.from(488.1811f).toFloat(), delta)
		assertEquals(-488.1811f, FixUncheckedMicro64.from(-488.1811f).toFloat(), delta)
		assertEquals(19484.523f, FixUncheckedMicro64.from(19484.523f).toFloat(), delta)
		assertEquals(-19484.523f, FixUncheckedMicro64.from(-19484.523f).toFloat(), delta)
		assertEquals(978553.25f, FixUncheckedMicro64.from(978553.25f).toFloat(), delta)
		assertEquals(-978553.25f, FixUncheckedMicro64.from(-978553.25f).toFloat(), delta)
		assertEquals(1.8665852E7f, FixUncheckedMicro64.from(1.8665852E7f).toFloat(), delta)
		assertEquals(-1.8665852E7f, FixUncheckedMicro64.from(-1.8665852E7f).toFloat(), delta)
		assertEquals(2.77667648E8f, FixUncheckedMicro64.from(2.77667648E8f).toFloat(), delta)
		assertEquals(-2.77667648E8f, FixUncheckedMicro64.from(-2.77667648E8f).toFloat(), delta)
		assertEquals(4.4010537E9f, FixUncheckedMicro64.from(4.4010537E9f).toFloat(), delta)
		assertEquals(-4.4010537E9f, FixUncheckedMicro64.from(-4.4010537E9f).toFloat(), delta)
		assertEquals(7.1891963E9f, FixUncheckedMicro64.from(7.1891963E9f).toFloat(), delta)
		assertEquals(-7.1891963E9f, FixUncheckedMicro64.from(-7.1891963E9f).toFloat(), delta)
		assertEquals(3.67286419E11f, FixUncheckedMicro64.from(3.67286419E11f).toFloat(), delta)
		assertEquals(-3.67286419E11f, FixUncheckedMicro64.from(-3.67286419E11f).toFloat(), delta)
		assertEquals(8.796093E12f, FixUncheckedMicro64.from(8.796093E12f).toFloat(), delta)
		assertEquals(-8.796093E12f, FixUncheckedMicro64.from(-8.796093E12f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 1.9073486328125E-6
		assertEquals(9.5367431640625E-7, FixUncheckedMicro64.from(9.5367431640625E-7).toDouble(), delta)
		assertEquals(-9.5367431640625E-7, FixUncheckedMicro64.from(-9.5367431640625E-7).toDouble(), delta)
		assertEquals(5.7965173783582514E-5, FixUncheckedMicro64.from(5.7965173783582514E-5).toDouble(), delta)
		assertEquals(-5.7965173783582514E-5, FixUncheckedMicro64.from(-5.7965173783582514E-5).toDouble(), delta)
		assertEquals(0.003084591527158651, FixUncheckedMicro64.from(0.003084591527158651).toDouble(), delta)
		assertEquals(-0.003084591527158651, FixUncheckedMicro64.from(-0.003084591527158651).toDouble(), delta)
		assertEquals(0.009314703658805575, FixUncheckedMicro64.from(0.009314703658805575).toDouble(), delta)
		assertEquals(-0.009314703658805575, FixUncheckedMicro64.from(-0.009314703658805575).toDouble(), delta)
		assertEquals(0.5088086753629321, FixUncheckedMicro64.from(0.5088086753629321).toDouble(), delta)
		assertEquals(-0.5088086753629321, FixUncheckedMicro64.from(-0.5088086753629321).toDouble(), delta)
		assertEquals(14.094133688218953, FixUncheckedMicro64.from(14.094133688218953).toDouble(), delta)
		assertEquals(-14.094133688218953, FixUncheckedMicro64.from(-14.094133688218953).toDouble(), delta)
		assertEquals(55.274475303202365, FixUncheckedMicro64.from(55.274475303202365).toDouble(), delta)
		assertEquals(-55.274475303202365, FixUncheckedMicro64.from(-55.274475303202365).toDouble(), delta)
		assertEquals(488.18110381699654, FixUncheckedMicro64.from(488.18110381699654).toDouble(), delta)
		assertEquals(-488.18110381699654, FixUncheckedMicro64.from(-488.18110381699654).toDouble(), delta)
		assertEquals(19484.523405800377, FixUncheckedMicro64.from(19484.523405800377).toDouble(), delta)
		assertEquals(-19484.523405800377, FixUncheckedMicro64.from(-19484.523405800377).toDouble(), delta)
		assertEquals(978553.2634048663, FixUncheckedMicro64.from(978553.2634048663).toDouble(), delta)
		assertEquals(-978553.2634048663, FixUncheckedMicro64.from(-978553.2634048663).toDouble(), delta)
		assertEquals(1.8665851216571603E7, FixUncheckedMicro64.from(1.8665851216571603E7).toDouble(), delta)
		assertEquals(-1.8665851216571603E7, FixUncheckedMicro64.from(-1.8665851216571603E7).toDouble(), delta)
		assertEquals(2.776676497250265E8, FixUncheckedMicro64.from(2.776676497250265E8).toDouble(), delta)
		assertEquals(-2.776676497250265E8, FixUncheckedMicro64.from(-2.776676497250265E8).toDouble(), delta)
		assertEquals(4.401053941594629E9, FixUncheckedMicro64.from(4.401053941594629E9).toDouble(), delta)
		assertEquals(-4.401053941594629E9, FixUncheckedMicro64.from(-4.401053941594629E9).toDouble(), delta)
		assertEquals(7.18919617871774E9, FixUncheckedMicro64.from(7.18919617871774E9).toDouble(), delta)
		assertEquals(-7.18919617871774E9, FixUncheckedMicro64.from(-7.18919617871774E9).toDouble(), delta)
		assertEquals(3.672864068723384E11, FixUncheckedMicro64.from(3.672864068723384E11).toDouble(), delta)
		assertEquals(-3.672864068723384E11, FixUncheckedMicro64.from(-3.672864068723384E11).toDouble(), delta)
		assertEquals(8.796093022208E12, FixUncheckedMicro64.from(8.796093022208E12).toDouble(), delta)
		assertEquals(-8.796093022208E12, FixUncheckedMicro64.from(-8.796093022208E12).toDouble(), delta)
	}

	@Test
	fun testUnaryMinus() {
		assertEquals(9223372036854775807, -FixUncheckedMicro64.raw(-9223372036854775807).raw)
		assertEquals(386268115287331654, -FixUncheckedMicro64.raw(-386268115287331654).raw)
		assertEquals(84771190261467287, -FixUncheckedMicro64.raw(-84771190261467287).raw)
		assertEquals(4950739029511539, -FixUncheckedMicro64.raw(-4950739029511539).raw)
		assertEquals(49399445901691, -FixUncheckedMicro64.raw(-49399445901691).raw)
		assertEquals(4706486801583, -FixUncheckedMicro64.raw(-4706486801583).raw)
		assertEquals(172834677536, -FixUncheckedMicro64.raw(-172834677536).raw)
		assertEquals(6828972630, -FixUncheckedMicro64.raw(-6828972630).raw)
		assertEquals(134392659, -FixUncheckedMicro64.raw(-134392659).raw)
		assertEquals(4481280, -FixUncheckedMicro64.raw(-4481280).raw)
		assertEquals(221202, -FixUncheckedMicro64.raw(-221202).raw)
		assertEquals(1963, -FixUncheckedMicro64.raw(-1963).raw)
		assertEquals(0, -FixUncheckedMicro64.raw(0).raw)
		assertEquals(-1, -FixUncheckedMicro64.raw(1).raw)
		assertEquals(-4732, -FixUncheckedMicro64.raw(4732).raw)
		assertEquals(-11204, -FixUncheckedMicro64.raw(11204).raw)
		assertEquals(-218793, -FixUncheckedMicro64.raw(218793).raw)
		assertEquals(-8234988, -FixUncheckedMicro64.raw(8234988).raw)
		assertEquals(-838900040, -FixUncheckedMicro64.raw(838900040).raw)
		assertEquals(-11818534200, -FixUncheckedMicro64.raw(11818534200).raw)
		assertEquals(-111327146492, -FixUncheckedMicro64.raw(111327146492).raw)
		assertEquals(-20393277992782, -FixUncheckedMicro64.raw(20393277992782).raw)
		assertEquals(-387856025849162, -FixUncheckedMicro64.raw(387856025849162).raw)
		assertEquals(-3232553098941491, -FixUncheckedMicro64.raw(3232553098941491).raw)
		assertEquals(-310510768390365198, -FixUncheckedMicro64.raw(310510768390365198).raw)
		assertEquals(-9223372036854775807, -FixUncheckedMicro64.raw(9223372036854775807).raw)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixUncheckedMicro64, b: FixUncheckedMicro64, c: FixUncheckedMicro64) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}
		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixUncheckedMicro64.from(a), FixUncheckedMicro64.from(b), FixUncheckedMicro64.from(c))
			assertEquals(FixUncheckedMicro64.from(c), FixUncheckedMicro64.from(a) + b)
			assertEquals(FixUncheckedMicro64.from(c), b + FixUncheckedMicro64.from(a))
			assertEquals(FixUncheckedMicro64.from(a), FixUncheckedMicro64.from(c) - b)
			assertEquals(FixUncheckedMicro64.from(b), c - FixUncheckedMicro64.from(a))
		}
		testValues(FixUncheckedMicro64.raw(Long.MIN_VALUE), FixUncheckedMicro64.ONE, FixUncheckedMicro64.raw(Long.MIN_VALUE + 1048576))
		testValues(-8796093022208, 4250525490550, -4545567531658)
		testValues(-3665265513131, 1572319008528, -2092946504603)
		testValues(-2101957453050, 3013897260411, 911939807361)
		testValues(-113026552393, 3226197953892, 3113171401499)
		testValues(-111811589905, 2893314125241, 2781502535336)
		testValues(-699311300, 2118498714080, 2117799402780)
		testValues(-208668271, 2609185120033, 2608976451762)
		testValues(-53790341, 1262876148796, 1262822358455)
		testValues(-23906501, 2503047446505, 2503023540004)
		testValues(-962916, 1048455272899, 1048454309983)
		testValues(-166757, 3891241525232, 3891241358475)
		testValues(-15396, 440111126222, 440111110826)
		testValues(-2530, 3693199124492, 3693199121962)
		testValues(0, 732641347195, 732641347195)
		testValues(1, 1359795590112, 1359795590113)
		testValues(20659, 1417182229815, 1417182250474)
		testValues(48024, 169667274289, 169667322313)
		testValues(55271, 1205438662370, 1205438717641)
		testValues(113965, 2622382838499, 2622382952464)
		testValues(2669533, 334399710050, 334402379583)
		testValues(3993045, 1410868280762, 1410872273807)
		testValues(110753805, 1019670186869, 1019780940674)
		testValues(163671190, 1757225130972, 1757388802162)
		testValues(3763306405, 2526853041442, 2530616347847)
		testValues(12278006269, 2452424718859, 2464702725128)
		testValues(516127733109, 970745878364, 1486873611473)
		testValues(647585397567, 3731783634366, 4379369031933)
		testValues(4398046511103, 610242518202, 5008289029305)
		testValues(FixUncheckedMicro64.raw(Long.MAX_VALUE - 1048576), FixUncheckedMicro64.ONE, FixUncheckedMicro64.raw(Long.MAX_VALUE))
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixUncheckedMicro64.raw(Long.MAX_VALUE), 1 * FixUncheckedMicro64.raw(Long.MAX_VALUE))
		assertEquals(FixUncheckedMicro64.raw(Long.MAX_VALUE), FixUncheckedMicro64.raw(Long.MAX_VALUE) / 1)
		assertEquals(FixUncheckedMicro64.raw(Long.MIN_VALUE), 1 * FixUncheckedMicro64.raw(Long.MIN_VALUE))
		assertEquals(FixUncheckedMicro64.raw(Long.MIN_VALUE), FixUncheckedMicro64.raw(Long.MIN_VALUE) / 1)
		assertEquals(FixUncheckedMicro64.raw(Long.MIN_VALUE + 1), -1 * FixUncheckedMicro64.raw(Long.MAX_VALUE))
		assertEquals(FixUncheckedMicro64.raw(Long.MIN_VALUE + 1), FixUncheckedMicro64.raw(Long.MAX_VALUE) / -1)

		fun testValues(a: Long, b: Long) {
			assertEquals(FixUncheckedMicro64.from(a * b), FixUncheckedMicro64.from(a) * FixUncheckedMicro64.from(b))
			assertEquals(FixUncheckedMicro64.from(a * b), FixUncheckedMicro64.from(a) * b)
			assertEquals(FixUncheckedMicro64.from(a * b), b * FixUncheckedMicro64.from(a))
			if (b != 0L) assertEquals(FixUncheckedMicro64.from(a), FixUncheckedMicro64.from(a * b) / b)
			if (a != 0L) assertEquals(FixUncheckedMicro64.from(b), FixUncheckedMicro64.from(a * b) / a)
		}
		testValues(-8796093022208, 0)
		testValues(-233656843420, 23)
		testValues(-9119428782, -328)
		testValues(-755474141, 4154)
		testValues(-521310339, 43)
		testValues(-128988175, 1)
		testValues(-69103277, 13474)
		testValues(-10399772, -20537)
		testValues(-484049, -3602089)
		testValues(-309459, 1977)
		testValues(-37181, -801)
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
		testValues(381, -17257)
		testValues(1977, -128988175)
		testValues(4154, 1977)
		testValues(23296, 125464405)
		testValues(25106, -14533912)
		testValues(191087, 2791028)
		testValues(2791028, 1782341)
		testValues(61720171, 101)
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixUncheckedMicro64.ZERO < FixUncheckedMicro64.ONE)
		assertTrue(0 < FixUncheckedMicro64.ONE)
		assertFalse(FixUncheckedMicro64.ZERO > FixUncheckedMicro64.ONE)
		assertFalse(0 > FixUncheckedMicro64.ONE)
		assertFalse(FixUncheckedMicro64.ONE < FixUncheckedMicro64.ONE)
		assertFalse(FixUncheckedMicro64.ONE < 1)
		assertFalse(FixUncheckedMicro64.ONE > FixUncheckedMicro64.ONE)
		assertTrue(FixUncheckedMicro64.ONE <= FixUncheckedMicro64.ONE)
		assertTrue(FixUncheckedMicro64.ONE >= FixUncheckedMicro64.ONE)
		assertTrue(FixUncheckedMicro64.raw(Long.MIN_VALUE) < FixUncheckedMicro64.raw(Long.MAX_VALUE))

		val minDelta = FixUncheckedMicro64.raw(1)
		assertEquals(FixUncheckedMicro64.from(12), FixUncheckedMicro64.from(12))
		assertNotEquals(FixUncheckedMicro64.from(12), FixUncheckedMicro64.from(12) - minDelta)
		assertTrue(FixUncheckedMicro64.from(9.5367431640625E-7) < FixUncheckedMicro64.from(9.5367431640625E-7) + minDelta)
		assertTrue(9.5367431640625E-7 < FixUncheckedMicro64.from(9.5367431640625E-7) + minDelta)
		assertFalse(FixUncheckedMicro64.from(3.911252279924859E-4) < FixUncheckedMicro64.from(3.911252279924859E-4) - minDelta)
		assertFalse(3.911252279924859E-4 < FixUncheckedMicro64.from(3.911252279924859E-4) - minDelta)
		assertTrue(FixUncheckedMicro64.from(3.911252279924859E-4) < FixUncheckedMicro64.from(3.911252279924859E-4) + minDelta)
		assertTrue(3.911252279924859E-4 < FixUncheckedMicro64.from(3.911252279924859E-4) + minDelta)
		assertFalse(FixUncheckedMicro64.from(0.0013926195514347168) < FixUncheckedMicro64.from(0.0013926195514347168) - minDelta)
		assertFalse(0.0013926195514347168 < FixUncheckedMicro64.from(0.0013926195514347168) - minDelta)
		assertTrue(FixUncheckedMicro64.from(0.0013926195514347168) < FixUncheckedMicro64.from(0.0013926195514347168) + minDelta)
		assertTrue(0.0013926195514347168 < FixUncheckedMicro64.from(0.0013926195514347168) + minDelta)
		assertFalse(FixUncheckedMicro64.from(0.007928885234495038) < FixUncheckedMicro64.from(0.007928885234495038) - minDelta)
		assertFalse(0.007928885234495038 < FixUncheckedMicro64.from(0.007928885234495038) - minDelta)
		assertTrue(FixUncheckedMicro64.from(0.007928885234495038) < FixUncheckedMicro64.from(0.007928885234495038) + minDelta)
		assertTrue(0.007928885234495038 < FixUncheckedMicro64.from(0.007928885234495038) + minDelta)
		assertFalse(FixUncheckedMicro64.from(0.060214617813443456) < FixUncheckedMicro64.from(0.060214617813443456) - minDelta)
		assertFalse(0.060214617813443456 < FixUncheckedMicro64.from(0.060214617813443456) - minDelta)
		assertTrue(FixUncheckedMicro64.from(0.060214617813443456) < FixUncheckedMicro64.from(0.060214617813443456) + minDelta)
		assertTrue(0.060214617813443456 < FixUncheckedMicro64.from(0.060214617813443456) + minDelta)
		assertFalse(FixUncheckedMicro64.from(0.18061000195143148) < FixUncheckedMicro64.from(0.18061000195143148) - minDelta)
		assertFalse(0.18061000195143148 < FixUncheckedMicro64.from(0.18061000195143148) - minDelta)
		assertTrue(FixUncheckedMicro64.from(0.18061000195143148) < FixUncheckedMicro64.from(0.18061000195143148) + minDelta)
		assertTrue(0.18061000195143148 < FixUncheckedMicro64.from(0.18061000195143148) + minDelta)
		assertFalse(FixUncheckedMicro64.from(2.6272550762762745) < FixUncheckedMicro64.from(2.6272550762762745) - minDelta)
		assertFalse(2.6272550762762745 < FixUncheckedMicro64.from(2.6272550762762745) - minDelta)
		assertTrue(FixUncheckedMicro64.from(2.6272550762762745) < FixUncheckedMicro64.from(2.6272550762762745) + minDelta)
		assertTrue(2.6272550762762745 < FixUncheckedMicro64.from(2.6272550762762745) + minDelta)
		assertFalse(FixUncheckedMicro64.from(10.160135609533143) < FixUncheckedMicro64.from(10.160135609533143) - minDelta)
		assertFalse(10.160135609533143 < FixUncheckedMicro64.from(10.160135609533143) - minDelta)
		assertTrue(FixUncheckedMicro64.from(10.160135609533143) < FixUncheckedMicro64.from(10.160135609533143) + minDelta)
		assertTrue(10.160135609533143 < FixUncheckedMicro64.from(10.160135609533143) + minDelta)
		assertFalse(FixUncheckedMicro64.from(143.3047592364777) < FixUncheckedMicro64.from(143.3047592364777) - minDelta)
		assertFalse(143.3047592364777 < FixUncheckedMicro64.from(143.3047592364777) - minDelta)
		assertTrue(FixUncheckedMicro64.from(143.3047592364777) < FixUncheckedMicro64.from(143.3047592364777) + minDelta)
		assertTrue(143.3047592364777 < FixUncheckedMicro64.from(143.3047592364777) + minDelta)
		assertFalse(FixUncheckedMicro64.from(3374.7500266128254) < FixUncheckedMicro64.from(3374.7500266128254) - minDelta)
		assertFalse(3374.7500266128254 < FixUncheckedMicro64.from(3374.7500266128254) - minDelta)
		assertTrue(FixUncheckedMicro64.from(3374.7500266128254) < FixUncheckedMicro64.from(3374.7500266128254) + minDelta)
		assertTrue(3374.7500266128254 < FixUncheckedMicro64.from(3374.7500266128254) + minDelta)
		assertFalse(FixUncheckedMicro64.from(12777.6029546566) < FixUncheckedMicro64.from(12777.6029546566) - minDelta)
		assertFalse(12777.6029546566 < FixUncheckedMicro64.from(12777.6029546566) - minDelta)
		assertTrue(FixUncheckedMicro64.from(12777.6029546566) < FixUncheckedMicro64.from(12777.6029546566) + minDelta)
		assertTrue(12777.6029546566 < FixUncheckedMicro64.from(12777.6029546566) + minDelta)
		assertFalse(FixUncheckedMicro64.from(110822.35838203174) < FixUncheckedMicro64.from(110822.35838203174) - minDelta)
		assertFalse(110822.35838203174 < FixUncheckedMicro64.from(110822.35838203174) - minDelta)
		assertTrue(FixUncheckedMicro64.from(110822.35838203174) < FixUncheckedMicro64.from(110822.35838203174) + minDelta)
		assertTrue(110822.35838203174 < FixUncheckedMicro64.from(110822.35838203174) + minDelta)
		assertFalse(FixUncheckedMicro64.from(604124.6329044729) < FixUncheckedMicro64.from(604124.6329044729) - minDelta)
		assertFalse(604124.6329044729 < FixUncheckedMicro64.from(604124.6329044729) - minDelta)
		assertTrue(FixUncheckedMicro64.from(604124.6329044729) < FixUncheckedMicro64.from(604124.6329044729) + minDelta)
		assertTrue(604124.6329044729 < FixUncheckedMicro64.from(604124.6329044729) + minDelta)
		assertFalse(FixUncheckedMicro64.from(1.6000793194106514E7) < FixUncheckedMicro64.from(1.6000793194106514E7) - minDelta)
		assertFalse(1.6000793194106514E7 < FixUncheckedMicro64.from(1.6000793194106514E7) - minDelta)
		assertTrue(FixUncheckedMicro64.from(1.6000793194106514E7) < FixUncheckedMicro64.from(1.6000793194106514E7) + minDelta)
		assertTrue(1.6000793194106514E7 < FixUncheckedMicro64.from(1.6000793194106514E7) + minDelta)
		assertFalse(FixUncheckedMicro64.from(2.3977921959639926E7) < FixUncheckedMicro64.from(2.3977921959639926E7) - minDelta)
		assertFalse(2.3977921959639926E7 < FixUncheckedMicro64.from(2.3977921959639926E7) - minDelta)
		assertTrue(FixUncheckedMicro64.from(2.3977921959639926E7) < FixUncheckedMicro64.from(2.3977921959639926E7) + minDelta)
		assertTrue(2.3977921959639926E7 < FixUncheckedMicro64.from(2.3977921959639926E7) + minDelta)
		assertFalse(FixUncheckedMicro64.from(9.521487864611689E8) < FixUncheckedMicro64.from(9.521487864611689E8) - minDelta)
		assertFalse(9.521487864611689E8 < FixUncheckedMicro64.from(9.521487864611689E8) - minDelta)
		assertTrue(FixUncheckedMicro64.from(9.521487864611689E8) < FixUncheckedMicro64.from(9.521487864611689E8) + minDelta)
		assertTrue(9.521487864611689E8 < FixUncheckedMicro64.from(9.521487864611689E8) + minDelta)
		assertFalse(FixUncheckedMicro64.from(1.5050441079000726E9) < FixUncheckedMicro64.from(1.5050441079000726E9) - minDelta)
		assertFalse(1.5050441079000726E9 < FixUncheckedMicro64.from(1.5050441079000726E9) - minDelta)
		assertTrue(FixUncheckedMicro64.from(1.5050441079000726E9) < FixUncheckedMicro64.from(1.5050441079000726E9) + minDelta)
		assertTrue(1.5050441079000726E9 < FixUncheckedMicro64.from(1.5050441079000726E9) + minDelta)
		assertFalse(FixUncheckedMicro64.from(5.3789076402765045E10) < FixUncheckedMicro64.from(5.3789076402765045E10) - minDelta)
		assertFalse(5.3789076402765045E10 < FixUncheckedMicro64.from(5.3789076402765045E10) - minDelta)
		assertTrue(FixUncheckedMicro64.from(5.3789076402765045E10) < FixUncheckedMicro64.from(5.3789076402765045E10) + minDelta)
		assertTrue(5.3789076402765045E10 < FixUncheckedMicro64.from(5.3789076402765045E10) + minDelta)
		assertFalse(FixUncheckedMicro64.from(6.900496279981952E10) < FixUncheckedMicro64.from(6.900496279981952E10) - minDelta)
		assertFalse(6.900496279981952E10 < FixUncheckedMicro64.from(6.900496279981952E10) - minDelta)
		assertTrue(FixUncheckedMicro64.from(6.900496279981952E10) < FixUncheckedMicro64.from(6.900496279981952E10) + minDelta)
		assertTrue(6.900496279981952E10 < FixUncheckedMicro64.from(6.900496279981952E10) + minDelta)
		assertFalse(FixUncheckedMicro64.from(1.415336468990056E12) < FixUncheckedMicro64.from(1.415336468990056E12) - minDelta)
		assertFalse(1.415336468990056E12 < FixUncheckedMicro64.from(1.415336468990056E12) - minDelta)
		assertTrue(FixUncheckedMicro64.from(1.415336468990056E12) < FixUncheckedMicro64.from(1.415336468990056E12) + minDelta)
		assertTrue(1.415336468990056E12 < FixUncheckedMicro64.from(1.415336468990056E12) + minDelta)
		assertFalse(FixUncheckedMicro64.from(3.5566628266165547E12) < FixUncheckedMicro64.from(3.5566628266165547E12) - minDelta)
		assertFalse(3.5566628266165547E12 < FixUncheckedMicro64.from(3.5566628266165547E12) - minDelta)
		assertTrue(FixUncheckedMicro64.from(3.5566628266165547E12) < FixUncheckedMicro64.from(3.5566628266165547E12) + minDelta)
		assertTrue(3.5566628266165547E12 < FixUncheckedMicro64.from(3.5566628266165547E12) + minDelta)
		assertFalse(FixUncheckedMicro64.from(8.796093022208E12) < FixUncheckedMicro64.from(8.796093022208E12) - minDelta)
		assertFalse(8.796093022208E12 < FixUncheckedMicro64.from(8.796093022208E12) - minDelta)
	}
}