package fixie

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private fun fi(value: Int) = FixNano64.fromInt(value)
private fun fl(value: Long) = FixNano64.fromLong(value)
private fun fd(value: Double) = FixNano64.fromDouble(value)

class TestFixNano64 {

    @Test
    fun testToFloat() {
        for (value in arrayOf(-8_000_000_000f, -10f, 0.0001f, 1.2f, 43.2f, 8_000_000_000f)) {
            assertEquals(value, FixNano64.fromFloat(value).toFloat(), 0.001f)
        }
        assertEquals(-5f, fi(-5).toFloat(), 0.001f)
    }

    @Test
    fun testToDouble() {
        for (value in arrayOf(-8_500_000_000.0, -10.0, 0.0001, 1.2, 43.2, 8_500_000_000.0)) {
            assertEquals(value, fd(value).toDouble(), 0.001)
        }
        assertEquals(-5.0, fi(-5).toDouble(), 0.001)
    }

    @Test
    fun testFromFloatOverflow() {
        assertThrows<FixedPointException> { FixNano64.fromFloat(-8_600_000_000f) }
        assertThrows<FixedPointException> { FixNano64.fromFloat(8_600_000_000f) }
    }

    @Test
    fun testFromDoubleOverflow() {
        assertThrows<FixedPointException> { FixNano64.fromDouble(-8_600_000_000.0) }
        assertThrows<FixedPointException> { FixNano64.fromDouble(8_600_000_000.0) }
    }

    @Test
    fun testToString() {
        assertEquals("MIN", FixNano64.raw(Long.MIN_VALUE).toString())
        assertEquals("MAX", FixNano64.raw(Long.MAX_VALUE).toString())
        assertEquals("8.59G", FixNano64.raw(Long.MAX_VALUE - 1).toString())
        assertEquals("-8.59G", FixNano64.raw(Long.MIN_VALUE + 1).toString())
        assertEquals("12M", fd(12_000_000.0).toString())
        assertEquals("-100.324M", fi(-100_324_000).toString())
        assertEquals("-1.2k", fd(-1200.0).toString())
        assertEquals("15", fi(15).toString())
        assertEquals("-999", fi(-999).toString())
        assertEquals("1.512", FixNano64.fromFloat(1.512f).toString())
        assertEquals("100.53m", FixNano64.fromFloat(0.100_53f).toString())
        assertEquals("13.45u", fd(0.000_013_45).toString())
        assertEquals("-999.987u", fd(-0.000_999_987).toString())
        assertEquals("254n", fd(0.000_000_254).toString())
        assertEquals("100.004k", fl(100_004).toString())
        assertEquals("100k", fd(100_000.4).toString())
        assertEquals("100.001k", fd(100_000.6).toString())
    }

    @Test
    fun testAdd() {
        val smallMargin = 0.000_000_01
        val largeMargin = 0.000_001

        assertEquals(fd(1.8), fi(1) + fd(0.8), smallMargin)
        assertEquals(FixNano64.fromFloat(-12.45f), fi(-25) + fd(12.55), largeMargin)
        assertEquals(fi(50), FixNano64.fromFloat(70f) + fd(-20.0), largeMargin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE - 10) + FixNano64.raw(10), smallMargin)
        assertEquals(FixNano64.raw(Long.MIN_VALUE), FixNano64.raw(Long.MIN_VALUE + 50) + FixNano64.raw(-50), smallMargin)

        assertThrows<FixedPointException> { FixNano64.raw(Long.MIN_VALUE) + FixNano64.raw(-1) }
        assertThrows<FixedPointException> { FixNano64.raw(Long.MAX_VALUE) + FixNano64.raw(1) }
        assertThrows<FixedPointException> { fl(5_000_000_000) + fd(3_600_000_000.0) }
        assertThrows<FixedPointException> { fd(-5_000_000_000.0) + fl(-3_600_000_000) }

        assertEquals(fd(8_550_000_000.0), fi(2_000_000_000) + fl(6_550_000_000), largeMargin)
        assertEquals(fd(-8_550_000_000.0), -fi(2_000_000_000) + fl(-6_550_000_000), largeMargin)
    }

    @Test
    fun testSubtract() {
        val smallMargin = 0.000_000_01
        val largeMargin = 0.000_001

        assertEquals(fd(1.8), fi(1) - fd(-0.8), smallMargin)
        assertEquals(FixNano64.fromFloat(-12.45f), fi(-25) - fd(-12.55), largeMargin)
        assertEquals(fi(50), FixNano64.fromFloat(70f) - fd(20.0), largeMargin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE - 10) - FixNano64.raw(-10), smallMargin)
        assertEquals(FixNano64.raw(Long.MIN_VALUE), FixNano64.raw(Long.MIN_VALUE + 50) - FixNano64.raw(50), smallMargin)

        assertThrows<FixedPointException> { FixNano64.raw(Long.MIN_VALUE) - FixNano64.raw(1) }
        assertThrows<FixedPointException> { FixNano64.raw(Long.MAX_VALUE) - FixNano64.raw(-1) }
        assertThrows<FixedPointException> { fl(5_000_000_000) - fl(-3_600_000_000) }
        assertThrows<FixedPointException> { fl(-5_000_000_000) - fl(3_600_000_000) }

        assertEquals(fd(8_550_000_000.0), fi(2_000_000_000) - fd(-6_550_000_000.0), largeMargin)
        assertEquals(fl(-8_550_000_000), -fi(2_000_000_000) - fl(6_550_000_000), largeMargin)
    }

    @Test
    fun testMultiplyInt() {
        val margin = 0.000_000_01

        assertEquals(fi(5000), fi(100) * 50, margin)
        assertEquals(fi(5000), 50 * fi(100), margin)

        assertEquals(fd(0.1), fd(0.02) * 5, margin)
        assertEquals(fd(-0.1), fd(0.02) * -5, margin)
        assertEquals(fd(-0.1), fd(-0.02) * 5, margin)
        assertEquals(fd(0.1), fd(-0.02) * -5, margin)

        assertEquals(fl(8_400_000_000), fi(2_100_000_000) * 4)
        assertEquals(fl(-8_400_000_000), 2_100_000_000 * -fi(4))

        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) * -1 * -1, margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), 1 * FixNano64.raw(Long.MAX_VALUE), margin)
        assertEquals(FixNano64.raw(Long.MIN_VALUE), 1 * FixNano64.raw(Long.MIN_VALUE), margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), -1 * FixNano64.raw(Long.MIN_VALUE + 1), margin)

        assertThrows<FixedPointException> { -1 * FixNano64.raw(Long.MIN_VALUE) }
        assertThrows<FixedPointException> { fi(1_000_000_000) * 8100 }
        assertThrows<FixedPointException> { fi(-1_000_000_000) * 8100 }
        assertThrows<FixedPointException> { fi(1_000_000_000) * -8100 }
        assertThrows<FixedPointException> { fi(-1_000_000_000) * -8100 }
    }

    @Test
    fun testMultiplyLong() {
        val margin = 0.000_000_01

        assertEquals(fi(5000), fi(100) * 50L, margin)
        assertEquals(fi(5000), 50L * fi(100), margin)

        assertEquals(fd(0.1), fd(0.02) * 5L, margin)
        assertEquals(fd(-0.1), fd(0.02) * -5L, margin)
        assertEquals(fd(-0.1), fd(-0.02) * 5L, margin)
        assertEquals(fd(0.1), fd(-0.02) * -5L, margin)

        assertEquals(fl(8_400_000_000), fi(2_100_000_000) * 4L)
        assertEquals(fl(-8_400_000_000), 2_100_000_000L * -fi(4))

        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) * -1L * -1L, margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), 1L * FixNano64.raw(Long.MAX_VALUE), margin)
        assertEquals(FixNano64.raw(Long.MIN_VALUE), 1L * FixNano64.raw(Long.MIN_VALUE), margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), -1L * FixNano64.raw(Long.MIN_VALUE + 1), margin)

        assertThrows<FixedPointException> { -1L * FixNano64.raw(Long.MIN_VALUE) }
        assertThrows<FixedPointException> { fi(1_000_000_000) * 8100L }
        assertThrows<FixedPointException> { fi(-1_000_000_000) * 8100L }
        assertThrows<FixedPointException> { fi(1_000_000_000) * -8100L }
        assertThrows<FixedPointException> { fi(-1_000_000_000) * -8100L }
    }

    @Test
    fun testMultiplyFloat() {
        val margin = 0.000_000_01

        assertEquals(fi(5000), fi(100) * 50f, margin)
        assertEquals(fi(5000), 50f * fi(100), margin)

        assertEquals(fd(0.1), fd(0.02) * 5f, margin)
        assertEquals(fd(-0.1), fd(0.02) * -5f, margin)
        assertEquals(fd(-0.1), fd(-0.02) * 5f, margin)
        assertEquals(fd(0.1), fd(-0.02) * -5f, margin)

        assertEquals(fl(8_400_000_000), fi(2_100_000_000) * 4f)
        assertEquals(fl(-8_400_000_000), 2_100_000_000f * -fi(4))

        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) * -1f * -1f, margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), 1f * FixNano64.raw(Long.MAX_VALUE), margin)
        assertEquals(FixNano64.raw(Long.MIN_VALUE), 1f * FixNano64.raw(Long.MIN_VALUE), margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), -1f * FixNano64.raw(Long.MIN_VALUE + 1), margin)

        assertThrows<FixedPointException> { -1f * FixNano64.raw(Long.MIN_VALUE) }
        assertThrows<FixedPointException> { fi(1_000_000_000) * 8100f }
        assertThrows<FixedPointException> { fi(-1_000_000_000) * 8100f }
        assertThrows<FixedPointException> { fi(1_000_000_000) * -8100f }
        assertThrows<FixedPointException> { fi(-1_000_000_000) * -8100f }
    }

    @Test
    fun testMultiplyDouble() {
        val margin = 0.000_000_01

        assertEquals(fi(5000), fi(100) * 50.0, margin)
        assertEquals(fi(5000), 50.0 * fi(100), margin)

        assertEquals(fd(0.1), fd(0.02) * 5.0, margin)
        assertEquals(fd(-0.1), fd(0.02) * -5.0, margin)
        assertEquals(fd(-0.1), fd(-0.02) * 5.0, margin)
        assertEquals(fd(0.1), fd(-0.02) * -5.0, margin)

        assertEquals(fl(8_400_000_000), fi(2_100_000_000) * 4.0)
        assertEquals(fl(-8_400_000_000), 2_100_000_000.0 * -fi(4))

        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) * -1.0 * -1.0, margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), 1.0 * FixNano64.raw(Long.MAX_VALUE), margin)
        assertEquals(FixNano64.raw(Long.MIN_VALUE), 1.0 * FixNano64.raw(Long.MIN_VALUE), margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), -1.0 * FixNano64.raw(Long.MIN_VALUE + 1), margin)

        assertThrows<FixedPointException> { -1.0 * FixNano64.raw(Long.MIN_VALUE) }
        assertThrows<FixedPointException> { fi(1_000_000_000) * 8100.0 }
        assertThrows<FixedPointException> { fi(-1_000_000_000) * 8100.0 }
        assertThrows<FixedPointException> { fi(1_000_000_000) * -8100.0 }
        assertThrows<FixedPointException> { fi(-1_000_000_000) * -8100.0 }
    }

    @Test
    fun testMultiplyFixed() {
        val margin = 0.000_000_01

        assertEquals(fi(5000), fi(100) * fi(50), margin)

        assertEquals(fd(0.1), fd(0.02) * fi(5), margin)
        assertEquals(fd(-0.1), fd(0.02) * fi(-5), margin)
        assertEquals(fd(-0.1), fd(-0.02) * fi(5), margin)
        assertEquals(fd(0.1), fd(-0.02) * -fi(5), margin)

        assertEquals(fl(8_400_000_000), fi(2_100_000_000) * fi(4))
        assertEquals(fd(-8_400_000_000.0), fi(2_100_000_000) * -fi(4))

        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) * -fi(1) * -fi(1), margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), fi(1) * FixNano64.raw(Long.MAX_VALUE), margin)
        assertEquals(FixNano64.raw(Long.MIN_VALUE), fi(1) * FixNano64.raw(Long.MIN_VALUE), margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), fi(-1) * FixNano64.raw(Long.MIN_VALUE + 1), margin)

        assertThrows<FixedPointException> { fi(-1) * FixNano64.raw(Long.MIN_VALUE) }
        assertThrows<FixedPointException> { fi(2) * FixNano64.raw(Long.MIN_VALUE / 2 - 1) }
        assertThrows<FixedPointException> { FixNano64.raw(Long.MAX_VALUE / 2 + 1) * fi(2) }
        assertThrows<FixedPointException> { fi(1_000_000_000) * fi(8100) }
        assertThrows<FixedPointException> { fi(-1_000_000_000) * fi(8100) }
        assertThrows<FixedPointException> { fi(1_000_000_000) * fi(-8100) }
        assertThrows<FixedPointException> { fi(-1_000_000_000) * fi(-8100) }
    }

    @Test
    fun testDivideInt() {
        val margin = 0.000_000_01

        assertEquals(fi(100), fi(5000) / 50, margin)
        assertEquals(fi(50), fi(5000) / 100, margin)
        assertEquals(fi(50), 5000 / fi(100))

        assertEquals(fd(0.02), fd(0.1) / 5, margin)
        assertEquals(fd(0.02), fd(-0.1) / -5, margin)
        assertEquals(fd(-0.02), fd(-0.1) / 5, margin)
        assertEquals(-fd(0.02), fd(0.1) / -5, margin)

        assertEquals(fi(4), fl(8_400_000_000) / 2_100_000_000, margin)
        assertEquals(-fi(4), fl(8_400_000_000) / -2_100_000_000, margin)

        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) / -1 / -1, margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) / 1, margin)
        assertEquals(FixNano64.raw(Long.MIN_VALUE), FixNano64.raw(Long.MIN_VALUE) / 1, margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MIN_VALUE + 1) / -1, margin)

        assertThrows<FixedPointException> { FixNano64.raw(Long.MIN_VALUE) / -1 }
    }

    @Test
    fun testDivideLong() {
        val margin = 0.000_000_01

        assertEquals(fi(100), fi(5000) / 50L, margin)
        assertEquals(fi(50), fi(5000) / 100L, margin)
        assertEquals(fi(100), 5000L / fi(50))

        assertEquals(fd(0.02), fd(0.1) / 5L, margin)
        assertEquals(fd(0.02), fd(-0.1) / -5L, margin)
        assertEquals(fd(-0.02), fd(-0.1) / 5L, margin)
        assertEquals(-fd(0.02), fd(0.1) / -5L, margin)

        assertEquals(fi(4), fl(8_400_000_000) / 2_100_000_000L, margin)
        assertEquals(-fi(4), fl(8_400_000_000) / -2_100_000_000L, margin)

        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) / -1L / -1L, margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) / 1L, margin)
        assertEquals(FixNano64.raw(Long.MIN_VALUE), FixNano64.raw(Long.MIN_VALUE) / 1L, margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MIN_VALUE + 1L) / -1L, margin)

        assertThrows<FixedPointException> { FixNano64.raw(Long.MIN_VALUE) / -1L }
    }

    @Test
    fun testDivideFloat() {
        val margin = 0.000_000_01

        assertEquals(fi(100), fi(5000) / 50f, margin)
        assertEquals(fi(50), fi(5000) / 100f, margin)
        assertEquals(fi(100), 5000f / fi(50), margin)

        assertEquals(fd(0.02), fd(0.1) / 5f, margin)
        assertEquals(fd(0.02), fd(-0.1) / -5f, margin)
        assertEquals(fd(-0.02), fd(-0.1) / 5f, margin)
        assertEquals(fd(-0.02), fd(0.1) / -5f, margin)

        assertEquals(fi(5), fd(0.1) / 0.02f, 0.000_001)
        assertEquals(fi(5), 0.1f / fd(0.02), 0.000_001)

        assertEquals(fi(2_100_000_000), fl(8_400_000_000) / 4f, margin)
        assertEquals(fi(-2_100_000_000), fl(8_400_000_000) / -4f, margin)

        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) / -1f / -1f, margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) / 1f, margin)
        assertEquals(FixNano64.raw(Long.MIN_VALUE), FixNano64.raw(Long.MIN_VALUE) / 1f, margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MIN_VALUE + 1) / -1f, margin)

        assertThrows<FixedPointException> { fi(1_000_000_000) / (1f / 8100f) }
        assertThrows<FixedPointException> { fi(-1_000_000_000) / (1f / 8100f) }
        assertThrows<FixedPointException> { fi(1_000_000_000) / (1f / -8100f) }
        assertThrows<FixedPointException> { fi(-1_000_000_000) / (1f / -8100f) }
    }

    @Test
    fun testDivideDouble() {
        val margin = 0.000_000_01

        assertEquals(fi(100), fi(5000) / 50.0, margin)
        assertEquals(fi(50), fi(5000) / 100.0, margin)
        assertEquals(fi(50), 5000.0 / fi(100), margin)

        assertEquals(fd(0.02), fd(0.1) / 5.0, margin)
        assertEquals(fd(0.02), fd(-0.1) / -5.0, margin)
        assertEquals(fd(-0.02), fd(-0.1) / 5.0, margin)
        assertEquals(fd(-0.02), fd(0.1) / -5.0, margin)

        assertEquals(fi(5), fd(0.1) / 0.02, 0.000_001)
        assertEquals(fi(5), 0.1 / fd(0.02), 0.000_001)

        assertEquals(fi(2_100_000_000), fl(8_400_000_000) / 4.0, margin)
        assertEquals(fi(-2_100_000_000), fl(8_400_000_000) / -4.0, margin)

        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) / -1.0 / -1.0, margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) / 1.0, margin)
        assertEquals(FixNano64.raw(Long.MIN_VALUE), FixNano64.raw(Long.MIN_VALUE) / 1.0, margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MIN_VALUE + 1) / -1.0, margin)

        assertThrows<FixedPointException> { fi(1_000_000_000) / (1.0 / 8100.0) }
        assertThrows<FixedPointException> { fi(-1_000_000_000) / (1.0 / 8100.0) }
        assertThrows<FixedPointException> { fi(1_000_000_000) / (1.0 / -8100.0) }
        assertThrows<FixedPointException> { fi(-1_000_000_000) / (1.0 / -8100.0) }
    }

    @Test
    fun testDivisionFixed() {
        val margin = 0.000_000_01

        assertEquals(fi(50), fi(5000) / fi(100), margin)

        assertEquals(fd(0.02), fd(0.1) / fi(5), margin)
        assertEquals(fd(0.02), fd(-0.1) / fi(-5), margin)
        assertEquals(fd(-0.02), fd(0.1) / fi(-5), margin)
        assertEquals(fd(-0.02), fd(-0.1) / fi(5), margin)

        assertEquals(fi(2_100_000_000), fl(8_400_000_000) / fi(4))
        assertEquals(fi(-2_100_000_000), fl(8_400_000_000) / fi(-4))

        assertEquals(fi(100_000), fi(1) / fd(0.00001), 100.0)
        assertEquals(fi(1_000_000), fi(1) / fd(0.000001), 1000.0)
        assertEquals(fi(0), fi(1) / fl(5_000_000_000L))

        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) / -fi(1) / -fi(1), margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MAX_VALUE) / fi(1), margin)
        assertEquals(FixNano64.raw(Long.MIN_VALUE), FixNano64.raw(Long.MIN_VALUE) / fi(1), margin)
        assertEquals(FixNano64.raw(Long.MAX_VALUE), FixNano64.raw(Long.MIN_VALUE + 1) / fi(-1), margin)

        assertThrows<FixedPointException> { FixNano64.raw(Long.MIN_VALUE) / fi(-1) }
        assertThrows<FixedPointException> { FixNano64.raw(Long.MIN_VALUE / 2 - 1) / fd(0.5) }
        assertThrows<FixedPointException> { FixNano64.raw(Long.MAX_VALUE / 2 + 1) / fd(0.5) }
        assertThrows<FixedPointException> { fi(1_000_000_000) / fd(1.0 / 8100) }
        assertThrows<FixedPointException> { fi(-1_000_000_000) / fd(1.0 / 8100) }
        assertThrows<FixedPointException> { fi(1_000_000_000) / fd(1.0 / -8100) }
        assertThrows<FixedPointException> { fi(-1_000_000_000) / fd(1.0 / -8100) }
        assertThrows<FixedPointException> { fi(1_000_000) / fd(1.0 / 8_600_000.0) }
    }

    @Test
    fun testFixedMultiplyAndDivisionEdgeCases() {
        // Systematically test edge cases
        for (bitCount1 in 0 until 64) {
            for (bitCount2 in 0 until 64) {

                val power1 = 1L shl bitCount1
                val power2 = 1L shl bitCount2

                val ones1 = (Long.MAX_VALUE shr bitCount1 shl bitCount1) xor Long.MAX_VALUE
                val ones2 = (Long.MAX_VALUE shr bitCount2 shl bitCount2) xor Long.MAX_VALUE

                fun test(value1: Long, value2: Long) {
                    var shouldOverflow = false
                    try {
                        fl(Math.multiplyExact(value1, value2))
                        fl(value1)
                        fl(value2)
                    } catch (overflow: FixedPointException) {
                        shouldOverflow = true
                    } catch (overflow: ArithmeticException) {
                        shouldOverflow = true
                    }

                    if (shouldOverflow) {
                        assertThrows<FixedPointException> { println(fl(value1) * fl(value2)) }
                    } else {
                        assertEquals(fl(value1 * value2), fl(value1) * fl(value2), 0.001)
                        if (value2 != 0L) assertEquals(fl(value1), fl(value1 * value2) / fl(value2), 0.001)
                        if (value1 != 0L) assertEquals(fl(value2), fl(value1 * value2) / fl(value1), 0.001)
                    }
                }

                test(power1, power2)
                test(ones1, ones2)
                test(power1, ones2)
            }
        }
    }

    @Test
    fun testFractionInt() {

        val margin = 0.000_000_01

        // Simple cases
        assertEquals(fd(0.04), FixNano64.fraction(4, 100), margin)
        assertEquals(fd(-43.71), FixNano64.fraction(4371, -100), margin)
        assertEquals(fi(82), FixNano64.fraction(-82, -1), margin)

        // Cases with large absolute value
        assertEquals(fi(100), FixNano64.fraction(2_000_000_000, 20_000_000), margin)
        assertEquals(fi(-100), FixNano64.fraction(2_000_000_000, -20_000_000), margin)
        assertEquals(fi(-100), FixNano64.fraction(-2_000_000_000, 20_000_000), margin)
        assertEquals(fi(100), FixNano64.fraction(-2_000_000_000, -20_000_000), margin)
        assertEquals(fd(1.5), FixNano64.fraction(1_500_000_000, 1_000_000_000))

        // Cases with the largest possible absolute values
        assertEquals(fi(Integer.MAX_VALUE), FixNano64.fraction(Integer.MAX_VALUE, 1), margin)
        assertEquals(fi(Integer.MAX_VALUE), FixNano64.fraction(-Integer.MAX_VALUE, -1), margin)
        assertEquals(fi(Integer.MIN_VALUE), FixNano64.fraction(Integer.MIN_VALUE, 1), margin)
        assertEquals(fi(Integer.MIN_VALUE + 1), FixNano64.fraction(-Integer.MAX_VALUE, 1), margin)
        assertEquals(fi(Integer.MIN_VALUE + 1), FixNano64.fraction(Integer.MAX_VALUE, -1), margin)
    }

    @Test
    fun testFractionLong() {

        val margin = 0.000_000_01

        // Simple cases
        assertEquals(fd(0.04), FixNano64.fraction(4L, 100L), margin)
        assertEquals(fd(-43.71), FixNano64.fraction(4371L, -100L), margin)
        assertEquals(fi(82), FixNano64.fraction(-82L, -1L), margin)

        // Cases with large absolute value

        val bil = 1_000_000_000L

        assertEquals(fl(8 * bil), FixNano64.fraction(8 * bil * bil, bil), margin)
        assertEquals(fl(8 * bil), FixNano64.fraction(-8 * bil * bil, -bil), margin)
        assertEquals(-fl(8 * bil), FixNano64.fraction(8 * bil * bil, -bil), margin)
        assertEquals(fl(-8 * bil), FixNano64.fraction(-8 * bil * bil, bil), margin)
        assertEquals(fd(-1.1234), FixNano64.fraction(-11_234 * bil, 10_000 * bil))
    }
}
