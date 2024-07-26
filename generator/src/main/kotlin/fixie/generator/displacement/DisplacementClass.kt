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
    val speed: SpeedClass?,
    val area: AreaClass?,
    val oneUnit: DistanceUnit,
    val displayUnit: DistanceUnit,
    createNumberExtensions: Boolean
) : FixedQuantityClass(className, number, createNumberExtensions) {

    fun computeSupportedUnits(): List<Pair<DistanceUnit, BigInteger>> {
        val supportedUnits = mutableListOf<Pair<DistanceUnit, BigInteger>>()
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

        return supportedUnits
    }
}
