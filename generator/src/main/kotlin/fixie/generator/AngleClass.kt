package fixie.generator

import fixie.AngleUnit

class AngleClass(
        val className: String,
        val number: NumberClass,
        val displayUnit: AngleUnit,
        val createNumberExtensions: Boolean,
        val allowDivisionAndMultiplication: Boolean,
        val allowComparisons: Boolean
)
