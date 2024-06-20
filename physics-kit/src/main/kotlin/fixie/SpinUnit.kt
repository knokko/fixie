package fixie

import kotlin.math.PI

enum class SpinUnit(val suffix: String, val abbreviation: String, val angleMax: Double) {
	DEGREES_PER_SECOND("°/s", "degps", 360.0),
	RADIANS_PER_SECOND("rad/s", "radps", 2 * PI)
}
