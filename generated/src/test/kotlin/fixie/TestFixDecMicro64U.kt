package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixDecMicro64U {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixDecMicro64U.from(value).toInt())
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
		fun testValue(value: Long) = assertEquals(value, FixDecMicro64U.from(value).toLong())
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
		testValue(18446744073709)
	}

	@Test
	fun testFloatConversion() {
		val delta = 2.0E-6f
		assertEquals(1.0E-6f, FixDecMicro64U.from(1.0E-6f).toFloat(), delta)
		assertEquals(6.078089E-5f, FixDecMicro64U.from(6.078089E-5f).toFloat(), delta)
		assertEquals(0.0032344286f, FixDecMicro64U.from(0.0032344286f).toFloat(), delta)
		assertEquals(0.009767175f, FixDecMicro64U.from(0.009767175f).toFloat(), delta)
		assertEquals(0.5335246f, FixDecMicro64U.from(0.5335246f).toFloat(), delta)
		assertEquals(14.77877f, FixDecMicro64U.from(14.77877f).toFloat(), delta)
		assertEquals(57.959488f, FixDecMicro64U.from(57.959488f).toFloat(), delta)
		assertEquals(511.895f, FixDecMicro64U.from(511.895f).toFloat(), delta)
		assertEquals(20431.004f, FixDecMicro64U.from(20431.004f).toFloat(), delta)
		assertEquals(1026087.44f, FixDecMicro64U.from(1026087.44f).toFloat(), delta)
		assertEquals(1.9572564E7f, FixDecMicro64U.from(1.9572564E7f).toFloat(), delta)
		assertEquals(2.91155648E8f, FixDecMicro64U.from(2.91155648E8f).toFloat(), delta)
		assertEquals(4.6148393E9f, FixDecMicro64U.from(4.6148393E9f).toFloat(), delta)
		assertEquals(7.5384187E9f, FixDecMicro64U.from(7.5384187E9f).toFloat(), delta)
		assertEquals(3.85127711E11f, FixDecMicro64U.from(3.85127711E11f).toFloat(), delta)
		assertEquals(1.8446744E13f, FixDecMicro64U.from(1.8446744E13f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 2.0E-6
		assertEquals(1.0E-6, FixDecMicro64U.from(1.0E-6).toDouble(), delta)
		assertEquals(6.0780890065293825E-5, FixDecMicro64U.from(6.0780890065293825E-5).toDouble(), delta)
		assertEquals(0.0032344286451819098, FixDecMicro64U.from(0.0032344286451819098).toDouble(), delta)
		assertEquals(0.009767174703735716, FixDecMicro64U.from(0.009767174703735716).toDouble(), delta)
		assertEquals(0.5335245655773619, FixDecMicro64U.from(0.5335245655773619).toDouble(), delta)
		assertEquals(14.778770326257877, FixDecMicro64U.from(14.778770326257877).toDouble(), delta)
		assertEquals(57.95948821553071, FixDecMicro64U.from(57.95948821553071).toDouble(), delta)
		assertEquals(511.894989116011, FixDecMicro64U.from(511.894989116011).toDouble(), delta)
		assertEquals(20431.003614760535, FixDecMicro64U.from(20431.003614760535).toDouble(), delta)
		assertEquals(1026087.466728021, FixDecMicro64U.from(1026087.466728021).toDouble(), delta)
		assertEquals(1.9572563605267785E7, FixDecMicro64U.from(1.9572563605267785E7).toDouble(), delta)
		assertEquals(2.9115563347806937E8, FixDecMicro64U.from(2.9115563347806937E8).toDouble(), delta)
		assertEquals(4.614839537861529E9, FixDecMicro64U.from(4.614839537861529E9).toDouble(), delta)
		assertEquals(7.538418572295134E9, FixDecMicro64U.from(7.538418572295134E9).toDouble(), delta)
		assertEquals(3.8512771137256915E11, FixDecMicro64U.from(3.8512771137256915E11).toDouble(), delta)
		assertEquals(1.844674407370955E13, FixDecMicro64U.from(1.844674407370955E13).toDouble(), delta)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixDecMicro64U, b: FixDecMicro64U, c: FixDecMicro64U) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}
		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixDecMicro64U.from(a), FixDecMicro64U.from(b), FixDecMicro64U.from(c))
			assertEquals(FixDecMicro64U.from(c), FixDecMicro64U.from(a) + b)
			assertEquals(FixDecMicro64U.from(c), b + FixDecMicro64U.from(a))
			assertEquals(FixDecMicro64U.from(a), FixDecMicro64U.from(c) - b)
			assertEquals(FixDecMicro64U.from(b), c - FixDecMicro64U.from(a))
		}
		testValues(FixDecMicro64U.raw(ULong.MIN_VALUE), FixDecMicro64U.ONE, FixDecMicro64U.raw(ULong.MIN_VALUE + 1000000u))
		testValues(0, 1234239275908, 1234239275908)
		testValues(1, 5386304923764, 5386304923765)
		testValues(10117, 923381497476, 923381507593)
		testValues(66285, 1288523218170, 1288523284455)
		testValues(113321, 5758728247683, 5758728361004)
		testValues(153631, 7229239532045, 7229239685676)
		testValues(5394758, 434227418752, 434232813510)
		testValues(6243102, 4823252085073, 4823258328175)
		testValues(233265371, 3714233293656, 3714466559027)
		testValues(234625011, 2729422676719, 2729657301730)
		testValues(671394804, 5843551359746, 5844222754550)
		testValues(7327972687, 8589092169653, 8596420142340)
		testValues(35152253539, 8403682424057, 8438834677596)
		testValues(696692891188, 7868593797235, 8565286688423)
		testValues(9223372036854, 6172454497872, 15395826534726)
		testValues(FixDecMicro64U.raw(ULong.MAX_VALUE - 1000000u), FixDecMicro64U.ONE, FixDecMicro64U.raw(ULong.MAX_VALUE))
	}

	@Test
	fun testMultiplication() {
		assertEquals(FixDecMicro64U.raw(ULong.MAX_VALUE), 1 * FixDecMicro64U.raw(ULong.MAX_VALUE))
		assertThrows(FixedPointException::class.java) { -1 * FixDecMicro64U.ONE }
		assertThrows(FixedPointException::class.java) { -1 * FixDecMicro64U.raw(ULong.MAX_VALUE)}

		fun testValues(a: Long, b: Long) {
			assertEquals(FixDecMicro64U.from(a * b), FixDecMicro64U.from(a) * FixDecMicro64U.from(b))
			assertEquals(FixDecMicro64U.from(a * b), FixDecMicro64U.from(a) * b)
			assertEquals(FixDecMicro64U.from(a * b), b * FixDecMicro64U.from(a))
		}
		testValues(0, 1389655)
		testValues(1, 1389655)
		testValues(42, 165)
		testValues(134, 44002482883)
		testValues(165, 39192496)
		testValues(231, 1493)
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1493) * 1222091827510 }
		testValues(1881, 39192496)
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(4505) * 12211872545 }
		testValues(11152, 39192496)
		testValues(11484, 1)
		testValues(21231, 329768868)
		testValues(252026, 32234314)
		testValues(301591, 231)
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1389655) * 364448964103 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1970917) * 12211872545 }
		testValues(6075644, 0)
		testValues(13371719, 11152)
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(13620980) * 1222091827510 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(32234314) * 1970917 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(39192496) * 1389655 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(329768868) * 44002482883 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1474163511) * 2854741395 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(2854741395) * 301591 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(5116806831) * 252026 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(12211872545) * 1881 }
		testValues(44002482883, 165)
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(71711606273) * 71711606273 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(364448964103) * 5116806831 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(500434653899) * 1493 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1222091827510) * 11152 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(2644929701422) * 1970917 }
		testValues(18446744073709, 0)
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixDecMicro64U.ZERO < FixDecMicro64U.ONE)
		assertFalse(FixDecMicro64U.ZERO > FixDecMicro64U.ONE)
		assertFalse(FixDecMicro64U.ONE < FixDecMicro64U.ONE)
		assertFalse(FixDecMicro64U.ONE > FixDecMicro64U.ONE)
		assertTrue(FixDecMicro64U.ONE <= FixDecMicro64U.ONE)
		assertTrue(FixDecMicro64U.ONE >= FixDecMicro64U.ONE)
		assertTrue(FixDecMicro64U.raw(ULong.MIN_VALUE) < FixDecMicro64U.raw(ULong.MAX_VALUE))

		val minDelta = FixDecMicro64U.raw(1u)
		assertEquals(FixDecMicro64U.from(12), FixDecMicro64U.from(12))
		assertNotEquals(FixDecMicro64U.from(12), FixDecMicro64U.from(12) - minDelta)
		assertTrue(FixDecMicro64U.from(1.0E-6) < FixDecMicro64U.from(1.0E-6) + minDelta)
		assertFalse(FixDecMicro64U.from(4.101245270674489E-4) < FixDecMicro64U.from(4.101245270674489E-4) - minDelta)
		assertTrue(FixDecMicro64U.from(4.101245270674489E-4) < FixDecMicro64U.from(4.101245270674489E-4) + minDelta)
		assertFalse(FixDecMicro64U.from(0.0014602674387652094) < FixDecMicro64U.from(0.0014602674387652094) - minDelta)
		assertTrue(FixDecMicro64U.from(0.0014602674387652094) < FixDecMicro64U.from(0.0014602674387652094) + minDelta)
		assertFalse(FixDecMicro64U.from(0.008314038763645868) < FixDecMicro64U.from(0.008314038763645868) - minDelta)
		assertTrue(FixDecMicro64U.from(0.008314038763645868) < FixDecMicro64U.from(0.008314038763645868) + minDelta)
		assertFalse(FixDecMicro64U.from(0.06313960308834927) < FixDecMicro64U.from(0.06313960308834927) - minDelta)
		assertTrue(FixDecMicro64U.from(0.06313960308834927) < FixDecMicro64U.from(0.06313960308834927) + minDelta)
		assertFalse(FixDecMicro64U.from(0.18938331340622416) < FixDecMicro64U.from(0.18938331340622416) - minDelta)
		assertTrue(FixDecMicro64U.from(0.18938331340622416) < FixDecMicro64U.from(0.18938331340622416) + minDelta)
		assertFalse(FixDecMicro64U.from(2.75487661886147) < FixDecMicro64U.from(2.75487661886147) - minDelta)
		assertTrue(FixDecMicro64U.from(2.75487661886147) < FixDecMicro64U.from(2.75487661886147) + minDelta)
		assertFalse(FixDecMicro64U.from(10.653674356901822) < FixDecMicro64U.from(10.653674356901822) - minDelta)
		assertTrue(FixDecMicro64U.from(10.653674356901822) < FixDecMicro64U.from(10.653674356901822) + minDelta)
		assertFalse(FixDecMicro64U.from(150.2659312211488) < FixDecMicro64U.from(150.2659312211488) - minDelta)
		assertTrue(FixDecMicro64U.from(150.2659312211488) < FixDecMicro64U.from(150.2659312211488) + minDelta)
		assertFalse(FixDecMicro64U.from(3538.6818839055695) < FixDecMicro64U.from(3538.6818839055695) - minDelta)
		assertTrue(FixDecMicro64U.from(3538.6818839055695) < FixDecMicro64U.from(3538.6818839055695) + minDelta)
		assertFalse(FixDecMicro64U.from(13398.287795781996) < FixDecMicro64U.from(13398.287795781996) - minDelta)
		assertTrue(FixDecMicro64U.from(13398.287795781996) < FixDecMicro64U.from(13398.287795781996) + minDelta)
		assertFalse(FixDecMicro64U.from(116205.6652627973) < FixDecMicro64U.from(116205.6652627973) - minDelta)
		assertTrue(FixDecMicro64U.from(116205.6652627973) < FixDecMicro64U.from(116205.6652627973) + minDelta)
		assertFalse(FixDecMicro64U.from(633470.5910724404) < FixDecMicro64U.from(633470.5910724404) - minDelta)
		assertTrue(FixDecMicro64U.from(633470.5910724404) < FixDecMicro64U.from(633470.5910724404) + minDelta)
		assertFalse(FixDecMicro64U.from(1.677804772430343E7) < FixDecMicro64U.from(1.677804772430343E7) - minDelta)
		assertTrue(FixDecMicro64U.from(1.677804772430343E7) < FixDecMicro64U.from(1.677804772430343E7) + minDelta)
		assertFalse(FixDecMicro64U.from(2.514267349675139E7) < FixDecMicro64U.from(2.514267349675139E7) - minDelta)
		assertTrue(FixDecMicro64U.from(2.514267349675139E7) < FixDecMicro64U.from(2.514267349675139E7) + minDelta)
		assertFalse(FixDecMicro64U.from(9.984003659123064E8) < FixDecMicro64U.from(9.984003659123064E8) - minDelta)
		assertTrue(FixDecMicro64U.from(9.984003659123064E8) < FixDecMicro64U.from(9.984003659123064E8) + minDelta)
		assertFalse(FixDecMicro64U.from(1.5781531304854264E9) < FixDecMicro64U.from(1.5781531304854264E9) - minDelta)
		assertTrue(FixDecMicro64U.from(1.5781531304854264E9) < FixDecMicro64U.from(1.5781531304854264E9) + minDelta)
		assertFalse(FixDecMicro64U.from(5.640193457810576E10) < FixDecMicro64U.from(5.640193457810576E10) - minDelta)
		assertTrue(FixDecMicro64U.from(5.640193457810576E10) < FixDecMicro64U.from(5.640193457810576E10) + minDelta)
		assertFalse(FixDecMicro64U.from(7.235694787278354E10) < FixDecMicro64U.from(7.235694787278354E10) - minDelta)
		assertTrue(FixDecMicro64U.from(7.235694787278354E10) < FixDecMicro64U.from(7.235694787278354E10) + minDelta)
		assertFalse(FixDecMicro64U.from(1.4840878533077163E12) < FixDecMicro64U.from(1.4840878533077163E12) - minDelta)
		assertTrue(FixDecMicro64U.from(1.4840878533077163E12) < FixDecMicro64U.from(1.4840878533077163E12) + minDelta)
		assertFalse(FixDecMicro64U.from(3.729431280082279E12) < FixDecMicro64U.from(3.729431280082279E12) - minDelta)
		assertTrue(FixDecMicro64U.from(3.729431280082279E12) < FixDecMicro64U.from(3.729431280082279E12) + minDelta)
		assertFalse(FixDecMicro64U.from(1.844674407370955E13) < FixDecMicro64U.from(1.844674407370955E13) - minDelta)
	}
}
