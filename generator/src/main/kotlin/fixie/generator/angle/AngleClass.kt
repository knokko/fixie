package fixie.generator.angle

import fixie.generator.number.IntType
import fixie.generator.spin.SpinClass

class AngleClass(
        val className: String,
        val internalType: IntType,
        val displayUnit: AngleUnit,
        val createNumberExtensions: Boolean,
        val allowDivisionAndFloatMultiplication: Boolean,
        val allowComparisons: Boolean,
        val spinClass: SpinClass?
)
