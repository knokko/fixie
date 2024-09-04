package fixie.generator.momentum

import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass
import fixie.generator.quantity.QuantityUnit

class SquareMomentumClass(
	className: String,
	floatType: FloatType,
	val momentumClassName: String?,
	createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions) {

	var momentum: MomentumClass? = null

	override fun getSupportedUnits() = listOf(QuantityUnit(
		"SQUARE_NEWTON_SECOND", "", "(Ns)^2", "squareNs", 0.0001, 1e6, 1.0
	))
}
