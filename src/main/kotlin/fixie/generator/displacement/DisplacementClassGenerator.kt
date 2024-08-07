package fixie.generator.displacement

import fixie.generator.quantity.FixedQuantityClassGenerator
import fixie.generator.speed.SpeedUnit
import java.io.PrintWriter

internal class DisplacementClassGenerator(
        writer: PrintWriter,
        displacement: DisplacementClass,
        packageName: String
) : FixedQuantityClassGenerator<DisplacementClass>(writer, displacement, packageName) {

    override fun getImports() = super.getImports() + if (quantity.speed != null) {
        arrayOf("kotlin.time.Duration", "kotlin.time.DurationUnit")
    } else emptyArray()

    override fun generateToDouble() {
        writer.println()
        writer.println("\tfun toDouble(unit: DistanceUnit) = when(unit) {")
        for (unit in DistanceUnit.entries) {
            var factor = unit.divisor.toDouble()
            if (unit.isMetric != quantity.oneUnit.isMetric) {
                if (unit.isMetric) factor *= 1.609344
                else factor /= 1.609344
            }
            factor /= quantity.oneUnit.divisor.toDouble()
            val conversion = if (factor == 1.0) "" else " * $factor"
            writer.println("\t\tDistanceUnit.$unit -> value.toDouble()$conversion")
        }
        writer.println("\t}")
    }

    override fun generateToString() {
        writer.println()
        writer.println("\toverride fun toString() = toString(DistanceUnit.${quantity.displayUnit})")
        writer.println()
        writer.println("\tfun toString(unit: DistanceUnit): String {")
        val body = arrayOf(
                "val rawDouble = raw(1${if (quantity.number.internalType.signed) "" else "u"}).toDouble(unit)",
                "var numDecimalDigits = 0",
                "var referenceValue = 0.88",
                "while (rawDouble < referenceValue) {",
                "\treferenceValue *= 0.1",
                "\tnumDecimalDigits += 1",
                "}",
                "return String.format(\"%.\${numDecimalDigits}f%s\", toDouble(unit), unit.abbreviation)"
        )
        for (line in body) writer.println("\t\t$line")
        writer.println("\t}")
    }

    override fun generateArithmetic() {
        super.generateArithmetic()

        quantity.area?.let { area ->
            writer.println()
            writer.println("\toperator fun times(right: ${quantity.className}) = " +
                    "${area.className}.SQUARE_METER * this.toDouble(DistanceUnit.METER) * right.toDouble(DistanceUnit.METER)")

            quantity.volume?.let { volume ->
                writer.println()
                writer.println("\toperator fun times(right: ${area.className}) = " +
                        "${volume.className}.CUBIC_METER * this.toDouble(DistanceUnit.METER) * right.toDouble(AreaUnit.SQUARE_METER)")
            }
            // TODO Test displacement * area
        }
        quantity.speed?.let { speed ->
            if (speed.computeSupportedUnits().find { it.first == SpeedUnit.METERS_PER_SECOND } != null) {
                writer.println()
                if (speed.number != null && speed.number.checkOverflow) {
                    writer.println("\t@Throws(FixedPointException::class)")
                }
                writer.println("\toperator fun div(right: Duration) = ${speed.className}.METERS_PER_SECOND * (this.toDouble(DistanceUnit.METER) / right.toDouble(DurationUnit.SECONDS))")
            }
        }
    }

    override fun generateCompanionContent() {
        super.generateCompanionContent()

        for ((unit, rawValue) in quantity.computeSupportedUnits()) {
            writer.println()
            writer.println("\t\tval $unit = raw($rawValue${if (quantity.number.internalType.signed) "" else "u"})")
        }
    }

    override fun generateNumberUnitExtensionFunctions(typeName: String) {
        for (unit in quantity.computeSupportedUnits().map { it.first }) {
            writer.println()
            writer.println("val $typeName.${unit.abbreviation}")
            writer.println("\tget() = ${quantity.className}.$unit * this")
        }
    }
}
