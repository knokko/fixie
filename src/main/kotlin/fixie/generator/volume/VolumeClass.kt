package fixie.generator.volume

import fixie.generator.area.AreaClass
import fixie.generator.density.DensityClass
import fixie.generator.displacement.DisplacementClass
import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClass

class VolumeClass(
        className: String,
        floatType: FloatType,
        val displayUnit: VolumeUnit,
        val displacementClassName: String?,
        val areaClassName: String?,
        val densityClassName: String?,
        createNumberExtensions: Boolean
) : FloatQuantityClass(className, floatType, createNumberExtensions) {

    var displacement: DisplacementClass? = null
    var area: AreaClass? = null
    var density: DensityClass? = null
}
