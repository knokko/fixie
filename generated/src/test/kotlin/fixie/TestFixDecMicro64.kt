package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixDecMicro64 {

	fun assertEquals(a: FixDecMicro64, b: FixDecMicro64, maxDelta: FixDecMicro64) {
		val rawDifference = a.raw - b.raw
		if (rawDifference.absoluteValue > maxDelta.raw) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixDecMicro64.ZERO.toString())
		assertEquals("1", FixDecMicro64.ONE.toString())
		assertTrue((FixDecMicro64.ONE / 3).toString().startsWith("0.33"))
		assertEquals("-1", (-FixDecMicro64.ONE).toString())
		assertTrue((FixDecMicro64.ONE / -3).toString().startsWith("-0.33"))
		assertTrue((FixDecMicro64.from(9223372036853) + FixDecMicro64.ONE / 3).toString().endsWith((FixDecMicro64.ONE / 3).toString().substring(1)))
		assertEquals("0.0625", (FixDecMicro64.ONE / 16).toString())
		assertEquals("0.01", (FixDecMicro64.ONE / 100).toString())
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixDecMicro64.ONE, FixDecMicro64.from(one))

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
		val one = 1L
		assertEquals(FixDecMicro64.ONE, FixDecMicro64.from(one))

		fun testValue(value: Long) = assertEquals(value, FixDecMicro64.from(value).toLong())
		fun testOverflow(value: Long) = assertThrows(FixedPointException::class.java) { FixDecMicro64.from(value) }
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

		testOverflow(Long.MIN_VALUE)
		testOverflow(-589153389781491222)
		testOverflow(-43117419028065805)
		testOverflow(-785861820617605)
		testOverflow(-9223372036855)

		testOverflow(9223372036855)
		testOverflow(365741874659691)
		testOverflow(257057323771581529)
		testOverflow(9223372036854775807)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixDecMicro64.ONE, FixDecMicro64.from(1f))
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

		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(9.2242948E12f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-9.2242948E12f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(9.3451442E14f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-9.3451442E14f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(7.7219346E16f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-7.7219346E16f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(4.17601388E18f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-4.17601388E18f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(8.840211E19f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-8.840211E19f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(4.930378E21f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-4.930378E21f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(2.2802529E22f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-2.2802529E22f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(1.4172286E23f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-1.4172286E23f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(8.507059E25f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-8.507059E25f) }
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixDecMicro64.ONE, FixDecMicro64.from(1.0))
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

		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(9.22429437405846E12) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-9.22429437405846E12) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(9.345144074598004E14) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-9.345144074598004E14) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(7.721934457285464E16) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-7.721934457285464E16) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(4.1760137547597286E18) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-4.1760137547597286E18) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(8.840211109918962E19) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-8.840211109918962E19) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(4.930378352110012E21) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-4.930378352110012E21) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(2.2802529915059296E22) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-2.2802529915059296E22) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(1.4172285872522651E23) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-1.4172285872522651E23) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(8.507059173023461E25) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64.from(-8.507059173023461E25) }
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
		testValues(-4866105022873, 5386304923764, 520199900891)
		testValues(-2254834648817, 923381497476, -1331453151341)
		testValues(-218459440131, 1288523218170, 1070063778039)
		testValues(-33209443226, 5758728247683, 5725518804457)
		testValues(-7663042257, 7229239532045, 7221576489788)
		testValues(-1267389897, 434227418752, 432960028855)
		testValues(-339499603, 4823252085073, 4822912585470)
		testValues(-280220893, 3714233293656, 3713953072763)
		testValues(-18746084, 2729422676719, 2729403930635)
		testValues(-5168907, 5843551359746, 5843546190839)
		testValues(-636842, 8589092169653, 8589091532811)
		testValues(-70493, 8403682424057, 8403682353564)
		testValues(-29830, 7868593797235, 7868593767405)
		testValues(-16707, 6172454497872, 6172454481165)
		testValues(-869, 9019258318635, 9019258317766)
		testValues(-719, 2497465094359, 2497465093640)
		testValues(0, 9112351890935, 9112351890935)
		testValues(1, 3680165503749, 3680165503750)
		testValues(691, 1996125505127, 1996125505818)
		testValues(5540, 7563456460566, 7563456466106)
		testValues(8380, 9074699840744, 9074699849124)
		testValues(11964, 3091486026889, 3091486038853)
		testValues(181239, 233453129064, 233453310303)
		testValues(344600, 1748076441002, 1748076785602)
		testValues(1574740, 7302240555701, 7302242130441)
		testValues(9040438, 4286571197451, 4286580237889)
		testValues(84208954, 4816537975545, 4816622184499)
		testValues(101093348, 5074116444312, 5074217537660)
		testValues(1206353206, 6395713156174, 6396919509380)
		testValues(4038401180, 3440432380590, 3444470781770)
		testValues(36594128927, 3302857375847, 3339451504774)
		testValues(97845004009, 6271458842431, 6369303846440)
		testValues(2033477222955, 4972898501254, 7006375724209)
		testValues(3343681293495, 2680817739629, 6024499033124)
		testValues(9223372036854, 0, 9223372036854)
		testValues(FixDecMicro64.raw(Long.MAX_VALUE - 1000000), FixDecMicro64.ONE, FixDecMicro64.raw(Long.MAX_VALUE))

		fun testOverflowPlus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) + FixDecMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro64.from(b) }
		}

		fun testOverflowMinus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) - FixDecMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro64.from(b) }
		}

		fun testOverflowPlus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) + FixDecMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro64.from(b) }
		}

		fun testOverflowMinus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) - FixDecMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro64.from(b) }
		}

		fun testOverflowPlus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) + FixDecMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro64.from(b) }
		}

		fun testOverflowMinus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) - FixDecMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro64.from(b) }
		}

		fun testOverflowPlus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) + FixDecMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro64.from(b) }
		}

		fun testOverflowMinus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) - FixDecMicro64.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro64.from(b) }
		}

		testOverflowPlus(-9223372036854L, 18446744073709L)
		testOverflowPlus(-9.223372E12f, 1.8446744E13f)
		testOverflowPlus(-9.223372036854E12, 1.8446744073709E13)
		testOverflowPlus(-9223372036854L, 1359717776898025796L)
		testOverflowMinus(-9223372036854L, 1L)
		testOverflowMinus(-9223372036854L, 4L)
		testOverflowPlus(-4652762433663L, 13876134470518L)
		testOverflowPlus(-4.6527624E12f, 1.38761344E13f)
		testOverflowPlus(-4.652762433663E12, 1.3876134470518E13)
		testOverflowMinus(-4652762433663L, 4570609603192L)
		testOverflowMinus(-4.652762433663E12, 4.570609603192E12)
		testOverflowPlus(-15328587599L, 9238700624454L)
		testOverflowPlus(-1.5328587599E10, 9.238700624454E12)
		testOverflowPlus(-15328587599L, 6337161121458921393L)
		testOverflowMinus(-15328587599L, 9208043449256L)
		testOverflowMinus(-1.5328587599E10, 9.208043449256E12)
		testOverflowMinus(-15328587599L, 3105328378172618129L)
		testOverflowPlus(-133365509L, 9223505402364L)
		testOverflowPlus(-133365509L, 7100185229923443721L)
		testOverflowMinus(-133365509L, 9223238671346L)
		testOverflowPlus(-12529082L, 9223384565937L)
		testOverflowMinus(-12529082L, 9223359507773L)
		testOverflowMinus(-12529082L, 9027008694536821508L)
		testOverflowPlus(-175706L, 9223372212561L)
		testOverflowPlus(-175706L, 3581151536056819268L)
		testOverflowMinus(-175706L, 9223371861149L)
		testOverflowPlus(0L, 9223372036855L)
		testOverflowPlus(0L, 339947890968580160L)
		testOverflowMinus(0L, 9223372036855L)
		testOverflowMinus(0L, 339947890968580160L)
		testOverflowPlus(1L, 9223372036854L)
		testOverflowPlus(1L, 339929444224506449L)
		testOverflowMinus(1L, 9223372036856L)
		testOverflowMinus(1L, 339966337712653873L)
		testOverflowPlus(532L, 9223372036323L)
		testOverflowPlus(532L, 330134223121648400L)
		testOverflowMinus(532L, 9223372037387L)
		testOverflowMinus(532L, 349761558816077968L)
		testOverflowPlus(174401L, 9223371862454L)
		testOverflowMinus(174401L, 9223372211256L)
		testOverflowMinus(174401L, 3557078534583735473L)
		testOverflowPlus(11981695L, 9223360055160L)
		testOverflowPlus(11981695L, 677759102223570737L)
		testOverflowMinus(11981695L, 9223384018550L)
		testOverflowMinus(11981695L, 2423801743735633L)
		testOverflowPlus(1270180182L, 9222101856673L)
		testOverflowPlus(1.270180182E9, 9.222101856673E12)
		testOverflowMinus(1270180182L, 9224642217037L)
		testOverflowMinus(1.270180182E9, 9.224642217037E12)
		testOverflowMinus(1270180182L, 5277076829513156548L)
		testOverflowPlus(21129122192L, 9202242914663L)
		testOverflowPlus(2.1129122192E10, 9.202242914663E12)
		testOverflowPlus(21129122192L, 1803850122819304000L)
		testOverflowMinus(21129122192L, 9244501159047L)
		testOverflowMinus(2.1129122192E10, 9.244501159047E12)
		testOverflowMinus(21129122192L, 6311939329993148480L)
		testOverflowPlus(9223372036854L, 1L)
		testOverflowPlus(9223372036854L, 4L)
		testOverflowMinus(9223372036854L, 18446744073709L)
		testOverflowMinus(9.223372036854E12, 1.8446744073709E13)
		testOverflowMinus(9223372036854L, 1359717776898025796L)
		assertEquals(FixDecMicro64.raw(9223372036853400407), FixDecMicro64.raw(-599593) + 9223372036854)
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
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixDecMicro64.from(b), FixDecMicro64.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixDecMicro64.from(a * b), FixDecMicro64.from(a) * b.toInt())
				assertEquals(FixDecMicro64.from(a * b), b.toInt() * FixDecMicro64.from(a))
			}
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
		assertEquals(FixDecMicro64.raw(-816256), (FixDecMicro64.raw(-816256) * FixDecMicro64.raw(858658)) / FixDecMicro64.raw(858658), FixDecMicro64.raw(10000))
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
		assertTrue(FixDecMicro64.raw(Long.MAX_VALUE) >= 9223372036854)
		assertTrue(FixDecMicro64.raw(Long.MAX_VALUE) > 9223372036854)
		assertTrue(FixDecMicro64.raw(Long.MAX_VALUE) < 9223372036855)
		assertTrue(FixDecMicro64.raw(Long.MAX_VALUE) < 9.232595408891854E12)
		assertTrue(FixDecMicro64.raw(Long.MAX_VALUE) < Long.MAX_VALUE)
		assertTrue(FixDecMicro64.raw(Long.MAX_VALUE) < Long.MAX_VALUE.toFloat())
		assertTrue(FixDecMicro64.raw(Long.MAX_VALUE) < Long.MAX_VALUE.toDouble())
		assertTrue(FixDecMicro64.raw(Long.MIN_VALUE) <= -9223372036854)
		assertTrue(FixDecMicro64.raw(Long.MIN_VALUE) < -9223372036854)
		assertTrue(FixDecMicro64.raw(Long.MIN_VALUE) > -9223372036855)
		assertTrue(FixDecMicro64.raw(Long.MIN_VALUE) > -9.232595408891854E12)
		assertTrue(FixDecMicro64.raw(Long.MIN_VALUE) > Long.MIN_VALUE)
		assertTrue(FixDecMicro64.raw(Long.MIN_VALUE) > Long.MIN_VALUE.toFloat())
		assertTrue(FixDecMicro64.raw(Long.MIN_VALUE) > Long.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixDecMicro64.Array(2) { FixDecMicro64.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixDecMicro64.ONE, testArray[0])
		assertEquals(FixDecMicro64.ONE, testArray[1])
		testArray[1] = FixDecMicro64.ZERO
		assertEquals(FixDecMicro64.ONE, testArray[0])
		assertEquals(FixDecMicro64.ZERO, testArray[1])
		testArray.fill(FixDecMicro64.ZERO)
		assertEquals(FixDecMicro64.ZERO, testArray[0])
		assertEquals(FixDecMicro64.ZERO, testArray[1])
	}

	@Test
	fun testAbs() {
		assertEquals(FixDecMicro64.ZERO, abs(FixDecMicro64.ZERO))
		assertEquals(FixDecMicro64.ONE, abs(FixDecMicro64.ONE))
		assertEquals(FixDecMicro64.ONE, abs(-FixDecMicro64.ONE))
		assertEquals(FixDecMicro64.raw(Long.MAX_VALUE), abs(FixDecMicro64.raw(Long.MAX_VALUE)))
		assertEquals(FixDecMicro64.raw(Long.MAX_VALUE), abs(-FixDecMicro64.raw(Long.MAX_VALUE)))
		assertThrows(FixedPointException::class.java) { abs(FixDecMicro64.raw(Long.MIN_VALUE)) }
	}

	@Test
	fun testMinMax() {
		assertEquals(FixDecMicro64.ZERO, min(FixDecMicro64.ZERO, FixDecMicro64.ZERO))
		assertEquals(FixDecMicro64.ZERO, max(FixDecMicro64.ZERO, FixDecMicro64.ZERO))
		assertEquals(FixDecMicro64.ZERO, min(FixDecMicro64.ONE, FixDecMicro64.ZERO))
		assertEquals(FixDecMicro64.ONE, max(FixDecMicro64.ONE, FixDecMicro64.ZERO))
		assertEquals(FixDecMicro64.ZERO, min(FixDecMicro64.ZERO, FixDecMicro64.ONE))
		assertEquals(FixDecMicro64.ONE, max(FixDecMicro64.ZERO, FixDecMicro64.ONE))
		assertEquals(FixDecMicro64.ZERO, min(FixDecMicro64.ZERO, FixDecMicro64.raw(Long.MAX_VALUE)))
		assertEquals(FixDecMicro64.raw(Long.MAX_VALUE), max(FixDecMicro64.ZERO, FixDecMicro64.raw(Long.MAX_VALUE)))
		assertEquals(-FixDecMicro64.ONE, min(-FixDecMicro64.ONE, FixDecMicro64.ZERO))
		assertEquals(FixDecMicro64.ZERO, max(-FixDecMicro64.ONE, FixDecMicro64.ZERO))
		assertEquals(FixDecMicro64.raw(Long.MIN_VALUE), min(FixDecMicro64.ZERO, FixDecMicro64.raw(Long.MIN_VALUE)))
		assertEquals(FixDecMicro64.ZERO, max(FixDecMicro64.ZERO, FixDecMicro64.raw(Long.MIN_VALUE)))
	}
}
