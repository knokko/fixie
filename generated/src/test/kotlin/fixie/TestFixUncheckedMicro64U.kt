package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixUncheckedMicro64U {

	fun assertEquals(a: FixUncheckedMicro64U, b: FixUncheckedMicro64U, maxDelta: FixUncheckedMicro64U) {
		val rawDifference = a.raw.toLong() - b.raw.toLong()
		if (rawDifference.absoluteValue > maxDelta.raw.toLong()) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixUncheckedMicro64U.ZERO.toString())
		assertEquals("1", FixUncheckedMicro64U.ONE.toString())
		assertTrue((FixUncheckedMicro64U.ONE / 3).toString().startsWith("0.33"))
		assertTrue((FixUncheckedMicro64U.from(17592186044414) + FixUncheckedMicro64U.ONE / 3).toString().endsWith((FixUncheckedMicro64U.ONE / 3).toString().substring(1)))
		assertEquals("0.0625", (FixUncheckedMicro64U.ONE / 16).toString())
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixUncheckedMicro64U.ONE, FixUncheckedMicro64U.from(one))

		fun testValue(value: Int) = assertEquals(value, FixUncheckedMicro64U.from(value).toInt())
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
		val one = 1L
		assertEquals(FixUncheckedMicro64U.ONE, FixUncheckedMicro64U.from(one))

		fun testValue(value: Long) = assertEquals(value, FixUncheckedMicro64U.from(value).toLong())
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
		assertEquals(FixUncheckedMicro64U.ONE, FixUncheckedMicro64U.from(1f))
		val delta = 1.9073486E-6f
		assertEquals(9.536743E-7f, FixUncheckedMicro64U.from(9.536743E-7f).toFloat(), delta)
		assertEquals(5.7965175E-5f, FixUncheckedMicro64U.from(5.7965175E-5f).toFloat(), delta)
		assertEquals(0.0030845916f, FixUncheckedMicro64U.from(0.0030845916f).toFloat(), delta)
		assertEquals(0.009314704f, FixUncheckedMicro64U.from(0.009314704f).toFloat(), delta)
		assertEquals(0.5088087f, FixUncheckedMicro64U.from(0.5088087f).toFloat(), delta)
		assertEquals(14.094133f, FixUncheckedMicro64U.from(14.094133f).toFloat(), delta)
		assertEquals(55.274475f, FixUncheckedMicro64U.from(55.274475f).toFloat(), delta)
		assertEquals(488.1811f, FixUncheckedMicro64U.from(488.1811f).toFloat(), delta)
		assertEquals(19484.523f, FixUncheckedMicro64U.from(19484.523f).toFloat(), delta)
		assertEquals(978553.25f, FixUncheckedMicro64U.from(978553.25f).toFloat(), delta)
		assertEquals(1.8665852E7f, FixUncheckedMicro64U.from(1.8665852E7f).toFloat(), delta)
		assertEquals(2.77667648E8f, FixUncheckedMicro64U.from(2.77667648E8f).toFloat(), delta)
		assertEquals(4.4010537E9f, FixUncheckedMicro64U.from(4.4010537E9f).toFloat(), delta)
		assertEquals(7.1891963E9f, FixUncheckedMicro64U.from(7.1891963E9f).toFloat(), delta)
		assertEquals(3.67286419E11f, FixUncheckedMicro64U.from(3.67286419E11f).toFloat(), delta)
		assertEquals(1.7592186E13f, FixUncheckedMicro64U.from(1.7592186E13f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixUncheckedMicro64U.ONE, FixUncheckedMicro64U.from(1.0))
		val delta = 1.9073486328125E-6
		assertEquals(9.5367431640625E-7, FixUncheckedMicro64U.from(9.5367431640625E-7).toDouble(), delta)
		assertEquals(5.7965173783582514E-5, FixUncheckedMicro64U.from(5.7965173783582514E-5).toDouble(), delta)
		assertEquals(0.003084591527158651, FixUncheckedMicro64U.from(0.003084591527158651).toDouble(), delta)
		assertEquals(0.009314703658805575, FixUncheckedMicro64U.from(0.009314703658805575).toDouble(), delta)
		assertEquals(0.5088086753629321, FixUncheckedMicro64U.from(0.5088086753629321).toDouble(), delta)
		assertEquals(14.094133688218953, FixUncheckedMicro64U.from(14.094133688218953).toDouble(), delta)
		assertEquals(55.274475303202365, FixUncheckedMicro64U.from(55.274475303202365).toDouble(), delta)
		assertEquals(488.18110381699654, FixUncheckedMicro64U.from(488.18110381699654).toDouble(), delta)
		assertEquals(19484.523405800377, FixUncheckedMicro64U.from(19484.523405800377).toDouble(), delta)
		assertEquals(978553.2634048663, FixUncheckedMicro64U.from(978553.2634048663).toDouble(), delta)
		assertEquals(1.8665851216571603E7, FixUncheckedMicro64U.from(1.8665851216571603E7).toDouble(), delta)
		assertEquals(2.776676497250265E8, FixUncheckedMicro64U.from(2.776676497250265E8).toDouble(), delta)
		assertEquals(4.401053941594629E9, FixUncheckedMicro64U.from(4.401053941594629E9).toDouble(), delta)
		assertEquals(7.18919617871774E9, FixUncheckedMicro64U.from(7.18919617871774E9).toDouble(), delta)
		assertEquals(3.672864068723384E11, FixUncheckedMicro64U.from(3.672864068723384E11).toDouble(), delta)
		assertEquals(1.7592186044416E13, FixUncheckedMicro64U.from(1.7592186044416E13).toDouble(), delta)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixUncheckedMicro64U, b: FixUncheckedMicro64U, c: FixUncheckedMicro64U) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}

		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixUncheckedMicro64U.from(a), FixUncheckedMicro64U.from(b), FixUncheckedMicro64U.from(c))
			assertEquals(FixUncheckedMicro64U.from(c), FixUncheckedMicro64U.from(a) + b)
			assertEquals(FixUncheckedMicro64U.from(c), b + FixUncheckedMicro64U.from(a))
			assertEquals(FixUncheckedMicro64U.from(a), FixUncheckedMicro64U.from(c) - b)
			assertEquals(FixUncheckedMicro64U.from(b), c - FixUncheckedMicro64U.from(a))
		}

		testValues(FixUncheckedMicro64U.raw(ULong.MIN_VALUE), FixUncheckedMicro64U.ONE, FixUncheckedMicro64U.raw(ULong.MIN_VALUE + 1048576u))
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
		testValues(FixUncheckedMicro64U.raw(ULong.MAX_VALUE - 1048576u), FixUncheckedMicro64U.ONE, FixUncheckedMicro64U.raw(ULong.MAX_VALUE))
		assertEquals(FixUncheckedMicro64U.raw(18446744073708100036u), FixUncheckedMicro64U.raw(645572u) + 17592186044414)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixUncheckedMicro64U.raw(ULong.MAX_VALUE), 1 * FixUncheckedMicro64U.raw(ULong.MAX_VALUE))
		assertEquals(FixUncheckedMicro64U.raw(ULong.MAX_VALUE), FixUncheckedMicro64U.raw(ULong.MAX_VALUE) / 1)

		fun testValues(a: Long, b: Long) {
			assertEquals(FixUncheckedMicro64U.from(a * b), FixUncheckedMicro64U.from(a) * FixUncheckedMicro64U.from(b))
			assertEquals(FixUncheckedMicro64U.from(a * b), FixUncheckedMicro64U.from(a) * b)
			assertEquals(FixUncheckedMicro64U.from(a * b), b * FixUncheckedMicro64U.from(a))
			if (b != 0L) assertEquals(FixUncheckedMicro64U.from(a), FixUncheckedMicro64U.from(a * b) / b)
			if (a != 0L) assertEquals(FixUncheckedMicro64U.from(b), FixUncheckedMicro64U.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixUncheckedMicro64U.from(b), FixUncheckedMicro64U.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixUncheckedMicro64U.from(a * b), FixUncheckedMicro64U.from(a) * b.toInt())
				assertEquals(FixUncheckedMicro64U.from(a * b), b.toInt() * FixUncheckedMicro64U.from(a))
			}
		}
		testValues(0, 71711606273)
		testValues(1, 231)
		testValues(42, 0)
		testValues(134, 21231)
		testValues(1493, 1474163511)
		testValues(1881, 13620980)
		testValues(11152, 329768868)
		testValues(21231, 1970917)
		testValues(252026, 252026)
		testValues(301591, 134)
		testValues(1970917, 165)
		testValues(13371719, 4505)
		testValues(13620980, 1493)
		testValues(39192496, 1881)
		testValues(329768868, 11152)
		testValues(5116806831, 231)
		assertEquals(FixUncheckedMicro64U.raw(796336u), (FixUncheckedMicro64U.raw(796336u) * FixUncheckedMicro64U.raw(691445u)) / FixUncheckedMicro64U.raw(691445u), FixUncheckedMicro64U.raw(10485u))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixUncheckedMicro64U.ZERO < FixUncheckedMicro64U.ONE)
		assertTrue(0 < FixUncheckedMicro64U.ONE)
		assertFalse(FixUncheckedMicro64U.ZERO > FixUncheckedMicro64U.ONE)
		assertFalse(0 > FixUncheckedMicro64U.ONE)
		assertFalse(FixUncheckedMicro64U.ONE < FixUncheckedMicro64U.ONE)
		assertFalse(FixUncheckedMicro64U.ONE < 1)
		assertFalse(FixUncheckedMicro64U.ONE > FixUncheckedMicro64U.ONE)
		assertTrue(FixUncheckedMicro64U.ONE <= FixUncheckedMicro64U.ONE)
		assertTrue(FixUncheckedMicro64U.ONE >= FixUncheckedMicro64U.ONE)
		assertTrue(FixUncheckedMicro64U.raw(ULong.MIN_VALUE) < FixUncheckedMicro64U.raw(ULong.MAX_VALUE))

		val minDelta = FixUncheckedMicro64U.raw(1u)
		assertEquals(FixUncheckedMicro64U.from(12), FixUncheckedMicro64U.from(12))
		assertNotEquals(FixUncheckedMicro64U.from(12), FixUncheckedMicro64U.from(12) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(9.5367431640625E-7) < FixUncheckedMicro64U.from(9.5367431640625E-7) + minDelta)
		assertTrue(9.5367431640625E-7 < FixUncheckedMicro64U.from(9.5367431640625E-7) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(3.911252279924859E-4) < FixUncheckedMicro64U.from(3.911252279924859E-4) - minDelta)
		assertFalse(3.911252279924859E-4 < FixUncheckedMicro64U.from(3.911252279924859E-4) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(3.911252279924859E-4) < FixUncheckedMicro64U.from(3.911252279924859E-4) + minDelta)
		assertTrue(3.911252279924859E-4 < FixUncheckedMicro64U.from(3.911252279924859E-4) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(0.0013926195514347168) < FixUncheckedMicro64U.from(0.0013926195514347168) - minDelta)
		assertFalse(0.0013926195514347168 < FixUncheckedMicro64U.from(0.0013926195514347168) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(0.0013926195514347168) < FixUncheckedMicro64U.from(0.0013926195514347168) + minDelta)
		assertTrue(0.0013926195514347168 < FixUncheckedMicro64U.from(0.0013926195514347168) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(0.007928885234495038) < FixUncheckedMicro64U.from(0.007928885234495038) - minDelta)
		assertFalse(0.007928885234495038 < FixUncheckedMicro64U.from(0.007928885234495038) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(0.007928885234495038) < FixUncheckedMicro64U.from(0.007928885234495038) + minDelta)
		assertTrue(0.007928885234495038 < FixUncheckedMicro64U.from(0.007928885234495038) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(0.060214617813443456) < FixUncheckedMicro64U.from(0.060214617813443456) - minDelta)
		assertFalse(0.060214617813443456 < FixUncheckedMicro64U.from(0.060214617813443456) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(0.060214617813443456) < FixUncheckedMicro64U.from(0.060214617813443456) + minDelta)
		assertTrue(0.060214617813443456 < FixUncheckedMicro64U.from(0.060214617813443456) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(0.18061000195143148) < FixUncheckedMicro64U.from(0.18061000195143148) - minDelta)
		assertFalse(0.18061000195143148 < FixUncheckedMicro64U.from(0.18061000195143148) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(0.18061000195143148) < FixUncheckedMicro64U.from(0.18061000195143148) + minDelta)
		assertTrue(0.18061000195143148 < FixUncheckedMicro64U.from(0.18061000195143148) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(2.6272550762762745) < FixUncheckedMicro64U.from(2.6272550762762745) - minDelta)
		assertFalse(2.6272550762762745 < FixUncheckedMicro64U.from(2.6272550762762745) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(2.6272550762762745) < FixUncheckedMicro64U.from(2.6272550762762745) + minDelta)
		assertTrue(2.6272550762762745 < FixUncheckedMicro64U.from(2.6272550762762745) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(10.160135609533143) < FixUncheckedMicro64U.from(10.160135609533143) - minDelta)
		assertFalse(10.160135609533143 < FixUncheckedMicro64U.from(10.160135609533143) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(10.160135609533143) < FixUncheckedMicro64U.from(10.160135609533143) + minDelta)
		assertTrue(10.160135609533143 < FixUncheckedMicro64U.from(10.160135609533143) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(143.3047592364777) < FixUncheckedMicro64U.from(143.3047592364777) - minDelta)
		assertFalse(143.3047592364777 < FixUncheckedMicro64U.from(143.3047592364777) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(143.3047592364777) < FixUncheckedMicro64U.from(143.3047592364777) + minDelta)
		assertTrue(143.3047592364777 < FixUncheckedMicro64U.from(143.3047592364777) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(3374.7500266128254) < FixUncheckedMicro64U.from(3374.7500266128254) - minDelta)
		assertFalse(3374.7500266128254 < FixUncheckedMicro64U.from(3374.7500266128254) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(3374.7500266128254) < FixUncheckedMicro64U.from(3374.7500266128254) + minDelta)
		assertTrue(3374.7500266128254 < FixUncheckedMicro64U.from(3374.7500266128254) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(12777.6029546566) < FixUncheckedMicro64U.from(12777.6029546566) - minDelta)
		assertFalse(12777.6029546566 < FixUncheckedMicro64U.from(12777.6029546566) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(12777.6029546566) < FixUncheckedMicro64U.from(12777.6029546566) + minDelta)
		assertTrue(12777.6029546566 < FixUncheckedMicro64U.from(12777.6029546566) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(110822.35838203174) < FixUncheckedMicro64U.from(110822.35838203174) - minDelta)
		assertFalse(110822.35838203174 < FixUncheckedMicro64U.from(110822.35838203174) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(110822.35838203174) < FixUncheckedMicro64U.from(110822.35838203174) + minDelta)
		assertTrue(110822.35838203174 < FixUncheckedMicro64U.from(110822.35838203174) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(604124.6329044729) < FixUncheckedMicro64U.from(604124.6329044729) - minDelta)
		assertFalse(604124.6329044729 < FixUncheckedMicro64U.from(604124.6329044729) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(604124.6329044729) < FixUncheckedMicro64U.from(604124.6329044729) + minDelta)
		assertTrue(604124.6329044729 < FixUncheckedMicro64U.from(604124.6329044729) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(1.6000793194106514E7) < FixUncheckedMicro64U.from(1.6000793194106514E7) - minDelta)
		assertFalse(1.6000793194106514E7 < FixUncheckedMicro64U.from(1.6000793194106514E7) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(1.6000793194106514E7) < FixUncheckedMicro64U.from(1.6000793194106514E7) + minDelta)
		assertTrue(1.6000793194106514E7 < FixUncheckedMicro64U.from(1.6000793194106514E7) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(2.3977921959639926E7) < FixUncheckedMicro64U.from(2.3977921959639926E7) - minDelta)
		assertFalse(2.3977921959639926E7 < FixUncheckedMicro64U.from(2.3977921959639926E7) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(2.3977921959639926E7) < FixUncheckedMicro64U.from(2.3977921959639926E7) + minDelta)
		assertTrue(2.3977921959639926E7 < FixUncheckedMicro64U.from(2.3977921959639926E7) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(9.521487864611689E8) < FixUncheckedMicro64U.from(9.521487864611689E8) - minDelta)
		assertFalse(9.521487864611689E8 < FixUncheckedMicro64U.from(9.521487864611689E8) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(9.521487864611689E8) < FixUncheckedMicro64U.from(9.521487864611689E8) + minDelta)
		assertTrue(9.521487864611689E8 < FixUncheckedMicro64U.from(9.521487864611689E8) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(1.5050441079000726E9) < FixUncheckedMicro64U.from(1.5050441079000726E9) - minDelta)
		assertFalse(1.5050441079000726E9 < FixUncheckedMicro64U.from(1.5050441079000726E9) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(1.5050441079000726E9) < FixUncheckedMicro64U.from(1.5050441079000726E9) + minDelta)
		assertTrue(1.5050441079000726E9 < FixUncheckedMicro64U.from(1.5050441079000726E9) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(5.3789076402765045E10) < FixUncheckedMicro64U.from(5.3789076402765045E10) - minDelta)
		assertFalse(5.3789076402765045E10 < FixUncheckedMicro64U.from(5.3789076402765045E10) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(5.3789076402765045E10) < FixUncheckedMicro64U.from(5.3789076402765045E10) + minDelta)
		assertTrue(5.3789076402765045E10 < FixUncheckedMicro64U.from(5.3789076402765045E10) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(6.900496279981952E10) < FixUncheckedMicro64U.from(6.900496279981952E10) - minDelta)
		assertFalse(6.900496279981952E10 < FixUncheckedMicro64U.from(6.900496279981952E10) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(6.900496279981952E10) < FixUncheckedMicro64U.from(6.900496279981952E10) + minDelta)
		assertTrue(6.900496279981952E10 < FixUncheckedMicro64U.from(6.900496279981952E10) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(1.415336468990056E12) < FixUncheckedMicro64U.from(1.415336468990056E12) - minDelta)
		assertFalse(1.415336468990056E12 < FixUncheckedMicro64U.from(1.415336468990056E12) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(1.415336468990056E12) < FixUncheckedMicro64U.from(1.415336468990056E12) + minDelta)
		assertTrue(1.415336468990056E12 < FixUncheckedMicro64U.from(1.415336468990056E12) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(3.5566628266165547E12) < FixUncheckedMicro64U.from(3.5566628266165547E12) - minDelta)
		assertFalse(3.5566628266165547E12 < FixUncheckedMicro64U.from(3.5566628266165547E12) - minDelta)
		assertTrue(FixUncheckedMicro64U.from(3.5566628266165547E12) < FixUncheckedMicro64U.from(3.5566628266165547E12) + minDelta)
		assertTrue(3.5566628266165547E12 < FixUncheckedMicro64U.from(3.5566628266165547E12) + minDelta)
		assertFalse(FixUncheckedMicro64U.from(1.7592186044416E13) < FixUncheckedMicro64U.from(1.7592186044416E13) - minDelta)
		assertFalse(1.7592186044416E13 < FixUncheckedMicro64U.from(1.7592186044416E13) - minDelta)
		assertTrue(FixUncheckedMicro64U.raw(ULong.MAX_VALUE) >= 17592186044415)
		assertTrue(FixUncheckedMicro64U.raw(ULong.MAX_VALUE) > 17592186044415)
		assertTrue(FixUncheckedMicro64U.raw(ULong.MAX_VALUE) < 17592186044416)
		assertTrue(FixUncheckedMicro64U.raw(ULong.MAX_VALUE) < 1.7609778230460414E13)
		assertTrue(FixUncheckedMicro64U.raw(ULong.MAX_VALUE) < ULong.MAX_VALUE)
		assertTrue(FixUncheckedMicro64U.raw(ULong.MAX_VALUE) < ULong.MAX_VALUE.toFloat())
		assertTrue(FixUncheckedMicro64U.raw(ULong.MAX_VALUE) < ULong.MAX_VALUE.toDouble())
		assertTrue(FixUncheckedMicro64U.ZERO > -1)
		assertTrue(FixUncheckedMicro64U.ZERO > -0.001f)
		assertTrue(FixUncheckedMicro64U.ZERO > -0.001)
		assertTrue(FixUncheckedMicro64U.ZERO > Long.MIN_VALUE)
		assertTrue(FixUncheckedMicro64U.ZERO > Long.MIN_VALUE.toFloat())
		assertTrue(FixUncheckedMicro64U.ZERO > Long.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixUncheckedMicro64U.Array(2) { FixUncheckedMicro64U.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixUncheckedMicro64U.ONE, testArray[0])
		assertEquals(FixUncheckedMicro64U.ONE, testArray[1])
		testArray[1] = FixUncheckedMicro64U.ZERO
		assertEquals(FixUncheckedMicro64U.ONE, testArray[0])
		assertEquals(FixUncheckedMicro64U.ZERO, testArray[1])
		testArray.fill(FixUncheckedMicro64U.ZERO)
		assertEquals(FixUncheckedMicro64U.ZERO, testArray[0])
		assertEquals(FixUncheckedMicro64U.ZERO, testArray[1])
	}

	@Test
	fun testMinMax() {
		assertEquals(FixUncheckedMicro64U.ZERO, min(FixUncheckedMicro64U.ZERO, FixUncheckedMicro64U.ZERO))
		assertEquals(FixUncheckedMicro64U.ZERO, max(FixUncheckedMicro64U.ZERO, FixUncheckedMicro64U.ZERO))
		assertEquals(FixUncheckedMicro64U.ZERO, min(FixUncheckedMicro64U.ONE, FixUncheckedMicro64U.ZERO))
		assertEquals(FixUncheckedMicro64U.ONE, max(FixUncheckedMicro64U.ONE, FixUncheckedMicro64U.ZERO))
		assertEquals(FixUncheckedMicro64U.ZERO, min(FixUncheckedMicro64U.ZERO, FixUncheckedMicro64U.ONE))
		assertEquals(FixUncheckedMicro64U.ONE, max(FixUncheckedMicro64U.ZERO, FixUncheckedMicro64U.ONE))
		assertEquals(FixUncheckedMicro64U.ZERO, min(FixUncheckedMicro64U.ZERO, FixUncheckedMicro64U.raw(ULong.MAX_VALUE)))
		assertEquals(FixUncheckedMicro64U.raw(ULong.MAX_VALUE), max(FixUncheckedMicro64U.ZERO, FixUncheckedMicro64U.raw(ULong.MAX_VALUE)))
	}
}
