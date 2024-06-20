package fixie.generator.spin

import fixie.generator.number.FloatType

class SpinClass(
        val className: String,
        val floatType: FloatType,
        val oneUnit: SpinUnit,
        val displayUnit: SpinUnit,
        val angleClassName: String?,
        val createNumberExtensions: Boolean
)
