package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixMicro64U {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixMicro64U.from(value).toInt())
		testValue(0)
		testValue(1)
		testValue(1042)
		testValue(2299)
		testValue(27518)
		testValue(134221)
		testValue(9763729)
		testValue(55681182)
		testValue(2147483647)
	}

	@Test
	fun testLongConversion() {
		fun testValue(value: Long) = assertEquals(value, FixMicro64U.from(value).toLong())
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
	}

	@Test
	fun testFloatConversion() {
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
	}

	@Test
	fun testDoubleConversion() {
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
		testValues(0, 4250525201524, 4250525201524)
		testValues(1, 1572318993297, 1572318993298)
		testValues(10117, 7411942889936, 7411942900053)
		testValues(66285, 3226197163462, 3226197229747)
		testValues(113321, 2893313220199, 2893313333520)
		testValues(153631, 6516544959997, 6516545113628)
		testValues(5394758, 7007231189724, 7007236584482)
		testValues(6243102, 5660921831307, 5660928074409)
		testValues(233265371, 6901093181789, 6901326447160)
		testValues(234625011, 1048454354665, 1048688979676)
		testValues(671394804, 3891241045956, 3891912440760)
		testValues(7327972687, 4838156837950, 4845484810637)
		testValues(35152253539, 8091245289848, 8126397543387)
		testValues(696692891188, 732640427031, 1429333318219)
		testValues(8796093022207, 1359795396451, 10155888418658)
		testValues(FixMicro64U.raw(ULong.MAX_VALUE - 1048576u), FixMicro64U.ONE, FixMicro64U.raw(ULong.MAX_VALUE))
	}

	@Test
	fun testMultiplication() {
		assertEquals(FixMicro64U.raw(ULong.MAX_VALUE), 1 * FixMicro64U.raw(ULong.MAX_VALUE))
		assertThrows(FixedPointException::class.java) { -1 * FixMicro64U.ONE }
		assertThrows(FixedPointException::class.java) { -1 * FixMicro64U.raw(ULong.MAX_VALUE)}

		fun testValues(a: Long, b: Long) {
			assertEquals(FixMicro64U.from(a * b), FixMicro64U.from(a) * FixMicro64U.from(b))
			assertEquals(FixMicro64U.from(a * b), FixMicro64U.from(a) * b)
			assertEquals(FixMicro64U.from(a * b), b * FixMicro64U.from(a))
		}
		testValues(0, 301591)
		testValues(1, 231)
		testValues(42, 364448964103)
		testValues(134, 71711606273)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(165) * 17592186044415 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(231) * 17592186044415 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1493) * 2644929701422 }
		testValues(1881, 0)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(4505) * 1222091827510 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(11152) * 1222091827510 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(11484) * 71711606273 }
		testValues(21231, 39192496)
		testValues(252026, 1389655)
		testValues(301591, 11152)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1389655) * 500434653899 }
		testValues(1970917, 4505)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(6075644) * 1474163511 }
		testValues(13371719, 11152)
		testValues(13620980, 0)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(32234314) * 364448964103 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(39192496) * 1389655 }
		testValues(329768868, 1)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1474163511) * 17592186044415 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(2854741395) * 12211872545 }
		testValues(5116806831, 1493)
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(12211872545) * 13620980 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(44002482883) * 1474163511 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(71711606273) * 1222091827510 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(364448964103) * 11152 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(500434653899) * 4505 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(1222091827510) * 2644929701422 }
		assertThrows(FixedPointException::class.java) { FixMicro64U.from(2644929701422) * 1493 }
		testValues(17592186044415, 0)
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixMicro64U.ZERO < FixMicro64U.ONE)
		assertFalse(FixMicro64U.ZERO > FixMicro64U.ONE)
		assertFalse(FixMicro64U.ONE < FixMicro64U.ONE)
		assertFalse(FixMicro64U.ONE > FixMicro64U.ONE)
		assertTrue(FixMicro64U.ONE <= FixMicro64U.ONE)
		assertTrue(FixMicro64U.ONE >= FixMicro64U.ONE)
		assertTrue(FixMicro64U.raw(ULong.MIN_VALUE) < FixMicro64U.raw(ULong.MAX_VALUE))

		val minDelta = FixMicro64U.raw(1u)
		assertEquals(FixMicro64U.from(12), FixMicro64U.from(12))
		assertNotEquals(FixMicro64U.from(12), FixMicro64U.from(12) - minDelta)
		assertTrue(FixMicro64U.from(9.5367431640625E-7) < FixMicro64U.from(9.5367431640625E-7) + minDelta)
		assertFalse(FixMicro64U.from(3.911252279924859E-4) < FixMicro64U.from(3.911252279924859E-4) - minDelta)
		assertTrue(FixMicro64U.from(3.911252279924859E-4) < FixMicro64U.from(3.911252279924859E-4) + minDelta)
		assertFalse(FixMicro64U.from(0.0013926195514347168) < FixMicro64U.from(0.0013926195514347168) - minDelta)
		assertTrue(FixMicro64U.from(0.0013926195514347168) < FixMicro64U.from(0.0013926195514347168) + minDelta)
		assertFalse(FixMicro64U.from(0.007928885234495038) < FixMicro64U.from(0.007928885234495038) - minDelta)
		assertTrue(FixMicro64U.from(0.007928885234495038) < FixMicro64U.from(0.007928885234495038) + minDelta)
		assertFalse(FixMicro64U.from(0.060214617813443456) < FixMicro64U.from(0.060214617813443456) - minDelta)
		assertTrue(FixMicro64U.from(0.060214617813443456) < FixMicro64U.from(0.060214617813443456) + minDelta)
		assertFalse(FixMicro64U.from(0.18061000195143148) < FixMicro64U.from(0.18061000195143148) - minDelta)
		assertTrue(FixMicro64U.from(0.18061000195143148) < FixMicro64U.from(0.18061000195143148) + minDelta)
		assertFalse(FixMicro64U.from(2.6272550762762745) < FixMicro64U.from(2.6272550762762745) - minDelta)
		assertTrue(FixMicro64U.from(2.6272550762762745) < FixMicro64U.from(2.6272550762762745) + minDelta)
		assertFalse(FixMicro64U.from(10.160135609533143) < FixMicro64U.from(10.160135609533143) - minDelta)
		assertTrue(FixMicro64U.from(10.160135609533143) < FixMicro64U.from(10.160135609533143) + minDelta)
		assertFalse(FixMicro64U.from(143.3047592364777) < FixMicro64U.from(143.3047592364777) - minDelta)
		assertTrue(FixMicro64U.from(143.3047592364777) < FixMicro64U.from(143.3047592364777) + minDelta)
		assertFalse(FixMicro64U.from(3374.7500266128254) < FixMicro64U.from(3374.7500266128254) - minDelta)
		assertTrue(FixMicro64U.from(3374.7500266128254) < FixMicro64U.from(3374.7500266128254) + minDelta)
		assertFalse(FixMicro64U.from(12777.6029546566) < FixMicro64U.from(12777.6029546566) - minDelta)
		assertTrue(FixMicro64U.from(12777.6029546566) < FixMicro64U.from(12777.6029546566) + minDelta)
		assertFalse(FixMicro64U.from(110822.35838203174) < FixMicro64U.from(110822.35838203174) - minDelta)
		assertTrue(FixMicro64U.from(110822.35838203174) < FixMicro64U.from(110822.35838203174) + minDelta)
		assertFalse(FixMicro64U.from(604124.6329044729) < FixMicro64U.from(604124.6329044729) - minDelta)
		assertTrue(FixMicro64U.from(604124.6329044729) < FixMicro64U.from(604124.6329044729) + minDelta)
		assertFalse(FixMicro64U.from(1.6000793194106514E7) < FixMicro64U.from(1.6000793194106514E7) - minDelta)
		assertTrue(FixMicro64U.from(1.6000793194106514E7) < FixMicro64U.from(1.6000793194106514E7) + minDelta)
		assertFalse(FixMicro64U.from(2.3977921959639926E7) < FixMicro64U.from(2.3977921959639926E7) - minDelta)
		assertTrue(FixMicro64U.from(2.3977921959639926E7) < FixMicro64U.from(2.3977921959639926E7) + minDelta)
		assertFalse(FixMicro64U.from(9.521487864611689E8) < FixMicro64U.from(9.521487864611689E8) - minDelta)
		assertTrue(FixMicro64U.from(9.521487864611689E8) < FixMicro64U.from(9.521487864611689E8) + minDelta)
		assertFalse(FixMicro64U.from(1.5050441079000726E9) < FixMicro64U.from(1.5050441079000726E9) - minDelta)
		assertTrue(FixMicro64U.from(1.5050441079000726E9) < FixMicro64U.from(1.5050441079000726E9) + minDelta)
		assertFalse(FixMicro64U.from(5.3789076402765045E10) < FixMicro64U.from(5.3789076402765045E10) - minDelta)
		assertTrue(FixMicro64U.from(5.3789076402765045E10) < FixMicro64U.from(5.3789076402765045E10) + minDelta)
		assertFalse(FixMicro64U.from(6.900496279981952E10) < FixMicro64U.from(6.900496279981952E10) - minDelta)
		assertTrue(FixMicro64U.from(6.900496279981952E10) < FixMicro64U.from(6.900496279981952E10) + minDelta)
		assertFalse(FixMicro64U.from(1.415336468990056E12) < FixMicro64U.from(1.415336468990056E12) - minDelta)
		assertTrue(FixMicro64U.from(1.415336468990056E12) < FixMicro64U.from(1.415336468990056E12) + minDelta)
		assertFalse(FixMicro64U.from(3.5566628266165547E12) < FixMicro64U.from(3.5566628266165547E12) - minDelta)
		assertTrue(FixMicro64U.from(3.5566628266165547E12) < FixMicro64U.from(3.5566628266165547E12) + minDelta)
		assertFalse(FixMicro64U.from(1.7592186044416E13) < FixMicro64U.from(1.7592186044416E13) - minDelta)
	}
}
