package fixie

import kotlin.math.PI

enum class AngleUnit(val suffix: String, val maxValue: Double) {
    DEGREES("°", 360.0),
    RADIANS("rad", 2 * PI);
}
