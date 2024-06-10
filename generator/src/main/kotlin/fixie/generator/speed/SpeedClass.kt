package fixie.generator.speed

import fixie.generator.acceleration.AccelerationClass
import fixie.generator.number.FloatType
import fixie.generator.number.NumberClass
import java.math.BigInteger

class SpeedClass(
        val className: String,
        val number: NumberClass?,
        val floatType: FloatType?,
        val oneUnit: SpeedUnit,
        val displayUnit: SpeedUnit,
        val displacementClassName: String?,
        val displacementOneValue: BigInteger?,
        val acceleration: AccelerationClass?,
        val createNumberExtensions: Boolean
) {
    init {
        if ((number != null) == (floatType != null)) {
            throw IllegalArgumentException("Speed classes must have either a floatType or a number")
        }
        if ((displacementClassName != null) != (displacementOneValue != null)) {
            throw IllegalArgumentException("Displacement class name must be null if and only if displacement one value is null")
        }
    }
}
