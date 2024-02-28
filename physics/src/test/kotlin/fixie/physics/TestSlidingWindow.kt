package fixie.physics

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestSlidingWindow {

    @Test
    fun testWithZeroElements() {
        assertThrows<IllegalArgumentException> { SlidingWindow(emptyArray<String>()) }
    }

    @Test
    fun testWithOneElement() {
        val element = Object()
        val window = SlidingWindow(arrayOf(element))
        assertEquals(-1, window.getMaximumAge())
        assertThrows<IllegalArgumentException> { window.get(0) }

        for (counter in 0 until 5) {
            assertSame(element, window.claim())
            assertEquals(0, window.getMaximumAge())
            assertSame(element, window.get(0))
            assertThrows<IllegalArgumentException> { window.get(1) }
        }
    }

    @Test
    fun testWithTwoElements() {
        val element1 = Object()
        val element2 = Object()
        val window = SlidingWindow(arrayOf(element1, element2))

        assertEquals(-1, window.getMaximumAge())
        assertThrows<IllegalArgumentException> { window.get(0) }

        assertSame(element1, window.claim())
        assertEquals(0, window.getMaximumAge())
        assertSame(element1, window.get(0))
        assertThrows<IllegalArgumentException> { window.get(1) }

        assertSame(element2, window.claim())
        assertEquals(1, window.getMaximumAge())
        assertSame(element2, window.get(0))
        assertSame(element1, window.get(1))
        assertThrows<IllegalArgumentException> { window.get(2) }

        assertSame(element1, window.claim())
        assertEquals(1, window.getMaximumAge())
        assertSame(element1, window.get(0))
        assertSame(element2, window.get(1))
        assertThrows<IllegalArgumentException> { window.get(2) }
    }

    @Test
    fun testWithThreeElements() {
        val element1 = Object()
        val element2 = Object()
        val element3 = Object()
        val window = SlidingWindow(arrayOf(element1, element2, element3))

        assertEquals(-1, window.getMaximumAge())
        assertThrows<IllegalArgumentException> { window.get(0) }

        assertSame(element1, window.claim())
        assertEquals(0, window.getMaximumAge())
        assertSame(element1, window.get(0))
        assertThrows<IllegalArgumentException> { window.get(1) }

        assertSame(element2, window.claim())
        assertEquals(1, window.getMaximumAge())
        assertSame(element2, window.get(0))
        assertSame(element1, window.get(1))
        assertThrows<IllegalArgumentException> { window.get(2) }

        assertSame(element3, window.claim())
        assertEquals(2, window.getMaximumAge())
        assertSame(element3, window.get(0))
        assertSame(element2, window.get(1))
        assertSame(element1, window.get(2))
        assertThrows<IllegalArgumentException> { window.get(3) }

        assertSame(element1, window.claim())
        assertEquals(2, window.getMaximumAge())
        assertSame(element1, window.get(0))
        assertSame(element3, window.get(1))
        assertSame(element2, window.get(2))
        assertThrows<IllegalArgumentException> { window.get(3) }
    }
}
