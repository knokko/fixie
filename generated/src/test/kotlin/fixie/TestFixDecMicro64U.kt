package fixie

import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestFixDecMicro64U {

	fun assertEquals(a: FixDecMicro64U, b: FixDecMicro64U, maxDelta: FixDecMicro64U) {
		val rawDifference = a.raw.toLong() - b.raw.toLong()
		if (rawDifference.absoluteValue > maxDelta.raw.toLong()) assertEquals(a, b)
	}

	@Test
	fun testToString() {
		assertEquals("0", FixDecMicro64U.ZERO.toString())
		assertEquals("1", FixDecMicro64U.ONE.toString())
		assertTrue((FixDecMicro64U.ONE / 3).toString().startsWith("0.33"))
		assertTrue((FixDecMicro64U.from(18446744073708) + FixDecMicro64U.ONE / 3).toString().endsWith((FixDecMicro64U.ONE / 3).toString().substring(1)))
	}

	@Test
	fun testIntConversion() {
		val one = 1
		assertEquals(FixDecMicro64U.ONE, FixDecMicro64U.from(one))

		fun testValue(value: Int) = assertEquals(value, FixDecMicro64U.from(value).toInt())
		fun testOverflow(value: Int) = assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(value) }
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
		assertEquals(FixDecMicro64U.ONE, FixDecMicro64U.from(one))

		fun testValue(value: Long) = assertEquals(value, FixDecMicro64U.from(value).toLong())
		fun testOverflow(value: Long) = assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(value) }
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

		testOverflow(18446744073710)
		testOverflow(378793816221296)
		testOverflow(850996366084859225)
		testOverflow(9223372036854775807)
	}

	@Test
	fun testFloatConversion() {
		assertEquals(FixDecMicro64U.ONE, FixDecMicro64U.from(1f))
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

		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1.84485895E13f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-1.84485895E13f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1.86902884E15f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-1.86902884E15f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1.54438692E17f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-1.54438692E17f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(8.3520278E18f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-8.3520278E18f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1.7680421E20f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-1.7680421E20f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(9.860756E21f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-9.860756E21f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(4.5605058E22f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-4.5605058E22f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(2.8344572E23f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-2.8344572E23f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(3.4028237E26f) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-3.4028237E26f) }
	}

	@Test
	fun testDoubleConversion() {
		assertEquals(FixDecMicro64U.ONE, FixDecMicro64U.from(1.0))
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

		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1.844858874811692E13) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-1.844858874811692E13) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1.8690288149196008E15) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-1.8690288149196008E15) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1.5443868914570928E17) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-1.5443868914570928E17) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(8.3520275095194573E18) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-8.3520275095194573E18) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1.7680422219837925E20) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-1.7680422219837925E20) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(9.860756704220024E21) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-9.860756704220024E21) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(4.560505983011859E22) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-4.560505983011859E22) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(2.8344571745045302E23) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-2.8344571745045302E23) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(3.4028236692093846E26) }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(-3.4028236692093846E26) }
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
		testValues(0, 10457611174944, 10457611174944)
		testValues(1, 14609676953356, 14609676953357)
		testValues(3567, 10148252149815, 10148252153382)
		testValues(3788, 10513322221140, 10513322224928)
		testValues(9103, 14986027884794, 14986027893897)
		testValues(9886, 16453861400699, 16453861410585)
		testValues(53441, 445475366350, 445475419791)
		testValues(469344, 14232062554153, 14232063023497)
		testValues(5279943, 14890861424109, 14890866704052)
		testValues(10532273, 7340956469527, 7340967001800)
		testValues(98281323, 634451787882, 634550069205)
		testValues(143170306, 17045029442279, 17045172612585)
		testValues(2251333916, 10679993359787, 10682244693703)
		testValues(3899907567, 3838728825856, 3842628733423)
		testValues(126403199838, 17915775533846, 18042178733684)
		testValues(143594785468, 8417875623789, 8561470409257)
		testValues(2185542315817, 2419919091924, 4605461407741)
		testValues(3480974189933, 5395203452609, 8876177642542)
		testValues(18446744073709, 0, 18446744073709)
		testValues(FixDecMicro64U.raw(ULong.MAX_VALUE - 1000000u), FixDecMicro64U.ONE, FixDecMicro64U.raw(ULong.MAX_VALUE))

		fun testOverflowPlus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) + FixDecMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro64U.from(b) }
		}

		fun testOverflowMinus(a: Int, b: Int) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) - FixDecMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro64U.from(b) }
		}

		fun testOverflowPlus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) + FixDecMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro64U.from(b) }
		}

		fun testOverflowMinus(a: Long, b: Long) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) - FixDecMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro64U.from(b) }
		}

		fun testOverflowPlus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) + FixDecMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro64U.from(b) }
		}

		fun testOverflowMinus(a: Float, b: Float) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) - FixDecMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro64U.from(b) }
		}

		fun testOverflowPlus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) + FixDecMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) + b }
			assertThrows(FixedPointException::class.java) { a + FixDecMicro64U.from(b) }
		}

		fun testOverflowMinus(a: Double, b: Double) {
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) - FixDecMicro64U.from(b) }
			assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(a) - b }
			assertThrows(FixedPointException::class.java) { a - FixDecMicro64U.from(b) }
		}

		testOverflowPlus(0L, 18446744073710L)
		testOverflowPlus(0L, 1359754670386173217L)
		testOverflowMinus(0, 1)
		testOverflowMinus(0L, 1L)
		testOverflowMinus(0, 4)
		testOverflowMinus(0L, 4L)
		testOverflowPlus(1L, 18446744073709L)
		testOverflowPlus(1L, 1359717776898025796L)
		testOverflowMinus(1, 2)
		testOverflowMinus(1L, 2L)
		testOverflowMinus(1.0f, 2.0f)
		testOverflowMinus(1.0, 2.0)
		testOverflowMinus(1, 9)
		testOverflowMinus(1L, 9L)
		testOverflowMinus(1.0f, 9.0f)
		testOverflowMinus(1.0, 9.0)
		testOverflowPlus(171418L, 18446743902292L)
		testOverflowMinus(171418, 171419)
		testOverflowMinus(171418L, 171419L)
		testOverflowMinus(171418.0, 171419.0)
		testOverflowMinus(171418L, 29384816400L)
		testOverflowPlus(358643L, 18446743715067L)
		testOverflowPlus(358643L, 6574907603064657936L)
		testOverflowMinus(358643, 358644)
		testOverflowMinus(358643L, 358644L)
		testOverflowMinus(358643.0, 358644.0)
		testOverflowMinus(358643L, 128626236025L)
		testOverflowPlus(18806101L, 18446725267609L)
		testOverflowPlus(18806101L, 8513718798062935204L)
		testOverflowMinus(18806101, 18806102)
		testOverflowMinus(18806101L, 18806102L)
		testOverflowMinus(1.8806101E7, 1.8806102E7)
		testOverflowMinus(18806101L, 353669510046609L)
		testOverflowPlus(511305813L, 18446232767897L)
		testOverflowPlus(511305813L, 8785424086195503268L)
		testOverflowMinus(511305813, 511305814)
		testOverflowMinus(511305813L, 511305814L)
		testOverflowMinus(5.11305813E8, 5.11305814E8)
		testOverflowMinus(511305813L, 261433636452814225L)
		testOverflowPlus(114750512288L, 18331993561422L)
		testOverflowPlus(1.14750512288E11, 1.8331993561422E13)
		testOverflowMinus(114750512288L, 114750512289L)
		testOverflowMinus(1.14750512288E11, 1.14750512289E11)
		testOverflowPlus(18446744073709L, 1L)
		testOverflowPlus(18446744073709L, 4L)
		testOverflowMinus(18446744073709L, 18446744073710L)
		testOverflowMinus(1.8446744073709E13, 1.844674407371E13)
		testOverflowMinus(18446744073709L, 1359754670386173217L)
		assertEquals(FixDecMicro64U.raw(18446744073708649763u), FixDecMicro64U.raw(649763u) + 18446744073708)
	}

	@Test
	fun testMultiplicationAndDivision() {
		assertEquals(FixDecMicro64U.raw(ULong.MAX_VALUE), 1 * FixDecMicro64U.raw(ULong.MAX_VALUE))
		assertEquals(FixDecMicro64U.raw(ULong.MAX_VALUE), FixDecMicro64U.raw(ULong.MAX_VALUE) / 1)
		assertThrows(FixedPointException::class.java) { -1 * FixDecMicro64U.ONE }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.ONE / -1 }
		assertThrows(FixedPointException::class.java) { -1 * FixDecMicro64U.raw(ULong.MAX_VALUE)}
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.raw(ULong.MAX_VALUE) / -1 }

		fun testValues(a: Long, b: Long) {
			assertEquals(FixDecMicro64U.from(a * b), FixDecMicro64U.from(a) * FixDecMicro64U.from(b))
			assertEquals(FixDecMicro64U.from(a * b), FixDecMicro64U.from(a) * b)
			assertEquals(FixDecMicro64U.from(a * b), b * FixDecMicro64U.from(a))
			if (b != 0L) assertEquals(FixDecMicro64U.from(a), FixDecMicro64U.from(a * b) / b)
			if (a != 0L) assertEquals(FixDecMicro64U.from(b), FixDecMicro64U.from(a * b) / a)
			if (a != 0L && a.toInt().toLong() == a) {
				assertEquals(FixDecMicro64U.from(b), FixDecMicro64U.from(a * b) / a.toInt())
			}
			if (b.toInt().toLong() == b) {
				assertEquals(FixDecMicro64U.from(a * b), FixDecMicro64U.from(a) * b.toInt())
				assertEquals(FixDecMicro64U.from(a * b), b.toInt() * FixDecMicro64U.from(a))
			}
		}
		testValues(0, 71711606273)
		testValues(1, 231)
		testValues(42, 0)
		testValues(134, 21231)
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(165) * 1222091827510 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(231) * 18446744073709 }
		testValues(1493, 1474163511)
		testValues(1881, 13620980)
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(4505) * 12211872545 }
		testValues(11152, 329768868)
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(11484) * 12211872545 }
		testValues(21231, 1970917)
		testValues(252026, 252026)
		testValues(301591, 134)
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1389655) * 1222091827510 }
		testValues(1970917, 165)
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(6075644) * 329768868 }
		testValues(13371719, 4505)
		testValues(13620980, 1493)
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(32234314) * 44002482883 }
		testValues(39192496, 1881)
		testValues(329768868, 11152)
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1474163511) * 301591 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(2854741395) * 1389655 }
		testValues(5116806831, 231)
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(12211872545) * 5116806831 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(44002482883) * 2644929701422 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(71711606273) * 1881 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(364448964103) * 2854741395 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(500434653899) * 1970917 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(1222091827510) * 252026 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(2644929701422) * 4505 }
		assertThrows(FixedPointException::class.java) { FixDecMicro64U.from(18446744073709) * 364448964103 }
		assertEquals(FixDecMicro64U.raw(674006u), (FixDecMicro64U.raw(674006u) * FixDecMicro64U.raw(709792u)) / FixDecMicro64U.raw(709792u), FixDecMicro64U.raw(10000u))
	}

	@Test
	fun testCompareTo() {
		assertTrue(FixDecMicro64U.ZERO < FixDecMicro64U.ONE)
		assertTrue(0 < FixDecMicro64U.ONE)
		assertFalse(FixDecMicro64U.ZERO > FixDecMicro64U.ONE)
		assertFalse(0 > FixDecMicro64U.ONE)
		assertFalse(FixDecMicro64U.ONE < FixDecMicro64U.ONE)
		assertFalse(FixDecMicro64U.ONE < 1)
		assertFalse(FixDecMicro64U.ONE > FixDecMicro64U.ONE)
		assertTrue(FixDecMicro64U.ONE <= FixDecMicro64U.ONE)
		assertTrue(FixDecMicro64U.ONE >= FixDecMicro64U.ONE)
		assertTrue(FixDecMicro64U.raw(ULong.MIN_VALUE) < FixDecMicro64U.raw(ULong.MAX_VALUE))

		val minDelta = FixDecMicro64U.raw(1u)
		assertEquals(FixDecMicro64U.from(12), FixDecMicro64U.from(12))
		assertNotEquals(FixDecMicro64U.from(12), FixDecMicro64U.from(12) - minDelta)
		assertTrue(FixDecMicro64U.from(1.0E-6) < FixDecMicro64U.from(1.0E-6) + minDelta)
		assertTrue(1.0E-6 < FixDecMicro64U.from(1.0E-6) + minDelta)
		assertFalse(FixDecMicro64U.from(4.101245270674489E-4) < FixDecMicro64U.from(4.101245270674489E-4) - minDelta)
		assertFalse(4.101245270674489E-4 < FixDecMicro64U.from(4.101245270674489E-4) - minDelta)
		assertTrue(FixDecMicro64U.from(4.101245270674489E-4) < FixDecMicro64U.from(4.101245270674489E-4) + minDelta)
		assertTrue(4.101245270674489E-4 < FixDecMicro64U.from(4.101245270674489E-4) + minDelta)
		assertFalse(FixDecMicro64U.from(0.0014602674387652094) < FixDecMicro64U.from(0.0014602674387652094) - minDelta)
		assertFalse(0.0014602674387652094 < FixDecMicro64U.from(0.0014602674387652094) - minDelta)
		assertTrue(FixDecMicro64U.from(0.0014602674387652094) < FixDecMicro64U.from(0.0014602674387652094) + minDelta)
		assertTrue(0.0014602674387652094 < FixDecMicro64U.from(0.0014602674387652094) + minDelta)
		assertFalse(FixDecMicro64U.from(0.008314038763645868) < FixDecMicro64U.from(0.008314038763645868) - minDelta)
		assertFalse(0.008314038763645868 < FixDecMicro64U.from(0.008314038763645868) - minDelta)
		assertTrue(FixDecMicro64U.from(0.008314038763645868) < FixDecMicro64U.from(0.008314038763645868) + minDelta)
		assertTrue(0.008314038763645868 < FixDecMicro64U.from(0.008314038763645868) + minDelta)
		assertFalse(FixDecMicro64U.from(0.06313960308834927) < FixDecMicro64U.from(0.06313960308834927) - minDelta)
		assertFalse(0.06313960308834927 < FixDecMicro64U.from(0.06313960308834927) - minDelta)
		assertTrue(FixDecMicro64U.from(0.06313960308834927) < FixDecMicro64U.from(0.06313960308834927) + minDelta)
		assertTrue(0.06313960308834927 < FixDecMicro64U.from(0.06313960308834927) + minDelta)
		assertFalse(FixDecMicro64U.from(0.18938331340622416) < FixDecMicro64U.from(0.18938331340622416) - minDelta)
		assertFalse(0.18938331340622416 < FixDecMicro64U.from(0.18938331340622416) - minDelta)
		assertTrue(FixDecMicro64U.from(0.18938331340622416) < FixDecMicro64U.from(0.18938331340622416) + minDelta)
		assertTrue(0.18938331340622416 < FixDecMicro64U.from(0.18938331340622416) + minDelta)
		assertFalse(FixDecMicro64U.from(2.75487661886147) < FixDecMicro64U.from(2.75487661886147) - minDelta)
		assertFalse(2.75487661886147 < FixDecMicro64U.from(2.75487661886147) - minDelta)
		assertTrue(FixDecMicro64U.from(2.75487661886147) < FixDecMicro64U.from(2.75487661886147) + minDelta)
		assertTrue(2.75487661886147 < FixDecMicro64U.from(2.75487661886147) + minDelta)
		assertFalse(FixDecMicro64U.from(10.653674356901822) < FixDecMicro64U.from(10.653674356901822) - minDelta)
		assertFalse(10.653674356901822 < FixDecMicro64U.from(10.653674356901822) - minDelta)
		assertTrue(FixDecMicro64U.from(10.653674356901822) < FixDecMicro64U.from(10.653674356901822) + minDelta)
		assertTrue(10.653674356901822 < FixDecMicro64U.from(10.653674356901822) + minDelta)
		assertFalse(FixDecMicro64U.from(150.2659312211488) < FixDecMicro64U.from(150.2659312211488) - minDelta)
		assertFalse(150.2659312211488 < FixDecMicro64U.from(150.2659312211488) - minDelta)
		assertTrue(FixDecMicro64U.from(150.2659312211488) < FixDecMicro64U.from(150.2659312211488) + minDelta)
		assertTrue(150.2659312211488 < FixDecMicro64U.from(150.2659312211488) + minDelta)
		assertFalse(FixDecMicro64U.from(3538.6818839055695) < FixDecMicro64U.from(3538.6818839055695) - minDelta)
		assertFalse(3538.6818839055695 < FixDecMicro64U.from(3538.6818839055695) - minDelta)
		assertTrue(FixDecMicro64U.from(3538.6818839055695) < FixDecMicro64U.from(3538.6818839055695) + minDelta)
		assertTrue(3538.6818839055695 < FixDecMicro64U.from(3538.6818839055695) + minDelta)
		assertFalse(FixDecMicro64U.from(13398.287795781996) < FixDecMicro64U.from(13398.287795781996) - minDelta)
		assertFalse(13398.287795781996 < FixDecMicro64U.from(13398.287795781996) - minDelta)
		assertTrue(FixDecMicro64U.from(13398.287795781996) < FixDecMicro64U.from(13398.287795781996) + minDelta)
		assertTrue(13398.287795781996 < FixDecMicro64U.from(13398.287795781996) + minDelta)
		assertFalse(FixDecMicro64U.from(116205.6652627973) < FixDecMicro64U.from(116205.6652627973) - minDelta)
		assertFalse(116205.6652627973 < FixDecMicro64U.from(116205.6652627973) - minDelta)
		assertTrue(FixDecMicro64U.from(116205.6652627973) < FixDecMicro64U.from(116205.6652627973) + minDelta)
		assertTrue(116205.6652627973 < FixDecMicro64U.from(116205.6652627973) + minDelta)
		assertFalse(FixDecMicro64U.from(633470.5910724404) < FixDecMicro64U.from(633470.5910724404) - minDelta)
		assertFalse(633470.5910724404 < FixDecMicro64U.from(633470.5910724404) - minDelta)
		assertTrue(FixDecMicro64U.from(633470.5910724404) < FixDecMicro64U.from(633470.5910724404) + minDelta)
		assertTrue(633470.5910724404 < FixDecMicro64U.from(633470.5910724404) + minDelta)
		assertFalse(FixDecMicro64U.from(1.677804772430343E7) < FixDecMicro64U.from(1.677804772430343E7) - minDelta)
		assertFalse(1.677804772430343E7 < FixDecMicro64U.from(1.677804772430343E7) - minDelta)
		assertTrue(FixDecMicro64U.from(1.677804772430343E7) < FixDecMicro64U.from(1.677804772430343E7) + minDelta)
		assertTrue(1.677804772430343E7 < FixDecMicro64U.from(1.677804772430343E7) + minDelta)
		assertFalse(FixDecMicro64U.from(2.514267349675139E7) < FixDecMicro64U.from(2.514267349675139E7) - minDelta)
		assertFalse(2.514267349675139E7 < FixDecMicro64U.from(2.514267349675139E7) - minDelta)
		assertTrue(FixDecMicro64U.from(2.514267349675139E7) < FixDecMicro64U.from(2.514267349675139E7) + minDelta)
		assertTrue(2.514267349675139E7 < FixDecMicro64U.from(2.514267349675139E7) + minDelta)
		assertFalse(FixDecMicro64U.from(9.984003659123064E8) < FixDecMicro64U.from(9.984003659123064E8) - minDelta)
		assertFalse(9.984003659123064E8 < FixDecMicro64U.from(9.984003659123064E8) - minDelta)
		assertTrue(FixDecMicro64U.from(9.984003659123064E8) < FixDecMicro64U.from(9.984003659123064E8) + minDelta)
		assertTrue(9.984003659123064E8 < FixDecMicro64U.from(9.984003659123064E8) + minDelta)
		assertFalse(FixDecMicro64U.from(1.5781531304854264E9) < FixDecMicro64U.from(1.5781531304854264E9) - minDelta)
		assertFalse(1.5781531304854264E9 < FixDecMicro64U.from(1.5781531304854264E9) - minDelta)
		assertTrue(FixDecMicro64U.from(1.5781531304854264E9) < FixDecMicro64U.from(1.5781531304854264E9) + minDelta)
		assertTrue(1.5781531304854264E9 < FixDecMicro64U.from(1.5781531304854264E9) + minDelta)
		assertFalse(FixDecMicro64U.from(5.640193457810576E10) < FixDecMicro64U.from(5.640193457810576E10) - minDelta)
		assertFalse(5.640193457810576E10 < FixDecMicro64U.from(5.640193457810576E10) - minDelta)
		assertTrue(FixDecMicro64U.from(5.640193457810576E10) < FixDecMicro64U.from(5.640193457810576E10) + minDelta)
		assertTrue(5.640193457810576E10 < FixDecMicro64U.from(5.640193457810576E10) + minDelta)
		assertFalse(FixDecMicro64U.from(7.235694787278354E10) < FixDecMicro64U.from(7.235694787278354E10) - minDelta)
		assertFalse(7.235694787278354E10 < FixDecMicro64U.from(7.235694787278354E10) - minDelta)
		assertTrue(FixDecMicro64U.from(7.235694787278354E10) < FixDecMicro64U.from(7.235694787278354E10) + minDelta)
		assertTrue(7.235694787278354E10 < FixDecMicro64U.from(7.235694787278354E10) + minDelta)
		assertFalse(FixDecMicro64U.from(1.4840878533077163E12) < FixDecMicro64U.from(1.4840878533077163E12) - minDelta)
		assertFalse(1.4840878533077163E12 < FixDecMicro64U.from(1.4840878533077163E12) - minDelta)
		assertTrue(FixDecMicro64U.from(1.4840878533077163E12) < FixDecMicro64U.from(1.4840878533077163E12) + minDelta)
		assertTrue(1.4840878533077163E12 < FixDecMicro64U.from(1.4840878533077163E12) + minDelta)
		assertFalse(FixDecMicro64U.from(3.729431280082279E12) < FixDecMicro64U.from(3.729431280082279E12) - minDelta)
		assertFalse(3.729431280082279E12 < FixDecMicro64U.from(3.729431280082279E12) - minDelta)
		assertTrue(FixDecMicro64U.from(3.729431280082279E12) < FixDecMicro64U.from(3.729431280082279E12) + minDelta)
		assertTrue(3.729431280082279E12 < FixDecMicro64U.from(3.729431280082279E12) + minDelta)
		assertFalse(FixDecMicro64U.from(1.844674407370955E13) < FixDecMicro64U.from(1.844674407370955E13) - minDelta)
		assertFalse(1.844674407370955E13 < FixDecMicro64U.from(1.844674407370955E13) - minDelta)
		assertTrue(FixDecMicro64U.raw(ULong.MAX_VALUE) >= 18446744073709)
		assertTrue(FixDecMicro64U.raw(ULong.MAX_VALUE) > 18446744073709)
		assertTrue(FixDecMicro64U.raw(ULong.MAX_VALUE) < 18446744073710)
		assertTrue(FixDecMicro64U.raw(ULong.MAX_VALUE) < 1.8465190817783707E13)
		assertTrue(FixDecMicro64U.raw(ULong.MAX_VALUE) < ULong.MAX_VALUE)
		assertTrue(FixDecMicro64U.raw(ULong.MAX_VALUE) < ULong.MAX_VALUE.toFloat())
		assertTrue(FixDecMicro64U.raw(ULong.MAX_VALUE) < ULong.MAX_VALUE.toDouble())
		assertTrue(FixDecMicro64U.ZERO > -1)
		assertTrue(FixDecMicro64U.ZERO > -0.001f)
		assertTrue(FixDecMicro64U.ZERO > -0.001)
		assertTrue(FixDecMicro64U.ZERO > Long.MIN_VALUE)
		assertTrue(FixDecMicro64U.ZERO > Long.MIN_VALUE.toFloat())
		assertTrue(FixDecMicro64U.ZERO > Long.MIN_VALUE.toDouble())
	}

	@Test
	fun testArrayClass() {
		val testArray = FixDecMicro64U.Array(2) { FixDecMicro64U.ONE }
		assertEquals(2, testArray.size)
		assertEquals(FixDecMicro64U.ONE, testArray[0])
		assertEquals(FixDecMicro64U.ONE, testArray[1])
		testArray[1] = FixDecMicro64U.ZERO
		assertEquals(FixDecMicro64U.ONE, testArray[0])
		assertEquals(FixDecMicro64U.ZERO, testArray[1])
		testArray.fill(FixDecMicro64U.ZERO)
		assertEquals(FixDecMicro64U.ZERO, testArray[0])
		assertEquals(FixDecMicro64U.ZERO, testArray[1])
	}
}
