package fixie.generator.parser

internal class Variation(
		path: String,
		val map: Map<String, List<String>>
) {
	init {
		var length = -1
		if (map.isEmpty()) throw InvalidConfigException("Expected variation at $path to be non-empty")
		for (values in map.values) {
			if (length == -1) length = values.size
			if (length != values.size) {
				throw InvalidConfigException("Expected all values of variation $path to have the same length")
			}
		}
	}

	fun getSpecifics(): List<SpecificVariation> {
		val specifics = mutableListOf<SpecificVariation>()
		val length = map.values.first().size
		for (index in 0 until length) {
			val specific = mutableMapOf<String, String>()
			for ((key, values) in map.entries) {
				specific[key] = values[index]
			}
			specifics.add(SpecificVariation(specific))
		}

		return specifics
	}

	companion object {

		fun allCombinations(variations: List<Variation>): List<SpecificVariation> {
			var combinations = listOf<SpecificVariation>()

			for (variation in variations) {
				val specifics = variation.getSpecifics()

				combinations = if (combinations.isEmpty()) specifics
				else {
					combinations.flatMap { original -> specifics.map { it.combined(original) } }
				}
			}

			return combinations
		}
	}
}
