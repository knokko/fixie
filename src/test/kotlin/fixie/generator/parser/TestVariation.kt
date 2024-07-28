package fixie.generator.parser

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestVariation {

	@Test
	fun testEmpty() {
		assertEquals("Expected variation at hello to be non-empty", assertThrows<InvalidConfigException> {
			Variation("hello", emptyMap())
		}.message)
	}

	@Test
	fun testLengthMismatch() {
		assertEquals("Expected all values of variation hi to have the same length", assertThrows<InvalidConfigException> {
			Variation("hi", mapOf(Pair("abc", listOf("d")), Pair("e", listOf("f", "g"))))
		}.message)
	}

	@Test
	fun testCombinationsSingle() {
		val variation1 = Variation("blabla", mapOf(Pair("ones", listOf("2", "5", "15")), Pair("types", listOf("Byte", "UInt", "Long"))))
		val variation2 = Variation("bla", mapOf(Pair("overflow", listOf("true", "false"))))

		val combinations = Variation.allCombinations(listOf(variation1, variation2))
		assertEquals(arrayOf(
				SpecificVariation(mapOf(Pair("ones", "2"), Pair("types", "Byte"), Pair("overflow", "true"))),
				SpecificVariation(mapOf(Pair("ones", "2"), Pair("types", "Byte"), Pair("overflow", "false"))),
				SpecificVariation(mapOf(Pair("ones", "5"), Pair("types", "UInt"), Pair("overflow", "true"))),
				SpecificVariation(mapOf(Pair("ones", "5"), Pair("types", "UInt"), Pair("overflow", "false"))),
				SpecificVariation(mapOf(Pair("ones", "15"), Pair("types", "Long"), Pair("overflow", "true"))),
				SpecificVariation(mapOf(Pair("ones", "15"), Pair("types", "Long"), Pair("overflow", "false"))),
		).toSet(), combinations.toSet())
	}
}
