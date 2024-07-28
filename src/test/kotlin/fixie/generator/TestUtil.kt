package fixie.generator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigInteger

class TestUtil {

    @Test
    fun testULongToBigInteger() {
        assertEquals(BigInteger.TEN, uLongToBigInteger(10uL))
        assertEquals(BigInteger.valueOf(Long.MAX_VALUE).times(BigInteger.TWO).add(BigInteger.ONE), uLongToBigInteger(ULong.MAX_VALUE))
    }

    @Test
    fun testBigIntegerToULong() {
        assertEquals(10uL, bigIntegerToULong(BigInteger.valueOf(10)))
        assertEquals(ULong.MAX_VALUE, bigIntegerToULong(BigInteger.TWO.pow(64).subtract(BigInteger.ONE)))
        assertThrows<IllegalArgumentException> { bigIntegerToULong(BigInteger.TWO.pow(64)) }
    }
}
