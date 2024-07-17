package fixie.generator.acceleration

import fixie.generator.number.FloatType

class AccelerationClass(
        val className: String,
        val floatType: FloatType,
        val speedClassName: String?,
        val createNumberExtensions: Boolean
) {
    override fun toString() = "$className($floatType)"
}
