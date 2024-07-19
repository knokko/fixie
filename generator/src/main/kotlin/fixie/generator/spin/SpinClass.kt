package fixie.generator.spin

import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass

class SpinClass(
        className: String,
        floatType: FloatType,
        val oneUnit: SpinUnit,
        val displayUnit: SpinUnit,
        val angleClassName: String?,
        createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions)
