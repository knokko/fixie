package fixie.generator.acceleration

import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass

class AccelerationClass(
        className: String,
        floatType: FloatType,
        val speedClassName: String?,
        createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions)
