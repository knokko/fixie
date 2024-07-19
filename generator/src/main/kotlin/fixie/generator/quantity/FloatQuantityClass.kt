package fixie.generator.quantity

import fixie.generator.number.FloatType

abstract class FloatQuantityClass(
        className: String,
        val floatType: FloatType,
        createNumberExtensions: Boolean
) : QuantityClass(className, createNumberExtensions) {

    override fun toString() = "$className($floatType)"
}
