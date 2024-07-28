package fixie.generator.speed

import fixie.generator.acceleration.AccelerationClass
import fixie.generator.displacement.DisplacementClass
import fixie.generator.number.FloatType
import fixie.generator.number.NumberClass
import fixie.generator.quantity.FixedQuantityClass.Companion.determineRawValue
import fixie.generator.quantity.HybridQuantityClass
import java.math.BigDecimal
import java.math.BigInteger

class SpeedClass(
        className: String,
        number: NumberClass?,
        floatType: FloatType?,
        val oneUnit: SpeedUnit,
        val displayUnit: SpeedUnit,
        val displacementClassName: String?,
        val accelerationClassName: String?,
        createNumberExtensions: Boolean
) : HybridQuantityClass(className, number, floatType, createNumberExtensions){

    var displacementClass: DisplacementClass? = null
    var accelerationClass: AccelerationClass? = null

    fun computeSupportedUnits(): List<Pair<SpeedUnit, BigInteger>> {
        if (number == null) return SpeedUnit.entries.map { Pair(it, BigInteger.ONE) }

        val supportedUnits = mutableListOf(Pair(oneUnit, number.oneValue))
        for (unit in SpeedUnit.entries.reversed()) {
            if (unit != this.oneUnit) {
                val divisor = unit.factor / this.oneUnit.factor
                val rawValue = determineRawValue(BigDecimal.valueOf(divisor), number)
                if (rawValue != null) supportedUnits.add(Pair(unit, rawValue))
            }
        }

        return supportedUnits
    }
}
