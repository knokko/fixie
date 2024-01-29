package fixie.generator

import fixie.DistanceUnit

class DisplacementClass(
    val className: String,
    val number: NumberClass,
    val oneUnit: DistanceUnit,
    val displayUnit: DistanceUnit,
    val createNumberExtensions: Boolean
)
