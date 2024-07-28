package fixie.generator.speed

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

    private fun shouldTestSeconds() = quantity.number == null || quantity.number.oneValue * BigInteger.valueOf(100L) < quantity.number.internalType.getMaxValue()

    private fun shouldTestHours() = quantity.number == null || quantity.number.oneValue * BigInteger.valueOf(100_000L) < quantity.number.internalType.getMaxValue()

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

        return (quantity.computeSupportedUnits() + arrayOf(Pair(quantity.oneUnit, quantity.number.oneValue))).map { (unit, rawValue) ->
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
        val units = mutableListOf(quantity.oneUnit)
        if (quantity.oneUnit == SpeedUnit.METERS_PER_SECOND) {
            units.add(SpeedUnit.KILOMETERS_PER_HOUR)
            units.add(SpeedUnit.KILOMETERS_PER_SECOND)
        }
        for (unit in units) {
            writer.println("\t\tassertTrue((0.5 * ${quantity.className}.$unit).toString(SpeedUnit.$unit).startsWith(\"0.5\"))")
            writer.println("\t\tassertTrue((0.5 * ${quantity.className}.$unit).toString(SpeedUnit.$unit).endsWith(\"${unit.abbreviation}\"))")
        }
    }

    override fun oneUnitName() = quantity.oneUnit.name

    override fun generateArithmeticBody() {
        super.generateArithmeticBody()

        if (shouldTestSeconds()) {
            val isSpeedAccurate = quantity.number == null || quantity.number.oneValue > BigInteger.TEN
            val displacementNumber = quantity.displacementClass?.number?.oneValue
            val isDisplacementAccurate = displacementNumber == null || displacementNumber > BigInteger.TEN
            val margin = if (isSpeedAccurate && isDisplacementAccurate) 0.01 else 5.0

            if (quantity.displacementClassName != null) {
                writer.println("\t\tassertEquals(20.0, (2.seconds * (10 * ${quantity.className}.METERS_PER_SECOND)).toDouble(DistanceUnit.METER), $margin)")
                if (shouldTestHours()) {
                    writer.println("\t\tassertEquals(20.0, (10 * ${quantity.className}.KILOMETERS_PER_HOUR * 2.hours).toDouble(DistanceUnit.KILOMETER), $margin)")
                }
            }
            if (quantity.accelerationClass != null) {
                writer.println("\t\tassertEquals(0.5, (${quantity.className}.METERS_PER_SECOND / 2.seconds).toDouble(), ${margin / 40})")
            }
        }
    }
}
