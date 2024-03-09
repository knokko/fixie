package fixie.generator.displacement

import fixie.generator.number.NumberClass
import fixie.generator.speed.SpeedClass

class DisplacementClass(
        val className: String,
        val number: NumberClass,
        val speed: SpeedClass?,
        val oneUnit: DistanceUnit,
        val displayUnit: DistanceUnit,
        val createNumberExtensions: Boolean
)
