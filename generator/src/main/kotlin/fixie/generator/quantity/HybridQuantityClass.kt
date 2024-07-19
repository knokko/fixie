package fixie.generator.quantity

import fixie.generator.number.FloatType
import fixie.generator.number.NumberClass

abstract class HybridQuantityClass(
    className: String,
    val number: NumberClass?,
    val floatType: FloatType?,
    createNumberExtensions: Boolean
) : QuantityClass(className, createNumberExtensions) {

    init {
        if ((number != null) == (floatType != null)) {
            throw IllegalArgumentException("Hybrid quantities must have either a floatType or a number")
        }
    }

    override fun toString() = "$className(${number?.toString() ?: ""}${floatType?.toString() ?: ""}"
}
