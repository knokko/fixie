package fixie.generator.spin

import fixie.generator.acceleration.AngularAccelerationClass
import fixie.generator.angle.AngleClass
import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass
import fixie.generator.quantity.QuantityUnit

class SpinClass(
	className: String,
	floatType: FloatType,
	val oneUnit: SpinUnit,
	val displayUnit: SpinUnit,
	val angleClassName: String?,
	val accelerationClassName: String?,
	createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions) {

	var angle: AngleClass? = null
	var acceleration: AngularAccelerationClass? = null

	override fun getSupportedUnits() = SpinUnit.entries.map {
		QuantityUnit(
			it.name, "SpinUnit", it.suffix, it.extensionName,
			0.001, 1e6, oneUnit.angleMax / it.angleMax
		)
	}
}
