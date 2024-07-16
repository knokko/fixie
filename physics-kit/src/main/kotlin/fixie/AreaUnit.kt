package fixie

enum class AreaUnit(val abbreviation: String, val factor: Double) {
	SQUARE_MILLIMETER("mm^2", 1.0E-6),
	SQUARE_INCH("in^2", 6.4516E-4),
	SQUARE_METER("m^2", 1.0),
	HECTARE("ha", 10000.0);
}
