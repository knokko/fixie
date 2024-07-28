package fixie.generator.parser

internal class SpecificVariation(
		val map: Map<String, String>
) {

	init {
		if (map.isEmpty()) throw InvalidConfigException("Empty specific variation")
	}

	fun combined(other: SpecificVariation): SpecificVariation {
		for (key in this.map.keys) {
			if (other.map.containsKey(key)) throw InvalidConfigException("Duplicate variation key $key")
		}

		val combined = HashMap(this.map)
		combined.putAll(other.map)
		return SpecificVariation(combined)
	}

	override fun equals(other: Any?) = other is SpecificVariation && this.map == other.map

	override fun hashCode() = this.map.hashCode()

	override fun toString() = "SVariation($map)"
}
