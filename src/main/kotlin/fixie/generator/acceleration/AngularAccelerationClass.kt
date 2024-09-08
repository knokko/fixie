package fixie.generator.acceleration

import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass
import fixie.generator.quantity.QuantityUnit
import fixie.generator.spin.SpinClass

class AngularAccelerationClass(
	className: String,
	floatType: FloatType,
	val spinClassName: String?,
	createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions) {

	var spin: SpinClass? = null

	override fun getSupportedUnits() = listOf(
		QuantityUnit(
			"RADPS2", "", "rad/s^2", "radps2", 0.0001, 1e6, 1.0
		)
	)
}
