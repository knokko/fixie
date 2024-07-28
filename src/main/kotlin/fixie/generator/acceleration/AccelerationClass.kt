package fixie.generator.acceleration

import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass
import fixie.generator.speed.SpeedClass

class AccelerationClass(
        className: String,
        floatType: FloatType,
        val speedClassName: String?,
        createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions) {

	var speed: SpeedClass? = null
}
