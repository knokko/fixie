package fixie

enum class AngleUnit(val suffix: String, val maxValue: Double) {
	DEGREES("°", 360.0),
	RADIANS("rad", 6.283185307179586);
}
