package fixie.generator.acceleration

import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass
import fixie.generator.quantity.QuantityUnit
import fixie.generator.speed.SpeedClass

class AccelerationClass(
        className: String,
        floatType: FloatType,
        val speedClassName: String?,
        createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions) {

	var speed: SpeedClass? = null

	override fun getSupportedUnits() = listOf(QuantityUnit(
		"MPS2", "", "m/s^2", "mps2", 0.0001, 1e6, 1.0
	))
}
