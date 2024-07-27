package fixie

enum class SpeedUnit(val abbreviation: String, val factor: Double) {
	KILOMETERS_PER_HOUR("km/h", 3.6),
	MILES_PER_HOUR("mi/h", 2.2369362921),
	METERS_PER_SECOND("m/s", 1.0),
	KILOMETERS_PER_SECOND("km/s", 0.001);
}
