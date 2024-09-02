package fixie.generator.mass

import fixie.generator.density.DensityClass
import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass
import fixie.generator.volume.VolumeClass

class MassClass(
    className: String,
    floatType: FloatType,
    val displayUnit: MassUnit,
    val densityClassName: String?,
    val volumeClassName: String?,
    createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions) {

    var density: DensityClass? = null
    var volume: VolumeClass? = null
}
