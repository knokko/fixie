package fixie.generator.mass

import fixie.generator.density.DensityClass
import fixie.generator.momentum.MomentumClass
import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass
import fixie.generator.quantity.QuantityUnit
import fixie.generator.speed.SpeedClass
import fixie.generator.volume.VolumeClass

class MassClass(
	className: String,
	floatType: FloatType,
	val displayUnit: MassUnit,
	val densityClassName: String?,
	val volumeClassName: String?,
	val speedClassName: String?,
	val momentumClassName: String?,
	createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions) {

	var density: DensityClass? = null
	var volume: VolumeClass? = null
	var speed: SpeedClass? = null
	var momentum: MomentumClass? = null

	override fun getSupportedUnits() = MassUnit.entries.map {
		QuantityUnit(
			it.name, "MassUnit", it.abbreviation, it.abbreviation, 0.001, 1e6, it.factor
		)
	}
}
