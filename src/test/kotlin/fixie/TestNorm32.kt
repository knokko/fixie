package fixie

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestNorm32 {

    @Test
    fun testToString() {
        assertEquals("0.1", Norm32.fromFloat(0.1f).toString())
        assertEquals("-0.1234", Norm32.fromDouble(-0.1234).toString())
        assertEquals("0.5294", Norm32.fromDouble(0.5294).toString())
        assertEquals("-0.529", (-Norm32.fromDouble(0.529)).toString())
        assertEquals("0.57", Norm32.fromFloat(0.57f).toString())
        assertEquals("0.0001", Norm32.fromDouble(0.0001).toString())
        assertEquals("-0.0001", Norm32.fromDouble(-0.0001).toString())
        assertEquals("0.9999", Norm32.fromDouble(0.9999).toString())
        assertEquals("-0.9999", (-Norm32.fromDouble(0.9999)).toString())
        assertEquals("0", Norm32.ZERO.toString())
        assertEquals("0", Norm32.createRaw(1).toString())
        assertEquals("0", Norm32.createRaw(-1).toString())
        assertEquals("1", Norm32.ONE.toString())
        assertEquals("1", Norm32.fromDouble(0.9999999).toString())
        assertEquals("-1", (-Norm32.ONE).toString())
        assertEquals("-1", Norm32.fromDouble(-0.9999999).toString())
    }

    @Test
    fun testToFloat() {
        val margin = 0.0000001f

        for (value in arrayOf(-1f, -0.99f, -0.56f, -0.32f, -0.1f, -0.001f, 0f, 0.001f, 0.1f, 0.32f, 0.56f, 0.99f, 1f)) {
            assertEquals(value, Norm32.fromFloat(value).toFloat(), margin)
        }
    }

    @Test
    fun testToDouble() {
        val margin = 0.00000001

        for (value in arrayOf(-1.0, -0.99, -0.56, -0.32, -0.1, -0.001, 0.0, 0.001, 0.1, 0.32, 0.56, 0.99, 1.0)) {
            assertEquals(value, Norm32.fromDouble(value).toDouble(), margin)
        }
    }

    @Test
    fun testFromFloatOverflow() {
        for (value in arrayOf(1000f, 100f, 10f, 1.1f, 1.001f)) {
            assertThrows<FixedPointException> { Norm32.fromFloat(value) }
            assertThrows<FixedPointException> { Norm32.fromFloat(-value) }
        }
    }

    @Test
    fun testFromDoubleOverflow() {
        for (value in arrayOf(1000.0, 100.0, 10.0, 1.1, 1.001)) {
            assertThrows<FixedPointException> { Norm32.fromDouble(value) }
            assertThrows<FixedPointException> { Norm32.fromDouble(-value) }
        }
    }

    @Test
    fun testAdd() {

    }
}
