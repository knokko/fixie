package fixie

enum class DistanceUnit(val abbreviation: String, val isMetric: Boolean, val divisor: Long) {
	PICOMETER("pm", true, 1000000000000000),
	NANOMETER("nm", true, 1000000000000),
	MICROMETER("um", true, 1000000000),
	MILLIMETER("mm", true, 1000000),
	INCH("inch", false, 63360),
	FOOT("ft", false, 5280),
	YARD("yd", false, 1760),
	METER("m", true, 1000),
	KILOMETER("km", true, 1),
	MILE("mi", false, 1);
}
