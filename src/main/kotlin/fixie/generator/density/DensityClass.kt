package fixie.generator.density

import fixie.generator.number.FloatType
import fixie.generator.number.NumberClass
import fixie.generator.quantity.HybridQuantityClass
import fixie.generator.volume.VolumeClass

class DensityClass(
		className: String,
		number: NumberClass?,
		floatType: FloatType?,
		val volumeClassName: String?,
		// TODO mass class
		createNumberExtensions: Boolean
) : HybridQuantityClass(className, number, floatType, createNumberExtensions) {

	var volume: VolumeClass? = null
}
