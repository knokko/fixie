package fixie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixDecMicro64 {

	@Test
	fun testIntConversion() {
		fun testValue(value: Int) = assertEquals(value, FixDecMicro64.from(value).toInt())
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
		fun testValue(value: Long) = assertEquals(value, FixDecMicro64.from(value).toLong())
		testValue(-9223372036854)
		testValue(-4256030801964)
		testValue(-334863196910)
		testValue(-28090131491)
		testValue(-1150945376)
		testValue(-29959655)
		testValue(-476556)
		testValue(-145265)
		testValue(-5487)
		testValue(-1251)
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
		testValue(9223372036854)
	}

	@Test
	fun testFloatConversion() {
		val delta = 2.0E-6f
		assertEquals(1.0E-6f, FixDecMicro64.from(1.0E-6f).toFloat(), delta)
		assertEquals(-1.0E-6f, FixDecMicro64.from(-1.0E-6f).toFloat(), delta)
		assertEquals(6.078089E-5f, FixDecMicro64.from(6.078089E-5f).toFloat(), delta)
		assertEquals(-6.078089E-5f, FixDecMicro64.from(-6.078089E-5f).toFloat(), delta)
		assertEquals(0.0032344286f, FixDecMicro64.from(0.0032344286f).toFloat(), delta)
		assertEquals(-0.0032344286f, FixDecMicro64.from(-0.0032344286f).toFloat(), delta)
		assertEquals(0.009767175f, FixDecMicro64.from(0.009767175f).toFloat(), delta)
		assertEquals(-0.009767175f, FixDecMicro64.from(-0.009767175f).toFloat(), delta)
		assertEquals(0.5335246f, FixDecMicro64.from(0.5335246f).toFloat(), delta)
		assertEquals(-0.5335246f, FixDecMicro64.from(-0.5335246f).toFloat(), delta)
		assertEquals(14.77877f, FixDecMicro64.from(14.77877f).toFloat(), delta)
		assertEquals(-14.77877f, FixDecMicro64.from(-14.77877f).toFloat(), delta)
		assertEquals(57.959488f, FixDecMicro64.from(57.959488f).toFloat(), delta)
		assertEquals(-57.959488f, FixDecMicro64.from(-57.959488f).toFloat(), delta)
		assertEquals(511.895f, FixDecMicro64.from(511.895f).toFloat(), delta)
		assertEquals(-511.895f, FixDecMicro64.from(-511.895f).toFloat(), delta)
		assertEquals(20431.004f, FixDecMicro64.from(20431.004f).toFloat(), delta)
		assertEquals(-20431.004f, FixDecMicro64.from(-20431.004f).toFloat(), delta)
		assertEquals(1026087.44f, FixDecMicro64.from(1026087.44f).toFloat(), delta)
		assertEquals(-1026087.44f, FixDecMicro64.from(-1026087.44f).toFloat(), delta)
		assertEquals(1.9572564E7f, FixDecMicro64.from(1.9572564E7f).toFloat(), delta)
		assertEquals(-1.9572564E7f, FixDecMicro64.from(-1.9572564E7f).toFloat(), delta)
		assertEquals(2.91155648E8f, FixDecMicro64.from(2.91155648E8f).toFloat(), delta)
		assertEquals(-2.91155648E8f, FixDecMicro64.from(-2.91155648E8f).toFloat(), delta)
		assertEquals(4.6148393E9f, FixDecMicro64.from(4.6148393E9f).toFloat(), delta)
		assertEquals(-4.6148393E9f, FixDecMicro64.from(-4.6148393E9f).toFloat(), delta)
		assertEquals(7.5384187E9f, FixDecMicro64.from(7.5384187E9f).toFloat(), delta)
		assertEquals(-7.5384187E9f, FixDecMicro64.from(-7.5384187E9f).toFloat(), delta)
		assertEquals(3.85127711E11f, FixDecMicro64.from(3.85127711E11f).toFloat(), delta)
		assertEquals(-3.85127711E11f, FixDecMicro64.from(-3.85127711E11f).toFloat(), delta)
		assertEquals(9.223372E12f, FixDecMicro64.from(9.223372E12f).toFloat(), delta)
		assertEquals(-9.223372E12f, FixDecMicro64.from(-9.223372E12f).toFloat(), delta)
	}

	@Test
	fun testDoubleConversion() {
		val delta = 2.0E-6
		assertEquals(1.0E-6, FixDecMicro64.from(1.0E-6).toDouble(), delta)
		assertEquals(-1.0E-6, FixDecMicro64.from(-1.0E-6).toDouble(), delta)
		assertEquals(6.0780890065293825E-5, FixDecMicro64.from(6.0780890065293825E-5).toDouble(), delta)
		assertEquals(-6.0780890065293825E-5, FixDecMicro64.from(-6.0780890065293825E-5).toDouble(), delta)
		assertEquals(0.0032344286451819098, FixDecMicro64.from(0.0032344286451819098).toDouble(), delta)
		assertEquals(-0.0032344286451819098, FixDecMicro64.from(-0.0032344286451819098).toDouble(), delta)
		assertEquals(0.009767174703735716, FixDecMicro64.from(0.009767174703735716).toDouble(), delta)
		assertEquals(-0.009767174703735716, FixDecMicro64.from(-0.009767174703735716).toDouble(), delta)
		assertEquals(0.5335245655773619, FixDecMicro64.from(0.5335245655773619).toDouble(), delta)
		assertEquals(-0.5335245655773619, FixDecMicro64.from(-0.5335245655773619).toDouble(), delta)
		assertEquals(14.778770326257877, FixDecMicro64.from(14.778770326257877).toDouble(), delta)
		assertEquals(-14.778770326257877, FixDecMicro64.from(-14.778770326257877).toDouble(), delta)
		assertEquals(57.95948821553071, FixDecMicro64.from(57.95948821553071).toDouble(), delta)
		assertEquals(-57.95948821553071, FixDecMicro64.from(-57.95948821553071).toDouble(), delta)
		assertEquals(511.894989116011, FixDecMicro64.from(511.894989116011).toDouble(), delta)
		assertEquals(-511.894989116011, FixDecMicro64.from(-511.894989116011).toDouble(), delta)
		assertEquals(20431.003614760535, FixDecMicro64.from(20431.003614760535).toDouble(), delta)
		assertEquals(-20431.003614760535, FixDecMicro64.from(-20431.003614760535).toDouble(), delta)
		assertEquals(1026087.466728021, FixDecMicro64.from(1026087.466728021).toDouble(), delta)
		assertEquals(-1026087.466728021, FixDecMicro64.from(-1026087.466728021).toDouble(), delta)
		assertEquals(1.9572563605267785E7, FixDecMicro64.from(1.9572563605267785E7).toDouble(), delta)
		assertEquals(-1.9572563605267785E7, FixDecMicro64.from(-1.9572563605267785E7).toDouble(), delta)
		assertEquals(2.9115563347806937E8, FixDecMicro64.from(2.9115563347806937E8).toDouble(), delta)
		assertEquals(-2.9115563347806937E8, FixDecMicro64.from(-2.9115563347806937E8).toDouble(), delta)
		assertEquals(4.614839537861529E9, FixDecMicro64.from(4.614839537861529E9).toDouble(), delta)
		assertEquals(-4.614839537861529E9, FixDecMicro64.from(-4.614839537861529E9).toDouble(), delta)
		assertEquals(7.538418572295134E9, FixDecMicro64.from(7.538418572295134E9).toDouble(), delta)
		assertEquals(-7.538418572295134E9, FixDecMicro64.from(-7.538418572295134E9).toDouble(), delta)
		assertEquals(3.8512771137256915E11, FixDecMicro64.from(3.8512771137256915E11).toDouble(), delta)
		assertEquals(-3.8512771137256915E11, FixDecMicro64.from(-3.8512771137256915E11).toDouble(), delta)
		assertEquals(9.223372036854775E12, FixDecMicro64.from(9.223372036854775E12).toDouble(), delta)
		assertEquals(-9.223372036854775E12, FixDecMicro64.from(-9.223372036854775E12).toDouble(), delta)
	}

	@Test
	fun testUnaryMinus() {
		assertThrows(FixedPointException::class.java) { -FixDecMicro64.raw(Long.MIN_VALUE) }
		assertEquals(9223372036854775807, -FixDecMicro64.raw(-9223372036854775807).raw)
		assertEquals(386268115287331654, -FixDecMicro64.raw(-386268115287331654).raw)
		assertEquals(84771190261467287, -FixDecMicro64.raw(-84771190261467287).raw)
		assertEquals(4950739029511539, -FixDecMicro64.raw(-4950739029511539).raw)
		assertEquals(49399445901691, -FixDecMicro64.raw(-49399445901691).raw)
		assertEquals(4706486801583, -FixDecMicro64.raw(-4706486801583).raw)
		assertEquals(172834677536, -FixDecMicro64.raw(-172834677536).raw)
		assertEquals(6828972630, -FixDecMicro64.raw(-6828972630).raw)
		assertEquals(134392659, -FixDecMicro64.raw(-134392659).raw)
		assertEquals(4481280, -FixDecMicro64.raw(-4481280).raw)
		assertEquals(221202, -FixDecMicro64.raw(-221202).raw)
		assertEquals(1963, -FixDecMicro64.raw(-1963).raw)
		assertEquals(0, -FixDecMicro64.raw(0).raw)
		assertEquals(-1, -FixDecMicro64.raw(1).raw)
		assertEquals(-4732, -FixDecMicro64.raw(4732).raw)
		assertEquals(-11204, -FixDecMicro64.raw(11204).raw)
		assertEquals(-218793, -FixDecMicro64.raw(218793).raw)
		assertEquals(-8234988, -FixDecMicro64.raw(8234988).raw)
		assertEquals(-838900040, -FixDecMicro64.raw(838900040).raw)
		assertEquals(-11818534200, -FixDecMicro64.raw(11818534200).raw)
		assertEquals(-111327146492, -FixDecMicro64.raw(111327146492).raw)
		assertEquals(-20393277992782, -FixDecMicro64.raw(20393277992782).raw)
		assertEquals(-387856025849162, -FixDecMicro64.raw(387856025849162).raw)
		assertEquals(-3232553098941491, -FixDecMicro64.raw(3232553098941491).raw)
		assertEquals(-310510768390365198, -FixDecMicro64.raw(310510768390365198).raw)
		assertEquals(-9223372036854775807, -FixDecMicro64.raw(9223372036854775807).raw)
	}

	@Test
	fun testAdditionAndSubtraction() {
		fun testValues(a: FixDecMicro64, b: FixDecMicro64, c: FixDecMicro64) {
			assertEquals(c, a + b)
			assertEquals(c, b + a)
			assertEquals(a, c - b)
			assertEquals(b, c - a)
		}
		fun testValues(a: Long, b: Long, c: Long) {
			testValues(FixDecMicro64.from(a), FixDecMicro64.from(b), FixDecMicro64.from(c))
			assertEquals(FixDecMicro64.from(c), FixDecMicro64.from(a) + b)
			assertEquals(FixDecMicro64.from(c), b + FixDecMicro64.from(a))
			assertEquals(FixDecMicro64.from(a), FixDecMicro64.from(c) - b)
			assertEquals(FixDecMicro64.from(b), c - FixDecMicro64.from(a))
		}
		testValues(FixDecMicro64.raw(Long.MIN_VALUE), FixDecMicro64.ONE, FixDecMicro64.raw(Long.MIN_VALUE + 1000000))
		testValues(-9223372036854, 1234239275908, -7989132760946)
		testValues(-5146039990603, 774618905337, -4371421085266)
		testValues(-2604345002012, 923381497476, -1680963504536)
		testValues(-64032653446, 1288523218170, 1224490564724)
		testValues(-37507758948, 1147042229256, 1109534470308)
		testValues(-1986664297, 2617553513618, 2615566849321)
		testValues(-509977914, 434227418752, 433717440838)
		testValues(-57487580, 211566066646, 211508579066)
		testValues(-44937610, 3714233293656, 3714188356046)
		testValues(-551323, 2729422676719, 2729422125396)
		testValues(-157586, 1231865341319, 1231865183733)
		testValues(-25505, 3977406151226, 3977406125721)
		testValues(-25017, 3791996405630, 3791996380613)
		testValues(0, 3256907778808, 3256907778808)
		testValues(1, 1560768479445, 1560768479446)
		testValues(1493, 4407572300208, 4407572301701)
		testValues(21718, 2497465094359, 2497465116077)
		testValues(42474, 4500665872508, 4500665914982)
		testValues(121049, 3680165503749, 3680165624798)
		testValues(2882947, 1995946154117, 1995949037064)
		testValues(3525157, 2950817651054, 2950821176211)
		testValues(43536660, 4461827489981, 4461871026641)
		testValues(197435715, 3084097355088, 3084294790803)
		testValues(6570574123, 187795652104, 194366226227)
		testValues(6776387713, 1405055015224, 1411831402937)
		testValues(13376411293, 1341447290672, 1354823701965)
		testValues(640384610823, 844412146125, 1484796756948)
		testValues(4611686018427, 1221605894121, 5833291912548)
		testValues(FixDecMicro64.raw(Long.MAX_VALUE - 1000000), FixDecMicro64.ONE, FixDecMicro64.raw(Long.MAX_VALUE))
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixDecMicro64.raw(Long.MAX_VALUE), 1 * FixDecMicro64.raw(Long.MAX_VALUE))
		assertEquals(FixDecMicro64.raw(Long.MAX_VALUE), FixDecMicro64.raw(Long.MAX_VALUE) / 1)
		assertEquals(FixDecMicro64.raw(Long.MIN_VALUE), 1 * FixDecMicro64.raw(Long.MIN_VALUE))
		assertEquals(FixDecMicro64.raw(Long.MIN_VALUE), FixDecMicro64.raw(Long.MIN_VALUE) / 1)
		assertEquals(FixDecMicro64.raw(Long.MIN_VALUE + 1), -1 * FixDecMicro64.raw(Long.MAX_VALUE))
		assertEquals(FixDecMicro64.raw(Long.MIN_VALUE + 1), FixDecMicro64.raw(Long.MAX_VALUE) / -1)
		assertThrows(FixedPointException::class.java) { -1 * FixDecMicro64.raw(Long.MIN_VALUE) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.raw(Long.MIN_VALUE) / -1 }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixDecMicro64.from(a * b), FixDecMicro64.from(a) * FixDecMicro64.from(b))
			assertEquals(FixDecMicro64.from(a * b), FixDecMicro64.from(a) * b)
			assertEquals(FixDecMicro64.from(a * b), b * FixDecMicro64.from(a))
			if (b != 0L) assertEquals(FixDecMicro64.from(a), FixDecMicro64.from(a * b) / b)
			if (a != 0L) assertEquals(FixDecMicro64.from(b), FixDecMicro64.from(a * b) / a)
		}
		testValues(-9223372036854, 0)
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-5059045360529) * 228136 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-3140930909013) * -52056635328 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-911319367812) * -2528868325 }
		testValues(-609868891566, 12)
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-136839608708) * -9829268785 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-52056635328) * 4576916 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-9829268785) * 5686 }
		testValues(-4560337848, -451)
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-2528868325) * 6130620204 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-1366906173) * -19856954 }
		testValues(-585805685, 5686)
		testValues(-88370421, 38)
		testValues(-81756778, 1)
		testValues(-51198987, 20438)
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-19856954) * 243980368 }
		testValues(-6383639, -62676)
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-1339836) * 25732843 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-622138) * -911319367812 }
		testValues(-574320, -1339836)
		testValues(-89960, 1969)
		testValues(-70434, -468)
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-62676) * 2040591839720 }
		testValues(-18139, -89960)
		testValues(-2937, -622138)
		testValues(-1190, 42)
		testValues(-468, -89960)
		testValues(-451, 5686)
		testValues(-395, 42)
		testValues(-81, 32830)
		testValues(-6, -574320)
		testValues(0, -2937)
		testValues(1, -52056635328)
		testValues(12, 12)
		testValues(38, -89960)
		testValues(42, -622138)
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(65) * 2211791693185 }
		testValues(544, -18139)
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(1096) * 76129392498 }
		testValues(1969, -81756778)
		testValues(5686, 1969)
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(20438) * -911319367812 }
		testValues(32830, 243980368)
		testValues(228136, -19856954)
		testValues(251783, 4576916)
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(1082069) * 9223372036854 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(1625155) * 369381973 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(3279758) * -609868891566 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(4576916) * 3279758 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(25732843) * 2040591839720 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(38783000) * -9829268785 }
		testValues(200720101, 65)
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(243980368) * 278055773 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(278055773) * 4576916 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(369381973) * -911319367812 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(6130620204) * -9223372036854 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(10588374717) * 1625155 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(50275406242) * -5059045360529 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(66942017193) * -585805685 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(76129392498) * -3140930909013 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(481844066680) * -88370421 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(2040591839720) * 2040591839720 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(2211791693185) * 1969 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(9223372036854) * 1625155 }
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixDecMicro64.ZERO < FixDecMicro64.ONE)
		assertTrue(0 < FixDecMicro64.ONE)
		assertFalse(FixDecMicro64.ZERO > FixDecMicro64.ONE)
		assertFalse(0 > FixDecMicro64.ONE)
		assertFalse(FixDecMicro64.ONE < FixDecMicro64.ONE)
		assertFalse(FixDecMicro64.ONE < 1)
		assertFalse(FixDecMicro64.ONE > FixDecMicro64.ONE)
		assertTrue(FixDecMicro64.ONE <= FixDecMicro64.ONE)
		assertTrue(FixDecMicro64.ONE >= FixDecMicro64.ONE)
		assertTrue(FixDecMicro64.raw(Long.MIN_VALUE) < FixDecMicro64.raw(Long.MAX_VALUE))

		val minDelta = FixDecMicro64.raw(1)
		assertEquals(FixDecMicro64.from(12), FixDecMicro64.from(12))
		assertNotEquals(FixDecMicro64.from(12), FixDecMicro64.from(12) - minDelta)
		assertTrue(FixDecMicro64.from(1.0E-6) < FixDecMicro64.from(1.0E-6) + minDelta)
		assertTrue(1.0E-6 < FixDecMicro64.from(1.0E-6) + minDelta)
		assertFalse(FixDecMicro64.from(4.101245270674489E-4) < FixDecMicro64.from(4.101245270674489E-4) - minDelta)
		assertFalse(4.101245270674489E-4 < FixDecMicro64.from(4.101245270674489E-4) - minDelta)
		assertTrue(FixDecMicro64.from(4.101245270674489E-4) < FixDecMicro64.from(4.101245270674489E-4) + minDelta)
		assertTrue(4.101245270674489E-4 < FixDecMicro64.from(4.101245270674489E-4) + minDelta)
		assertFalse(FixDecMicro64.from(0.0014602674387652094) < FixDecMicro64.from(0.0014602674387652094) - minDelta)
		assertFalse(0.0014602674387652094 < FixDecMicro64.from(0.0014602674387652094) - minDelta)
		assertTrue(FixDecMicro64.from(0.0014602674387652094) < FixDecMicro64.from(0.0014602674387652094) + minDelta)
		assertTrue(0.0014602674387652094 < FixDecMicro64.from(0.0014602674387652094) + minDelta)
		assertFalse(FixDecMicro64.from(0.008314038763645868) < FixDecMicro64.from(0.008314038763645868) - minDelta)
		assertFalse(0.008314038763645868 < FixDecMicro64.from(0.008314038763645868) - minDelta)
		assertTrue(FixDecMicro64.from(0.008314038763645868) < FixDecMicro64.from(0.008314038763645868) + minDelta)
		assertTrue(0.008314038763645868 < FixDecMicro64.from(0.008314038763645868) + minDelta)
		assertFalse(FixDecMicro64.from(0.06313960308834927) < FixDecMicro64.from(0.06313960308834927) - minDelta)
		assertFalse(0.06313960308834927 < FixDecMicro64.from(0.06313960308834927) - minDelta)
		assertTrue(FixDecMicro64.from(0.06313960308834927) < FixDecMicro64.from(0.06313960308834927) + minDelta)
		assertTrue(0.06313960308834927 < FixDecMicro64.from(0.06313960308834927) + minDelta)
		assertFalse(FixDecMicro64.from(0.18938331340622416) < FixDecMicro64.from(0.18938331340622416) - minDelta)
		assertFalse(0.18938331340622416 < FixDecMicro64.from(0.18938331340622416) - minDelta)
		assertTrue(FixDecMicro64.from(0.18938331340622416) < FixDecMicro64.from(0.18938331340622416) + minDelta)
		assertTrue(0.18938331340622416 < FixDecMicro64.from(0.18938331340622416) + minDelta)
		assertFalse(FixDecMicro64.from(2.75487661886147) < FixDecMicro64.from(2.75487661886147) - minDelta)
		assertFalse(2.75487661886147 < FixDecMicro64.from(2.75487661886147) - minDelta)
		assertTrue(FixDecMicro64.from(2.75487661886147) < FixDecMicro64.from(2.75487661886147) + minDelta)
		assertTrue(2.75487661886147 < FixDecMicro64.from(2.75487661886147) + minDelta)
		assertFalse(FixDecMicro64.from(10.653674356901822) < FixDecMicro64.from(10.653674356901822) - minDelta)
		assertFalse(10.653674356901822 < FixDecMicro64.from(10.653674356901822) - minDelta)
		assertTrue(FixDecMicro64.from(10.653674356901822) < FixDecMicro64.from(10.653674356901822) + minDelta)
		assertTrue(10.653674356901822 < FixDecMicro64.from(10.653674356901822) + minDelta)
		assertFalse(FixDecMicro64.from(150.2659312211488) < FixDecMicro64.from(150.2659312211488) - minDelta)
		assertFalse(150.2659312211488 < FixDecMicro64.from(150.2659312211488) - minDelta)
		assertTrue(FixDecMicro64.from(150.2659312211488) < FixDecMicro64.from(150.2659312211488) + minDelta)
		assertTrue(150.2659312211488 < FixDecMicro64.from(150.2659312211488) + minDelta)
		assertFalse(FixDecMicro64.from(3538.6818839055695) < FixDecMicro64.from(3538.6818839055695) - minDelta)
		assertFalse(3538.6818839055695 < FixDecMicro64.from(3538.6818839055695) - minDelta)
		assertTrue(FixDecMicro64.from(3538.6818839055695) < FixDecMicro64.from(3538.6818839055695) + minDelta)
		assertTrue(3538.6818839055695 < FixDecMicro64.from(3538.6818839055695) + minDelta)
		assertFalse(FixDecMicro64.from(13398.287795781996) < FixDecMicro64.from(13398.287795781996) - minDelta)
		assertFalse(13398.287795781996 < FixDecMicro64.from(13398.287795781996) - minDelta)
		assertTrue(FixDecMicro64.from(13398.287795781996) < FixDecMicro64.from(13398.287795781996) + minDelta)
		assertTrue(13398.287795781996 < FixDecMicro64.from(13398.287795781996) + minDelta)
		assertFalse(FixDecMicro64.from(116205.6652627973) < FixDecMicro64.from(116205.6652627973) - minDelta)
		assertFalse(116205.6652627973 < FixDecMicro64.from(116205.6652627973) - minDelta)
		assertTrue(FixDecMicro64.from(116205.6652627973) < FixDecMicro64.from(116205.6652627973) + minDelta)
		assertTrue(116205.6652627973 < FixDecMicro64.from(116205.6652627973) + minDelta)
		assertFalse(FixDecMicro64.from(633470.5910724404) < FixDecMicro64.from(633470.5910724404) - minDelta)
		assertFalse(633470.5910724404 < FixDecMicro64.from(633470.5910724404) - minDelta)
		assertTrue(FixDecMicro64.from(633470.5910724404) < FixDecMicro64.from(633470.5910724404) + minDelta)
		assertTrue(633470.5910724404 < FixDecMicro64.from(633470.5910724404) + minDelta)
		assertFalse(FixDecMicro64.from(1.677804772430343E7) < FixDecMicro64.from(1.677804772430343E7) - minDelta)
		assertFalse(1.677804772430343E7 < FixDecMicro64.from(1.677804772430343E7) - minDelta)
		assertTrue(FixDecMicro64.from(1.677804772430343E7) < FixDecMicro64.from(1.677804772430343E7) + minDelta)
		assertTrue(1.677804772430343E7 < FixDecMicro64.from(1.677804772430343E7) + minDelta)
		assertFalse(FixDecMicro64.from(2.514267349675139E7) < FixDecMicro64.from(2.514267349675139E7) - minDelta)
		assertFalse(2.514267349675139E7 < FixDecMicro64.from(2.514267349675139E7) - minDelta)
		assertTrue(FixDecMicro64.from(2.514267349675139E7) < FixDecMicro64.from(2.514267349675139E7) + minDelta)
		assertTrue(2.514267349675139E7 < FixDecMicro64.from(2.514267349675139E7) + minDelta)
		assertFalse(FixDecMicro64.from(9.984003659123064E8) < FixDecMicro64.from(9.984003659123064E8) - minDelta)
		assertFalse(9.984003659123064E8 < FixDecMicro64.from(9.984003659123064E8) - minDelta)
		assertTrue(FixDecMicro64.from(9.984003659123064E8) < FixDecMicro64.from(9.984003659123064E8) + minDelta)
		assertTrue(9.984003659123064E8 < FixDecMicro64.from(9.984003659123064E8) + minDelta)
		assertFalse(FixDecMicro64.from(1.5781531304854264E9) < FixDecMicro64.from(1.5781531304854264E9) - minDelta)
		assertFalse(1.5781531304854264E9 < FixDecMicro64.from(1.5781531304854264E9) - minDelta)
		assertTrue(FixDecMicro64.from(1.5781531304854264E9) < FixDecMicro64.from(1.5781531304854264E9) + minDelta)
		assertTrue(1.5781531304854264E9 < FixDecMicro64.from(1.5781531304854264E9) + minDelta)
		assertFalse(FixDecMicro64.from(5.640193457810576E10) < FixDecMicro64.from(5.640193457810576E10) - minDelta)
		assertFalse(5.640193457810576E10 < FixDecMicro64.from(5.640193457810576E10) - minDelta)
		assertTrue(FixDecMicro64.from(5.640193457810576E10) < FixDecMicro64.from(5.640193457810576E10) + minDelta)
		assertTrue(5.640193457810576E10 < FixDecMicro64.from(5.640193457810576E10) + minDelta)
		assertFalse(FixDecMicro64.from(7.235694787278354E10) < FixDecMicro64.from(7.235694787278354E10) - minDelta)
		assertFalse(7.235694787278354E10 < FixDecMicro64.from(7.235694787278354E10) - minDelta)
		assertTrue(FixDecMicro64.from(7.235694787278354E10) < FixDecMicro64.from(7.235694787278354E10) + minDelta)
		assertTrue(7.235694787278354E10 < FixDecMicro64.from(7.235694787278354E10) + minDelta)
		assertFalse(FixDecMicro64.from(1.4840878533077163E12) < FixDecMicro64.from(1.4840878533077163E12) - minDelta)
		assertFalse(1.4840878533077163E12 < FixDecMicro64.from(1.4840878533077163E12) - minDelta)
		assertTrue(FixDecMicro64.from(1.4840878533077163E12) < FixDecMicro64.from(1.4840878533077163E12) + minDelta)
		assertTrue(1.4840878533077163E12 < FixDecMicro64.from(1.4840878533077163E12) + minDelta)
		assertFalse(FixDecMicro64.from(3.729431280082279E12) < FixDecMicro64.from(3.729431280082279E12) - minDelta)
		assertFalse(3.729431280082279E12 < FixDecMicro64.from(3.729431280082279E12) - minDelta)
		assertTrue(FixDecMicro64.from(3.729431280082279E12) < FixDecMicro64.from(3.729431280082279E12) + minDelta)
		assertTrue(3.729431280082279E12 < FixDecMicro64.from(3.729431280082279E12) + minDelta)
		assertFalse(FixDecMicro64.from(9.223372036854775E12) < FixDecMicro64.from(9.223372036854775E12) - minDelta)
		assertFalse(9.223372036854775E12 < FixDecMicro64.from(9.223372036854775E12) - minDelta)
	}
}
