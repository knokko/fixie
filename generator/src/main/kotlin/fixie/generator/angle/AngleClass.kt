package fixie.generator.angle

import fixie.generator.number.IntType
import fixie.generator.quantity.QuantityClass
import fixie.generator.spin.SpinClass

class AngleClass(
        className: String,
        val internalType: IntType,
        val displayUnit: AngleUnit,
        createNumberExtensions: Boolean,
        val allowDivisionAndFloatMultiplication: Boolean,
        val allowComparisons: Boolean,
        val spinClass: SpinClass?
) : QuantityClass(className, createNumberExtensions) {
    override fun toString() = "$className($internalType)"
}
