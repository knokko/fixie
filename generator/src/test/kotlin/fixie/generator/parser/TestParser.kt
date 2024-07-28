package fixie.generator.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestParser {

    @Test
    fun testSyntaxError() {
        assertTrue(assertThrows<InvalidConfigException> {
            parseModule("hello world")
        }.message!!.contains("Unexpected token"))
    }

    @Test
    fun testWrongRootType() {
        assertEquals("Expected root to be a map", assertThrows<InvalidConfigException> {
            parseModule("hello")
        }.message)
    }

    @Test
    fun testMissingPackageName() {
        assertEquals("Missing required string property (root).packageName", assertThrows<InvalidConfigException> {
            parseModule("{ moduleName = 'test' }")
        }.message)
    }

    @Test
    fun testMissingModuleName() {
        assertEquals("Missing required string property (root).moduleName", assertThrows<InvalidConfigException> {
            parseModule("{ packageName = 'test' }")
        }.message)
    }

    @Test
    fun testBadNumbersType() {
        assertEquals("Expected property (root).numbers to be a list, but got {}", assertThrows<Throwable> {
            parseModule("{ \"packageName\": \"test\", \"moduleName\": \"test\", \"numbers\": {} }")
        }.message)
    }

    @Test
    fun testBadNumberType() {
        assertEquals("Expected all elements of (root).numbers to be maps, but found a \"hi\" at index 0", assertThrows<Throwable> {
            parseModule("{ \"packageName\": \"test\", \"moduleName\": \"test\", \"numbers\": ['hi'] }")
        }.message)
    }

    @Test
    fun testMissingNumberClassName() {
        assertEquals("Missing required string property (root).numbers[0].className", assertThrows<Throwable> {
            parseModule("""{
                    "packageName": "test",
                    "moduleName": "test",
                    "numbers": [{
                        internalType = "Int",
                        oneValue = 100,
                        checkOverflow = false
                    }]
                }""")
        }.message)
    }

    @Test
    fun testBadNumberIntType() {
        assertEquals("Unknown IntType Ient at (root).numbers[0].internalType", assertThrows<Throwable> {
            parseModule("""{
                    "packageName": "test",
                    "moduleName": "test",
                    "numbers": [{
                        className = "Hello",
                        internalType = "Ient",
                        oneValue = 100,
                        checkOverflow = false
                    }]
                }""")
        }.message)
    }
}
