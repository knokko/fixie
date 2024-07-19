package fixie.generator.speed

import fixie.generator.acceleration.AccelerationClass
import fixie.generator.number.FloatType
import fixie.generator.number.NumberClass
import fixie.generator.quantity.HybridQuantityClass
import java.math.BigInteger

class SpeedClass(
        className: String,
        number: NumberClass?,
        floatType: FloatType?,
        val oneUnit: SpeedUnit,
        val displayUnit: SpeedUnit,
        val displacementClassName: String?,
        val displacementNumber: NumberClass?,
        val acceleration: AccelerationClass?,
        createNumberExtensions: Boolean
) : HybridQuantityClass(className, number, floatType, createNumberExtensions){
    init {
        if ((displacementClassName != null) != (displacementNumber != null)) {
            throw IllegalArgumentException("Displacement class name must be null if and only if displacement number is null")
        }
    }
}
