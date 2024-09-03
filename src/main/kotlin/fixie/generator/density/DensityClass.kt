package fixie.generator.density

import fixie.generator.mass.MassClass
import fixie.generator.number.FloatType
import fixie.generator.number.NumberClass
import fixie.generator.quantity.HybridQuantityClass
import fixie.generator.quantity.QuantityUnit
import fixie.generator.volume.VolumeClass

class DensityClass(
		className: String,
		number: NumberClass?,
		floatType: FloatType?,
		val volumeClassName: String?,
		val massClassName: String?,
		createNumberExtensions: Boolean
) : HybridQuantityClass(className, number, floatType, createNumberExtensions) {

	var volume: VolumeClass? = null
	var mass: MassClass? = null

	override fun getSupportedUnits() = listOf(QuantityUnit(
		"KGPL", "", "kg/l", "kgpl",
		if (number == null) 0.0001 else 1.0 / number.oneValue.toDouble(),
		if (number == null) 1e6 else number.internalType.getMaxValue().toDouble() / number.oneValue.toDouble(),
		1.0
	))

	override fun getNumberOfUnits() = 1
}
