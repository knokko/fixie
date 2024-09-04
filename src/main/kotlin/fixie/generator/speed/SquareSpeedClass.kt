package fixie.generator.speed

import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass
import fixie.generator.quantity.QuantityUnit

class SquareSpeedClass(
	className: String,
	floatType: FloatType,
	val speedClassName: String?,
	createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions) {

	var speed: SpeedClass? = null

	override fun getSupportedUnits() = listOf(QuantityUnit(
		"SQUARE_METERS_PER_SECOND", "", "(m/s)^2", "squareMps", 0.0001, 1e6, 1.0
	))
}
