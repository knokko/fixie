package fixie.generator.speed

import fixie.generator.displacement.DistanceUnit
import fixie.generator.number.FloatType
import fixie.generator.quantity.HybridQuantityClassGenerator
import java.io.PrintWriter

internal class SpeedClassGenerator(
        writer: PrintWriter,
        speed: SpeedClass,
        packageName: String
) : HybridQuantityClassGenerator<SpeedClass>(writer, speed, packageName) {

    override fun getImports() = super.getImports() + arrayOf("kotlin.time.Duration", "kotlin.time.DurationUnit")

    override fun generateToDouble() {
        writer.println()
        writer.println("\tfun toDouble(unit: SpeedUnit) = when(unit) {")
        for (unit in SpeedUnit.entries) {
            val factor = unit.factor / quantity.oneUnit.factor
            val conversion = if (factor == 1.0) "" else " * $factor"
            val toDouble = if (quantity.floatType == FloatType.DoublePrecision) "" else ".toDouble()"
            writer.println("\t\tSpeedUnit.$unit -> value$toDouble$conversion")
        }
        writer.println("\t}")
    }

    override fun generateToString() {
        writer.println()
        writer.println("\toverride fun toString() = toString(SpeedUnit.${quantity.displayUnit})")
        writer.println()
        writer.println("\tfun toString(unit: SpeedUnit): String {")
        writer.println("\t\treturn String.format(\"%.4f%s\", toDouble(unit), unit.abbreviation)")
        writer.println("\t}")
    }

    private fun generateTimesDuration(): Boolean {
        quantity.displacementClass?.let {
            displacement -> return displacement.computeSupportedUnits().find { it.first == DistanceUnit.METER } != null
        }
        return false
    }

    override fun generateArithmetic() {
        super.generateArithmetic()

        if (generateTimesDuration()) {
            writer.println()
            if (quantity.displacementClass!!.number.checkOverflow) writer.println("\t@Throws(FixedPointException::class)")
            writer.println("\toperator fun times(right: Duration) = toDouble(SpeedUnit.METERS_PER_SECOND) * " +
                    "${quantity.displacementClassName}.METER * right.toDouble(DurationUnit.SECONDS)")
        }

        quantity.accelerationClass?.let { acceleration ->
            writer.println()
            writer.println("\toperator fun div(right: Duration) = toDouble(SpeedUnit.METERS_PER_SECOND) * " +
                    "${acceleration.className}.MPS2 / right.toDouble(DurationUnit.SECONDS)")
        }
    }

    override fun generateCompanionContent() {
        super.generateCompanionContent()

        if (quantity.number == null) {
            writer.println()
            writer.println("\t\tval ${quantity.oneUnit} = ${quantity.className}(1${if (quantity.floatType == FloatType.SinglePrecision) "f" else ".0"})")
        }

        for ((unit, rawValue) in quantity.computeSupportedUnits()) {
            if (quantity.number != null) {
                writer.println()
                writer.println("\t\tval $unit = raw($rawValue${if (quantity.number.internalType.signed) "" else "u"})")
            } else {
                val factor = quantity.oneUnit.factor / unit.factor
                if (unit != quantity.oneUnit) {
                    writer.println()
                    writer.println("\t\tval $unit = ${quantity.oneUnit} * $factor")
                }
            }
        }
    }

    override fun generateNumberUnitExtensionFunctions(typeName: String) {
        for (unit in quantity.computeSupportedUnits().map { it.first }) {
            writer.println()
            writer.println("val $typeName.${unit.abbreviation.replace('/', 'p')}")
            writer.println("\tget() = ${quantity.className}.$unit * this")
        }
    }

    override fun generateExtensionFunctions() {
        super.generateExtensionFunctions()

        if (generateTimesDuration()) {
            writer.println()
            if (quantity.displacementClass!!.number.checkOverflow) writer.println("@Throws(FixedPointException::class)")
            writer.println("operator fun Duration.times(right: ${quantity.className}) = right * this")
        }
    }
}
