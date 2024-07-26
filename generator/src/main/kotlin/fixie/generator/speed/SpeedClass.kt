package fixie.generator.speed

import fixie.generator.acceleration.AccelerationClass
import fixie.generator.displacement.DistanceUnit
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
        val displacementNumber: NumberClass?,
        val acceleration: AccelerationClass?,
        createNumberExtensions: Boolean
) : HybridQuantityClass(className, number, floatType, createNumberExtensions){
    init {
        if ((displacementClassName != null) != (displacementNumber != null)) {
            throw IllegalArgumentException("Displacement class name must be null if and only if displacement number is null")
        }
    }

    fun computeSupportedUnits(): List<Pair<SpeedUnit, BigInteger>> {
        if (number == null) return SpeedUnit.entries.filter { it != oneUnit }.map { Pair(it, BigInteger.ONE) }

        val supportedUnits = mutableListOf<Pair<SpeedUnit, BigInteger>>()
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
