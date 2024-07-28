package fixie.generator.parser

import fixie.generator.module.FixieModule
import fixie.generator.number.IntType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigInteger

class TestParser {

    private fun parseSingleModule(body: String): FixieModule {
        val modules = parseModule(body)
        assertEquals(1, modules.size)
        return modules[0]
    }

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

    @Test
    fun testNegativeOneValue() {
        assertEquals(
            "Expected (root).numbers[0].oneValue to be larger than 1, but got -1",
            assertThrows<InvalidConfigException> { parseModule("""{
                packageName = "negative.one",
                moduleName = "negative-one",
                numbers = [{
                    className = "NegativeOne",
                    internalType = "Int",
                    oneValue = -1,
                    checkOverflow = false
                }]
            }""") }.message
        )
    }

    @Test
    fun testSmallOneValue() {
        assertEquals(
            "Expected (root).numbers[0].oneValue to be larger than 1, but got 1",
            assertThrows<InvalidConfigException> { parseModule("""{
                packageName = "small.one",
                moduleName = "small-one",
                numbers = [{
                    className = "SmallOne",
                    internalType = "Int",
                    oneValue = 1,
                    checkOverflow = true
                }]
            }""") }.message
        )
    }

    @Test
    fun testOneValueTooLarge() {
        assertEquals(
            "Expected (root).numbers[0].oneValue to be at most 4294967295, but got 5000000000",
            assertThrows<InvalidConfigException> { parseModule("""{
                packageName = "large.one",
                moduleName = "large-one",
                numbers = [{
                    className = "LargeOne",
                    internalType = "UInt",
                    oneValue = 5e9,
                    checkOverflow = false
                }]
            }""") }.message
        )
    }

    @Test
    fun testDisplacementArea() {
        val module = parseSingleModule("""
            {
                packageName = "test",
                moduleName = "test",
                numbers = [{
                    className = "Fixed",
                    internalType = "Int",
                    oneValue = 1e5,
                    checkOverflow = true
                }],
                areas = [{
                    className = "Area",
                    floatType = "Double",
                    displayUnit = "Square meter",
                    displacement = "Displacement",
                    createNumberExtensions = true
                }],
                displacements = [{
                    className = "Displacement",
                    number = "Fixed",
                    oneUnit = "Meter",
                    displayUnit = "Meter",
                    area = "Area",
                    createNumberExtensions = true
                }]
            }
        """)

        assertSame(module.areas[0], module.displacements[0].area)
        assertEquals("Area", module.displacements[0].areaClassName)

        var test = false
        module.displacements[0].area?.let {
            test = true
        }
        assertTrue(test)
    }

    @Test
    fun testMixedSyntax() {
        val module = parseSingleModule("""{
            "moduleName": "mixed",
            "packageName": "generated.mixed",
            areas = [{
                className = "Area",
                floatType = "Float",
                displayUnit = "Hectare",
                createNumberExtensions = false
            }],
            "numbers": [{
                className = "Unused",
                oneValue: "10",
                "checkOverflow": true,
                "internalType": "UInt"
            }]
        }""")

        assertEquals("Area", module.areas[0].className)
        assertEquals("Unused", module.numbers[0].className)
    }

    @Test
    fun testModuleVariations() {
        val modules = parseModule("""{
            packageName = 'kit.%TYPE%.%OVERFLOW%',
            moduleName = 'module.kit.%TYPE%.%OVERFLOW%',
            numbers = [{
                className = 'Hello',
                internalType = '%TYPE%',
                oneValue = '%ONE%',
                checkOverflow = '%OVERFLOW%'
            }],
            variations = [{
                '%TYPE%' = ['UByte', 'Long'],
                '%ONE%' = [30, 1e10],
                '%OVERFLOW%' = ['true', 'false']
            }]
        }""")
        assertEquals(2, modules.size)

        val packageNames = arrayOf("kit.UByte.true", "kit.Long.false")
        assertEquals(packageNames.toSet(), modules.map { it.packageName }.toSet())

        val module1 = modules.find { it.packageName == packageNames[0] }!!
        assertEquals(IntType(false, 1), module1.numbers[0].internalType)
        assertEquals(BigInteger.valueOf(30), module1.numbers[0].oneValue)
        assertTrue(module1.numbers[0].checkOverflow)

        val module2 = modules.find { it.packageName == packageNames[1] }!!
        assertEquals(IntType(true, 8), module2.numbers[0].internalType)
        assertEquals(BigInteger.TEN.pow(10), module2.numbers[0].oneValue)
        assertFalse(module2.numbers[0].checkOverflow)
    }

    @Test
    fun testDuplicateModuleNamesWithVariation() {
        assertEquals(
                "Duplicate moduleName hello; you must use all variations in the moduleName to avoid this",
                assertThrows<InvalidConfigException> { parseModule("""{
            packageName = 'hello',
            moduleName = 'hello',
            numbers = [{
                className = 'Hello',
                internalType = '%TYPE%',
                oneValue = 50,
                checkOverflow = false
            }],
            variations = [{
                '%TYPE%': ['UByte', 'Int']
            }]
        }""") }.message)
    }

    @Test
    fun testDuplicateModuleNamesWithVariations() {
        val error = assertThrows<InvalidConfigException> { parseModule("""{
            packageName = 'hello',
            moduleName = 'hello%TYPE%',
            numbers = [{
                className = 'Hello',
                internalType = '%TYPE%',
                oneValue = 50,
                checkOverflow = "%OVERFLOW%"
            }],
            variations = [
                {
                    '%TYPE%': ['UByte', 'Int']
                },
                {
                    '%OVERFLOW%': [false, true]
                }
            ]
        }""") }.message!!

        assertTrue(
                error.contains("Duplicate moduleName hello"),
                "Expected $error to contain 'Duplicate moduleName hello'"
        )
        assertTrue(
                error.contains("you must use all variations in the moduleName to avoid this"),
                "Expected $error to contain 'you must use all variations in the moduleName to avoid this'"
        )
    }
}
