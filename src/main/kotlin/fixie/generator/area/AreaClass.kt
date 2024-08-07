package fixie.generator.area

import fixie.generator.displacement.DisplacementClass
import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass
import fixie.generator.volume.VolumeClass

class AreaClass(
        className: String,
        floatType: FloatType,
        val displayUnit: AreaUnit,
        val displacementClassName: String?,
		val volumeClassName: String?,
        createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions) {

	var displacement: DisplacementClass? = null
	var volume: VolumeClass? = null
}
