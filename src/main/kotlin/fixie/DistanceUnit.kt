package fixie

enum class DistanceUnit(val abbreviation: String, val isMetric: Boolean, val divisor: Long) {
    PICOMETER("pm", true, 1000L * 1000L * 1000L * 1000L * 1000L),
    NANOMETER("nm", true, 1000L * 1000L * 1000L * 1000L),
    MICROMETER("um", true, 1000 * 1000 * 1000),
    MILLIMETER("mm", true, 1000 * 1000),
    INCH("in", false, 63360),
    FOOT("ft", false, 5280),
    YARD("yd", false, 1760),
    METER("m", true, 1000),
    KILOMETER("km", true, 1),
    MILE("mi", false, 1);
}
