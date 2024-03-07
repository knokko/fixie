package fixie.generator.speed

import fixie.SpeedUnit
import fixie.generator.number.NumberClass

class SpeedClass(
        val className: String,
        val number: NumberClass,
        val oneUnit: SpeedUnit,
        val displayUnit: SpeedUnit,
        val createNumberExtensions: Boolean
)
