package fixie.generator.angle

import fixie.generator.number.IntType

class AngleClass(
        val className: String,
        val internalType: IntType,
        val displayUnit: AngleUnit,
        val createNumberExtensions: Boolean,
        val allowDivisionAndMultiplication: Boolean,
        val allowComparisons: Boolean
)
