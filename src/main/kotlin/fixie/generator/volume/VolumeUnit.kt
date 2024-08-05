package fixie.generator.volume

enum class VolumeUnit(val abbreviation: String, val factor: Double) {
	LITER("l", 0.001),
	CUBIC_METER("m^3", 1.0)
}
