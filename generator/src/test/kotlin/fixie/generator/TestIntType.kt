package fixie.generator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigInteger

class TestIntType {

    @Test
    fun testClassName() {
        assertEquals("Byte", IntType(true, 1).className)
        assertEquals("UByte", IntType(false, 1).className)
        assertEquals("Short", IntType(true, 2).className)
        assertEquals("UShort", IntType(false, 2).className)
        assertEquals("Int", IntType(true, 4).className)
        assertEquals("UInt", IntType(false, 4).className)
        assertEquals("Long", IntType(true, 8).className)
        assertEquals("ULong", IntType(false, 8).className)
    }

    @Test
    fun testGetMinValue() {
        assertEquals(BigInteger.valueOf(Byte.MIN_VALUE.toLong()), IntType(true, 1).getMinValue())
        assertEquals(BigInteger.valueOf(Short.MIN_VALUE.toLong()), IntType(true, 2).getMinValue())
        assertEquals(BigInteger.valueOf(Int.MIN_VALUE.toLong()), IntType(true, 4).getMinValue())
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE), IntType(true, 8).getMinValue())


        for (numBytes in arrayOf(1, 2, 4, 8)) {
            assertEquals(BigInteger.ZERO, IntType(false, numBytes).getMinValue())
        }
    }

    @Test
    fun testGetMaxValue() {
        assertEquals(BigInteger.valueOf(Byte.MAX_VALUE.toLong()), IntType(true, 1).getMaxValue())
        assertEquals(BigInteger.valueOf(255), IntType(false, 1).getMaxValue())
        assertEquals(BigInteger.valueOf(Short.MAX_VALUE.toLong()), IntType(true, 2).getMaxValue())
        assertEquals(BigInteger.valueOf(65535), IntType(false, 2).getMaxValue())
        assertEquals(BigInteger.valueOf(Int.MAX_VALUE.toLong()), IntType(true, 4).getMaxValue())
        assertEquals(BigInteger.valueOf(4294967295), IntType(false, 4).getMaxValue())
        assertEquals(BigInteger.valueOf(Long.MAX_VALUE), IntType(true, 8).getMaxValue())
        assertEquals(BigInteger.TWO.pow(64).subtract(BigInteger.ONE), IntType(false, 8).getMaxValue())
    }

    @Test
    fun testCanRepresentByte() {
        val byteType = IntType(true, 1)
        for (candidate in -128 .. 127) assertTrue(byteType.canRepresent(candidate.toLong()))
        assertFalse(byteType.canRepresent(-129))
        assertFalse(byteType.canRepresent(128))
        assertFalse(byteType.canRepresent(Long.MIN_VALUE))
        assertFalse(byteType.canRepresent(Long.MAX_VALUE))
    }

    @Test
    fun testCanRepresentUByte() {
        val uByteType = IntType(false, 1)
        for (candidate in 0 .. 255) assertTrue(uByteType.canRepresent(candidate.toLong()))

        assertFalse(uByteType.canRepresent(-1))
        assertFalse(uByteType.canRepresent(256))
        assertFalse(uByteType.canRepresent(Long.MIN_VALUE))
        assertFalse(uByteType.canRepresent(Long.MAX_VALUE))
    }

    @Test
    fun testCanRepresentShort() {
        val shortType = IntType(true, 2)
        for (candidate in -32768 .. 32767) assertTrue(shortType.canRepresent(candidate.toLong()))
        assertFalse(shortType.canRepresent(-32769))
        assertFalse(shortType.canRepresent(32768))
        assertFalse(shortType.canRepresent(Long.MIN_VALUE))
        assertFalse(shortType.canRepresent(Long.MAX_VALUE))
    }

    @Test
    fun testCanRepresentUShort() {
        val uShortType = IntType(false, 2)
        for (candidate in 0 .. 65535) {
            assertTrue(uShortType.canRepresent(candidate.toLong()))
        }

        assertFalse(uShortType.canRepresent(-1))
        assertFalse(uShortType.canRepresent(65536))
        assertFalse(uShortType.canRepresent(Long.MIN_VALUE))
        assertFalse(uShortType.canRepresent(Long.MAX_VALUE))
    }

    @Test
    fun testCanRepresentInt() {
        val intType = IntType(true, 4)
        for (candidate in -2147483648 .. -2147483000) assertTrue(intType.canRepresent(candidate.toLong()))
        for (candidate in -1000 .. 1000) assertTrue(intType.canRepresent(candidate.toLong()))
        for (candidate in 2147483000 .. 2147483647) assertTrue(intType.canRepresent(candidate.toLong()))
        assertFalse(intType.canRepresent(-2147483649))
        assertFalse(intType.canRepresent(2147483648))
        assertFalse(intType.canRepresent(Long.MIN_VALUE))
        assertFalse(intType.canRepresent(Long.MAX_VALUE))
    }

    @Test
    fun testCanRepresentUInt() {
        val uIntType = IntType(false, 4)
        for (candidate in -1000 .. -1) assertFalse(uIntType.canRepresent(candidate.toLong()))
        for (candidate in 0 .. 1000) assertTrue(uIntType.canRepresent(candidate.toLong()))
        for (candidate in 4294967000 .. 4294967295) assertTrue(uIntType.canRepresent(candidate))
        assertFalse(uIntType.canRepresent(4294967296))
        assertFalse(uIntType.canRepresent(Long.MIN_VALUE))
        assertFalse(uIntType.canRepresent(Long.MAX_VALUE))
    }

    @Test
    fun testCanRepresentLong() {
        val longType = IntType(true, 8)
        for (candidate in Long.MIN_VALUE .. -9223372036854775000L) assertTrue(longType.canRepresent(candidate))
        for (candidate in -1000 .. 1000) assertTrue(longType.canRepresent(candidate.toLong()))
        for (candidate in 9223372036854775000L .. 9223372036854775807L) assertTrue(longType.canRepresent(candidate))
        assertFalse(longType.canRepresent(BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE)))
        assertFalse(longType.canRepresent(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE)))
    }

    @Test
    fun testCanRepresentULong() {
        val uLongType = IntType(false, 8)
        for (candidate in -1000 .. -1) assertFalse(uLongType.canRepresent(candidate.toLong()))
        for (candidate in 0 .. 1000) assertTrue(uLongType.canRepresent(candidate.toLong()))

        val maxValue = BigInteger.valueOf(Long.MIN_VALUE).multiply(BigInteger.valueOf(-2)).subtract(BigInteger.ONE)
        for (difference in 0 until 1000L) assertTrue(uLongType.canRepresent(maxValue.subtract(BigInteger.valueOf(difference))))
        assertFalse(uLongType.canRepresent(maxValue.add(BigInteger.ONE)))
    }

    @Test
    fun testDeclareByteConstant() {
        val testValue: Byte = -123
        assertEquals("val testValue: Byte = -123",
                IntType(true, 1).declareValue("val testValue", testValue.toLong()))
    }

    @Test
    fun testDeclareUByteConstant() {
        val testValue: UByte = 234u
        assertEquals("val testValue: UByte = 234u",
                IntType(false, 1).declareValue("val testValue", testValue.toLong()))
    }

    @Test
    fun testDeclareShortValue() {
        val testValue: Short = -12345
        assertEquals("val testValue: Short = -12345",
                IntType(true, 2).declareValue("val testValue", testValue.toLong()))
    }

    @Test
    fun testDeclareUShortValue() {
        val testValue: UShort = 45678u
        assertEquals("val testValue: UShort = 45678u",
                IntType(false, 2).declareValue("val testValue", testValue.toLong()))
    }

    @Test
    fun testDeclareIntValue() {
        val testValue = -1234567890
        assertEquals("val testValue = -1234567890",
                IntType(true, 4).declareValue("val testValue", testValue.toLong()))
    }

    @Test
    fun testDeclareUIntValue() {
        val testValue = 3456789012u
        assertEquals("val testValue = 3456789012u",
                IntType(false, 4).declareValue("val testValue", testValue.toLong()))
    }

    @Test
    fun testDeclareLongValue() {
        val testValue = -123456789123456789L
        assertEquals("val testValue = -123456789123456789L",
                IntType(true, 8).declareValue("val testValue", testValue))
    }

    @Test
    fun testDeclareULongValue() {
        val testValue = 18446744073709551600uL
        val bigTestValue = BigInteger.valueOf((testValue / 2uL).toLong()).multiply(BigInteger.valueOf(2))
        assertEquals("val testValue = 18446744073709551600uL",
                IntType(false, 8).declareValue("val testValue", bigTestValue))
    }
}
