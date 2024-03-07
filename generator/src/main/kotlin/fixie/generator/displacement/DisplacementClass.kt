package fixie.generator.displacement

import fixie.DistanceUnit
import fixie.generator.number.NumberClass

class DisplacementClass(
        val className: String,
        val number: NumberClass,
        val oneUnit: DistanceUnit,
        val displayUnit: DistanceUnit,
        val createNumberExtensions: Boolean
)
