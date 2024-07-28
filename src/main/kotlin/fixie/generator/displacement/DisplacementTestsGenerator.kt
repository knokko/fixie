package fixie.generator.displacement

import fixie.generator.quantity.FixedQuantityTestsGenerator
import fixie.generator.quantity.QuantityUnit
import fixie.generator.speed.SpeedUnit
import java.io.PrintWriter
import java.math.BigInteger

internal class DisplacementTestsGenerator(
        writer: PrintWriter,
        displacement: DisplacementClass,
        packageName: String
) : FixedQuantityTestsGenerator<DisplacementClass>(writer, displacement, packageName) {

    override fun oneUnitName() = quantity.oneUnit.name

    override fun getImports() = super.getImports() + if (quantity.speed != null) {
        arrayOf("kotlin.time.Duration.Companion.seconds")
    } else emptyArray()

    override fun canSupportMultipleUnits() = true

    override fun getFixedUnits() = quantity.computeSupportedUnits().map { (unit, rawValue) ->
        QuantityUnit(
            name = unit.name,
            enumName = "DistanceUnit",
            suffix = unit.abbreviation,
            extensionName = unit.abbreviation,
            minDelta = determineFixedUnitMinDelta(rawValue, quantity.number),
            maxAmount = determineFixedUnitMaxAmount(rawValue, quantity.number)
        )
    }

    override fun generateToStringBody() {
        writer.println("\t\tval displacementString = (0.5 * ${quantity.className}.${quantity.oneUnit}).toString(DistanceUnit.${quantity.oneUnit})")
        writer.println("\t\tassertTrue(displacementString.startsWith(\"0.5\"))")
        writer.println("\t\tassertTrue(displacementString.endsWith(\"${quantity.oneUnit.abbreviation}\"))")
    }

    override fun generateArithmeticBody() {
        super.generateArithmeticBody()

        val meterPair = quantity.computeSupportedUnits().find { it.first == DistanceUnit.METER }
        val maxMeterValue = if (meterPair != null) quantity.number.internalType.getMaxValue() / meterPair.second else BigInteger.ZERO

        if (maxMeterValue > BigInteger.TEN) {
            quantity.speed?.let { speed ->
                val speedPair = speed.computeSupportedUnits().find { it.first == SpeedUnit.METERS_PER_SECOND }
                if (speedPair != null) {
                    var smallestOneValue = quantity.number.oneValue
                    if (speed.number != null) {
                        smallestOneValue = smallestOneValue.min(speed.number.oneValue)
                        smallestOneValue = smallestOneValue.min(speedPair.second)
                    }
                    val margin = if (smallestOneValue > BigInteger.TEN) 0.001 else 0.2
                    if (smallestOneValue > BigInteger.TEN) {
                        writer.println("\t\tassertEquals(2.0, (10 * ${quantity.className}.METER / 5.seconds).toDouble(SpeedUnit.METERS_PER_SECOND), $margin)")
                    }
                }
            }
        }
    }
}
