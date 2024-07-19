package fixie.generator.quantity

import fixie.generator.number.NumberClass

abstract class FixedQuantityClass(
        className: String,
        val number: NumberClass,
        createNumberExtensions: Boolean
) : QuantityClass(className, createNumberExtensions) {

    override fun toString() = "$className($number)"
}
