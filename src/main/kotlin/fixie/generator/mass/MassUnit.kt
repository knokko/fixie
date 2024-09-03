package fixie.generator.mass

enum class MassUnit(val abbreviation: String, val factor: Double) {
	MILLIGRAM("mg", 1e-6),
	GRAM("g", 1e-3),
	OUNCE("oz", 0.028349523125),
	POUND("lbs", 0.45359237),
	KILOGRAM("kg", 1.0),
	TON("t", 1e3)
}
