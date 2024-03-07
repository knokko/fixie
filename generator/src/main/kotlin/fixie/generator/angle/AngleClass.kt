package fixie.generator.angle

import fixie.AngleUnit
import fixie.generator.number.NumberClass

class AngleClass(
        val className: String,
        val number: NumberClass,
        val displayUnit: AngleUnit,
        val createNumberExtensions: Boolean,
        val allowDivisionAndMultiplication: Boolean,
        val allowComparisons: Boolean
)
