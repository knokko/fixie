package fixie.generator.area

import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass

class AreaClass(
        className: String,
        floatType: FloatType,
        val displayUnit: AreaUnit,
        val displacementClassName: String?,
        createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions)
