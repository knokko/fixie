package fixie.generator.angle

import fixie.generator.number.IntType
import fixie.generator.quantity.QuantityClass
import fixie.generator.quantity.QuantityUnit
import fixie.generator.spin.SpinClass
import java.util.*

class AngleClass(
	className: String,
	val internalType: IntType,
	val displayUnit: AngleUnit,
	createNumberExtensions: Boolean,
	val allowDivisionAndFloatMultiplication: Boolean,
	val allowComparisons: Boolean,
	val spinClassName: String?
) : QuantityClass(className, createNumberExtensions) {

	var spinClass: SpinClass? = null
	override fun toString() = "$className($internalType)"

	override fun getSupportedUnits() = AngleUnit.entries.map {
		QuantityUnit(
			name = it.name,
			enumName = "AngleUnit",
			suffix = it.suffix,
			extensionName = it.name.lowercase(Locale.ROOT),
			minDelta = internalType.getMaxValue().toDouble() / it.maxValue,
			maxAmount = 1e6,
			relativeSize = 1.0 / it.maxValue
		)
	}

	override fun getNumberOfUnits() = AngleUnit.entries.size
}
