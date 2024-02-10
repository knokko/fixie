package fixie.generator

import fixie.SpeedUnit

class SpeedClass(
    val className: String,
    val number: NumberClass,
    val oneUnit: SpeedUnit,
    val displayUnit: SpeedUnit,
    val createNumberExtensions: Boolean
)
