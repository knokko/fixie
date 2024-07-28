package fixie.generator.speed

import fixie.generator.displacement.DistanceUnit
import fixie.generator.quantity.HybridQuantityTestsGenerator
import fixie.generator.quantity.QuantityUnit
import java.io.PrintWriter
import java.math.BigInteger

internal class SpeedTestsGenerator(
        writer: PrintWriter,
        speed: SpeedClass,
        packageName: String
) : HybridQuantityTestsGenerator<SpeedClass>(
    writer, speed, packageName
) {

    private fun shouldTest(requiredMaxMeter: Long, requiredMaxSpeed: Long): Boolean {
        quantity.displacementClass?.let { displacement ->
            val meterPair = displacement.computeSupportedUnits().find { it.first == DistanceUnit.METER }
            if (meterPair != null) {
                val maxMeterValue = displacement.number.internalType.getMaxValue() / meterPair.second
                return maxMeterValue > BigInteger.valueOf(requiredMaxMeter) &&
                        (quantity.number == null || quantity.number.oneValue * BigInteger.valueOf(requiredMaxSpeed) <
                                quantity.number.internalType.getMaxValue())
            }
        }
        return false
    }

    private fun getMaxValue(unit: SpeedUnit): BigInteger {
        if (quantity.number == null) return BigInteger.TEN.pow(6)
        val pair = quantity.computeSupportedUnits().find { it.first == unit } ?: return BigInteger.ZERO
        return quantity.number.internalType.getMaxValue() / pair.second
    }

    private fun shouldTestSeconds() = shouldTest(25, 100) &&
            getMaxValue(SpeedUnit.METERS_PER_SECOND) > BigInteger.valueOf(15)

    private fun shouldTestHours() = shouldTest(25000, 100_000) &&
            getMaxValue(SpeedUnit.KILOMETERS_PER_HOUR) > BigInteger.valueOf(15)

    override fun getImports() = super.getImports() + if (shouldTestSeconds()) {
        arrayOf("kotlin.time.Duration.Companion.seconds")
    } else { emptyArray<String>() } + if (shouldTestHours()) {
        arrayOf("kotlin.time.Duration.Companion.hours")
    } else { emptyArray<String>() }

    override fun getUnits(): List<QuantityUnit> {
        if (quantity.number == null) return SpeedUnit.entries.map { QuantityUnit(
            name = it.name,
            enumName = "SpeedUnit",
            suffix = it.abbreviation,
            extensionName = it.abbreviation.replace('/', 'p'),
            minDelta = 0.001,
            maxAmount = 1e6
        ) }

        return quantity.computeSupportedUnits().map { (unit, rawValue) ->
            QuantityUnit(
                name = unit.name,
                enumName = "SpeedUnit",
                suffix = unit.abbreviation,
                extensionName = unit.abbreviation.replace('/', 'p'),
                minDelta = determineFixedUnitMinDelta(rawValue, quantity.number),
                maxAmount = determineFixedUnitMaxAmount(rawValue, quantity.number)
            )
        }.sortedBy { -it.maxAmount }
    }

    override fun canSupportMultipleUnits() = true

    override fun generateToStringBody() {
        for ((unit, oneValue) in quantity.computeSupportedUnits()) {
            if (oneValue < BigInteger.valueOf(40)) continue

            fun printLines(unitString: String) {
                writer.println("\t\tassertTrue((0.55 * ${quantity.className}.$unit).toString($unitString).startsWith(\"0.5\"))")
                writer.println("\t\tassertTrue((0.55 * ${quantity.className}.$unit).toString($unitString).endsWith(\"${unit.abbreviation}\"))")
            }
            printLines("SpeedUnit.$unit")
            if (unit == quantity.displayUnit) printLines("")
        }
    }

    override fun oneUnitName() = quantity.oneUnit.name

    override fun generateArithmeticBody() {
        super.generateArithmeticBody()

        if (shouldTestSeconds()) {
            val isSpeedAccurate = quantity.number == null || quantity.number.oneValue > BigInteger.TEN
            val speedMargin = if (isSpeedAccurate) 0.01 else 5.0

            quantity.displacementClass?.let { displacement ->
                val meterPair = displacement.computeSupportedUnits().find { it.first == DistanceUnit.METER }!!
                val isDisplacementAccurate = meterPair.second > BigInteger.valueOf(100)
                var margin = speedMargin
                if (!isDisplacementAccurate) margin = 5.0

                writer.println("\t\tassertEquals(20.0, (2.seconds * (10 * ${quantity.className}.METERS_PER_SECOND)).toDouble(DistanceUnit.METER), $margin)")
                if (shouldTestHours()) {
                    writer.println("\t\tassertEquals(20.0, (10 * ${quantity.className}.KILOMETERS_PER_HOUR * 2.hours).toDouble(DistanceUnit.KILOMETER), $margin)")
                }
            }
            if (quantity.accelerationClass != null) {
                writer.println("\t\tassertEquals(0.5, (${quantity.className}.METERS_PER_SECOND / 2.seconds).toDouble(), ${speedMargin / 40})")
            }
        }
    }
}
