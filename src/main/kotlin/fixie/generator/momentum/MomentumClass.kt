package fixie.generator.momentum

import fixie.generator.mass.MassClass
import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass
import fixie.generator.quantity.QuantityUnit
import fixie.generator.speed.SpeedClass

class MomentumClass(
	className: String,
	floatType: FloatType,
	val speedClassName: String?,
	val massClassName: String?,
	createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions) {

	var speed: SpeedClass? = null
	var mass: MassClass? = null

	override fun getSupportedUnits() = listOf(QuantityUnit(
		"NEWTON_SECOND", "", "Ns", "newSec", 0.0001, 1e6, 1.0
	))
}
