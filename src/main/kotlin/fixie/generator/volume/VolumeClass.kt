package fixie.generator.volume

import fixie.generator.area.AreaClass
import fixie.generator.density.DensityClass
import fixie.generator.displacement.DisplacementClass
import fixie.generator.mass.MassClass
import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass
import fixie.generator.quantity.QuantityUnit

class VolumeClass(
	className: String,
	floatType: FloatType,
	val displayUnit: VolumeUnit,
	val displacementClassName: String?,
	val areaClassName: String?,
	val densityClassName: String?,
	val massClassName: String?,
	createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions) {

	var displacement: DisplacementClass? = null
	var area: AreaClass? = null
	var density: DensityClass? = null
	var mass: MassClass? = null

	override fun getSupportedUnits() = VolumeUnit.entries.map {
		QuantityUnit(
			it.name, "VolumeUnit", it.abbreviation, it.abbreviation.replace("^", ""),
			0.001, 1e6, it.factor
		)
	}
}
