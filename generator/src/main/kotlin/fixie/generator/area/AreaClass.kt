package fixie.generator.area

import fixie.generator.number.FloatType

class AreaClass(
        val className: String,
        val floatType: FloatType,
        val displayUnit: AreaUnit,
        val displacementClassName: String?,
        val createNumberExtensions: Boolean
) {
    override fun toString() = "$className($floatType)"
}
