package fixie.generator.displacement

import fixie.generator.area.AreaClass
import fixie.generator.number.NumberClass
import fixie.generator.quantity.FixedQuantityClass
import fixie.generator.speed.SpeedClass

class DisplacementClass(
        className: String,
        number: NumberClass,
        val speed: SpeedClass?,
        val area: AreaClass?,
        val oneUnit: DistanceUnit,
        val displayUnit: DistanceUnit,
        createNumberExtensions: Boolean
) : FixedQuantityClass(className, number, createNumberExtensions)
