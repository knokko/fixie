package fixie.generator.displacement

import fixie.generator.area.AreaClass
import fixie.generator.number.NumberClass
import fixie.generator.quantity.FixedQuantityClass
import fixie.generator.speed.SpeedClass
import java.math.BigDecimal
import java.math.BigInteger

class DisplacementClass(
    className: String,
    number: NumberClass,
    val speedClassName: String?,
    val areaClassName: String?,
    val oneUnit: DistanceUnit,
    val displayUnit: DistanceUnit,
    createNumberExtensions: Boolean
) : FixedQuantityClass(className, number, createNumberExtensions) {

    var speed: SpeedClass? = null
    var area: AreaClass? = null

    fun computeSupportedUnits(): List<Pair<DistanceUnit, BigInteger>> {
        val supportedUnits = mutableListOf(Pair(oneUnit, number.oneValue))
        for (unit in DistanceUnit.entries.reversed()) {
            if (unit != this.oneUnit) {
                val conversionFactor = if (this.oneUnit.isMetric == unit.isMetric) 1.0
                else if (unit.isMetric) 1.609344 else 1.0 / 1.609344

                val precision = 100
                val divisor = BigDecimal.valueOf(conversionFactor).setScale(precision) *
                        BigDecimal.valueOf(unit.divisor, precision) / BigDecimal.valueOf(this.oneUnit.divisor, precision)
                val rawValue = determineRawValue(divisor)
                if (rawValue != null) supportedUnits.add(Pair(unit, rawValue))
            }
        }

        supportedUnits.removeIf { (own, ownValue) ->
            if (own == oneUnit || own == displayUnit) return@removeIf false

            for ((other, otherValue) in supportedUnits) {
                if (own != other && ownValue.subtract(otherValue) == BigInteger.ZERO) return@removeIf true
            }

            return@removeIf false
        }

        return supportedUnits
    }
}
